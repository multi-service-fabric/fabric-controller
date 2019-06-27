
package msf.fc.node.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.LeafRr;
import msf.fc.common.config.type.data.Rr;
import msf.fc.common.config.type.data.Rrs;
import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfBreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfPhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeSubStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;
import msf.mfcfc.node.nodes.AbstractNodeScenarioBase;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutIfForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutIfForUserEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForUserEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLagIfOptionEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalOptionEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagIfForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagIfForUserEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeOppositeIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeOppositeLagIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalIfForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalIfForUserEntity;
import msf.mfcfc.node.nodes.rrs.data.entity.RrNodeRrEntity;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBreakoutIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLagIfOptionEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalOptionEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeLagIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeOppositeIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeOppositeLagIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalIfEntity;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of the node-related processing
 * in the configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractNodeScenarioBase<T extends RestRequestBase> extends AbstractNodeScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractNodeScenarioBase.class);

  protected boolean checkForCancelExecNodeInfo(SessionWrapper sessionWrapper, String clusterId, NodeType nodeType,
      String nodeId) throws MsfException {

    boolean isCreateNodeCancelled = false;
    try {
      logger.methodStart(new String[] { "clusterId", "nodeType", "nodeId" },
          new Object[] { clusterId, nodeType, nodeId });
      FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
      List<FcAsyncRequest> fcAsyncRequests = fcAsyncRequestsDao.readListExecNodeInfo(sessionWrapper);
      for (FcAsyncRequest fcAsyncRequest : fcAsyncRequests) {

        if (!fcAsyncRequest.getOperationId().equals(getOperationId())) {

          isCreateNodeCancelled = checkForExecDeleteNodeInfo(fcAsyncRequest, clusterId, nodeType, nodeId);
        }
      }
      return isCreateNodeCancelled;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkForExecDeleteNodeInfo(FcAsyncRequest fcAsyncRequest, String clusterId, NodeType nodeType,
      String nodeId) throws MsfException {
    try {

      logger.methodStart(new String[] { "fcAsyncRequest", "clusterId", "nodeType", "nodeId" },
          new Object[] { fcAsyncRequest, clusterId, nodeType, nodeId });
      if (HttpMethod.getEnumFromMessage(fcAsyncRequest.getRequestMethod()).equals(HttpMethod.POST)) {
        String asyncClusterId = null;
        NodeType asyncNodeType = null;
        String asyncNodeId = null;
        Matcher matcher = MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
        if (matcher.matches()) {

          asyncClusterId = matcher.group(1);

          asyncNodeType = NodeType.LEAF;

          LeafNodeCreateRequestBody requestBody = JsonUtil.fromJson(fcAsyncRequest.getRequestBody(),
              LeafNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
          asyncNodeId = requestBody.getNodeId();
        } else {
          matcher = MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(fcAsyncRequest.getRequestUri());
          if (matcher.matches()) {

            asyncClusterId = matcher.group(1);

            asyncNodeType = NodeType.SPINE;

            SpineNodeCreateRequestBody requestBody = JsonUtil.fromJson(fcAsyncRequest.getRequestBody(),
                SpineNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
            asyncNodeId = requestBody.getNodeId();
          } else {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "cluster status check error.");
          }
        }
        if ((!asyncClusterId.equals(clusterId)) || (!asyncNodeType.equals(nodeType)) || (!asyncNodeId.equals(nodeId))) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
              "Another node related operation is currently in progress.");
        } else {
          return true;
        }
      } else {

        throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR,
            "Another node related operation is currently in progress.");
      }
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

  protected FcNode getNode(SessionWrapper sessionWrapper, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeType", "nodeId" }, new Object[] { nodeType, nodeId });
      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType, nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource is not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getLeafNodeIdList(List<FcNode> nodes) {
    try {
      logger.methodStart(new String[] { "nodes" }, new Object[] { nodes });
      List<String> leafNodeIdList = new ArrayList<>();
      for (FcNode fcNode : nodes) {
        leafNodeIdList.add(String.valueOf(fcNode.getNodeId()));
      }
      return leafNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getSpineNodeIdList(List<FcNode> nodes) {
    try {
      logger.methodStart(new String[] { "nodes" }, new Object[] { nodes });
      List<String> spineNodeIdList = new ArrayList<>();
      for (FcNode fcNode : nodes) {
        spineNodeIdList.add(String.valueOf(fcNode.getNodeId()));
      }
      return spineNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getRrNodeIdList(List<Rr> rrs) {
    try {
      logger.methodStart(new String[] { "rrs" }, new Object[] { rrs });
      List<String> rrNodeIdList = new ArrayList<>();
      for (Rr rr : rrs) {
        rrNodeIdList.add(String.valueOf(rr.getRrNodeId()));
      }
      return rrNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafNodeForOwnerEntity getLeafOwnerEntity(FcNode fcNode, NodeEcEntity nodeEcEntity,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "nodeEcEntity", "interfaceReadListEcResponseBody" },
          new Object[] { fcNode, nodeEcEntity, interfaceReadListEcResponseBody });
      LeafNodeForOwnerEntity leaf = new LeafNodeForOwnerEntity();

      leaf.setNodeId(String.valueOf(fcNode.getNodeId()));
      leaf.setEquipmentTypeId(nodeEcEntity.getEquipmentTypeId());
      leaf.setLeafType(LeafType.getEnumFromCode(fcNode.getLeafNode().getLeafType()).getMessage());
      leaf.setHostName(nodeEcEntity.getHostName());
      leaf.setMacAddress(nodeEcEntity.getMacAddr());
      leaf.setUsername(nodeEcEntity.getUsername());
      leaf.setProvisioning(nodeEcEntity.getProvisioning());
      leaf.setVpnType(nodeEcEntity.getVpnType());
      leaf.setPlane(Integer.parseInt(nodeEcEntity.getPlane()));
      leaf.setSnmpCommunity(nodeEcEntity.getSnmpCommunity());
      leaf.setNtpServerAddress(nodeEcEntity.getNtpServerAddress());
      leaf.setRouterId(nodeEcEntity.getLoopbackIfAddress());
      leaf.setManagementIfAddress(nodeEcEntity.getManagementIfAddress());
      leaf.setProvisioningStatus(nodeEcEntity.getNodeState());

      List<FcPhysicalIf> physicalIfs = fcNode.getPhysicalIfs();
      leaf.setPhysicalIfList(
          getLeafPhysicalIfForOwnerEntities(physicalIfs, interfaceReadListEcResponseBody.getIfs().getPhysicalIfList()));

      List<FcLagIf> fcLagIfs = fcNode.getLagIfs();
      leaf.setLagIfList(
          getLeafLagIfForOwnerEntities(fcLagIfs, interfaceReadListEcResponseBody.getIfs().getLagIfList()));

      List<FcBreakoutIf> fcBreakoutIfs = fcNode.getBreakoutIfs();
      leaf.setBreakoutIfList(getLeafBreakoutIfForOwnerEntities(fcBreakoutIfs,
          interfaceReadListEcResponseBody.getIfs().getBreakoutIfList()));

      leaf.setInternalLinkIfList(getInternalLinkOwnerEntities(physicalIfs, fcLagIfs, fcBreakoutIfs));

      leaf.setRegisteredRrNodeIdList(
          getLeafRrNodeIdList(FcConfigManager.getInstance().getDataConfSwClusterData().getRrs()));

      leaf.setIrbType(nodeEcEntity.getIrbType());

      leaf.setQInQType(nodeEcEntity.getQInQType());

      return leaf;
    } finally {
      logger.methodEnd();
    }
  }

  private List<InternalLinkIfEntity> getInternalLinkOwnerEntities(List<FcPhysicalIf> physicalIfs,
      List<FcLagIf> fcLagIfs, List<FcBreakoutIf> fcBreakoutIfs) throws MsfException {
    try {
      logger.methodStart();
      List<InternalLinkIfEntity> internalLinkIfList = new ArrayList<>();

      for (FcPhysicalIf fcPhysicalIf : physicalIfs) {
        if (CollectionUtils.isNotEmpty(fcPhysicalIf.getInternalLinkIfs())) {
          InternalLinkIfEntity internaLinklIfEntity = new InternalLinkIfEntity();
          internaLinklIfEntity.setPhysicalIfId(fcPhysicalIf.getPhysicalIfId());

          FcInternalLinkIf fcInternalLinkIf = fcPhysicalIf.getInternalLinkIfs().get(0);
          internaLinklIfEntity.setInternalLinkIfId(String.valueOf(fcInternalLinkIf.getInternalLinkIfId()));
          internalLinkIfList.add(internaLinklIfEntity);
        }
      }

      for (FcLagIf fcLagIf : fcLagIfs) {
        if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {
          InternalLinkIfEntity internaLinklIfEntity = new InternalLinkIfEntity();
          internaLinklIfEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));

          FcInternalLinkIf fcInternalLinkIf = fcLagIf.getInternalLinkIfs().get(0);
          internaLinklIfEntity.setInternalLinkIfId(String.valueOf(fcInternalLinkIf.getInternalLinkIfId()));
          internalLinkIfList.add(internaLinklIfEntity);
        }
      }

      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {
        if (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {
          InternalLinkIfEntity internaLinklIfEntity = new InternalLinkIfEntity();
          internaLinklIfEntity.setBreakoutIfId(fcBreakoutIf.getBreakoutIfId());

          FcInternalLinkIf fcInternalLinkIf = fcBreakoutIf.getInternalLinkIfs().get(0);
          internaLinklIfEntity.setInternalLinkIfId(String.valueOf(fcInternalLinkIf.getInternalLinkIfId()));
          internalLinkIfList.add(internaLinklIfEntity);
        }
      }

      return internalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  protected SpineNodeEntity getSpainOwnerEntity(FcNode fcNode, NodeEcEntity nodeEcEntity,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "nodeEcEntity", "interfaceReadListEcResponseBody" },
          new Object[] { fcNode, nodeEcEntity, interfaceReadListEcResponseBody });
      SpineNodeEntity spine = new SpineNodeEntity();

      spine.setNodeId(String.valueOf(fcNode.getNodeId()));
      spine.setEquipmentTypeId(String.valueOf(nodeEcEntity.getEquipmentTypeId()));
      spine.setHostName(nodeEcEntity.getHostName());
      spine.setMacAddress(nodeEcEntity.getMacAddr());
      spine.setUsername(nodeEcEntity.getUsername());
      spine.setProvisioning(nodeEcEntity.getProvisioning());
      spine.setSnmpCommunity(nodeEcEntity.getSnmpCommunity());
      spine.setNtpServerAddress(nodeEcEntity.getNtpServerAddress());
      spine.setRouterId(nodeEcEntity.getLoopbackIfAddress());
      spine.setManagementIfAddress(nodeEcEntity.getManagementIfAddress());
      spine.setProvisioningStatus(nodeEcEntity.getNodeState());

      List<FcPhysicalIf> physicalIfs = fcNode.getPhysicalIfs();
      spine.setPhysicalIfList(
          getSpinePhysicalIfEntities(physicalIfs, interfaceReadListEcResponseBody.getIfs().getPhysicalIfList()));

      List<FcBreakoutIf> fcBreakoutIfs = fcNode.getBreakoutIfs();
      spine.setBreakoutIfList(
          getSpineBreakoutIfEntities(fcBreakoutIfs, interfaceReadListEcResponseBody.getIfs().getBreakoutIfList()));

      List<FcLagIf> fcLagIfs = fcNode.getLagIfs();
      spine.setLagIfList(getSpineLagIfEntities(fcLagIfs, interfaceReadListEcResponseBody.getIfs().getLagIfList()));

      spine.setInternalLinkIfList(getInternalLinkOwnerEntities(physicalIfs, fcLagIfs, fcBreakoutIfs));

      return spine;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodePhysicalIfForOwnerEntity> getLeafPhysicalIfForOwnerEntities(List<FcPhysicalIf> physicalIfs,
      List<PhysicalIfEcEntity> physicalIfEcList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<LeafNodePhysicalIfForOwnerEntity> leafNodePhysicalIfForOwnerEntities = new ArrayList<>();
      boolean isExist;
      for (FcPhysicalIf physicalIf : physicalIfs) {
        isExist = false;
        for (PhysicalIfEcEntity physicalIfData : physicalIfEcList) {
          if (physicalIf.getPhysicalIfId().equals(physicalIfData.getPhysicalIfId())) {
            leafNodePhysicalIfForOwnerEntities.add(getLeafPhysicalIfData(physicalIf, physicalIfData));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (physicalIfEcList.size() != leafNodePhysicalIfForOwnerEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodePhysicalIfForOwnerEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafNodePhysicalIfForOwnerEntity getLeafPhysicalIfData(FcPhysicalIf physicalIf,
      PhysicalIfEcEntity physicalIfEcResponseData) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf", "physicalIfEcResponseData" },
          new Object[] { physicalIf, physicalIfEcResponseData });
      LeafNodePhysicalIfForOwnerEntity physicalIfData = new LeafNodePhysicalIfForOwnerEntity();
      physicalIfData.setPhysicalIfId(physicalIf.getPhysicalIfId());
      List<FcInternalLinkIf> fcInternalLinkIfList = physicalIf.getInternalLinkIfs();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        LeafNodeInternalOptionEntity internalOptionData = new LeafNodeInternalOptionEntity();
        internalOptionData.setIpv4Address(physicalIfEcResponseData.getIpv4Address());
        LeafNodeOppositeIfEntity oppositeIf = new LeafNodeOppositeIfEntity();

        FcInternalLinkIf fcInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);
        FcPhysicalIf fcPhysicalIf = fcInternalLinkIf.getPhysicalIf();
        FcBreakoutIf fcBreakoutIf = fcInternalLinkIf.getBreakoutIf();
        if (fcPhysicalIf != null) {
          oppositeIf.setFabricType(NodeType.getEnumFromCode(fcPhysicalIf.getNode().getNodeType()).getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcPhysicalIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          oppositeIf.setIfId(fcPhysicalIf.getPhysicalIfId());
        } else {
          oppositeIf.setFabricType(NodeType.getEnumFromCode(fcBreakoutIf.getNode().getNodeType()).getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcBreakoutIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          oppositeIf.setIfId(fcBreakoutIf.getBreakoutIfId());
        }
        internalOptionData.setOppositeIf(oppositeIf);
        internalOptionData.setTrafficThreshold(fcInternalLinkIfList.get(0).getTrafficThreshold());

        physicalIfData.setInternalOptions(internalOptionData);
      }
      physicalIfData.setSpeed(physicalIfEcResponseData.getLinkSpeed());

      return physicalIfData;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodeLagIfForOwnerEntity> getLeafLagIfForOwnerEntities(List<FcLagIf> fcLagIfs,
      List<LagIfEcEntity> lagIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<LeafNodeLagIfForOwnerEntity> leafNodeLagIfForOwnerEntities = new ArrayList<>();
      boolean isExist;
      for (FcLagIf fcLagIf : fcLagIfs) {
        isExist = false;
        for (LagIfEcEntity lagIfEcEntity : lagIfEcList) {
          if (lagIfEcEntity.getLagIfId().equals(String.valueOf(fcLagIf.getLagIfId()))) {
            leafNodeLagIfForOwnerEntities.add(getLeafLagIfData(fcLagIf, lagIfEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (lagIfEcList.size() != leafNodeLagIfForOwnerEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodeLagIfForOwnerEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafNodeLagIfForOwnerEntity getLeafLagIfData(FcLagIf fcLagIf, LagIfEcEntity lagIfEcEntity)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf", "readLagIfEcEntity" }, new Object[] { fcLagIf, lagIfEcEntity });
      LeafNodeLagIfForOwnerEntity lagIfEntity = new LeafNodeLagIfForOwnerEntity();
      lagIfEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));
      if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {
        LeafNodeInternalLagIfOptionEntity leafNodeInternalLagIfOptionEntity = new LeafNodeInternalLagIfOptionEntity();

        leafNodeInternalLagIfOptionEntity.setIpv4Address(lagIfEcEntity.getIpv4Address());
        LeafNodeOppositeLagIfEntity lagIfOpposite = new LeafNodeOppositeLagIfEntity();
        FcInternalLinkIf oppositeInternalLinkIf = fcLagIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
            .get(0);
        lagIfOpposite.setFabricType(
            NodeType.getEnumFromCode(oppositeInternalLinkIf.getLagIf().getNode().getNodeType()).getSingularMessage());
        lagIfOpposite.setNodeId(String.valueOf(oppositeInternalLinkIf.getLagIf().getNode().getNodeId()));
        lagIfOpposite.setLagIfId(String.valueOf(oppositeInternalLinkIf.getLagIf().getLagIfId()));
        leafNodeInternalLagIfOptionEntity.setOppositeIf(lagIfOpposite);
        leafNodeInternalLagIfOptionEntity
            .setTrafficThreshold(fcLagIf.getInternalLinkIfs().get(0).getTrafficThreshold());
        lagIfEntity.setInternalOptions(leafNodeInternalLagIfOptionEntity);
      }
      lagIfEntity.setSpeed(lagIfEcEntity.getLinkSpeed());
      int minimumLinks = 0;
      List<String> physicalIfIdList = new ArrayList<>();
      List<String> breakoutIfIdList = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getPhysicalIfList())) {
        for (LagIfPhysicalIfEcEntity lagIfPhysicalIfEcEntity : lagIfEcEntity.getLagMember().getPhysicalIfList()) {
          if (lagIfPhysicalIfEcEntity != null) {
            physicalIfIdList.add(lagIfPhysicalIfEcEntity.getPhysicalIfId());
            minimumLinks++;
          }
        }
      }
      lagIfEntity.setPhysicalIfIdList(physicalIfIdList);

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getBreakoutIfList())) {
        for (LagIfBreakoutIfEcEntity lagIfBreakoutIfEcEntity : lagIfEcEntity.getLagMember().getBreakoutIfList()) {
          if (lagIfBreakoutIfEcEntity != null) {
            breakoutIfIdList.add(lagIfBreakoutIfEcEntity.getBreakoutIfId());
            minimumLinks++;
          }
        }
      }
      lagIfEntity.setBreakoutIfIdList(breakoutIfIdList);

      lagIfEntity.setMinimumLinks(minimumLinks);
      return lagIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodeBreakoutIfForOwnerEntity> getLeafBreakoutIfForOwnerEntities(List<FcBreakoutIf> fcBreakoutIfs,
      List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<LeafNodeBreakoutIfForOwnerEntity> leafNodeBreakoutIfForOwnerEntities = new ArrayList<>();
      boolean isExist;
      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {
        isExist = false;
        for (BreakoutIfEcEntity breakoutIfEcEntity : breakoutIfEcList) {
          if (breakoutIfEcEntity.getBreakoutIfId().equals(fcBreakoutIf.getBreakoutIfId())) {
            leafNodeBreakoutIfForOwnerEntities.add(getLeafBreakoutIfData(fcBreakoutIf, breakoutIfEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (breakoutIfEcList.size() != leafNodeBreakoutIfForOwnerEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodeBreakoutIfForOwnerEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafNodeBreakoutIfForOwnerEntity getLeafBreakoutIfData(FcBreakoutIf fcBreakoutIf,
      BreakoutIfEcEntity breakoutIfEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcBreakoutIf", "breakoutIfEcEntity" },
          new Object[] { fcBreakoutIf, breakoutIfEcEntity });
      LeafNodeBreakoutIfForOwnerEntity breakoutIfEntity = new LeafNodeBreakoutIfForOwnerEntity();
      breakoutIfEntity.setBreakoutIfId(String.valueOf(fcBreakoutIf.getBreakoutIfId()));
      breakoutIfEntity.setSpeed(breakoutIfEcEntity.getLinkSpeed());

      if (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {
        LeafNodeInternalOptionEntity leafNodeInternalOptionEntity = new LeafNodeInternalOptionEntity();

        leafNodeInternalOptionEntity.setIpv4Address(breakoutIfEcEntity.getIpv4Address());
        LeafNodeOppositeIfEntity opposite = new LeafNodeOppositeIfEntity();
        FcInternalLinkIf oppositeInternalLinkIf = fcBreakoutIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
            .get(0);

        FcPhysicalIf fcPhysicalIf = oppositeInternalLinkIf.getPhysicalIf();
        FcBreakoutIf fcBreakoutIfData = oppositeInternalLinkIf.getBreakoutIf();
        if (fcPhysicalIf != null) {
          opposite.setFabricType(NodeType.getEnumFromCode(fcPhysicalIf.getNode().getNodeType()).getSingularMessage());
          opposite.setNodeId(String.valueOf(fcPhysicalIf.getNode().getNodeId()));
          opposite.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          opposite.setIfId(fcPhysicalIf.getPhysicalIfId());
        } else {
          opposite
              .setFabricType(NodeType.getEnumFromCode(fcBreakoutIfData.getNode().getNodeType()).getSingularMessage());
          opposite.setNodeId(String.valueOf(fcBreakoutIfData.getNode().getNodeId()));
          opposite.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          opposite.setIfId(fcBreakoutIfData.getBreakoutIfId());
        }
        leafNodeInternalOptionEntity.setOppositeIf(opposite);
        leafNodeInternalOptionEntity
            .setTrafficThreshold(fcBreakoutIf.getInternalLinkIfs().get(0).getTrafficThreshold());

        breakoutIfEntity.setInternalOptions(leafNodeInternalOptionEntity);
      }

      return breakoutIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getLeafRrNodeIdList(Rrs rrs) {
    try {
      logger.methodStart(new String[] { "rrs" }, new Object[] { rrs });
      List<String> rrNodeIdList = new ArrayList<>();
      for (LeafRr leafRr : rrs.getLeafRr()) {
        rrNodeIdList.add(String.valueOf(leafRr.getLeafRrNodeId()));
      }
      return rrNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected ArrayList<SpineNodePhysicalIfEntity> getSpinePhysicalIfEntities(List<FcPhysicalIf> physicalIfs,
      List<PhysicalIfEcEntity> physicalIfEcList) throws MsfException {
    try {
      logger.methodStart();
      ArrayList<SpineNodePhysicalIfEntity> spineNodePhysicalIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcPhysicalIf physicalIf : physicalIfs) {
        isExist = false;
        for (PhysicalIfEcEntity physicalIfData : physicalIfEcList) {
          if (physicalIf.getPhysicalIfId().equals(physicalIfData.getPhysicalIfId())) {
            spineNodePhysicalIfEntities.add(getSpinePhysicalIfData(physicalIf, physicalIfData));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (physicalIfEcList.size() != spineNodePhysicalIfEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return spineNodePhysicalIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  private SpineNodePhysicalIfEntity getSpinePhysicalIfData(FcPhysicalIf physicalIf,
      PhysicalIfEcEntity physicalIfEcResponseData) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf", "physicalIfEcResponseData" },
          new Object[] { physicalIf, physicalIfEcResponseData });
      SpineNodePhysicalIfEntity physicalIfData = new SpineNodePhysicalIfEntity();
      physicalIfData.setPhysicalIfId(physicalIf.getPhysicalIfId());
      List<FcInternalLinkIf> fcInternalLinkIfList = physicalIf.getInternalLinkIfs();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        SpineNodeInternalOptionEntity internalOptionData = new SpineNodeInternalOptionEntity();
        internalOptionData.setIpv4Address(physicalIfEcResponseData.getIpv4Address());
        SpineNodeOppositeIfEntity oppositeIf = new SpineNodeOppositeIfEntity();

        FcInternalLinkIf fcInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);
        FcPhysicalIf fcPhysicalIf = fcInternalLinkIf.getPhysicalIf();
        FcBreakoutIf fcBreakoutIf = fcInternalLinkIf.getBreakoutIf();
        if (fcPhysicalIf != null) {
          oppositeIf.setFabricType(NodeType.getEnumFromCode(fcPhysicalIf.getNode().getNodeType()).getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcPhysicalIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          oppositeIf.setIfId(fcPhysicalIf.getPhysicalIfId());
        } else {
          oppositeIf.setFabricType(NodeType.getEnumFromCode(fcBreakoutIf.getNode().getNodeType()).getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(fcBreakoutIf.getNode().getNodeId()));
          oppositeIf.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          oppositeIf.setIfId(fcBreakoutIf.getBreakoutIfId());
        }
        internalOptionData.setOppositeIf(oppositeIf);
        internalOptionData.setTrafficThreshold(fcInternalLinkIfList.get(0).getTrafficThreshold());

        physicalIfData.setInternalOptions(internalOptionData);
      }
      physicalIfData.setSpeed(physicalIfEcResponseData.getLinkSpeed());

      return physicalIfData;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<SpineNodeLagIfEntity> getSpineLagIfEntities(List<FcLagIf> fcLagIfs, List<LagIfEcEntity> lagIfEcList)
      throws MsfException {
    try {
      logger.methodStart();
      List<SpineNodeLagIfEntity> spineNodeLagIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcLagIf fcLagIf : fcLagIfs) {
        isExist = false;
        for (LagIfEcEntity lagIfEcEntity : lagIfEcList) {
          if (lagIfEcEntity.getLagIfId().equals(String.valueOf(fcLagIf.getLagIfId()))) {
            spineNodeLagIfEntities.add(getSpineLagIfData(fcLagIf, lagIfEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (lagIfEcList.size() != spineNodeLagIfEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return spineNodeLagIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected SpineNodeLagIfEntity getSpineLagIfData(FcLagIf fcLagIf, LagIfEcEntity lagIfEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf", "readLagIfEcEntity" }, new Object[] { fcLagIf, lagIfEcEntity });
      SpineNodeLagIfEntity lagIfEntity = new SpineNodeLagIfEntity();
      lagIfEntity.setLagIfId(String.valueOf(fcLagIf.getLagIfId()));
      if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {
        SpineNodeInternalLagIfOptionEntity spineNodeInternalLagIfOptionEntity = new SpineNodeInternalLagIfOptionEntity();

        spineNodeInternalLagIfOptionEntity.setIpv4Address(lagIfEcEntity.getIpv4Address());
        SpineNodeOppositeLagIfEntity lagIfOpposite = new SpineNodeOppositeLagIfEntity();
        FcInternalLinkIf oppositeInternalLinkIf = fcLagIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
            .get(0);
        lagIfOpposite.setFabricType(
            NodeType.getEnumFromCode(oppositeInternalLinkIf.getLagIf().getNode().getNodeType()).getSingularMessage());
        lagIfOpposite.setNodeId(String.valueOf(oppositeInternalLinkIf.getLagIf().getNode().getNodeId()));
        lagIfOpposite.setLagIfId(String.valueOf(oppositeInternalLinkIf.getLagIf().getLagIfId()));
        spineNodeInternalLagIfOptionEntity.setOppositeIf(lagIfOpposite);
        spineNodeInternalLagIfOptionEntity
            .setTrafficThreshold(fcLagIf.getInternalLinkIfs().get(0).getTrafficThreshold());
        lagIfEntity.setInternalOption(spineNodeInternalLagIfOptionEntity);
      }
      lagIfEntity.setSpeed(lagIfEcEntity.getLinkSpeed());
      int minimumLinks = 0;
      List<String> physicalIfIdList = new ArrayList<>();
      List<String> breakoutIfIdList = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getPhysicalIfList())) {
        for (LagIfPhysicalIfEcEntity lagIfPhysicalIfEcEntity : lagIfEcEntity.getLagMember().getPhysicalIfList()) {
          if (lagIfPhysicalIfEcEntity != null) {
            physicalIfIdList.add(lagIfPhysicalIfEcEntity.getPhysicalIfId());
            minimumLinks++;
          }
        }
      }
      lagIfEntity.setPhysicalIfIdList(physicalIfIdList);

      if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getBreakoutIfList())) {
        for (LagIfBreakoutIfEcEntity lagIfBreakoutIfEcEntity : lagIfEcEntity.getLagMember().getBreakoutIfList()) {
          if (lagIfBreakoutIfEcEntity != null) {
            breakoutIfIdList.add(lagIfBreakoutIfEcEntity.getBreakoutIfId());
            minimumLinks++;
          }
        }
      }
      lagIfEntity.setBreakoutIfIdList(breakoutIfIdList);

      lagIfEntity.setMinimumLinks(minimumLinks);
      return lagIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<SpineNodeBreakoutIfEntity> getSpineBreakoutIfEntities(List<FcBreakoutIf> fcBreakoutIfs,
      List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<SpineNodeBreakoutIfEntity> spineNodeBreakoutIfEntities = new ArrayList<>();
      boolean isExist;
      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {
        isExist = false;
        for (BreakoutIfEcEntity breakoutIfEcEntity : breakoutIfEcList) {
          if (breakoutIfEcEntity.getBreakoutIfId().equals(fcBreakoutIf.getBreakoutIfId())) {
            spineNodeBreakoutIfEntities.add(getSpineBreakoutIfData(fcBreakoutIf, breakoutIfEcEntity));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (breakoutIfEcList.size() != spineNodeBreakoutIfEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return spineNodeBreakoutIfEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected SpineNodeBreakoutIfEntity getSpineBreakoutIfData(FcBreakoutIf fcBreakoutIf,
      BreakoutIfEcEntity breakoutIfEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcBreakoutIf", "breakoutIfEcEntity" },
          new Object[] { fcBreakoutIf, breakoutIfEcEntity });
      SpineNodeBreakoutIfEntity breakoutIfEntity = new SpineNodeBreakoutIfEntity();
      breakoutIfEntity.setBreakoutIfId(String.valueOf(fcBreakoutIf.getBreakoutIfId()));
      breakoutIfEntity.setSpeed(breakoutIfEcEntity.getLinkSpeed());

      if (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {
        SpineNodeInternalOptionEntity spineNodeInternalOptionEntity = new SpineNodeInternalOptionEntity();

        spineNodeInternalOptionEntity.setIpv4Address(breakoutIfEcEntity.getIpv4Address());
        SpineNodeOppositeIfEntity opposite = new SpineNodeOppositeIfEntity();
        FcInternalLinkIf oppositeInternalLinkIf = fcBreakoutIf.getInternalLinkIfs().get(0).getOppositeInternalLinkIfs()
            .get(0);
        FcPhysicalIf fcPhysicalIf = oppositeInternalLinkIf.getPhysicalIf();
        FcBreakoutIf fcBreakoutIfData = oppositeInternalLinkIf.getBreakoutIf();

        if (fcPhysicalIf != null) {
          opposite.setFabricType(NodeType.getEnumFromCode(fcPhysicalIf.getNode().getNodeType()).getSingularMessage());
          opposite.setNodeId(String.valueOf(fcPhysicalIf.getNode().getNodeId()));
          opposite.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          opposite.setIfId(fcPhysicalIf.getPhysicalIfId());
        } else {
          opposite
              .setFabricType(NodeType.getEnumFromCode(fcBreakoutIfData.getNode().getNodeType()).getSingularMessage());
          opposite.setNodeId(String.valueOf(fcBreakoutIfData.getNode().getNodeId()));
          opposite.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          opposite.setIfId(fcBreakoutIfData.getBreakoutIfId());
        }

        spineNodeInternalOptionEntity.setOppositeIf(opposite);
        spineNodeInternalOptionEntity
            .setTrafficThreshold(fcBreakoutIf.getInternalLinkIfs().get(0).getTrafficThreshold());
        breakoutIfEntity.setInternalOptions(spineNodeInternalOptionEntity);
      }

      return breakoutIfEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafNodeForUserEntity getLeafUserEntity(FcNode fcNode, NodeEcEntity nodeEcEntity,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "nodeEcEntity", "interfaceReadListEcResponseBody" },
          new Object[] { fcNode, nodeEcEntity, interfaceReadListEcResponseBody });
      LeafNodeForUserEntity leaf = new LeafNodeForUserEntity();
      leaf.setNodeId(String.valueOf(fcNode.getNodeId()));
      leaf.setEquipmentTypeId(nodeEcEntity.getEquipmentTypeId());
      leaf.setVpnType(nodeEcEntity.getVpnType());
      leaf.setPlane(Integer.parseInt(nodeEcEntity.getPlane()));
      leaf.setProvisioningStatus(nodeEcEntity.getNodeState());

      List<FcPhysicalIf> physicalIfs = fcNode.getPhysicalIfs();
      setPhysicalIfLeafUserList(leaf, interfaceReadListEcResponseBody, physicalIfs);

      List<FcLagIf> lagIfs = fcNode.getLagIfs();
      setLagIfLeafUserList(leaf, interfaceReadListEcResponseBody, lagIfs);

      List<FcBreakoutIf> breakoutIfs = fcNode.getBreakoutIfs();
      setBreakoutIfLeafUserList(leaf, interfaceReadListEcResponseBody, breakoutIfs);

      leaf.setIrbType(nodeEcEntity.getIrbType());

      leaf.setQInQType(nodeEcEntity.getQInQType());

      return leaf;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setPhysicalIfLeafUserList(LeafNodeForUserEntity leaf,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody, List<FcPhysicalIf> physicalIfs)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "leaf", "interfaceReadListEcResponseBody" },
          new Object[] { leaf, interfaceReadListEcResponseBody });
      leaf.setPhysicalIfList(
          getLeafPhysicalIfForUserEntities(physicalIfs, interfaceReadListEcResponseBody.getIfs().getPhysicalIfList()));
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodePhysicalIfForUserEntity> getLeafPhysicalIfForUserEntities(List<FcPhysicalIf> physicalIfs,
      List<PhysicalIfEcEntity> physicalIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<LeafNodePhysicalIfForUserEntity> leafNodePhysicalIfForUserEntities = new ArrayList<>();
      boolean isExist;
      for (FcPhysicalIf physicalIf : physicalIfs) {
        isExist = false;
        for (PhysicalIfEcEntity physicalIfData : physicalIfEcList) {
          if (physicalIf.getPhysicalIfId().equals(physicalIfData.getPhysicalIfId())) {
            LeafNodePhysicalIfForUserEntity leafNodePhysicalIfForUserEntity = new LeafNodePhysicalIfForUserEntity();
            leafNodePhysicalIfForUserEntity.setPhysicalIfId(physicalIfData.getPhysicalIfId());
            if (null != physicalIfData.getLinkSpeed()) {
              leafNodePhysicalIfForUserEntity.setSpeed(physicalIfData.getLinkSpeed());
            }
            leafNodePhysicalIfForUserEntities.add(leafNodePhysicalIfForUserEntity);
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (physicalIfEcList.size() != leafNodePhysicalIfForUserEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodePhysicalIfForUserEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setLagIfLeafUserList(LeafNodeForUserEntity leaf,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody, List<FcLagIf> lagIfs) throws MsfException {
    try {
      logger.methodStart(new String[] { "leaf", "interfaceReadListEcResponseBody" },
          new Object[] { leaf, interfaceReadListEcResponseBody });
      leaf.setLagIfList(getLeafLagIfForUserEntities(lagIfs, interfaceReadListEcResponseBody.getIfs().getLagIfList()));
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodeLagIfForUserEntity> getLeafLagIfForUserEntities(List<FcLagIf> fcLagIfs,
      List<LagIfEcEntity> lagIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<LeafNodeLagIfForUserEntity> leafNodeLagIfForUserEntities = new ArrayList<>();
      boolean isExist;
      for (FcLagIf fcLagIf : fcLagIfs) {
        isExist = false;
        for (LagIfEcEntity lagIfEcEntity : lagIfEcList) {
          if (lagIfEcEntity.getLagIfId().equals(String.valueOf(fcLagIf.getLagIfId()))) {
            LeafNodeLagIfForUserEntity lagIfEntity = new LeafNodeLagIfForUserEntity();
            lagIfEntity.setLagIfId(String.valueOf(lagIfEcEntity.getLagIfId()));
            lagIfEntity.setSpeed(lagIfEcEntity.getLinkSpeed());
            List<String> physicalIfIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getPhysicalIfList())) {
              for (LagIfPhysicalIfEcEntity lagIfPhysicalIfEcEntity : lagIfEcEntity.getLagMember().getPhysicalIfList()) {
                physicalIfIdList.add(lagIfPhysicalIfEcEntity.getPhysicalIfId());
              }
            }
            List<String> breakoutIfIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(lagIfEcEntity.getLagMember().getBreakoutIfList())) {
              for (LagIfBreakoutIfEcEntity lagIfBreakoutIfEcEntity : lagIfEcEntity.getLagMember().getBreakoutIfList()) {
                breakoutIfIdList.add(lagIfBreakoutIfEcEntity.getBreakoutIfId());
              }
            }
            lagIfEntity.setPhysicalIfIdList(physicalIfIdList);
            lagIfEntity.setBreakoutIfIdList(breakoutIfIdList);
            leafNodeLagIfForUserEntities.add(lagIfEntity);
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (lagIfEcList.size() != leafNodeLagIfForUserEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodeLagIfForUserEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setBreakoutIfLeafUserList(LeafNodeForUserEntity leaf,
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody, List<FcBreakoutIf> breakoutIfs)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "leaf", "interfaceReadListEcResponseBody" },
          new Object[] { leaf, interfaceReadListEcResponseBody });
      leaf.setBreakoutIfList(
          getLeafBreakoutIfForUserEntities(breakoutIfs, interfaceReadListEcResponseBody.getIfs().getBreakoutIfList()));
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafNodeBreakoutIfForUserEntity> getLeafBreakoutIfForUserEntities(List<FcBreakoutIf> fcBreakoutIfs,
      List<BreakoutIfEcEntity> breakoutIfEcList) throws MsfException {
    try {
      logger.methodStart();
      List<LeafNodeBreakoutIfForUserEntity> leafNodeBreakoutIfForUserEntities = new ArrayList<>();
      boolean isExist;
      for (FcBreakoutIf fcBreakoutIf : fcBreakoutIfs) {
        isExist = false;
        for (BreakoutIfEcEntity breakoutIfEcEntity : breakoutIfEcList) {
          if (breakoutIfEcEntity.getBreakoutIfId().equals(fcBreakoutIf.getBreakoutIfId())) {
            LeafNodeBreakoutIfForUserEntity leafNodeBreakoutIfForUserEntity = new LeafNodeBreakoutIfForUserEntity();
            leafNodeBreakoutIfForUserEntity.setBreakoutIfId(breakoutIfEcEntity.getBreakoutIfId());
            leafNodeBreakoutIfForUserEntity.setSpeed(breakoutIfEcEntity.getLinkSpeed());
            leafNodeBreakoutIfForUserEntities.add(leafNodeBreakoutIfForUserEntity);
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "There is no appropriate data in the EC system.");
        }
      }
      if (breakoutIfEcList.size() != leafNodeBreakoutIfForUserEntities.size()) {

        logger.warn("There is no appropriate data in the FC system. It sometimes occurs during node addition.");
      }
      return leafNodeBreakoutIfForUserEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected NodeReadEcResponseBody sendNodeRead(FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ.getHttpMethod(),
          EcRequestUri.NODE_READ.getUri(String.valueOf(fcNode.getEcNodeId())), null, ecControlIpAddress, ecControlPort);

      NodeReadEcResponseBody nodeReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          nodeReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return nodeReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected NodeReadListEcResponseBody sendNodeReadList() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_READ_LIST.getHttpMethod(),
          EcRequestUri.NODE_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      NodeReadListEcResponseBody nodeReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          NodeReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          nodeReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return nodeReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected InterfaceReadListEcResponseBody sendInterfaceInfoReadList(FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.IF_READ_LIST.getHttpMethod(),
          EcRequestUri.IF_READ_LIST.getUri(String.valueOf(fcNode.getEcNodeId())), null, ecControlIpAddress,
          ecControlPort);
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), InterfaceReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          interfaceReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return interfaceReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected RrNodeRrEntity getRrEntity(Rr rr) {
    try {
      logger.methodStart(new String[] { "rr" }, new Object[] { rr });
      RrNodeRrEntity rrEntity = new RrNodeRrEntity();
      rrEntity.setNodeId(String.valueOf(rr.getRrNodeId()));
      rrEntity.setRouterId(rr.getRrRouterId());
      return rrEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RrNodeRrEntity> getRrEntities(List<Rr> rrs) {
    try {
      logger.methodStart(new String[] { "rrs" }, new Object[] { rrs });
      List<RrNodeRrEntity> rrEntities = new ArrayList<>();
      for (Rr rr : rrs) {
        rrEntities.add(getRrEntity(rr));
      }
      return rrEntities;
    } finally {
      logger.methodEnd();
    }
  }
}
