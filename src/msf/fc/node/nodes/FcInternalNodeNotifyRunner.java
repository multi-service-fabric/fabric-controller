
package msf.fc.node.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.node.FcNodeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeBootStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.ErrorResponse;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.data.InternalNodeRequest;
import msf.mfcfc.node.nodes.data.NodeNotifyRequestBody;
import msf.mfcfc.node.nodes.data.entity.NodeBreakoutBaseIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeCreateNodeIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeInternalLinkIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeLagMemberEntity;
import msf.mfcfc.node.nodes.data.entity.NodeOppositeNodeEntity;
import msf.mfcfc.node.nodes.data.entity.NodeUnusedPhysicalEntity;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateAsyncResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalLinkEntity;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeLagLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalLinkEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to implement the asynchronous processing in receiving startup
 * completion notification from the EC node.
 *
 * @author NTT
 *
 */
public class FcInternalNodeNotifyRunner extends FcAbstractNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalNodeNotifyRunner.class);

  private InternalNodeRequest request;
  private NodeNotifyRequestBody requestBody;

  private static final Integer FIRST_ID_NUM = 1;

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
  public FcInternalNodeNotifyRunner(InternalNodeRequest request, NodeNotifyRequestBody requestBody) {

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

          SessionWrapper sessionWrapper = new SessionWrapper();
          FcNode createFcNode = null;
          RestResponseBase restResponse = null;

          try {
            sessionWrapper.openSession();

            FcNodeDao fcNodeDao = new FcNodeDao();

            List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

            createFcNode = checkCreateNode(fcNodeList);

            sessionWrapper.beginTransaction();

            FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
            List<FcAsyncRequest> readListExecNodeInfo = fcAsyncRequestsDao.readListExecNodeInfo(sessionWrapper);

            FcAsyncRequest targetAsyncRequest = getTargetAsyncRequest(readListExecNodeInfo, createFcNode);
            switch (targetAsyncRequest.getRequestMethodEnum()) {
              case POST:

                restResponse = nodeCreateCompleteProcess(sessionWrapper, fcNodeList, createFcNode);
                break;

              case PUT:

                restResponse = recoverNodeCompleteProcess(sessionWrapper, createFcNode, targetAsyncRequest);
                break;

              default:

                throw new MsfException(ErrorCode.UNDEFINED_ERROR, "method = " + targetAsyncRequest.getRequestMethod());
            }
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            if (createFcNode != null) {

              nodeNotifyCompleteProcess(createFcNode, NodeBootStatus.FAILED,
                  new ErrorResponse(ErrorCode.FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED, SystemInterfaceType.EXTERNAL));
            }

            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          nodeNotifyCompleteProcess(createFcNode, NodeBootStatus.getEnumFromMessage(requestBody.getStatus()),
              restResponse);

          return new RestResponseBase(HttpStatus.OK_200, (String) null);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase nodeCreateCompleteProcess(SessionWrapper sessionWrapper, List<FcNode> fcNodeList,
      FcNode createFcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "createFcNode" }, new Object[] { fcNodeList, createFcNode });
      RestResponseBase restResponse = null;

      switch (NodeBootStatus.getEnumFromMessage(requestBody.getStatus())) {
        case FAILED:
        case CANCEL:
          FcNodeDao fcNodeDao = new FcNodeDao();

          getLockForNodeStatusFailure(sessionWrapper, createFcNode);

          fcNodeDao.delete(sessionWrapper, createFcNode.getNodeInfoId());

          restResponse = new ErrorResponse(ErrorCode.EC_CONTROL_ERROR, SystemInterfaceType.EXTERNAL);

          break;

        case SUCCESS:

          TreeMap<Integer, FcNode> oppositeNodeMap = getLockForNodeStatusSuccess(sessionWrapper, createFcNode,
              fcNodeList);

          FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
          FcLagIfDao fcLagIfDao = new FcLagIfDao();
          FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

          TreeMap<Integer, List<Object>> connectionIfMap = createNodeIfs(sessionWrapper, createFcNode, fcPhysicalIfDao,
              fcLagIfDao, fcBreakoutIfDao);

          TreeMap<Integer, List<Object>> connectionOppositeIfMap = createOppositeNodeIfs(sessionWrapper,
              oppositeNodeMap, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao);

          createInternalLinkIfs(sessionWrapper, connectionIfMap, connectionOppositeIfMap, createFcNode);

          LeafNodeCreateAsyncResponseBody body = new LeafNodeCreateAsyncResponseBody();
          body.setNodeId(String.valueOf(createFcNode.getNodeId()));
          restResponse = createRestResponse(body, HttpStatus.CREATED_201);

          break;
        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter = " + requestBody.getStatus());
      }

      sessionWrapper.commit();

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase recoverNodeCompleteProcess(SessionWrapper sessionWrapper, FcNode createFcNode,
      FcAsyncRequest targetAsyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "createFcNode", "targetAsyncRequest" },
          new Object[] { createFcNode, targetAsyncRequest });
      RestResponseBase restResponse = null;

      switch (NodeBootStatus.getEnumFromMessage(requestBody.getStatus())) {
        case FAILED:
        case CANCEL:

          restResponse = new ErrorResponse(ErrorCode.EC_CONTROL_ERROR, SystemInterfaceType.EXTERNAL);

          break;

        case SUCCESS:

          LeafNodeUpdateRequestBody targetAsyncRequestBody = JsonUtil.fromJson(targetAsyncRequest.getRequestBody(),
              LeafNodeUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

          Integer requestEquipmentTypeId = Integer
              .valueOf(targetAsyncRequestBody.getRecoverNodeOption().getEquipmentTypeId());

          if (!createFcNode.getEquipment().getEquipmentTypeId().equals(requestEquipmentTypeId)) {

            List<FcNode> nodes = new ArrayList<>();
            nodes.add(createFcNode);

            logger.performance("start get recover leaf resources lock.");
            FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
            logger.performance("end get recover leaf resources lock.");

            FcEquipment fcEquipment = new FcEquipment();
            fcEquipment.setEquipmentTypeId(requestEquipmentTypeId);

            createFcNode.setEquipment(fcEquipment);
            FcNodeDao fcNodeDao = new FcNodeDao();
            fcNodeDao.update(sessionWrapper, createFcNode);

            sessionWrapper.commit();
          }

          restResponse = new RestResponseBase(HttpStatus.OK_200, (String) null);

          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter = " + requestBody.getStatus());
      }
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private void createInternalLinkIfs(SessionWrapper sessionWrapper, TreeMap<Integer, List<Object>> connectionIfMap,
      TreeMap<Integer, List<Object>> connectionOppositeIfMap, FcNode fcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "connectionIfMap", "connectionOppositeIfMap" },
          new Object[] { connectionIfMap, connectionOppositeIfMap });
      FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
      Integer nextInternalLinkIfId = getNextInternalLinkIfId(sessionWrapper, fcInternalLinkIfDao);

      Map<String, Double> localTrafficThresholdMap = new HashMap<>();
      Map<String, Double> oppositTrafficThresholdMap = new HashMap<>();
      updateTrafficThresholdMap(sessionWrapper, fcNode, localTrafficThresholdMap, oppositTrafficThresholdMap);

      for (Entry<Integer, List<Object>> connectionIfEntry : connectionIfMap.entrySet()) {

        Double localTrafficThreshold = localTrafficThresholdMap.get(connectionIfEntry.getKey().toString());
        Double oppositeTrafficThreshold = oppositTrafficThresholdMap.get(connectionIfEntry.getKey().toString());

        FcInternalLinkIf fcInternalLinkIf = createInternalLinkIf(nextInternalLinkIfId, connectionIfEntry.getValue(),
            localTrafficThreshold);

        nextInternalLinkIfId++;

        FcInternalLinkIf fcOppositeInternalLinkIf = createInternalLinkIf(nextInternalLinkIfId,
            connectionOppositeIfMap.get(connectionIfEntry.getKey()), oppositeTrafficThreshold);

        nextInternalLinkIfId++;

        setOppositeInternalLinkIfs(fcInternalLinkIf, fcOppositeInternalLinkIf);
        setOppositeInternalLinkIfs(fcOppositeInternalLinkIf, fcInternalLinkIf);

        fcInternalLinkIfDao.create(sessionWrapper, fcInternalLinkIf);

        fcInternalLinkIfDao.create(sessionWrapper, fcOppositeInternalLinkIf);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateTrafficThresholdMap(SessionWrapper sessionWrapper, FcNode fcNode,
      Map<String, Double> localTrafficThresholdMap, Map<String, Double> oppositTrafficThresholdMap)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "localTrafficThresholdMap", "oppositTrafficThresholdMap" },
          new Object[] { fcNode, localTrafficThresholdMap, oppositTrafficThresholdMap });
      NodeType nodeType = fcNode.getNodeTypeEnum();
      FcAsyncRequest targetAsyncRequest = getTargetAsyncRequest(sessionWrapper, nodeType);

      if (NodeType.LEAF == nodeType) {
        updateTrafficThresholdMapLeaf(targetAsyncRequest, localTrafficThresholdMap, oppositTrafficThresholdMap);
      } else if (NodeType.SPINE == nodeType) {
        updateTrafficThresholdMapSpine(targetAsyncRequest, localTrafficThresholdMap, oppositTrafficThresholdMap);
      } else {

      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateTrafficThresholdMapLeaf(FcAsyncRequest targetAsyncRequest,
      Map<String, Double> localTrafficThresholdMap, Map<String, Double> oppositTrafficThresholdMap)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "targetAsyncRequest", "localTrafficThresholdMap", "oppositTrafficThresholdMap" },
          new Object[] { targetAsyncRequest, localTrafficThresholdMap, oppositTrafficThresholdMap });

      LeafNodeCreateRequestBody leafRequestBody = JsonUtil.fromJson(targetAsyncRequest.getRequestBody(),
          LeafNodeCreateRequestBody.class);
      LeafNodeInternalLinkEntity leafNodeInternalLink = leafRequestBody.getInternalLinks();

      if (leafNodeInternalLink != null) {
        if (leafNodeInternalLink.getPhysicalLinkList() != null) {
          List<LeafNodePhysicalLinkEntity> physicalLinkList = leafNodeInternalLink.getPhysicalLinkList();

          for (LeafNodePhysicalLinkEntity physicalLink : physicalLinkList) {
            String oppositeNodeId = physicalLink.getOppositeNodeId();
            localTrafficThresholdMap.put(oppositeNodeId, physicalLink.getOppositeTrafficThreshold());
            oppositTrafficThresholdMap.put(oppositeNodeId, physicalLink.getOppositeTrafficThreshold());
          }
        } else if (leafNodeInternalLink.getLagLinkList() != null) {
          List<LeafNodeLagLinkEntity> lagLinkList = leafNodeInternalLink.getLagLinkList();

          for (LeafNodeLagLinkEntity lagLink : lagLinkList) {
            String oppositeNodeId = lagLink.getOppositeNodeId();
            localTrafficThresholdMap.put(oppositeNodeId, lagLink.getOppositeTrafficThreshold());
            oppositTrafficThresholdMap.put(oppositeNodeId, lagLink.getOppositeTrafficThreshold());
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateTrafficThresholdMapSpine(FcAsyncRequest targetAsyncRequest,
      Map<String, Double> localTrafficThresholdMap, Map<String, Double> oppositTrafficThresholdMap)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "targetAsyncRequest", "localTrafficThresholdMap", "oppositTrafficThresholdMap" },
          new Object[] { targetAsyncRequest, localTrafficThresholdMap, oppositTrafficThresholdMap });

      SpineNodeCreateRequestBody spineRequestBody = JsonUtil.fromJson(targetAsyncRequest.getRequestBody(),
          SpineNodeCreateRequestBody.class);
      SpineNodeInternalLinkEntity spineNodeInternalLink = spineRequestBody.getInternalLinks();

      if (spineNodeInternalLink != null) {
        if (spineNodeInternalLink.getPhysicalLinkList() != null) {
          List<SpineNodePhysicalLinkEntity> physicalLinkList = spineNodeInternalLink.getPhysicalLinkList();

          for (SpineNodePhysicalLinkEntity physicalLink : physicalLinkList) {
            String oppositeNodeId = physicalLink.getOppositeNodeId();
            localTrafficThresholdMap.put(oppositeNodeId, physicalLink.getOppositeTrafficThreshold());
            oppositTrafficThresholdMap.put(oppositeNodeId, physicalLink.getOppositeTrafficThreshold());
          }
        } else if (spineNodeInternalLink.getLagLinkList() != null) {
          List<SpineNodeLagLinkEntity> lagLinkList = spineNodeInternalLink.getLagLinkList();

          for (SpineNodeLagLinkEntity lagLink : lagLinkList) {
            String oppositeNodeId = lagLink.getOppositeNodeId();
            localTrafficThresholdMap.put(oppositeNodeId, lagLink.getOppositeTrafficThreshold());
            oppositTrafficThresholdMap.put(oppositeNodeId, lagLink.getOppositeTrafficThreshold());
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcAsyncRequest getTargetAsyncRequest(SessionWrapper sessionWrapper, NodeType nodeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeType" }, new Object[] { nodeType });

      FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
      List<FcAsyncRequest> readListExecNodeInfo = fcAsyncRequestsDao.readListExecNodeInfo(sessionWrapper);

      for (FcAsyncRequest fcAsyncRequest : readListExecNodeInfo) {
        Matcher nodeMattcher;
        if (NodeType.LEAF == nodeType) {
          nodeMattcher = MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
          if (nodeMattcher.matches()) {
            return fcAsyncRequest;
          }
        } else {
          nodeMattcher = MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
          if (nodeMattcher.matches()) {
            return fcAsyncRequest;
          }
        }
      }

      throw new MsfException(ErrorCode.UNDEFINED_ERROR, "targetAsyncRequest == null");
    } finally {
      logger.methodEnd();
    }
  }

  private void setOppositeInternalLinkIfs(FcInternalLinkIf fcInternalLinkIf, FcInternalLinkIf fcOppositeInternalLinkIf)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcInternalLinkIf", "fcOppositeInternalLinkIf" },
          new Object[] { fcInternalLinkIf, fcOppositeInternalLinkIf });

      fcInternalLinkIf.setOppositeInternalLinkIfs(new ArrayList<>());
      fcInternalLinkIf.addOppositeInternalLinkIf(fcOppositeInternalLinkIf);
    } finally {
      logger.methodEnd();
    }
  }

  private FcInternalLinkIf createInternalLinkIf(Integer nextInternalLinkIfId, List<Object> internalLinkInfoList,
      Double trafficThreshold) {
    try {
      logger.methodStart(new String[] { "nextInternalLinkIfId", "value", "trafficThreshold" },
          new Object[] { nextInternalLinkIfId, internalLinkInfoList, trafficThreshold });
      FcInternalLinkIf fcInternalLinkIf = new FcInternalLinkIf();
      fcInternalLinkIf.setInternalLinkIfId(nextInternalLinkIfId);

      Object ifType = internalLinkInfoList.get(0);
      if (ifType instanceof FcPhysicalIf) {
        FcPhysicalIf fcPhysicalIf = (FcPhysicalIf) ifType;
        fcInternalLinkIf.setPhysicalIf(fcPhysicalIf);
      } else if (ifType instanceof FcLagIf) {
        FcLagIf fcLagIf = (FcLagIf) ifType;
        fcInternalLinkIf.setLagIf(fcLagIf);
      } else {
        FcBreakoutIf fcBreakoutIf = (FcBreakoutIf) ifType;
        fcInternalLinkIf.setBreakoutIf(fcBreakoutIf);
      }
      fcInternalLinkIf.setTrafficThreshold(trafficThreshold);

      fcInternalLinkIf.setIgpCost((int) internalLinkInfoList.get(1));
      return fcInternalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private Integer getNextInternalLinkIfId(SessionWrapper sessionWrapper, FcInternalLinkIfDao fcInternalLinkIfDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcInternalLinkIfDao" }, new Object[] { fcInternalLinkIfDao });
      FcInternalLinkIf biggestInternalLinkIfId = fcInternalLinkIfDao.readFromBiggestId(sessionWrapper);
      if (null == biggestInternalLinkIfId) {

        return FIRST_ID_NUM;
      } else {
        return biggestInternalLinkIfId.getInternalLinkIfId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<Integer, List<Object>> createOppositeNodeIfs(SessionWrapper sessionWrapper,
      TreeMap<Integer, FcNode> oppositeNodeMap, FcPhysicalIfDao fcPhysicalIfDao, FcLagIfDao fcLagIfDao,
      FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodeMap", "fcPhysicalIfDao", "fcLagIfDao", "fcBreakoutIfDao" },
          new Object[] { oppositeNodeMap, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao });
      List<NodeOppositeNodeEntity> oppositeNodeList = requestBody.getNodeInfo().getCreateNode().getOppositeNodeList();

      TreeMap<Integer, List<Object>> connectionOppositeIfMap = new TreeMap<>();

      if (oppositeNodeList != null) {
        for (NodeOppositeNodeEntity nodeOppositeNodeEntity : oppositeNodeList) {
          FcNode oppositeNode = oppositeNodeMap.get(Integer.valueOf(nodeOppositeNodeEntity.getNodeId()));
          if (oppositeNode == null) {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                "target resource is not found. parameters = opposite node");
          }

          ArrayList<String> internalIfForBreakoutIfIds = new ArrayList<>();

          createOppositeInternalInterfaces(sessionWrapper, oppositeNode, nodeOppositeNodeEntity, fcPhysicalIfDao,
              fcLagIfDao, fcBreakoutIfDao, connectionOppositeIfMap, internalIfForBreakoutIfIds);

          if (nodeOppositeNodeEntity.getBreakoutBaseIfList() != null) {

            for (NodeBreakoutBaseIfEntity nodeBreakoutBaseIfEntity : nodeOppositeNodeEntity.getBreakoutBaseIfList()) {
              for (String breakoutIfId : nodeBreakoutBaseIfEntity.getBreakoutIfIdList()) {
                if (!internalIfForBreakoutIfIds.contains(breakoutIfId)) {

                  FcBreakoutIf fcBreakoutIf = new FcBreakoutIf();
                  fcBreakoutIf.setNode(oppositeNode);
                  fcBreakoutIf.setBreakoutIfId(breakoutIfId);
                  fcBreakoutIfDao.create(sessionWrapper, fcBreakoutIf);
                }
              }
            }
          }
        }
      }
      return connectionOppositeIfMap;
    } finally {
      logger.methodEnd();
    }
  }

  private void createOppositeInternalInterfaces(SessionWrapper sessionWrapper, FcNode oppositeNode,
      NodeOppositeNodeEntity nodeOppositeNodeEntity, FcPhysicalIfDao fcPhysicalIfDao, FcLagIfDao fcLagIfDao,
      FcBreakoutIfDao fcBreakoutIfDao, TreeMap<Integer, List<Object>> connectionOppositeIfMap,
      ArrayList<String> internalIfForBreakoutIfIds) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "oppositeNode", "nodeOppositeNodeEntity", "fcPhysicalIfDao", "fcLagIfDao", "fcBreakoutIfDao",
              "connectionOppositeIfMap", "internalIfForBreakoutIfIds" },
          new Object[] { oppositeNode, nodeOppositeNodeEntity, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao,
              connectionOppositeIfMap, internalIfForBreakoutIfIds });
      Integer oppositeEcNodeId = oppositeNode.getEcNodeId();
      String ifId = nodeOppositeNodeEntity.getInternalLinkIf().getIfId();

      Integer oppositeCost = nodeOppositeNodeEntity.getInternalLinkIf().getCost();

      List<Object> oppositeInternalInfoList = new ArrayList<>();

      switch (InterfaceType.getEnumFromMessage(nodeOppositeNodeEntity.getInternalLinkIf().getIfType())) {
        case PHYSICAL_IF:
          FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, oppositeNode.getNodeType(),
              oppositeNode.getNodeId(), ifId);
          if (fcPhysicalIf == null) {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                "target resource is not found. parameters = opposite physicalIf");
          }

          oppositeInternalInfoList.add(fcPhysicalIf);
          oppositeInternalInfoList.add(oppositeCost);
          connectionOppositeIfMap.put(oppositeEcNodeId, oppositeInternalInfoList);
          break;

        case LAG_IF:
          FcLagIf fcLagIf = new FcLagIf();
          fcLagIf.setNode(oppositeNode);
          fcLagIf.setLagIfId(Integer.valueOf(ifId));

          fcLagIfDao.create(sessionWrapper, fcLagIf);

          oppositeInternalInfoList.add(fcLagIf);
          oppositeInternalInfoList.add(oppositeCost);
          connectionOppositeIfMap.put(oppositeEcNodeId, oppositeInternalInfoList);
          break;

        case BREAKOUT_IF:
          FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, oppositeNode.getNodeType(),
              oppositeNode.getNodeId(), ifId);
          if (fcBreakoutIf == null) {

            fcBreakoutIf = new FcBreakoutIf();
            fcBreakoutIf.setNode(oppositeNode);
            fcBreakoutIf.setBreakoutIfId(ifId);

            fcBreakoutIfDao.create(sessionWrapper, fcBreakoutIf);
          }

          internalIfForBreakoutIfIds.add(ifId);

          oppositeInternalInfoList.add(fcBreakoutIf);
          oppositeInternalInfoList.add(oppositeCost);
          connectionOppositeIfMap.put(oppositeEcNodeId, oppositeInternalInfoList);
          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter = " + nodeOppositeNodeEntity.getInternalLinkIf().getIfType());
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<Integer, List<Object>> createNodeIfs(SessionWrapper sessionWrapper, FcNode createFcNode,
      FcPhysicalIfDao fcPhysicalIfDao, FcLagIfDao fcLagIfDao, FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "createFcNode", "fcPhysicalIfDao", "fcLagIfDao", "fcBreakoutIfDao" },
          new Object[] { createFcNode, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao });

      TreeMap<Integer, List<Object>> connectionIfMap = new TreeMap<>();

      ArrayList<String> internalIfForBreakoutIfIds = new ArrayList<>();

      NodeCreateNodeIfEntity createNodeIf = requestBody.getNodeInfo().getCreateNode().getCreateNodeIf();

      createInternalInterfaces(sessionWrapper, createFcNode, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao, createNodeIf,
          connectionIfMap, internalIfForBreakoutIfIds);

      if (createNodeIf.getBreakoutBaseIfList() != null) {

        for (NodeBreakoutBaseIfEntity nodeBreakoutBaseIfEntity : createNodeIf.getBreakoutBaseIfList()) {

          FcPhysicalIf fcPhysicalIf = new FcPhysicalIf();
          fcPhysicalIf.setNode(createFcNode);
          fcPhysicalIf.setPhysicalIfId(nodeBreakoutBaseIfEntity.getBasePhysicalIfId());
          fcPhysicalIfDao.create(sessionWrapper, fcPhysicalIf);

          for (String breakoutIfId : nodeBreakoutBaseIfEntity.getBreakoutIfIdList()) {
            if (!internalIfForBreakoutIfIds.contains(breakoutIfId)) {

              FcBreakoutIf fcBreakoutIf = new FcBreakoutIf();
              fcBreakoutIf.setNode(createFcNode);
              fcBreakoutIf.setBreakoutIfId(breakoutIfId);
              fcBreakoutIfDao.create(sessionWrapper, fcBreakoutIf);
            }
          }

        }
      }
      if (createNodeIf.getUnusedPhysicalIfList() != null) {
        for (NodeUnusedPhysicalEntity nodeUnusedPhysicalEntity : createNodeIf.getUnusedPhysicalIfList()) {

          FcPhysicalIf fcPhysicalIf = new FcPhysicalIf();
          fcPhysicalIf.setNode(createFcNode);
          fcPhysicalIf.setPhysicalIfId(nodeUnusedPhysicalEntity.getPhysicalIfId());
          fcPhysicalIfDao.create(sessionWrapper, fcPhysicalIf);
        }
      }

      return connectionIfMap;
    } finally {
      logger.methodEnd();
    }
  }

  private void createInternalInterfaces(SessionWrapper sessionWrapper, FcNode createFcNode,
      FcPhysicalIfDao fcPhysicalIfDao, FcLagIfDao fcLagIfDao, FcBreakoutIfDao fcBreakoutIfDao,
      NodeCreateNodeIfEntity createNodeIf, TreeMap<Integer, List<Object>> connectionIfMap,
      ArrayList<String> internalIfForBreakoutIfIds) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "createFcNode", "fcPhysicalIfDao", "fcLagIfDao", "fcBreakoutIfDao", "createNodeIf",
              "connectionIfMap", "internalIfForBreakoutIfIds" },
          new Object[] { createFcNode, fcPhysicalIfDao, fcLagIfDao, fcBreakoutIfDao, createNodeIf, connectionIfMap,
              internalIfForBreakoutIfIds });
      List<NodeOppositeNodeEntity> oppositeNodeList = requestBody.getNodeInfo().getCreateNode().getOppositeNodeList();
      if (createNodeIf.getInternalLinkIfList() != null) {
        for (int i = 0; i < createNodeIf.getInternalLinkIfList().size(); i++) {
          NodeInternalLinkIfEntity nodeInternalLinkIfEntity = createNodeIf.getInternalLinkIfList().get(i);
          String ifId = nodeInternalLinkIfEntity.getInternalLinkIf().getIfId();

          Integer localCost = nodeInternalLinkIfEntity.getInternalLinkIf().getCost();

          List<Object> localInternalInfoList = new ArrayList<>();

          if (oppositeNodeList != null) {

            Integer oppositeEcNodeId = Integer.valueOf(oppositeNodeList.get(i).getNodeId());
            switch (InterfaceType.getEnumFromMessage(nodeInternalLinkIfEntity.getInternalLinkIf().getIfType())) {
              case PHYSICAL_IF:
                FcPhysicalIf fcPhysicalIf = new FcPhysicalIf();
                fcPhysicalIf.setNode(createFcNode);
                fcPhysicalIf.setPhysicalIfId(ifId);

                fcPhysicalIfDao.create(sessionWrapper, fcPhysicalIf);

                localInternalInfoList.add(fcPhysicalIf);
                localInternalInfoList.add(localCost);
                connectionIfMap.put(oppositeEcNodeId, localInternalInfoList);
                break;

              case LAG_IF:
                FcLagIf fcLagIf = new FcLagIf();
                fcLagIf.setNode(createFcNode);
                fcLagIf.setLagIfId(Integer.valueOf(ifId));

                fcLagIfDao.create(sessionWrapper, fcLagIf);

                localInternalInfoList.add(fcLagIf);
                localInternalInfoList.add(localCost);
                connectionIfMap.put(oppositeEcNodeId, localInternalInfoList);

                for (NodeLagMemberEntity nodeLagMemberEntity : nodeInternalLinkIfEntity.getInternalLinkIf()
                    .getLagMemberList()) {
                  if (InterfaceType.PHYSICAL_IF.equals(nodeLagMemberEntity.getIfTypeEnum())) {
                    FcPhysicalIf lagMenberPhysicalIf = new FcPhysicalIf();
                    lagMenberPhysicalIf.setNode(createFcNode);
                    lagMenberPhysicalIf.setPhysicalIfId(nodeLagMemberEntity.getIfId());
                    fcPhysicalIfDao.create(sessionWrapper, lagMenberPhysicalIf);
                  }
                }
                break;

              case BREAKOUT_IF:
                FcBreakoutIf fcBreakoutIf = new FcBreakoutIf();
                fcBreakoutIf.setNode(createFcNode);
                fcBreakoutIf.setBreakoutIfId(ifId);

                fcBreakoutIfDao.create(sessionWrapper, fcBreakoutIf);

                internalIfForBreakoutIfIds.add(ifId);

                localInternalInfoList.add(fcBreakoutIf);
                localInternalInfoList.add(localCost);
                connectionIfMap.put(oppositeEcNodeId, localInternalInfoList);
                break;

              default:

                throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                    "Illegal parameter = " + nodeInternalLinkIfEntity.getInternalLinkIf().getIfType());
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void getLockForNodeStatusFailure(SessionWrapper sessionWrapper, FcNode createFcNode) throws MsfException {
    try {
      logger.methodStart();
      List<FcNode> nodes = new ArrayList<>();
      nodes.add(createFcNode);
      if (NodeType.getEnumFromCode(createFcNode.getNodeType()).equals(NodeType.LEAF)) {
        logger.performance("start get leaf resources lock.");
        FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");
      } else {
        logger.performance("start get spine resources lock.");
        FcDbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
        logger.performance("end get spine resources lock.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TreeMap<Integer, FcNode> getLockForNodeStatusSuccess(SessionWrapper sessionWrapper, FcNode createFcNode,
      List<FcNode> fcNodeList) throws MsfException {
    try {
      logger.methodStart();
      List<FcNode> nodes = new ArrayList<>();
      nodes.add(createFcNode);

      TreeMap<Integer, FcNode> oppositeNodeMap = new TreeMap<>();

      if (NodeType.getEnumFromCode(createFcNode.getNodeType()).equals(NodeType.LEAF)) {

        List<FcNode> oppositeSpineNodeList = new ArrayList<>();
        for (FcNode fcNode : fcNodeList) {
          if (NodeType.getEnumFromCode(fcNode.getNodeType()).equals(NodeType.SPINE)) {
            oppositeSpineNodeList.add(fcNode);

            oppositeNodeMap.put(fcNode.getEcNodeId(), fcNode);
          }
        }
        logger.performance("start get create leaf and opposite spine resources lock.");
        FcDbManager.getInstance().getResourceLock(null, null, nodes, oppositeSpineNodeList, sessionWrapper);
        logger.performance("end get create leaf and opposite spine resources lock.");
      } else {

        List<FcNode> oppositeLeafNodeList = new ArrayList<>();
        for (FcNode fcNode : fcNodeList) {
          if (NodeType.getEnumFromCode(fcNode.getNodeType()).equals(NodeType.LEAF)) {
            oppositeLeafNodeList.add(fcNode);

            oppositeNodeMap.put(fcNode.getEcNodeId(), fcNode);
          }
        }
        logger.performance("start get create spine and opposite leaf resources lock.");
        FcDbManager.getInstance().getResourceLock(null, null, oppositeLeafNodeList, nodes, sessionWrapper);
        logger.performance("end get create spine and opposite leaf resources lock.");
      }
      return oppositeNodeMap;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode checkCreateNode(List<FcNode> fcNodeList) throws MsfException {
    try {
      logger.methodStart();
      FcNode createFcNode = null;
      for (FcNode fcNode : fcNodeList) {
        if (fcNode.getEcNodeId().equals(Integer.valueOf(request.getNodeId()))) {

          createFcNode = fcNode;
          break;
        }
      }
      if (createFcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcNode");
      }
      return createFcNode;
    } finally {
      logger.methodEnd();
    }
  }
}
