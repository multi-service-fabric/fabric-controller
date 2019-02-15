
package msf.fc.node.nodes.leafs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.LeafRr;
import msf.fc.common.config.type.data.SwClusterData;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcLeafNode;
import msf.fc.common.data.FcNode;
import msf.fc.common.util.FcIpAddressUtil;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadEcResponseBody;
import msf.fc.rest.ec.node.nodes.operation.data.NodeCreateDeleteEcRequestBody;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeAsEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeBgpNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeBreakoutBaseIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateNodeIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeCreateUpdateNodeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeEquipmentEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeInternalLinkIfCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeL2VpnEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeL3VpnEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeLagMemberEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeLoopbackInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeManagementInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeNeighborEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeOppositeNodeCreateEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeRangeEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeUnusedPhysicalIfEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeVirtualLinkEcEntity;
import msf.fc.rest.ec.node.nodes.operation.data.entity.NodeVpnEcEntity;
import msf.mfcfc.common.constant.EcNodeOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeSubStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.VpnType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateAsyncResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutIfCreateEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalBreakoutIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLocalEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeMemberIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeOppositeEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalIfCreateEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalLinkEntity;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the Leaf node addition.
 *
 * @author NTT
 *
 */
public class FcLeafNodeCreateRunner extends FcAbstractLeafNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeCreateRunner.class);

  private LeafNodeCreateRequestBody requestBody;

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
  public FcLeafNodeCreateRunner(LeafNodeRequest request, LeafNodeCreateRequestBody requestBody) {

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

              List<FcNode> oppositeSpineNodeList = getOppositeNodeList(fcNodeList, NodeType.SPINE);

              List<FcNode> borderLeafNodeList = getOtherBorderLeafNodeList(fcNodeList,
                  Integer.valueOf(requestBody.getNodeId()));

              sessionWrapper.beginTransaction();
              if ((!oppositeSpineNodeList.isEmpty()) || (!borderLeafNodeList.isEmpty())) {
                logger.performance("start get spine and b-leaf resources lock.");
                FcDbManager.getInstance().getResourceLock(null, null, borderLeafNodeList, oppositeSpineNodeList,
                    sessionWrapper);
                logger.performance("end get spine and b-leaf resources lock.");
              }

              boolean isBorderLeafCreate = LeafType.getEnumFromMessage(requestBody.getLeafType())
                  .equals(LeafType.BORDER_LEAF);
              checkNodeAfterLock(fcNodeList, fcNodeDao, sessionWrapper, NodeType.LEAF, requestBody.getNodeId(),
                  isBorderLeafCreate, borderLeafNodeList);

              List<LeafNodePhysicalLinkEntity> physicalLinkList = null;
              List<LeafNodeLagLinkEntity> lagLinkList = null;
              LeafNodeInternalLinkEntity internalLinks = requestBody.getInternalLinks();
              if (internalLinks != null) {
                physicalLinkList = internalLinks.getPhysicalLinkList();
                lagLinkList = internalLinks.getLagLinkList();
              }
              LeafNodeBreakoutEntity breakoutEntity = requestBody.getBreakout();

              if (physicalLinkList == null) {
                physicalLinkList = new ArrayList<>();
              }
              if (lagLinkList == null) {
                lagLinkList = new ArrayList<>();
              }

              TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap = new TreeMap<>();
              if ((breakoutEntity != null) && (CollectionUtils.isNotEmpty(breakoutEntity.getOppositeList()))) {
                oppositeBreakoutIfMap = createOppositeBreakoutIfMap(breakoutEntity.getOppositeList());
              }

              TreeMap<Integer, Object> oppositeNodeMap = checkOppositeNode(oppositeSpineNodeList, sessionWrapper,
                  physicalLinkList, lagLinkList, oppositeBreakoutIfMap);

              FcEquipment fcEquipment = getEquipment(sessionWrapper, Integer.valueOf(requestBody.getEquipmentTypeId()));
              EquipmentReadEcResponseBody sendEquipmentRead = sendEquipmentRead(fcEquipment);
              ArrayList<String> physicalIfIds = getPhysicalIfIds(sendEquipmentRead);

              LeafNodeLocalEntity localBreakoutIf = null;
              if (breakoutEntity != null) {
                localBreakoutIf = breakoutEntity.getLocal();
              }

              ArrayList<String> unusedPhysicalIfs = checkExpansionNode(physicalLinkList, lagLinkList, localBreakoutIf,
                  physicalIfIds);

              NodeCreateNodeEcEntity createLeafCreateData = createLeafCreateData(isBorderLeafCreate, localBreakoutIf,
                  oppositeNodeMap, unusedPhysicalIfs, oppositeBreakoutIfMap, borderLeafNodeList);

              NodeCreateUpdateNodeEcEntity borderLeafCreateUpdateData = createBorderLeafCreateUpdateData(
                  isBorderLeafCreate, borderLeafNodeList);

              createLeafNode(sessionWrapper, fcNodeDao, fcEquipment);

              if (requestBody.getProvisioning()) {

                updateNodeSubStatus(sessionWrapper, NodeSubStatus.ZTP_FEASIBLE, getOperationId());
              }

              sendLeafNodeCreate(createLeafCreateData, borderLeafCreateUpdateData);

              responseBase = responseLeafNodeCreateData();

              sessionWrapper.commit();

              setOperationEndFlag(false);

            } catch (MsfException msfException) {
              logger.error(msfException.getMessage(), msfException);
              sessionWrapper.rollback();

              FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
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

  private NodeCreateNodeEcEntity createLeafCreateData(boolean isBorderLeafCreate, LeafNodeLocalEntity localBreakoutIf,
      TreeMap<Integer, Object> oppositeNodeMap, ArrayList<String> unusedPhysicalIfs,
      TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap, List<FcNode> borderLeafNodeList)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "isBorderLeafCreate", "localBreakoutIf", "oppositeNodeMap", "unusedPhysicalIfs",
              "oppositeBreakoutIfMap", "borderLeafNodeList" },
          new Object[] { isBorderLeafCreate, localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, oppositeBreakoutIfMap,
              borderLeafNodeList });
      NodeCreateNodeEcEntity createNodeEcEntity = new NodeCreateNodeEcEntity();

      Integer createLeafNodeId = Integer.valueOf(requestBody.getNodeId());

      createNodeEcEntity.setNodeId(String.valueOf(createEcNodeId(createLeafNodeId, NodeType.LEAF)));
      createNodeEcEntity.setHostName(requestBody.getHostName());
      createNodeEcEntity
          .setNodeType(isBorderLeafCreate ? InternalNodeType.B_LEAF.getMessage() : InternalNodeType.LEAF.getMessage());
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

        managementInterface.setAddress(FcIpAddressUtil.getXlmgi(createLeafNodeId));
        managementInterface.setPrefix(MANAGEMENT_ADDRESS_PREFIX);
      }
      createNodeEcEntity.setManagementInterface(managementInterface);

      NodeLoopbackInterfaceEcEntity loopbackInterfaceEcEntity = new NodeLoopbackInterfaceEcEntity();

      loopbackInterfaceEcEntity.setAddress(FcIpAddressUtil.getX0lloi(createLeafNodeId));
      loopbackInterfaceEcEntity.setPrefix(LOOPBACK_ADDRESS_PREFIX);
      createNodeEcEntity.setLoopbackInterface(loopbackInterfaceEcEntity);

      createNodeEcEntity.setPlane(String.valueOf(requestBody.getPlane()));
      createNodeEcEntity.setSnmpCommunity(requestBody.getSnmpCommunity());

      createNodeEcEntity.setCreateNodeIf(
          getNodeCreateNodeIfEcEntity(localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, createLeafNodeId));

      createNodeEcEntity
          .setOppositeNodeList(getOppositeNodeListEntity(oppositeNodeMap, oppositeBreakoutIfMap, createLeafNodeId));

      SwClusterData dataConfSwClusterData = FcConfigManager.getInstance().getDataConfSwClusterData();

      createNodeEcEntity.setVpn(getNodeVpnEcEntity(dataConfSwClusterData));

      if (requestBody.getIrbTypeEnum().getCode() > 0) {

        createNodeEcEntity.setIrbType(requestBody.getIrbType());
      }

      createNodeEcEntity.setClusterArea(String.valueOf(dataConfSwClusterData.getSwCluster().getOspfArea()));

      if (isBorderLeafCreate) {

        createNodeEcEntity.setVirtualLink(getNodeVirtualLinkEcEntity(borderLeafNodeList));

        createNodeEcEntity.setRange(getNodeRangeEcEntity());
      }

      return createNodeEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeCreateUpdateNodeEcEntity createBorderLeafCreateUpdateData(boolean isBorderLeafCreate,
      List<FcNode> borderLeafNodeList) {
    try {
      logger.methodStart(new String[] { "isBorderLeafCreate", "borderLeafNodeList" },
          new Object[] { isBorderLeafCreate, borderLeafNodeList });
      NodeCreateUpdateNodeEcEntity updateNodeEcEntity = null;
      if (isBorderLeafCreate) {
        if (!borderLeafNodeList.isEmpty()) {
          updateNodeEcEntity = new NodeCreateUpdateNodeEcEntity();

          updateNodeEcEntity.setNodeId(String.valueOf(borderLeafNodeList.get(0).getEcNodeId()));
          updateNodeEcEntity.setNodeType(InternalNodeType.B_LEAF.getMessage());
          int ospfArea = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getOspfArea();
          updateNodeEcEntity.setClusterArea(String.valueOf(ospfArea));
          NodeVirtualLinkEcEntity virtualLink = new NodeVirtualLinkEcEntity();
          virtualLink.setNodeId(requestBody.getNodeId());
          updateNodeEcEntity.setVirtualLink(virtualLink);
        }
      }
      return updateNodeEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeOppositeNodeCreateEcEntity> getOppositeNodeListEntity(TreeMap<Integer, Object> oppositeNodeMap,
      TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap, Integer createLeafNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodeMap", "oppositeBreakoutIfMap", "createLeafNodeId" },
          new Object[] { oppositeNodeMap, oppositeBreakoutIfMap, createLeafNodeId });
      List<NodeOppositeNodeCreateEcEntity> oppositeNodeList = new ArrayList<>();
      for (Entry<Integer, Object> oppositeNodeEntry : oppositeNodeMap.entrySet()) {
        NodeOppositeNodeCreateEcEntity oppositeNodeCreateEcEntity = new NodeOppositeNodeCreateEcEntity();
        NodeInternalLinkIfCreateEcEntity internalLinkIfCreate = new NodeInternalLinkIfCreateEcEntity();

        oppositeNodeCreateEcEntity
            .setNodeId(String.valueOf(createEcNodeId(oppositeNodeEntry.getKey(), NodeType.SPINE)));
        if (oppositeNodeEntry.getValue() instanceof LeafNodePhysicalLinkEntity) {

          LeafNodePhysicalLinkEntity physicalLinkEntity = (LeafNodePhysicalLinkEntity) oppositeNodeEntry.getValue();

          oppositeNodeCreateEcEntity
              .setBreakoutBaseIfsList(getBreakoutBaseIfsList(oppositeBreakoutIfMap, oppositeNodeEntry.getKey()));

          LeafNodePhysicalIfCreateEntity physicalIf = physicalLinkEntity.getInternalLinkIf().getOpposite()
              .getPhysicalIf();
          if (physicalIf != null) {
            internalLinkIfCreate.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
            internalLinkIfCreate.setIfId(physicalIf.getPhysicalIfId());
            internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
          } else {
            LeafNodeInternalBreakoutIfEntity breakoutIf = physicalLinkEntity.getInternalLinkIf().getOpposite()
                .getBreakoutIf();
            internalLinkIfCreate.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
            internalLinkIfCreate.setIfId(breakoutIf.getBreakoutIfId());

          }
        } else {

          oppositeNodeCreateEcEntity
              .setBreakoutBaseIfsList(getBreakoutBaseIfsList(oppositeBreakoutIfMap, oppositeNodeEntry.getKey()));

          internalLinkIfCreate.setIfType(InterfaceType.LAG_IF.getMessage());

          Map<String, String> createLagIfIds = createLagIfIds(createLeafNodeId, oppositeNodeEntry.getKey());
          internalLinkIfCreate.setIfId(createLagIfIds.get(IpAddressUtil.SPINE));
          List<NodeLagMemberEcEntity> lagMemberList = new ArrayList<>();

          LeafNodeLagLinkEntity lagLinkEntity = (LeafNodeLagLinkEntity) oppositeNodeEntry.getValue();
          for (LeafNodeMemberIfEntity leafNodeMemberIfEntity : lagLinkEntity.getMemberIfList()) {
            NodeLagMemberEcEntity lagMemberEcEntity = new NodeLagMemberEcEntity();
            LeafNodePhysicalIfCreateEntity physicalIf = leafNodeMemberIfEntity.getOpposite().getPhysicalIf();
            if (physicalIf != null) {
              lagMemberEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
              lagMemberEcEntity.setIfId(physicalIf.getPhysicalIfId());

              internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
            } else {
              LeafNodeInternalBreakoutIfEntity breakoutIf = leafNodeMemberIfEntity.getOpposite().getBreakoutIf();
              lagMemberEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
              lagMemberEcEntity.setIfId(breakoutIf.getBreakoutIfId());
            }
            lagMemberList.add(lagMemberEcEntity);
          }
          internalLinkIfCreate.setLagMemberList(lagMemberList);
        }

        Map<String, String> nintrai = FcIpAddressUtil.getNintrai(createLeafNodeId, oppositeNodeEntry.getKey());
        internalLinkIfCreate.setLinkIpAddress(nintrai.get(IpAddressUtil.SPINE));
        internalLinkIfCreate.setPrefix(INTERNAL_LINK_IF_PREFIX);

        internalLinkIfCreate.setCost(
            FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getInternalLinkNormalIgpCost());

        oppositeNodeCreateEcEntity.setInternalLink(internalLinkIfCreate);
        oppositeNodeList.add(oppositeNodeCreateEcEntity);
      }

      return oppositeNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfsList(
      TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap, Integer oppositeNodeId) {
    try {
      logger.methodStart(new String[] { "oppositeBreakoutIfMap", "oppositeNodeId" },
          new Object[] { oppositeBreakoutIfMap, oppositeNodeId });
      List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfsList = new ArrayList<>();
      LeafNodeOppositeEntity leafNodeOppositeEntity = oppositeBreakoutIfMap.get(oppositeNodeId);
      if (leafNodeOppositeEntity != null) {
        for (LeafNodeBreakoutIfCreateEntity leafNodeBreakoutIfCreateEntity : leafNodeOppositeEntity
            .getBreakoutIfList()) {
          NodeBreakoutBaseIfEcEntity nodeBreakoutBaseIfEcEntity = new NodeBreakoutBaseIfEcEntity();
          nodeBreakoutBaseIfEcEntity.setBasePhysicalIfId(leafNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId());
          nodeBreakoutBaseIfEcEntity.setSpeed(leafNodeBreakoutIfCreateEntity.getBreakoutIfSpeed());
          nodeBreakoutBaseIfEcEntity.setBreakoutIfIdList(leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList());
          breakoutBaseIfsList.add(nodeBreakoutBaseIfEcEntity);
        }
      }
      return breakoutBaseIfsList;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeCreateNodeIfEcEntity getNodeCreateNodeIfEcEntity(LeafNodeLocalEntity localBreakoutIf,
      TreeMap<Integer, Object> oppositeNodeMap, ArrayList<String> unusedPhysicalIfs, Integer createLeafNodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "localBreakoutIf", "oppositeNodeMap", "unusedPhysicalIfs", "createLeafNodeId" },
          new Object[] { localBreakoutIf, oppositeNodeMap, unusedPhysicalIfs, createLeafNodeId });

      NodeCreateNodeIfEcEntity createNodeIf = new NodeCreateNodeIfEcEntity();

      if (localBreakoutIf != null) {
        createNodeIf.setBreakoutBaseIfList(getBreakoutBaseIfList(localBreakoutIf));
      } else {
        createNodeIf.setBreakoutBaseIfList(new ArrayList<>());
      }

      createNodeIf.setInternalLinkList(getInternalLinkList(oppositeNodeMap, createLeafNodeId));

      createNodeIf.setUnusedPhysicalIfList(getUnusedPhysicalIfList(unusedPhysicalIfs));

      return createNodeIf;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfList(LeafNodeLocalEntity localBreakoutIf) {
    try {
      logger.methodStart(new String[] { "localBreakoutIf" }, new Object[] { localBreakoutIf });

      List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfList = new ArrayList<>();
      for (LeafNodeBreakoutIfCreateEntity leafNodeBreakoutIfCreateEntity : localBreakoutIf.getBreakoutIfList()) {
        NodeBreakoutBaseIfEcEntity nodeBreakoutBaseIfEcEntity = new NodeBreakoutBaseIfEcEntity();
        nodeBreakoutBaseIfEcEntity.setBasePhysicalIfId(leafNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId());
        nodeBreakoutBaseIfEcEntity.setSpeed(leafNodeBreakoutIfCreateEntity.getBreakoutIfSpeed());
        nodeBreakoutBaseIfEcEntity.setBreakoutIfIdList(leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList());
        breakoutBaseIfList.add(nodeBreakoutBaseIfEcEntity);
      }
      return breakoutBaseIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<NodeInternalLinkCreateEcEntity> getInternalLinkList(TreeMap<Integer, Object> oppositeNodeMap,
      Integer createLeafNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodeMap", "createLeafNodeId" },
          new Object[] { oppositeNodeMap, createLeafNodeId });

      List<NodeInternalLinkCreateEcEntity> internalLinkList = new ArrayList<>();
      for (Entry<Integer, Object> oppositeNodeEntry : oppositeNodeMap.entrySet()) {
        NodeInternalLinkIfCreateEcEntity internalLinkIfCreate = new NodeInternalLinkIfCreateEcEntity();
        if (oppositeNodeEntry.getValue() instanceof LeafNodePhysicalLinkEntity) {

          LeafNodePhysicalLinkEntity physicalLinkEntity = (LeafNodePhysicalLinkEntity) oppositeNodeEntry.getValue();
          LeafNodePhysicalIfCreateEntity physicalIf = physicalLinkEntity.getInternalLinkIf().getLocal().getPhysicalIf();
          if (physicalIf != null) {
            internalLinkIfCreate.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
            internalLinkIfCreate.setIfId(physicalIf.getPhysicalIfId());
            internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
          } else {
            LeafNodeInternalBreakoutIfEntity breakoutIf = physicalLinkEntity.getInternalLinkIf().getLocal()
                .getBreakoutIf();
            internalLinkIfCreate.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
            internalLinkIfCreate.setIfId(breakoutIf.getBreakoutIfId());

          }
        } else {

          LeafNodeLagLinkEntity lagLinkEntity = (LeafNodeLagLinkEntity) oppositeNodeEntry.getValue();
          internalLinkIfCreate.setIfType(InterfaceType.LAG_IF.getMessage());

          Map<String, String> createLagIfIds = createLagIfIds(createLeafNodeId, oppositeNodeEntry.getKey());
          internalLinkIfCreate.setIfId(createLagIfIds.get(IpAddressUtil.LEAF));
          List<NodeLagMemberEcEntity> lagMemberList = new ArrayList<>();
          for (LeafNodeMemberIfEntity leafNodeMemberIfEntity : lagLinkEntity.getMemberIfList()) {
            NodeLagMemberEcEntity lagMemberEcEntity = new NodeLagMemberEcEntity();
            LeafNodePhysicalIfCreateEntity physicalIf = leafNodeMemberIfEntity.getLocal().getPhysicalIf();
            if (physicalIf != null) {
              lagMemberEcEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
              lagMemberEcEntity.setIfId(physicalIf.getPhysicalIfId());

              internalLinkIfCreate.setLinkSpeed(physicalIf.getPhysicalIfSpeed());
            } else {
              LeafNodeInternalBreakoutIfEntity breakoutIf = leafNodeMemberIfEntity.getLocal().getBreakoutIf();
              lagMemberEcEntity.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
              lagMemberEcEntity.setIfId(breakoutIf.getBreakoutIfId());
            }
            lagMemberList.add(lagMemberEcEntity);
          }
          internalLinkIfCreate.setLagMemberList(lagMemberList);
        }

        Map<String, String> nintrai = FcIpAddressUtil.getNintrai(createLeafNodeId, oppositeNodeEntry.getKey());
        internalLinkIfCreate.setLinkIpAddress(nintrai.get(IpAddressUtil.LEAF));
        internalLinkIfCreate.setPrefix(INTERNAL_LINK_IF_PREFIX);

        internalLinkIfCreate.setCost(
            FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getInternalLinkNormalIgpCost());

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

  private NodeVpnEcEntity getNodeVpnEcEntity(SwClusterData dataConfSwClusterData) {
    try {
      logger.methodStart(new String[] { "dataConfSwClusterData" }, new Object[] { dataConfSwClusterData });
      NodeVpnEcEntity vpn = new NodeVpnEcEntity();
      VpnType vpnType = VpnType.getEnumFromMessage(requestBody.getVpnType());
      vpn.setVpnType(vpnType.getMessage());

      int asNum = dataConfSwClusterData.getSwCluster().getAsNum();
      if (vpnType.equals(VpnType.L3VPN)) {

        NodeBgpNodeEcEntity bgpNode = new NodeBgpNodeEcEntity();
        NodeNeighborEcEntity neighbor = new NodeNeighborEcEntity();
        List<String> addressList = new ArrayList<>();
        for (LeafRr leafRr : dataConfSwClusterData.getRrs().getLeafRr()) {
          addressList.add(leafRr.getLeafRrRouterId());
        }
        neighbor.setAddressList(addressList);
        bgpNode.setNeighbor(neighbor);
        bgpNode.setCommunity(asNum + ":" + requestBody.getPlane());
        bgpNode.setCommunityWildcard(asNum + ":*");

        NodeAsEcEntity as = new NodeAsEcEntity();
        as.setAsNumber(String.valueOf(asNum));

        NodeL3VpnEcEntity l3Vpn = new NodeL3VpnEcEntity();

        l3Vpn.setBgpNode(bgpNode);
        l3Vpn.setAs(as);
        vpn.setL3Vpn(l3Vpn);
      } else {

        NodeBgpNodeEcEntity bgpNode = new NodeBgpNodeEcEntity();
        NodeNeighborEcEntity neighbor = new NodeNeighborEcEntity();
        List<String> addressList = new ArrayList<>();
        for (LeafRr leafRr : dataConfSwClusterData.getRrs().getLeafRr()) {
          addressList.add(leafRr.getLeafRrRouterId());
        }
        neighbor.setAddressList(addressList);
        bgpNode.setNeighbor(neighbor);
        bgpNode.setCommunity(asNum + ":" + requestBody.getPlane());
        bgpNode.setCommunityWildcard(asNum + ":*");

        NodeAsEcEntity as = new NodeAsEcEntity();
        as.setAsNumber(String.valueOf(asNum));

        NodeL2VpnEcEntity l2Vpn = new NodeL2VpnEcEntity();

        l2Vpn.setBgpNode(bgpNode);
        l2Vpn.setAs(as);
        vpn.setL2Vpn(l2Vpn);
      }
      return vpn;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeVirtualLinkEcEntity getNodeVirtualLinkEcEntity(List<FcNode> borderLeafNodeList) {
    try {
      logger.methodStart(new String[] { "borderLeafNodeList" }, new Object[] { borderLeafNodeList });
      NodeVirtualLinkEcEntity virtualLink = null;
      if (!borderLeafNodeList.isEmpty()) {
        virtualLink = new NodeVirtualLinkEcEntity();

        virtualLink.setNodeId(String.valueOf(borderLeafNodeList.get(0).getEcNodeId()));
      }
      return virtualLink;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeRangeEcEntity getNodeRangeEcEntity() {
    try {
      logger.methodStart();
      NodeRangeEcEntity range = new NodeRangeEcEntity();

      range.setAddress(FcIpAddressUtil.getXagri());
      range.setPrefix(FcIpAddressUtil.getPintrai());
      return range;
    } finally {
      logger.methodEnd();
    }
  }

  private void createLeafNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, FcEquipment fcEquipment)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "fcEquipment" }, new Object[] { fcNodeDao, fcEquipment });
      FcNode createNode = new FcNode();
      createNode.setEquipment(fcEquipment);
      createNode.setNodeTypeEnum(NodeType.LEAF);
      createNode.setNodeId(Integer.valueOf(requestBody.getNodeId()));

      createNode.setEcNodeId(createEcNodeId(createNode.getNodeId(), NodeType.LEAF));

      FcLeafNode leafNode = new FcLeafNode();
      leafNode.setNode(createNode);
      leafNode.setLeafType(LeafType.getEnumFromMessage(requestBody.getLeafType()).getCode());
      createNode.setLeafNode(leafNode);
      fcNodeDao.create(sessionWrapper, createNode);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLeafNodeCreate(NodeCreateNodeEcEntity createLeafCreateData,
      NodeCreateUpdateNodeEcEntity borderLeafCreateUpdateData) throws MsfException {
    try {
      logger.methodStart(new String[] { "createLeafCreateData", "borderLeafCreateUpdateData" },
          new Object[] { createLeafCreateData, borderLeafCreateUpdateData });

      NodeCreateDeleteEcRequestBody nodeCreateDeleteEcRequestBody = new NodeCreateDeleteEcRequestBody();
      nodeCreateDeleteEcRequestBody.setAction(EcNodeOperationAction.ADD_NODE.getMessage());
      NodeCreateEcEntity addNodeOption = new NodeCreateEcEntity();
      NodeEquipmentEcEntity equipment = new NodeEquipmentEcEntity();
      equipment.setEquipmentTypeId(requestBody.getEquipmentTypeId());
      addNodeOption.setEquipment(equipment);
      addNodeOption.setCreateNode(createLeafCreateData);
      addNodeOption.setUpdateNode(borderLeafCreateUpdateData);
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
        ErrorInternalResponseBody nodeCreateDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = nodeCreateDeleteEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeCreateData() {
    try {
      logger.methodStart();
      LeafNodeCreateAsyncResponseBody body = new LeafNodeCreateAsyncResponseBody();
      body.setNodeId(requestBody.getNodeId());
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
