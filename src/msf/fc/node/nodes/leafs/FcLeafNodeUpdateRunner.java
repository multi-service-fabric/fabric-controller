
package msf.fc.node.nodes.leafs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLeafNode;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.util.FcIpAddressUtil;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.nodes.operation.data.NodeCreateDeleteEcRequestBody;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodePairNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeRangeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeUpdateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeUpdateNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeVirtualLinkEcEntity;
import msf.fc.rest.ec.node.recovernode.data.RecoverNodeCreateEcRequestBody;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverEquipmentEcEntity;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverNodeEcEntity;
import msf.mfcfc.common.constant.EcNodeOperationAction;
import msf.mfcfc.common.constant.EcNodeOperationUpdateAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateRequestBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in Leaf node update.
 *
 * @author NTT
 *
 */
public class FcLeafNodeUpdateRunner extends FcAbstractLeafNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeUpdateRunner.class);

  private LeafNodeRequest request;
  private LeafNodeUpdateRequestBody requestBody;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcLeafNodeUpdateRunner(LeafNodeRequest request, LeafNodeUpdateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to node increasing/decreasing process.");
      synchronized (FcNodeManager.getInstance().getFcNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait to node increasing/decreasing process.");
        logger.performance("start wait to node update process.");
        synchronized (FcNodeManager.getInstance().getFcNodeUpdateLockObject()) {
          logger.performance("end wait to node update process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();

          try {
            sessionWrapper.openSession();

            FcNodeDao fcNodeDao = new FcNodeDao();

            List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);
            FcNode updateNode = getUpdateNode(fcNodeList, NodeType.LEAF, Integer.valueOf(request.getNodeId()));

            List<FcNode> leafNodes = new ArrayList<>();
            leafNodes.add(updateNode);

            sessionWrapper.beginTransaction();

            switch (requestBody.getActionEnum()) {
              case CHG_LEAF_TYPE:

                responseBase = changeLeafTypeProcess(sessionWrapper, fcNodeList, updateNode, leafNodes);
                break;

              case RECOVER_NODE:

                responseBase = recoverNodeProcess(sessionWrapper, fcNodeList, updateNode, leafNodes);
                break;

              default:

                throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                    "leaf node update action = " + requestBody.getAction());
            }

          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase changeLeafTypeProcess(SessionWrapper sessionWrapper, List<FcNode> fcNodeList,
      FcNode updateNode, List<FcNode> leafNodes) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "updateNode", "leafNodes" },
          new Object[] { fcNodeList, updateNode, leafNodes });

      List<FcNode> borderLeafNodeList = getOtherBorderLeafNodeList(fcNodeList, updateNode.getNodeId());

      leafNodes.addAll(borderLeafNodeList);

      logger.performance("start get leaf resources lock.");
      FcDbManager.getInstance().getLeafsLock(leafNodes, sessionWrapper);
      logger.performance("end get leaf resources lock.");

      checkNodeAfterLock(updateNode, requestBody, borderLeafNodeList);

      NodeUpdateEcEntity nodeUpdateEcEntity = new NodeUpdateEcEntity();
      NodeUpdateNodeEcEntity nodeUpdateNodeEcEntity = new NodeUpdateNodeEcEntity();

      nodeUpdateNodeEcEntity.setNodeId(String.valueOf(updateNode.getEcNodeId()));
      int ospfArea = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getOspfArea();
      nodeUpdateNodeEcEntity.setClusterArea(String.valueOf(ospfArea));

      boolean isChgBorderLeaf = requestBody.getLeafTypeOption().getLeafTypeEnum().equals(LeafType.BORDER_LEAF);
      if (isChgBorderLeaf) {

        if (CollectionUtils.isNotEmpty(borderLeafNodeList)) {
          nodeUpdateEcEntity.setAction(EcNodeOperationUpdateAction.CHG_B_LEAF.getMessage());
        } else {
          nodeUpdateEcEntity.setAction(EcNodeOperationUpdateAction.ADD_OSPF_ROUTE.getMessage());
        }

        nodeUpdateNodeEcEntity.setNodeType(InternalNodeType.LEAF.getMessage());

        NodeRangeEcEntity range = new NodeRangeEcEntity();
        range.setAddress(FcIpAddressUtil.getXagri());
        range.setPrefix(FcIpAddressUtil.getPintrai());
        nodeUpdateNodeEcEntity.setRange(range);
      } else {

        if (CollectionUtils.isNotEmpty(borderLeafNodeList)) {
          nodeUpdateEcEntity.setAction(EcNodeOperationUpdateAction.CHG_LEAF.getMessage());
        } else {
          nodeUpdateEcEntity.setAction(EcNodeOperationUpdateAction.DELETE_OSPF_ROUTE.getMessage());
        }

        nodeUpdateNodeEcEntity.setNodeType(InternalNodeType.B_LEAF.getMessage());
      }

      if (CollectionUtils.isNotEmpty(borderLeafNodeList)) {

        for (FcNode fcNode : borderLeafNodeList) {

          NodePairNodeEcEntity nodePairNodeEcEntity = new NodePairNodeEcEntity();
          nodePairNodeEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));
          nodePairNodeEcEntity.setNodeType(InternalNodeType.B_LEAF.getMessage());
          nodePairNodeEcEntity.setClusterArea(String.valueOf(ospfArea));
          if (isChgBorderLeaf) {

            NodeVirtualLinkEcEntity nodeOppositeVirtualLinkEcEntity = new NodeVirtualLinkEcEntity();
            nodeOppositeVirtualLinkEcEntity.setNodeId(String.valueOf(updateNode.getEcNodeId()));
            nodePairNodeEcEntity.setVirtualLink(nodeOppositeVirtualLinkEcEntity);

            NodeVirtualLinkEcEntity nodeVirtualLinkEcEntity = new NodeVirtualLinkEcEntity();
            nodeVirtualLinkEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));
            nodeUpdateNodeEcEntity.setVirtualLink(nodeVirtualLinkEcEntity);
          }
          nodeUpdateEcEntity.setPairNode(nodePairNodeEcEntity);
        }
      }
      nodeUpdateEcEntity.setNode(nodeUpdateNodeEcEntity);

      FcLeafNode leafNode = updateNode.getLeafNode();
      if (isChgBorderLeaf) {
        leafNode.setLeafType(LeafType.BORDER_LEAF.getCode());
      } else {
        leafNode.setLeafType(LeafType.IP_VPN_LEAF.getCode());
      }
      updateNode.setLeafNode(leafNode);

      FcNodeDao fcNodeDao = new FcNodeDao();
      fcNodeDao.update(sessionWrapper, updateNode);

      sendLeafNodeUpdate(nodeUpdateEcEntity);

      RestResponseBase responseBase = responseLeafNodeUpdateData();

      sessionWrapper.commit();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase recoverNodeProcess(SessionWrapper sessionWrapper, List<FcNode> fcNodeList, FcNode updateNode,
      List<FcNode> leafNodes) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "updateNode", "leafNodes" },
          new Object[] { fcNodeList, updateNode, leafNodes });

      logger.performance("start get leaf resources lock.");
      FcDbManager.getInstance().getLeafsLock(leafNodes, sessionWrapper);
      logger.performance("end get leaf resources lock.");

      checkRecoverNodeAfterLock(sessionWrapper, requestBody);

      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody = createRecoverNodeData(updateNode, requestBody);

      sendLeafRecoverNode(updateNode, recoverNodeCreateEcRequestBody);

      sessionWrapper.rollback();

      RestResponseBase responseBase = responseLeafNodeUpdateData();

      setOperationEndFlag(false);

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getUpdateNode(List<FcNode> fcNodeList, NodeType nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "nodeType", "nodeId" },
          new Object[] { fcNodeList, nodeType, nodeId });
      FcNode updateFcNode = null;
      for (FcNode fcNode : fcNodeList) {
        if ((fcNode.getNodeTypeEnum().equals(nodeType)) && (fcNode.getNodeId().equals(nodeId))) {

          updateFcNode = fcNode;
          break;
        }
      }
      if (updateFcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = fcNode");
      }
      return updateFcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkNodeAfterLock(FcNode updateNode, LeafNodeUpdateRequestBody requestBody,
      List<FcNode> borderLeafNodeList) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "requestBody", "borderLeafNodeList" },
          new Object[] { updateNode, requestBody, borderLeafNodeList });

      if (!LeafNodeUpdateAction.CHG_LEAF_TYPE.equals(requestBody.getActionEnum())) {
        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "Update Error Exists.");
      }

      switch (requestBody.getLeafTypeOption().getLeafTypeEnum()) {
        case BORDER_LEAF:

          if (borderLeafNodeList.size() >= MAX_BORDER_LEAF_NUM) {
            throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "It exceeds the maximum number of B-Leaf node.");
          }

          if (LeafType.getEnumFromCode(updateNode.getLeafNode().getLeafType()).equals(LeafType.BORDER_LEAF)) {
            throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "Be not change in the identical node type.");
          }

          break;

        case IP_VPN_LEAF:

          for (FcPhysicalIf fcPhysicalIf : updateNode.getPhysicalIfs()) {
            if (CollectionUtils.isNotEmpty(fcPhysicalIf.getClusterLinkIfs())) {
              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                  "target node is related to the cluster link interfaces.");
            }
          }

          for (FcBreakoutIf fcBreakoutIf : updateNode.getBreakoutIfs()) {
            if (CollectionUtils.isNotEmpty(fcBreakoutIf.getClusterLinkIfs())) {
              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                  "target node is related to the cluster link interfaces.");
            }
          }

          for (FcLagIf fcLagIf : updateNode.getLagIfs()) {
            if (CollectionUtils.isNotEmpty(fcLagIf.getClusterLinkIfs())) {
              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                  "target node is related to the cluster link interfaces.");
            }
          }

          if (LeafType.getEnumFromCode(updateNode.getLeafNode().getLeafType()).equals(LeafType.IP_VPN_LEAF)) {
            throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "Be not change in the identical node type.");
          }

          break;
        default:

          throw new IllegalArgumentException(
              MessageFormat.format("LeafType={0}", requestBody.getLeafTypeOption().getLeafType()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLeafNodeUpdate(NodeUpdateEcEntity nodeUpdateEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeUpdateEcEntity" }, new Object[] { nodeUpdateEcEntity });

      NodeCreateDeleteEcRequestBody nodeCreateDeleteEcRequestBody = new NodeCreateDeleteEcRequestBody();
      nodeCreateDeleteEcRequestBody.setAction(EcNodeOperationAction.UPDATE_NODE.getMessage());
      nodeCreateDeleteEcRequestBody.setUpdateNodeOption(nodeUpdateEcEntity);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(nodeCreateDeleteEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(
          EcRequestUri.NODE_CREATE_DELETE_REQUEST.getHttpMethod(), EcRequestUri.NODE_CREATE_DELETE_REQUEST.getUri(),
          restRequest, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody nodeUpdateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = nodeUpdateEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkRecoverNodeAfterLock(SessionWrapper sessionWrapper, LeafNodeUpdateRequestBody requestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "requestBody" },
          new Object[] { sessionWrapper, requestBody });

      getEquipmentForRecoverNode(sessionWrapper,
          Integer.valueOf(requestBody.getRecoverNodeOption().getEquipmentTypeId()));
    } finally {
      logger.methodEnd();
    }
  }

  private FcEquipment getEquipmentForRecoverNode(SessionWrapper sessionWrapper, Integer equipmentTypeId)
      throws MsfException {
    try {
      logger.methodStart();
      FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();
      FcEquipment fcEquipment = fcEquipmentDao.read(sessionWrapper, equipmentTypeId);
      if (fcEquipment == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = fcEquipment");
      }
      return fcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  private RecoverNodeCreateEcRequestBody createRecoverNodeData(FcNode updateNode, LeafNodeUpdateRequestBody requestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "requestBody" }, new Object[] { updateNode, requestBody });
      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody = new RecoverNodeCreateEcRequestBody();
      RecoverEquipmentEcEntity equipment = new RecoverEquipmentEcEntity();
      equipment.setEquipmentTypeId(requestBody.getRecoverNodeOption().getEquipmentTypeId());
      recoverNodeCreateEcRequestBody.setEquipment(equipment);

      RecoverNodeEcEntity node = new RecoverNodeEcEntity();
      boolean isBorderLeaf = LeafType.getEnumFromCode(updateNode.getLeafNode().getLeafType())
          .equals(LeafType.BORDER_LEAF);
      node.setNodeType(isBorderLeaf ? InternalNodeType.B_LEAF.getMessage() : InternalNodeType.LEAF.getMessage());
      node.setUsername(requestBody.getRecoverNodeOption().getUsername());
      node.setPassword(requestBody.getRecoverNodeOption().getPassword());
      node.setMacAddr(requestBody.getRecoverNodeOption().getMacAddress());
      recoverNodeCreateEcRequestBody.setNode(node);
      return recoverNodeCreateEcRequestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLeafRecoverNode(FcNode updateNode,
      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "recoverNodeCreateEcRequestBody" },
          new Object[] { updateNode, recoverNodeCreateEcRequestBody });

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(recoverNodeCreateEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.SERVICE_RECOVERY_CREATE.getHttpMethod(),
          EcRequestUri.SERVICE_RECOVERY_CREATE.getUri(String.valueOf(updateNode.getEcNodeId())), restRequest,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody recoverNodeEcResponceBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = recoverNodeEcResponceBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.ACCEPTED_202, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeUpdateData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
