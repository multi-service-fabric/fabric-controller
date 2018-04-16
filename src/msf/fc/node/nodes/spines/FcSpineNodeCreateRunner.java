
package msf.fc.node.nodes.spines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.SwClusterData;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcNode;
import msf.fc.common.util.FcIpAddressUtil;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.nodes.operation.data.NodeCreateDeleteEcRequestBody;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeBreakoutBaseIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateNodeIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeEquipmentEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkIfCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeLagMemberEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeLoopbackInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeManagementInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeOppositeNodeCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeUnusedPhysicalIfEcEntity;
import msf.mfcfc.common.constant.EcNodeOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.NodeSubStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateAsyncResponseBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBreakoutEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBreakoutIfCreateEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalBreakoutIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeLagLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeLocalEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeMemberIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeOppositeEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalIfCreateEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalLinkEntity;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in Spine node addition.
 *
 * @author NTT
 *
 */
public class FcSpineNodeCreateRunner extends FcAbstractSpineNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeCreateRunner.class);

  private SpineNodeCreateRequestBody requestBody;

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
  public FcSpineNodeCreateRunner(SpineNodeRequest request, SpineNodeCreateRequestBody requestBody) {

    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to equipment decreasing process.");
      synchronized (FcNodeManager.getInstance().getFcEquipmentDeleteLockObject()) {
        logger.performance("end wait to equipment decreasing process.");
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

              List<FcNode> oppositeLeafNodeList = getOppositeNodeList(fcNodeList, NodeType.LEAF);

              sessionWrapper.beginTransaction();
              if (!oppositeLeafNodeList.isEmpty()) {
                logger.performance("start get leaf resources lock.");
                FcDbManager.getInstance().getLeafsLock(oppositeLeafNodeList, sessionWrapper);
                logger.performance("end get leaf resources lock.");
              }

              checkNodeAfterLock(fcNodeList, fcNodeDao, sessionWrapper, NodeType.SPINE, requestBody.getNodeId(), false,
                  null);

              List<SpineNodePhysicalLinkEntity> physicalLinkList = null;
              List<SpineNodeLagLinkEntity> lagLinkList = null;
              SpineNodeInternalLinkEntity internalLinks = requestBody.getInternalLinks();
              if (internalLinks != null) {
                physicalLinkList = internalLinks.getPhysicalLinkList();
                lagLinkList = internalLinks.getLagLinkList();
              }
              SpineNodeBreakoutEntity breakoutEntity = requestBody.getBreakout();

              if (physicalLinkList == null) {
                physicalLinkList = new ArrayList<>();
              }
              if (lagLinkList == null) {
                lagLinkList = new ArrayList<>();
              }

              TreeMap<Integer, SpineNodeOppositeEntity> oppositeBreakoutIfMap = new TreeMap<>();
              if ((breakoutEntity != null) && (CollectionUtils.isNotEmpty(breakoutEntity.getOppositeList()))) {
                oppositeBreakoutIfMap = createOppositeBreakoutIfMap(breakoutEntity.getOppositeList());
              }

              TreeMap<Integer, Object> oppositeNodeMap = checkOppositeNode(oppositeLeafNodeList, sessionWrapper,
                  physicalLinkList, lagLinkList, oppositeBreakoutIfMap);

              FcEquipment fcEquipment = getEquipment(sessionWrapper, Integer.valueOf(requestBody.getEquipmentTypeId()));
              ArrayList<String> physicalIfIds = getPhysicalIfIds(fcEquipment);

              SpineNodeLocalEntity localBreakoutIf = null;
              if (breakoutEntity != null) {
                localBreakoutIf = breakoutEntity.getLocal();
              }

              ArrayList<String> unusedPhysicalIfs = checkExpansionNode(physicalLinkList, lagLinkList, localBreakoutIf,
                  physicalIfIds);

              NodeCreateNodeEcEntity createSpineCreateData = createSpineCreateData(localBreakoutIf, oppositeNodeMap,
                  unusedPhysicalIfs, oppositeBreakoutIfMap);

              createSpineNode(sessionWrapper, fcNodeDao, fcEquipment);

              if (requestBody.getProvisioning()) {

                updateNodeSubStatus(sessionWrapper, NodeSubStatus.ZTP_FEASIBLE, getOperationId());
              }

              sendSpineNodeCreate(createSpineCreateData);

              responseBase = responseSpineNodeCreateData();

              sessionWrapper.commit();

              setOperationEndFlag(false);

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
      }
    } finally {
      logger.methodEnd();
    }
  }

  private NodeCreateNodeEcEntity createSpineCreateData(SpineNodeLocalEntity localBreakoutIf,
      TreeMap<Integer, Object> oppositeNodeMap, ArrayList<String> unusedPhysicalIfs,
      TreeMap<Integer, SpineNodeOppositeEntity> oppositeBreakoutIfMap) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "localBreakoutIf", "oppositeNodeMap", "unusedPhysicalIfs", "oppositeBreakoutIfMap" },
          new Object[] { localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, oppositeBreakoutIfMap });
      NodeCreateNodeEcEntity createNodeEcEntity = new NodeCreateNodeEcEntity();

      Integer createSpineNodeId = Integer.valueOf(requestBody.getNodeId());

      createNodeEcEntity.setNodeId(String.valueOf(createEcNodeId(createSpineNodeId, NodeType.SPINE)));
      createNodeEcEntity.setHostName(requestBody.getHostName());
      createNodeEcEntity.setNodeType(InternalNodeType.SPINE.getMessage());
      createNodeEcEntity.setUsername(requestBody.getUsername());
      createNodeEcEntity.setPassword(requestBody.getPassword());
      createNodeEcEntity.setMacAddr(requestBody.getMacAddress());
      createNodeEcEntity.setProvisioning(requestBody.getProvisioning());
      createNodeEcEntity.setNtpServerAddress(requestBody.getNtpServerAddress());

      NodeManagementInterfaceEcEntity managementInterface = new NodeManagementInterfaceEcEntity();
      String managementIfAddress = requestBody.getManagementIfAddress();
      if (managementIfAddress != null) {
        managementInterface.setAddress(managementIfAddress);
        managementInterface.setPrefix(requestBody.getManagementIfPrefix());
      } else {

        managementInterface.setAddress(FcIpAddressUtil.getXsmgi(createSpineNodeId));
        managementInterface.setPrefix(MANAGEMENT_ADDRESS_PREFIX);
      }
      createNodeEcEntity.setManagementInterface(managementInterface);

      NodeLoopbackInterfaceEcEntity loopbackInterfaceEcEntity = new NodeLoopbackInterfaceEcEntity();

      loopbackInterfaceEcEntity.setAddress(FcIpAddressUtil.getXslloi(createSpineNodeId));
      loopbackInterfaceEcEntity.setPrefix(LOOPBACK_ADDRESS_PREFIX);
      createNodeEcEntity.setLoopbackInterface(loopbackInterfaceEcEntity);

      createNodeEcEntity.setSnmpCommunity(requestBody.getSnmpCommunity());

      createNodeEcEntity.setCreateNodeIf(
          getNodeCreateNodeIfEcEntity(localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, createSpineNodeId));

      createNodeEcEntity
          .setOppositeNodeList(getOppositeNodeListEntity(oppositeNodeMap, oppositeBreakoutIfMap, createSpineNodeId));

      SwClusterData dataConfSwClusterData = FcConfigManager.getInstance().getDataConfSwClusterData();

      createNodeEcEntity.setClusterArea(String.valueOf(dataConfSwClusterData.getSwCluster().getOspfArea()));

      return createNodeEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeCreateNodeIfEcEntity getNodeCreateNodeIfEcEntity(SpineNodeLocalEntity localBreakoutIf,
      TreeMap<Integer, Object> oppositeNodeMap, ArrayList<String> unusedPhysicalIfs, Integer createSpineNodeId)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "localBreakoutIf", "oppositeNodeMap", "unusedPhysicalIfs", "createSpineNodeId" },
          new Object[] { localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, createSpineNodeId });

      NodeCreateNodeIfEcEntity createNodeIf = new NodeCreateNodeIfEcEntity();

      if (localBreakoutIf != null) {
        createNodeIf.setBreakoutBaseIfList(getBreakoutBaseIfList(localBreakoutIf));
      } else {
        createNodeIf.setBreakoutBaseIfList(new ArrayList<>());
      }

      createNodeIf.setInternalLinkList(getInternalLinkList(oppositeNodeMap, createSpineNodeId));

      createNodeIf.setUnusedPhysicalIfList(getUnusedPhysicalIfList(unusedPhysicalIfs));

      return createNodeIf;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfList(SpineNodeLocalEntity localBreakoutIf) {
    try {
      logger.methodStart(new String[] { "localBreakoutIf" }, new Object[] { localBreakoutIf });

      List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfList = new ArrayList<>();
      for (SpineNodeBreakoutIfCreateEntity spineNodeBreakoutIfCreateEntity : localBreakoutIf.getBreakoutIfList()) {
        NodeBreakoutBaseIfEcEntity nodeBreakoutBaseIfEcEntity = new NodeBreakoutBaseIfEcEntity();
        nodeBreakoutBaseIfEcEntity.setBasePhysicalIfId(spineNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId());
        nodeBreakoutBaseIfEcEntity.setSpeed(spineNodeBreakoutIfCreateEntity.getBreakoutIfSpeed());
        nodeBreakoutBaseIfEcEntity.setBreakoutIfIdList(spineNodeBreakoutIfCreateEntity.getBreakoutIfIdList());
        breakoutBaseIfList.add(nodeBreakoutBaseIfEcEntity);
      }
      return breakoutBaseIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeInternalLinkCreateEcEntity> getInternalLinkList(TreeMap<Integer, Object> oppositeNodeMap,
      Integer createSpineNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodeMap", "createSpineNodeId" },
          new Object[] { oppositeNodeMap, createSpineNodeId });

      List<NodeInternalLinkCreateEcEntity> internalLinkList = new ArrayList<>();
      for (Entry<Integer, Object> oppositeNodeEntry : oppositeNodeMap.entrySet()) {
        NodeInternalLinkIfCreateEcEntity internalLinkIfCreate = new NodeInternalLinkIfCreateEcEntity();
        if (oppositeNodeEntry.getValue() instanceof SpineNodePhysicalLinkEntity) {

          SpineNodePhysicalLinkEntity physicalLinkEntity = (SpineNodePhysicalLinkEntity) oppositeNodeEntry.getValue();
          SpineNodePhysicalIfCreateEntity physicalIf = physicalLinkEntity.getInternalLinkIf().getLocal()
              .getPhysicalIf();
          if (physicalIf != null) {
            internalLinkIfCreate.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
            internalLinkIfCreate.setIfId(physicalIf.getPhysicalIfId());
            internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
          } else {
            SpineNodeInternalBreakoutIfEntity breakoutIf = physicalLinkEntity.getInternalLinkIf().getLocal()
                .getBreakoutIf();
            internalLinkIfCreate.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
            internalLinkIfCreate.setIfId(breakoutIf.getBreakoutIfId());

          }
        } else {

          SpineNodeLagLinkEntity lagLinkEntity = (SpineNodeLagLinkEntity) oppositeNodeEntry.getValue();
          internalLinkIfCreate.setIfType(InterfaceType.LAG_IF.getMessage());

          Map<String, String> createLagIfIds = createLagIfIds(oppositeNodeEntry.getKey(), createSpineNodeId);
          internalLinkIfCreate.setIfId(createLagIfIds.get(IpAddressUtil.SPINE));
          List<NodeLagMemberEcEntity> lagMemberList = new ArrayList<>();
          for (SpineNodeMemberIfEntity spineNodeMemberIfEntity : lagLinkEntity.getMemberIfList()) {
            NodeLagMemberEcEntity lagMemberEcEntity = new NodeLagMemberEcEntity();
            SpineNodePhysicalIfCreateEntity physicalIf = spineNodeMemberIfEntity.getLocal().getPhysicalIf();
            if (physicalIf != null) {
              lagMemberEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
              lagMemberEcEntity.setIfId(physicalIf.getPhysicalIfId());

              internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
            } else {
              SpineNodeInternalBreakoutIfEntity breakoutIf = spineNodeMemberIfEntity.getLocal().getBreakoutIf();
              lagMemberEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
              lagMemberEcEntity.setIfId(breakoutIf.getBreakoutIfId());
            }
            lagMemberList.add(lagMemberEcEntity);
          }
          internalLinkIfCreate.setLagMemberList(lagMemberList);
        }

        Map<String, String> nintrai = FcIpAddressUtil.getNintrai(oppositeNodeEntry.getKey(), createSpineNodeId);
        internalLinkIfCreate.setLinkIpAddress(nintrai.get(IpAddressUtil.SPINE));
        internalLinkIfCreate.setPrefix(INTERNAL_LINK_IF_PREFIX);

        NodeInternalLinkCreateEcEntity linkCreateEcEntity = new NodeInternalLinkCreateEcEntity();
        linkCreateEcEntity.setInternalLinkIfCreate(internalLinkIfCreate);
        internalLinkList.add(linkCreateEcEntity);
      }

      return internalLinkList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeUnusedPhysicalIfEcEntity> getUnusedPhysicalIfList(ArrayList<String> unusedPhysicalIfs) {
    try {
      logger.methodStart(new String[] { "unusedPhysicalIfs" }, new Object[] { unusedPhysicalIfs });

      List<NodeUnusedPhysicalIfEcEntity> unusedPhysicalIfList = new ArrayList<>();
      for (String unUsedPhysicalIf : unusedPhysicalIfs) {
        NodeUnusedPhysicalIfEcEntity nodeUnusedPhysicalIfEcEntity = new NodeUnusedPhysicalIfEcEntity();
        nodeUnusedPhysicalIfEcEntity.setPhysicalIfId(unUsedPhysicalIf);
        unusedPhysicalIfList.add(nodeUnusedPhysicalIfEcEntity);
      }
      return unusedPhysicalIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeOppositeNodeCreateEcEntity> getOppositeNodeListEntity(TreeMap<Integer, Object> oppositeNodeMap,
      TreeMap<Integer, SpineNodeOppositeEntity> oppositeBreakoutIfMap, Integer createSpineNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodeMap", "oppositeBreakoutIfMap", "createSpineNodeId" },
          new Object[] { oppositeNodeMap, oppositeBreakoutIfMap, createSpineNodeId });
      List<NodeOppositeNodeCreateEcEntity> oppositeNodeList = new ArrayList<>();
      for (Entry<Integer, Object> oppositeNodeEntry : oppositeNodeMap.entrySet()) {
        NodeOppositeNodeCreateEcEntity oppositeNodeCreateEcEntity = new NodeOppositeNodeCreateEcEntity();
        NodeInternalLinkIfCreateEcEntity internalLinkIfCreate = new NodeInternalLinkIfCreateEcEntity();

        oppositeNodeCreateEcEntity.setNodeId(String.valueOf(createEcNodeId(oppositeNodeEntry.getKey(), NodeType.LEAF)));
        if (oppositeNodeEntry.getValue() instanceof SpineNodePhysicalLinkEntity) {

          SpineNodePhysicalLinkEntity physicalLinkEntity = (SpineNodePhysicalLinkEntity) oppositeNodeEntry.getValue();

          oppositeNodeCreateEcEntity
              .setBreakoutBaseIfsList(getBreakoutBaseIfsList(oppositeBreakoutIfMap, oppositeNodeEntry.getKey()));

          SpineNodePhysicalIfCreateEntity physicalIf = physicalLinkEntity.getInternalLinkIf().getOpposite()
              .getPhysicalIf();
          if (physicalIf != null) {
            internalLinkIfCreate.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
            internalLinkIfCreate.setIfId(physicalIf.getPhysicalIfId());
            internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
          } else {
            SpineNodeInternalBreakoutIfEntity breakoutIf = physicalLinkEntity.getInternalLinkIf().getOpposite()
                .getBreakoutIf();
            internalLinkIfCreate.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
            internalLinkIfCreate.setIfId(breakoutIf.getBreakoutIfId());

          }
        } else {

          oppositeNodeCreateEcEntity
              .setBreakoutBaseIfsList(getBreakoutBaseIfsList(oppositeBreakoutIfMap, oppositeNodeEntry.getKey()));

          internalLinkIfCreate.setIfType(InterfaceType.LAG_IF.getMessage());

          Map<String, String> createLagIfIds = createLagIfIds(oppositeNodeEntry.getKey(), createSpineNodeId);
          internalLinkIfCreate.setIfId(createLagIfIds.get(IpAddressUtil.LEAF));
          List<NodeLagMemberEcEntity> lagMemberList = new ArrayList<>();

          SpineNodeLagLinkEntity lagLinkEntity = (SpineNodeLagLinkEntity) oppositeNodeEntry.getValue();
          for (SpineNodeMemberIfEntity spineNodeMemberIfEntity : lagLinkEntity.getMemberIfList()) {
            NodeLagMemberEcEntity lagMemberEcEntity = new NodeLagMemberEcEntity();
            SpineNodePhysicalIfCreateEntity physicalIf = spineNodeMemberIfEntity.getOpposite().getPhysicalIf();
            if (physicalIf != null) {
              lagMemberEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
              lagMemberEcEntity.setIfId(physicalIf.getPhysicalIfId());

              internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
            } else {
              SpineNodeInternalBreakoutIfEntity breakoutIf = spineNodeMemberIfEntity.getOpposite().getBreakoutIf();
              lagMemberEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
              lagMemberEcEntity.setIfId(breakoutIf.getBreakoutIfId());
            }
            lagMemberList.add(lagMemberEcEntity);
          }
          internalLinkIfCreate.setLagMemberList(lagMemberList);
        }

        Map<String, String> nintrai = FcIpAddressUtil.getNintrai(oppositeNodeEntry.getKey(), createSpineNodeId);
        internalLinkIfCreate.setLinkIpAddress(nintrai.get(IpAddressUtil.LEAF));
        internalLinkIfCreate.setPrefix(INTERNAL_LINK_IF_PREFIX);

        oppositeNodeCreateEcEntity.setInternalLink(internalLinkIfCreate);
        oppositeNodeList.add(oppositeNodeCreateEcEntity);
      }

      return oppositeNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfsList(
      TreeMap<Integer, SpineNodeOppositeEntity> oppositeBreakoutIfMap, Integer oppositeNodeId) {
    try {
      logger.methodStart(new String[] { "oppositeBreakoutIfMap", "oppositeNodeId" },
          new Object[] { oppositeBreakoutIfMap, oppositeNodeId });
      List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfsList = new ArrayList<>();
      SpineNodeOppositeEntity spineNodeOppositeEntity = oppositeBreakoutIfMap.get(oppositeNodeId);
      if (spineNodeOppositeEntity != null) {
        for (SpineNodeBreakoutIfCreateEntity spineNodeBreakoutIfCreateEntity : spineNodeOppositeEntity
            .getBreakoutIfList()) {
          NodeBreakoutBaseIfEcEntity nodeBreakoutBaseIfEcEntity = new NodeBreakoutBaseIfEcEntity();
          nodeBreakoutBaseIfEcEntity.setBasePhysicalIfId(spineNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId());
          nodeBreakoutBaseIfEcEntity.setSpeed(spineNodeBreakoutIfCreateEntity.getBreakoutIfSpeed());
          nodeBreakoutBaseIfEcEntity.setBreakoutIfIdList(spineNodeBreakoutIfCreateEntity.getBreakoutIfIdList());
          breakoutBaseIfsList.add(nodeBreakoutBaseIfEcEntity);
        }
      }
      return breakoutBaseIfsList;
    } finally {
      logger.methodEnd();
    }
  }

  private void createSpineNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, FcEquipment fcEquipment)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "fcEquipment" }, new Object[] { fcNodeDao, fcEquipment });
      FcNode createNode = new FcNode();
      createNode.setEquipment(fcEquipment);
      createNode.setNodeTypeEnum(NodeType.SPINE);
      createNode.setNodeId(Integer.valueOf(requestBody.getNodeId()));

      createNode.setEcNodeId(createEcNodeId(createNode.getNodeId(), NodeType.SPINE));
      fcNodeDao.create(sessionWrapper, createNode);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendSpineNodeCreate(NodeCreateNodeEcEntity createSpineCreateData) throws MsfException {
    try {
      logger.methodStart(new String[] { "createSpineCreateData" }, new Object[] { createSpineCreateData });

      NodeCreateDeleteEcRequestBody nodeCreateDeleteEcRequestBody = new NodeCreateDeleteEcRequestBody();
      nodeCreateDeleteEcRequestBody.setAction(EcNodeOperationAction.ADD_NODE.getMessage());
      NodeCreateEcEntity addNodeOption = new NodeCreateEcEntity();
      NodeEquipmentEcEntity equipment = new NodeEquipmentEcEntity();
      equipment.setEquipmentTypeId(requestBody.getEquipmentTypeId());
      addNodeOption.setEquipment(equipment);
      addNodeOption.setCreateNode(createSpineCreateData);
      nodeCreateDeleteEcRequestBody.setAddNodeOption(addNodeOption);

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
        ErrorInternalResponseBody nodeCreateDeleteEcResponceBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = nodeCreateDeleteEcResponceBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeCreateData() {
    try {
      logger.methodStart();
      SpineNodeCreateAsyncResponseBody body = new SpineNodeCreateAsyncResponseBody();
      body.setNodeId(requestBody.getNodeId());
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
