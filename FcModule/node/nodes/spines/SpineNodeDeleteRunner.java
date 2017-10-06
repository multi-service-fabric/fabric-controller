package msf.fc.node.nodes.spines;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcEmControlStatus;
import msf.fc.common.constant.EcNodeOperationAction;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.NodeManager;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;
import msf.fc.rest.common.EcControlStatusUtil;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeUpdateEcRequestBody;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeUpdateEcResponseBody;
import msf.fc.rest.ec.node.nodes.spine.data.entity.DelNodeOptionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.InternalLinkIfRemoveEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.LagIfRemoveEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.OppositeNodeRemoveEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.PhysicalIfIdEcEntity;
import msf.fc.traffic.TrafficManager;

public class SpineNodeDeleteRunner extends AbstractSpineNodeRunnerBase {

  private SpineNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  private ErrorCode ecResponseStatus = null;
  private EcEmControlStatus ecEmControlStatus = null;

  public SpineNodeDeleteRunner(SpineNodeRequest request) {
    this.request = request;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.performance("start wait to node increasing/decreasing process.");
      synchronized (NodeManager.getInstance().getNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait to node increasing/decreasing process.");
        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          NodeDao nodeDao = new NodeDao();

          List<Node> nodes = nodeDao.readList(sessionWrapper, request.getClusterId());

          Node deletedNode = getNode(nodes);

          List<Node> oppositeNodes = createOppositeNodeList(nodes, NodeType.LEAF);

          logger.performance("start get leaf/spine resources lock.");
          sessionWrapper.beginTransaction();
          List<Node> deletedNodes = new ArrayList<>();
          deletedNodes.add(deletedNode);
          DbManager.getInstance().getResourceLock(null, null, oppositeNodes, deletedNodes, sessionWrapper);
          logger.performance("end get leaf/spine resources lock.");

          nodes = nodeDao.readList(sessionWrapper, request.getClusterId());
          deletedNode = getNode(nodes);
          oppositeNodes = createOppositeNodeList(nodes, NodeType.LEAF);

          checkNodeAfterLock(nodes, deletedNode.getNodeInfoId());

          DelNodeOptionEcEntity delNodeOption = deleteOppositeNode(sessionWrapper, oppositeNodes,
              deletedNode.getNodeId());

          nodeDao.delete(sessionWrapper, deletedNode.getNodeInfoId());

          sendSpineNodeDelete(delNodeOption);

          responseBase = responseSpineNodeDeleteAsyncData();

          try {
            sessionWrapper.commit();
          } catch (MsfException msfException) {
            logger.warn(msfException.getMessage(), msfException);
            ecEmControlStatus = EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus);
            try {
              sessionWrapper.rollback();
            } catch (MsfException msfException2) {
              logger.warn(msfException2.getMessage(), msfException2);
            }
          }
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        if (ecEmControlStatus != null) {
          commitErrorAfterEcControl(ecEmControlStatus);
        }

        TrafficManager.getInstance().setRenewTopology(true);

        checkNodeDeleteEcError(ecResponseStatus);

        return responseBase;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Node getNode(List<Node> nodes) throws MsfException {
    try {
      logger.methodStart();
      Node node = null;
      for (Node nodeItem : nodes) {
        if ((nodeItem.getNodeId().equals(Integer.parseInt(request.getNodeId())))
            && (nodeItem.getNodeTypeEnum().equals(NodeType.SPINE))) {
          node = nodeItem;
          break;
        }
      }
      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  private List<Node> createOppositeNodeList(List<Node> nodes, NodeType nodeType) {
    try {
      logger.methodStart();
      List<Node> oppositeNodes = new ArrayList<>();
      for (Node nodeItem : nodes) {
        if (nodeItem.getNodeTypeEnum().equals(nodeType)) {
          oppositeNodes.add(nodeItem);
        }
      }
      return oppositeNodes;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkNodeAfterLock(List<Node> nodes, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart();
      for (Node node : nodes) {
        if ((!node.getNodeInfoId().equals(nodeInfoId))
            && (!node.getProvisioningStatusEnum().equals(ProvisioningStatus.BOOT_COMPLETE))) {
          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
              "There is nodes of the other in a different state.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private DelNodeOptionEcEntity deleteOppositeNode(SessionWrapper sessionWrapper, List<Node> oppositeNodes,
      Integer deleteNodeId) throws MsfException {
    try {
      logger.methodStart();
      DelNodeOptionEcEntity delNodeOption = new DelNodeOptionEcEntity();
      List<OppositeNodeRemoveEcEntity> oppositeNodeList = new ArrayList<>();
      for (Node oppositeNode : oppositeNodes) {
        List<OppositeNodeRemoveEcEntity> oppositeNodeEcEntityList = deleteLagIfs(sessionWrapper, oppositeNode,
            deleteNodeId);
        oppositeNodeList.addAll(oppositeNodeEcEntityList);
        updatePhysicalIfs(sessionWrapper, oppositeNode, deleteNodeId);
      }
      delNodeOption.setOppositeNodeList(oppositeNodeList);
      return delNodeOption;
    } finally {
      logger.methodEnd();
    }
  }

  private List<OppositeNodeRemoveEcEntity> deleteLagIfs(SessionWrapper sessionWrapper, Node oppositeNode,
      Integer deleteNodeId) throws MsfException {
    try {
      logger.methodStart();
      LagIfDao lagIfDao = new LagIfDao();
      List<OppositeNodeRemoveEcEntity> oppositeNodeEcEntityList = new ArrayList<>();
      for (LagIf lagIf : oppositeNode.getLagIfs()) {
        if (lagIf.getOppositeNode() != null) {
          if (lagIf.getOppositeNode().getNodeId().equals(deleteNodeId)) {
            OppositeNodeRemoveEcEntity oppositeNodeEcEntity = new OppositeNodeRemoveEcEntity();
            oppositeNodeEcEntity.setNodeId(String.valueOf(oppositeNode.getNodeId()));
            InternalLinkIfRemoveEcEntity internalLinkIf = new InternalLinkIfRemoveEcEntity();
            internalLinkIf.setInternalLinkIfId(String.valueOf(lagIf.getInternalLinkIf().getInternalLinkIfId()));
            LagIfRemoveEcEntity lagIfEcEntity = new LagIfRemoveEcEntity();
            lagIfEcEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
            List<PhysicalIfIdEcEntity> physicalIfList = new ArrayList<>();
            for (LagConstruction lagConstruction : lagIf.getLagConstructions()) {
              PhysicalIfIdEcEntity physicalIfIdEcEntity = new PhysicalIfIdEcEntity();
              physicalIfIdEcEntity.setPhysicalIfId(lagConstruction.getPhysicalIf().getPhysicalIfId());
              physicalIfList.add(physicalIfIdEcEntity);
            }
            lagIfEcEntity.setPhysicalIfList(physicalIfList);
            lagIfEcEntity.setMinimumLinks(physicalIfList.size());
            internalLinkIf.setLagIf(lagIfEcEntity);
            oppositeNodeEcEntity.setInternalLinkIf(internalLinkIf);
            oppositeNodeEcEntityList.add(oppositeNodeEcEntity);

            lagIfDao.delete(sessionWrapper, lagIf.getLagIfInfoId());
          }
        }
      }
      return oppositeNodeEcEntityList;
    } finally {
      logger.methodEnd();
    }
  }

  private void updatePhysicalIfs(SessionWrapper sessionWrapper, Node oppositeNode, Integer deleteNodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "oppositeNode", "deleteNodeId" },
          new Object[] { sessionWrapper, oppositeNode, deleteNodeId });
      PhysicalIfDao physicalIfDao = new PhysicalIfDao();
      for (PhysicalIf physicalIf : oppositeNode.getPhysicalIfs()) {
        if ((physicalIf.getOppositeNode() != null) && (physicalIf.getOppositeNode().getNodeId().equals(deleteNodeId))) {
          physicalIf.setOppositeNode(null);
          physicalIf.setOppositePhysicalIfId(null);
          physicalIf.setSpeed(null);
          physicalIfDao.update(sessionWrapper, physicalIf);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private SpineNodeUpdateEcResponseBody sendSpineNodeDelete(DelNodeOptionEcEntity delNodeOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "delNodeOption" }, new Object[] { delNodeOption });
      RestResponseBase restResponseBase = null;
      SpineNodeUpdateEcRequestBody spineNodeUpdateEcRequestBody = new SpineNodeUpdateEcRequestBody();
      spineNodeUpdateEcRequestBody.setActionEnum(EcNodeOperationAction.DEL_NODE);
      spineNodeUpdateEcRequestBody.setDelNodeOption(delNodeOption);

      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(spineNodeUpdateEcRequestBody));
      try {
        restResponseBase = RestClient.sendRequest(EcRequestUri.SPINE_NODE_UPDATE.getHttpMethod(),
            EcRequestUri.SPINE_NODE_UPDATE.getUri(request.getNodeId()), restRequest);
      } catch (MsfException msfException) {
        logger.warn(msfException.getMessage(), msfException);
        if (msfException.getErrorCode() == ErrorCode.EC_CONTROL_TIMEOUT) {
          ecResponseStatus = msfException.getErrorCode();
          return null;
        } else if (msfException.getErrorCode() == ErrorCode.EC_CONNECTION_ERROR) {
          throw msfException;
        } else {
          throw msfException;
        }
      }

      SpineNodeUpdateEcResponseBody spineNodeDeleteEcResponseBody = new SpineNodeUpdateEcResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        try {
          spineNodeDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
              SpineNodeUpdateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        } catch (MsfException msfException) {
          logger.warn(msfException.getMessage(), msfException);
          if (msfException.getErrorCode() == ErrorCode.EC_CONTROL_ERROR) {
            ecResponseStatus = msfException.getErrorCode();
            return null;
          } else {
            throw msfException;
          }
        }
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), spineNodeDeleteEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        ecResponseStatus = checkEcEmControlErrorCodeAfterNodeDelete(spineNodeDeleteEcResponseBody.getErrorCode());
      }

      return spineNodeDeleteEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeDeleteAsyncData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}