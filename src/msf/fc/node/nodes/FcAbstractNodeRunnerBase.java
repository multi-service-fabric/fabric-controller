
package msf.fc.node.nodes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.SwCluster;
import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeDeleteNodesEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkDeleteEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkIfDeleteEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeOppositeNodeDeleteEcEntity;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeBootStatus;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeSubStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.AbstractNodeRunnerBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of the node-related
 * asynchronous processing in the configuration management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractNodeRunnerBase extends AbstractNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractNodeRunnerBase.class);
  protected static final Integer MAX_BORDER_LEAF_NUM = 2;

  protected static final Integer INTERNAL_LINK_IF_PREFIX = 30;
  protected static final Integer MANAGEMENT_ADDRESS_PREFIX = 32;
  protected static final Integer LOOPBACK_ADDRESS_PREFIX = 32;

  protected static final Integer EC_LEAF_NODE_START_ID = 0;
  protected static final Integer EC_SPINE_NODE_START_ID = 100;
  protected static final Integer EC_RR_NODE_START_ID = 200;

  private static Pattern ecErrorRollBackPattern = Pattern.compile("^80[0-9]{4}$");

  protected Integer createEcNodeId(Integer nodeId, NodeType nodeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeId", "nodeType" }, new Object[] { nodeId, nodeType });
      Integer ecNodeId = null;
      switch (nodeType) {
        case LEAF:
          ecNodeId = nodeId + EC_LEAF_NODE_START_ID;
          break;

        case SPINE:
          ecNodeId = nodeId + EC_SPINE_NODE_START_ID;
          break;

        case RR:
          ecNodeId = nodeId + EC_RR_NODE_START_ID;
          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter. nodeType = " + nodeType);
      }
      return ecNodeId;
    } finally {
      logger.methodEnd();
    }
  }

  protected static Map<String, String> createLagIfIds(int leafId, int spineId) {
    try {
      logger.methodStart(new String[] { "leafId", "spineId" }, new Object[] { leafId, spineId });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      int nsi = swCluster.getMaxSpineNum();

      int leafLagIfId = 4 * (nsi * (leafId - 1) + spineId - 1) + 1;

      int spineLagIfId = 4 * (nsi * (leafId - 1) + spineId - 1) + 2;

      Map<String, String> retMap = new HashMap<String, String>();
      retMap.put(IpAddressUtil.LEAF, String.valueOf(leafLagIfId));
      retMap.put(IpAddressUtil.SPINE, String.valueOf(spineLagIfId));

      return retMap;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeAfterLock(List<FcNode> nodes, FcNodeDao nodeDao, SessionWrapper sessionWrapper,
      NodeType nodeType, String nodeId, boolean isBorderLeafCreate, List<FcNode> borderLeafNodeList)
      throws MsfException {

    try {
      logger.methodStart();
      int spineNode = 0;
      int leafNode = 0;
      int rrNode = 0;
      for (FcNode node : nodes) {
        switch (NodeType.getEnumFromCode(node.getNodeType())) {
          case SPINE:
            spineNode++;
            break;

          case LEAF:
            leafNode++;
            break;

          case RR:
            rrNode++;
            break;

          default:
            throw new IllegalArgumentException();
        }
      }
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      if (null == swCluster) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is no information of the swCluster.");
      }
      switch (nodeType) {
        case SPINE:
          if (spineNode >= swCluster.getMaxSpineNum()) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "It exceeds the maximum number of Spine node.");
          }
          break;

        case LEAF:
          if (leafNode >= swCluster.getMaxLeafNum()) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "It exceeds the maximum number of Leaf node.");
          }
          break;

        case RR:
          if (rrNode >= swCluster.getMaxRrNum()) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "It exceeds the maximum number of RR node.");
          }
          break;

        default:
          throw new IllegalArgumentException();
      }

      if (null != nodeDao.read(sessionWrapper, nodeType.getCode(), Integer.valueOf(nodeId))) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, "target resouece already exist. target = node");
      }

      if (isBorderLeafCreate) {
        if (borderLeafNodeList.size() >= MAX_BORDER_LEAF_NUM) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "It exceeds the maximum number of B-Leaf node.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected FcPhysicalIf checkPhisicalIf(SessionWrapper sessionWrapper, FcNode fcNode, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "physicalIfId" }, new Object[] { fcNode, physicalIfId });
      FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
          physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = physicalIf");
      }
      if ((CollectionUtils.isNotEmpty(fcPhysicalIf.getClusterLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcPhysicalIf.getInternalLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcPhysicalIf.getEdgePoints()))) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified physical If is used for other.");
      }
      return fcPhysicalIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcBreakoutIf checkBreakoutIf(SessionWrapper sessionWrapper, FcNode fcNode, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfId" }, new Object[] { fcNode, breakoutIfId });
      FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();
      FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(),
          breakoutIfId);
      if (fcBreakoutIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = breakoutIf");
      }
      if ((CollectionUtils.isNotEmpty(fcBreakoutIf.getClusterLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcBreakoutIf.getEdgePoints()))) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified breakout If is used for other.");
      }
      return fcBreakoutIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected void updateNodeSubStatus(SessionWrapper sessionWrapper, NodeSubStatus nodeSubStatus, String operationId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeSubStatus", "operationId" }, new Object[] { nodeSubStatus, operationId });
      FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
      FcAsyncRequest fcAsyncRequest = fcAsyncRequestsDao.read(sessionWrapper, operationId);

      fcAsyncRequest.setSubStatus(nodeSubStatus.getMessage());
      fcAsyncRequestsDao.update(sessionWrapper, fcAsyncRequest);
    } finally {
      logger.methodEnd();
    }
  }

  protected void nodeNotifyCompleteProcess(FcNode fcNode, NodeBootStatus nodeBootStatus, RestResponseBase restResBase)
      throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();
      FcAsyncRequest targetAsyncRequest = null;
      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();
        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
        List<FcAsyncRequest> readListExecNodeInfo = fcAsyncRequestsDao.readListExecNodeInfo(sessionWrapper);

        targetAsyncRequest = getTargetAsyncRequest(readListExecNodeInfo, fcNode);

        switch (nodeBootStatus) {
          case FAILED:
            targetAsyncRequest.setStatusEnum(AsyncProcessStatus.FAILED);
            break;
          case CANCEL:
            targetAsyncRequest.setStatusEnum(AsyncProcessStatus.CANCELED);
            break;
          case SUCCESS:
            targetAsyncRequest.setStatusEnum(AsyncProcessStatus.COMPLETED);
            break;
          default:

            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter. nodeBootStatus = " + nodeBootStatus);
        }
        targetAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        targetAsyncRequest.setResponseStatusCode(restResBase.getHttpStatusCode());
        targetAsyncRequest.setResponseBody(restResBase.getResponseBody());

        fcAsyncRequestsDao.update(sessionWrapper, targetAsyncRequest);

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

        OperationManager.getInstance().releaseOperationId(targetAsyncRequest.getOperationId());

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      try {

        notifyOperationResult(createOperationNotifyBody(targetAsyncRequest.getCommonEntity()));
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected FcAsyncRequest getTargetAsyncRequest(List<FcAsyncRequest> readListExecNodeInfo, FcNode fcNode)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "readListExecNodeInfo", "createFcNode" },
          new Object[] { readListExecNodeInfo, fcNode });
      FcAsyncRequest targetAsyncRequest = null;
      for (FcAsyncRequest fcAsyncRequest : readListExecNodeInfo) {
        Matcher nodeMattcher;
        switch (fcAsyncRequest.getRequestMethodEnum()) {
          case POST:
            if (NodeType.getEnumFromCode(fcNode.getNodeType()).equals(NodeType.LEAF)) {
              nodeMattcher = MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
              if (nodeMattcher.matches()) {

                targetAsyncRequest = fcAsyncRequest;
              }
            } else {
              nodeMattcher = MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
              if (nodeMattcher.matches()) {

                targetAsyncRequest = fcAsyncRequest;
              }
            }
            break;

          case PUT:
            nodeMattcher = MfcFcRequestUri.LEAF_NODE_UPDATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
            if (nodeMattcher.matches()) {

              targetAsyncRequest = fcAsyncRequest;
            }
            break;

          default:

            break;
        }
        if (targetAsyncRequest != null) {

          break;
        }
      }
      if (targetAsyncRequest == null) {

        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "targetAsyncRequest == null");
      }
      return targetAsyncRequest;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<FcNode> getOppositeNodeList(List<FcNode> fcNodeList, NodeType oppositeNodeType) {
    try {
      logger.methodStart();
      List<FcNode> oppositeSpineNodeList = new ArrayList<>();
      for (FcNode fcNode : fcNodeList) {
        if (NodeType.getEnumFromCode(fcNode.getNodeType()).equals(oppositeNodeType)) {
          oppositeSpineNodeList.add(fcNode);
        }
      }
      return oppositeSpineNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkDeleteNodeIfs(FcNode deleteNode) throws MsfException {

    try {
      logger.methodStart();

      checkDeleteNodePhysicalIfs(deleteNode);

      checkDeleteNodeBreakoutIfs(deleteNode);

      checkDeleteNodeLagIfs(deleteNode);
    } finally {
      logger.methodEnd();
    }
  }

  private void checkDeleteNodePhysicalIfs(FcNode deleteNode) throws MsfException {
    try {
      logger.methodStart();
      for (FcPhysicalIf fcPhysicalIf : deleteNode.getPhysicalIfs()) {
        if (!fcPhysicalIf.getClusterLinkIfs().isEmpty()) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
              "target node is related to the cluster link interfaces.");
        }

        if (!fcPhysicalIf.getEdgePoints().isEmpty()) {

          if ((!fcPhysicalIf.getEdgePoints().get(0).getL2Cps().isEmpty())
              || (!fcPhysicalIf.getEdgePoints().get(0).getL3Cps().isEmpty())) {

            throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "target node is related to the l2/l3 cps.");
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkDeleteNodeBreakoutIfs(FcNode deleteNode) throws MsfException {
    try {
      logger.methodStart();
      for (FcBreakoutIf fcBreakoutIf : deleteNode.getBreakoutIfs()) {
        if (!fcBreakoutIf.getClusterLinkIfs().isEmpty()) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
              "target node is related to the cluster link interfaces.");
        }

        if (!fcBreakoutIf.getEdgePoints().isEmpty()) {

          if ((!fcBreakoutIf.getEdgePoints().get(0).getL2Cps().isEmpty())
              || (!fcBreakoutIf.getEdgePoints().get(0).getL3Cps().isEmpty())) {

            throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "target node is related to the l2/l3 cps.");
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkDeleteNodeLagIfs(FcNode deleteNode) throws MsfException {
    try {
      logger.methodStart();
      for (FcLagIf fcLagIf : deleteNode.getLagIfs()) {
        if (!fcLagIf.getClusterLinkIfs().isEmpty()) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
              "target node is related to the cluster link interfaces.");
        }

        if (!fcLagIf.getEdgePoints().isEmpty()) {

          if ((!fcLagIf.getEdgePoints().get(0).getL2Cps().isEmpty())
              || (!fcLagIf.getEdgePoints().get(0).getL3Cps().isEmpty())) {

            throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "target node is related to the l2/l3 cps.");
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected NodeDeleteNodesEcEntity deleteOppositeInternalIfs(SessionWrapper sessionWrapper, FcNode deleteNode,
      List<FcNode> oppositeNodeList, InternalNodeType internalNodeType) throws MsfException {
    try {
      logger.methodStart();

      NodeDeleteNodesEcEntity nodeDeleteNodesEcEntity = new NodeDeleteNodesEcEntity();
      nodeDeleteNodesEcEntity.setNodeId(String.valueOf(deleteNode.getEcNodeId()));
      nodeDeleteNodesEcEntity.setNodeType(internalNodeType.getMessage());
      List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList = new ArrayList<>();
      if (!oppositeNodeList.isEmpty()) {
        FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
        FcLagIfDao fcLagIfDao = new FcLagIfDao();

        deleteOppositePhysicalInternalIfs(sessionWrapper, deleteNode, fcInternalLinkIfDao, oppositeNodeDeleteList);

        deleteOppositeBreakoutInternalIfs(sessionWrapper, deleteNode, fcInternalLinkIfDao, oppositeNodeDeleteList);

        deleteOppositeLagInternalIfs(sessionWrapper, deleteNode, fcInternalLinkIfDao, fcLagIfDao,
            oppositeNodeDeleteList);
      }
      nodeDeleteNodesEcEntity.setOppositeNodeDeleteList(oppositeNodeDeleteList);
      return nodeDeleteNodesEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteOppositePhysicalInternalIfs(SessionWrapper sessionWrapper, FcNode deleteNode,
      FcInternalLinkIfDao fcInternalLinkIfDao, List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList)
      throws MsfException {
    try {
      logger.methodStart();

      for (FcPhysicalIf fcPhysicalIf : deleteNode.getPhysicalIfs()) {
        if (!fcPhysicalIf.getInternalLinkIfs().isEmpty()) {

          FcInternalLinkIf oppositeInternalLinkIf = fcPhysicalIf.getInternalLinkIfs().get(0)
              .getOppositeInternalLinkIfs().get(0);
          fcInternalLinkIfDao.delete(sessionWrapper, oppositeInternalLinkIf.getInternalLinkIfId());

          oppositeNodeDeleteList.add(getNodeOppositeNodeDeleteEcEntity(oppositeInternalLinkIf));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteOppositeBreakoutInternalIfs(SessionWrapper sessionWrapper, FcNode deleteNode,
      FcInternalLinkIfDao fcInternalLinkIfDao, List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList)
      throws MsfException {
    try {
      logger.methodStart();

      for (FcBreakoutIf fcBreakoutIf : deleteNode.getBreakoutIfs()) {
        if (!fcBreakoutIf.getInternalLinkIfs().isEmpty()) {

          FcInternalLinkIf oppositeInternalLinkIf = fcBreakoutIf.getInternalLinkIfs().get(0)
              .getOppositeInternalLinkIfs().get(0);
          fcInternalLinkIfDao.delete(sessionWrapper, oppositeInternalLinkIf.getInternalLinkIfId());

          oppositeNodeDeleteList.add(getNodeOppositeNodeDeleteEcEntity(oppositeInternalLinkIf));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteOppositeLagInternalIfs(SessionWrapper sessionWrapper, FcNode deleteNode,
      FcInternalLinkIfDao fcInternalLinkIfDao, FcLagIfDao fcLagIfDao,
      List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList) throws MsfException {
    try {
      logger.methodStart();
      for (FcLagIf fcLagIf : deleteNode.getLagIfs()) {
        if (!fcLagIf.getInternalLinkIfs().isEmpty()) {

          FcInternalLinkIf oppositeInternalLinkIf = fcLagIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
              .get(0);
          fcInternalLinkIfDao.delete(sessionWrapper, oppositeInternalLinkIf.getInternalLinkIfId());

          fcLagIfDao.delete(sessionWrapper, oppositeInternalLinkIf.getLagIf().getLagIfId());

          oppositeNodeDeleteList.add(getNodeOppositeNodeDeleteEcEntity(oppositeInternalLinkIf));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private NodeOppositeNodeDeleteEcEntity getNodeOppositeNodeDeleteEcEntity(FcInternalLinkIf oppositeInternalLinkIf)
      throws MsfException {
    try {
      logger.methodStart();
      NodeOppositeNodeDeleteEcEntity nodeOppositeNodeDeleteEcEntity = new NodeOppositeNodeDeleteEcEntity();
      NodeInternalLinkDeleteEcEntity internalLinkIfDelete = new NodeInternalLinkDeleteEcEntity();
      NodeInternalLinkIfDeleteEcEntity nodeInternalLinkIfDeleteEcEntity = new NodeInternalLinkIfDeleteEcEntity();
      if (oppositeInternalLinkIf.getPhysicalIf() != null) {
        nodeOppositeNodeDeleteEcEntity
            .setNodeId(String.valueOf(oppositeInternalLinkIf.getPhysicalIf().getNode().getEcNodeId()));
        nodeInternalLinkIfDeleteEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
        nodeInternalLinkIfDeleteEcEntity.setIfId(oppositeInternalLinkIf.getPhysicalIf().getPhysicalIfId());
      } else if (oppositeInternalLinkIf.getBreakoutIf() != null) {
        nodeOppositeNodeDeleteEcEntity
            .setNodeId(String.valueOf(oppositeInternalLinkIf.getBreakoutIf().getNode().getEcNodeId()));
        nodeInternalLinkIfDeleteEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
        nodeInternalLinkIfDeleteEcEntity.setIfId(oppositeInternalLinkIf.getBreakoutIf().getBreakoutIfId());
      } else {
        nodeOppositeNodeDeleteEcEntity
            .setNodeId(String.valueOf(oppositeInternalLinkIf.getLagIf().getNode().getEcNodeId()));
        nodeInternalLinkIfDeleteEcEntity.setIfType(InterfaceType.LAG_IF.getMessage());
        nodeInternalLinkIfDeleteEcEntity.setIfId(String.valueOf(oppositeInternalLinkIf.getLagIf().getLagIfId()));
      }
      internalLinkIfDelete.setInternalLinkIfDelete(nodeInternalLinkIfDeleteEcEntity);
      nodeOppositeNodeDeleteEcEntity.setInternalLinkIfDelete(internalLinkIfDelete);
      return nodeOppositeNodeDeleteEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeDeleteEcError(ErrorCode ecResponseStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecResponseStatus" }, new Object[] { ecResponseStatus });
      if (ecResponseStatus != null) {
        String errorMsg = "Check Node Delete EC Error.";
        switch (ecResponseStatus) {
          case EC_CONTROL_ERROR:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
          case EC_CONTROL_TIMEOUT:
            throw new MsfException(ErrorCode.EC_CONTROL_TIMEOUT, errorMsg);
          case EC_CONTROL_ERROR_EM_CONTROL_COMPLETED:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, errorMsg);
          default:
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected FcNode getDeleteNode(List<FcNode> fcNodeList, NodeType nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "nodeType", "nodeId" },
          new Object[] { fcNodeList, nodeType, nodeId });
      FcNode deleteFcNode = null;
      for (FcNode fcNode : fcNodeList) {
        if ((fcNode.getNodeTypeEnum().equals(nodeType)) && (fcNode.getNodeId().equals(nodeId))) {

          deleteFcNode = fcNode;
          break;
        }
      }
      if (deleteFcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcNode");
      }
      return deleteFcNode;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcEquipment getEquipment(SessionWrapper sessionWrapper, Integer equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();
      FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();
      FcEquipment fcEquipment = fcEquipmentDao.read(sessionWrapper, equipmentTypeId);
      if (fcEquipment == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcEquipment");
      }
      return fcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  protected ArrayList<String> getPhysicalIfIds(EquipmentReadEcResponseBody sendEquipmentRead) {
    try {
      logger.methodStart(new String[] { "sendEquipmentRead" }, new Object[] { sendEquipmentRead });
      ArrayList<String> physicalIfIds = new ArrayList<>();
      for (EquipmentIfEcEntity equipmentIfEcEntity : sendEquipmentRead.getEquipment().getEquipmentIfList()) {
        physicalIfIds.add(equipmentIfEcEntity.getPhysicalIfId());
      }

      return physicalIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  protected EquipmentReadEcResponseBody sendEquipmentRead(FcEquipment fcEquipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcEquipment" }, new Object[] { fcEquipment });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_READ.getHttpMethod(),
          EcRequestUri.EQUIPMENT_READ.getUri(String.valueOf(fcEquipment.getEquipmentTypeId())), null,
          ecControlIpAddress, ecControlPort);

      EquipmentReadEcResponseBody equipmentReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          equipmentReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return equipmentReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get an error code of the occurred control error from enumerated values
   * corresponding to the error code string responded from the specified EC.
   *
   * @param ecErrorCode
   *          Error code responded from EC
   * @return EC control error enumeration values
   * @throws MsfException
   *           When the error code (80XXXX) to execute rollback is responded
   *           from EC
   */
  public static ErrorCode checkEcEmControlErrorCodeAfterNodeDelete(String ecErrorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecErrorCode" }, new Object[] { ecErrorCode });
      if (ecErrorCode != null && !ecErrorCode.isEmpty()) {

        if (ecErrorRollBackPattern.matcher(ecErrorCode).find()) {
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, "Roll back by EC control error.");
        } else {
          return ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }
}
