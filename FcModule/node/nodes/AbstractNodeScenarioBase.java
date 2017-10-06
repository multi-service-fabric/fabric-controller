package msf.fc.node.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.AddressType;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.data.Rr;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.IpAddressUtil;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;
import msf.fc.node.nodes.leafs.data.entity.InternalOptionsLeafEntity;
import msf.fc.node.nodes.leafs.data.entity.LagIfLeafEntity;
import msf.fc.node.nodes.leafs.data.entity.LeafEntity;
import msf.fc.node.nodes.leafs.data.entity.OppositeLagIfLeafEntity;
import msf.fc.node.nodes.leafs.data.entity.OppositePhysicalIfLeafEntity;
import msf.fc.node.nodes.leafs.data.entity.PhysicalIfLeafEntity;
import msf.fc.node.nodes.rrs.data.entity.RrEntity;
import msf.fc.node.nodes.spines.data.entity.InternalOptionsSpineEntity;
import msf.fc.node.nodes.spines.data.entity.LagIfSpineEntity;
import msf.fc.node.nodes.spines.data.entity.OppositeLagIfSpineEntity;
import msf.fc.node.nodes.spines.data.entity.OppositePhysicalIfSpineEntity;
import msf.fc.node.nodes.spines.data.entity.PhysicalIfSpineEntity;
import msf.fc.node.nodes.spines.data.entity.SpineEntity;
import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.rest.common.JsonUtil;

public abstract class AbstractNodeScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractNodeScenarioBase.class);

  private SwClusterDao swClusterDao = new SwClusterDao();
  private NodeDao nodeDao = new NodeDao();
  private LagIfDao lagIfDao = new LagIfDao();

  private static final Integer FIRST_ID_NUM = 1;

  protected String createLinkIpAddress(SessionWrapper session, String swClusterId, NodeType nodeType, int spineNodeId,
      int leafNodeId, AddressType addressType) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "session", "swClusterId", "nodeType", "spineNodeId", "leafNodeId", "addressType" },
          new Object[] { session, swClusterId, nodeType, spineNodeId, leafNodeId, addressType });

      SwCluster swCluster = swClusterDao.read(session, swClusterId);

      String interfaceAddress = swCluster.getInterfaceStartAddress();

      int startIntAddress = IpAddressUtil.convertIpAddressToIntFromStr(interfaceAddress);
      int lmax = swCluster.getMaxLeafNum();

      int createIntAddress = ((spineNodeId - 1) * lmax + (leafNodeId - 1)) * 2;
      switch (addressType) {
        case IPV4:
          createIntAddress = createIntAddress * 2;
          break;

        default:
          throw new IllegalArgumentException();
      }
      switch (nodeType) {
        case SPINE:
          createIntAddress = startIntAddress + createIntAddress + 1;
          break;

        case LEAF:
          createIntAddress = startIntAddress + createIntAddress + 2;
          break;

        default:
          throw new IllegalArgumentException();
      }

      validateCreateIntAddress(swCluster, startIntAddress, createIntAddress);

      String address = IpAddressUtil.convertIpAddressToStrFromInt(createIntAddress);

      validateCreateStringAddress(swCluster, session, address);
      return address;
    } finally {
      logger.methodEnd();
    }
  }

  protected String createLoopbackIpAddress(SessionWrapper session, String swClusterId, NodeType type, int nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "type", "nodeId" },
          new Object[] { session, swClusterId, type, nodeId });

      SwCluster swCluster = swClusterDao.read(session, swClusterId);

      String startAddress = swCluster.getLoopbackStartAddress();
      int startIntAddress = IpAddressUtil.convertIpAddressToIntFromStr(startAddress);

      int createIntAddress;
      switch (type) {
        case SPINE:
          createIntAddress = startIntAddress + nodeId;
          break;

        case LEAF:
          int smax = swCluster.getMaxSpineNum();
          createIntAddress = startIntAddress + smax + nodeId;
          break;

        default:
          throw new IllegalArgumentException();
      }

      String address = IpAddressUtil.convertIpAddressToStrFromInt(createIntAddress);

      validateCreateStringAddress(swCluster, session, address);
      return address;
    } finally {
      logger.methodEnd();
    }
  }

  protected String createManagementIpAddress(SessionWrapper session, String swClusterId, NodeType type, int nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "type", "nodeId" },
          new Object[] { session, swClusterId, type, nodeId });

      SwCluster swCluster = swClusterDao.read(session, swClusterId);

      String startAddress = swCluster.getManagementStartAddress();
      int startIntAddress = IpAddressUtil.convertIpAddressToIntFromStr(startAddress);

      int createIntAddress;
      switch (type) {
        case SPINE:
          createIntAddress = startIntAddress + nodeId;
          break;

        case LEAF:
          int smax = swCluster.getMaxSpineNum();
          createIntAddress = startIntAddress + smax + nodeId;
          break;

        default:
          throw new IllegalArgumentException();
      }

      String address = IpAddressUtil.convertIpAddressToStrFromInt(createIntAddress);

      validateCreateStringAddress(swCluster, session, address);
      return address;
    } finally {
      logger.methodEnd();
    }
  }

  private void validateCreateIntAddress(SwCluster swCluster, int startIntAddress, int createIntAddress)
      throws MsfException {
    logger.methodStart(new String[] { "startIntAddress", "createIntAddress" },
        new Object[] { startIntAddress, createIntAddress });

    int sia = startIntAddress >> (32 - swCluster.getManagementAddressPrefix());
    int cia = createIntAddress >> (32 - swCluster.getManagementAddressPrefix());
    if (sia != cia) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "IP allocating error");
    }
  }

  private void validateCreateStringAddress(SwCluster swCluster, SessionWrapper session, String address)
      throws MsfException {
    logger.methodStart(new String[] { "address" }, new Object[] { address });

    if (IpAddressUtil.isLoopbackAddress(address)) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Local loop back address error");
    }
    if (IpAddressUtil.isNetworkAddress(address, swCluster.getManagementAddressPrefix())) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Network address error");
    }
    if (IpAddressUtil.isBroadcastAddress(address, swCluster.getManagementAddressPrefix())) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Broadcast address error");
    }
    List<Node> nodes = nodeDao.readList(session, address, address);
    if (!nodes.isEmpty()) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "IP address duplication error");
    }
    List<LagIf> lagIfs = lagIfDao.readList(session, address);
    if (!lagIfs.isEmpty()) {
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "IP address duplication error");
    }
  }

  protected Node getNode(SessionWrapper sessionWrapper, NodeDao nodeDao, String swClusterId, Integer nodeType,
      Integer nodeId) throws MsfException {
    try {
      logger.methodStart();
      Node node = nodeDao.read(sessionWrapper, swClusterId, nodeType, nodeId);
      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getLeafNodeIdList(List<Node> nodes) {
    try {
      logger.methodStart();
      List<String> leafNodeIdList = new ArrayList<>();
      for (Node node : nodes) {
        leafNodeIdList.add(String.valueOf(node.getNodeId()));
      }
      return leafNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LeafEntity> getLeafEntities(List<Node> nodes) throws MsfException {
    try {
      logger.methodStart();
      List<LeafEntity> leafEntities = new ArrayList<>();
      for (Node node : nodes) {
        leafEntities.add(getLeafEntity(node));
      }
      return leafEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected LeafEntity getLeafEntity(Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      LeafEntity leaf = new LeafEntity();
      leaf.setNodeId(String.valueOf(node.getNodeId()));
      leaf.setEquipmentTypeId(String.valueOf(node.getEquipment().getId().getEquipmentTypeId()));
      leaf.setHostName(node.getHostName());
      leaf.setMacAddr(node.getMacAddr());
      leaf.setUsername(node.getUsername());
      leaf.setProvisioning(node.getProvisioning());
      leaf.setVpnTypeEnum(node.getVpnTypeEnum());
      leaf.setPlaneEnum(node.getPlaneEnum());
      leaf.setSnmpCommunity(node.getSnmpCommunity());
      leaf.setNtpServerAddress(node.getNtpServerAddress());
      leaf.setRouterId(node.getRouterId());
      leaf.setManagementIfAddress(node.getManagementIfAddress());
      leaf.setProvisioningStatusEnum(node.getProvisioningStatusEnum());
      setPhysicalIfLeafList(leaf, node);
      setLagIfLeafList(leaf, node);
      leaf.setRegisteredRrNodeIdList(getRrNodeIdList(node.getRrs()));
      return leaf;
    } finally {
      logger.methodEnd();
    }
  }

  private void setPhysicalIfLeafList(LeafEntity leaf, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "leaf", "node" }, new Object[] { leaf, node });
      List<PhysicalIfLeafEntity> physicalIfList = new ArrayList<>();
      for (PhysicalIf physicalIf : node.getPhysicalIfs()) {
        PhysicalIfLeafEntity physicalIfEntity = new PhysicalIfLeafEntity();
        physicalIfEntity.setPhysicalIfId(physicalIf.getPhysicalIfId());
        Node oppositeNode = physicalIf.getOppositeNode();
        if (null != oppositeNode) {
          OppositePhysicalIfLeafEntity oppositeIf = new OppositePhysicalIfLeafEntity();
          oppositeIf.setFabricType(oppositeNode.getNodeTypeEnum().getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(oppositeNode.getNodeId()));
          oppositeIf.setPhysicalIfId(physicalIf.getOppositePhysicalIfId());
          physicalIfEntity.setOppositeIf(oppositeIf);
        }
        physicalIfEntity.setSpeed(physicalIf.getSpeed());
        physicalIfList.add(physicalIfEntity);
      }
      leaf.setPhysicalIfList(physicalIfList);
    } finally {
      logger.methodEnd();
    }
  }

  private void setLagIfLeafList(LeafEntity leaf, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "leaf", "node" }, new Object[] { leaf, node });
      List<LagIfLeafEntity> lagIfList = new ArrayList<>();
      List<InternalIfEntity> internalLinkIfList = new ArrayList<>();
      for (LagIf lagIf : node.getLagIfs()) {
        LagIfLeafEntity lagIfEntity = new LagIfLeafEntity();
        lagIfEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
        if (null != lagIf.getInternalLinkIf()) {
          InternalOptionsLeafEntity internalOption = new InternalOptionsLeafEntity();
          internalOption.setIpv4Address(lagIf.getIpv4Address());
          OppositeLagIfLeafEntity oppositeIf = new OppositeLagIfLeafEntity();
          if (lagIf.getOppositeNode() != null) {
            oppositeIf.setFabricType(NodeType.SPINE.getSingularMessage());
            oppositeIf.setNodeId(String.valueOf(lagIf.getOppositeNode().getNodeId()));
            oppositeIf.setLaglIfId(String.valueOf(lagIf.getOppositeLagIfId()));
          }
          internalOption.setOppositeIf(oppositeIf);
          lagIfEntity.setInternalOption(internalOption);
          InternalIfEntity internalIfEntity = new InternalIfEntity();
          internalIfEntity.setInternalIfId(String.valueOf(lagIf.getInternalLinkIf().getInternalLinkIfId()));
          internalIfEntity.setLaglIfId(String.valueOf(lagIf.getLagIfId()));
          internalIfEntity.setOperationStatusEnum(lagIf.getInternalLinkIf().getOperationStatusEnum());
          internalLinkIfList.add(internalIfEntity);
        }
        lagIfEntity.setMinimumLinks(lagIf.getMinimumLinks());
        lagIfEntity.setSpeed(lagIf.getSpeed());
        List<String> physicalIfIdList = new ArrayList<>();
        for (LagConstruction lagConstruction : lagIf.getLagConstructions()) {
          physicalIfIdList.add(lagConstruction.getPhysicalIf().getPhysicalIfId());
        }
        lagIfEntity.setPhysicalIfIdList(physicalIfIdList);
        lagIfList.add(lagIfEntity);
      }
      leaf.setInternalLinkIfList(internalLinkIfList);
      leaf.setLagIfList(lagIfList);
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RrEntity> getRrEntities(List<Rr> rrs) {
    try {
      logger.methodStart();
      List<RrEntity> rrEntities = new ArrayList<>();
      for (Rr rr : rrs) {
        rrEntities.add(getRrEntity(rr));
      }
      return rrEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getRrNodeIdList(List<Rr> rrs) {
    try {
      logger.methodStart();
      List<String> rrNodeIdList = new ArrayList<>();
      for (Rr rr : rrs) {
        rrNodeIdList.add(String.valueOf(rr.getId().getRrNodeId()));
      }
      return rrNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected RrEntity getRrEntity(Rr rr) {
    try {
      logger.methodStart(new String[] { "rr" }, new Object[] { rr });
      RrEntity rrEntity = new RrEntity();
      rrEntity.setNodeId(String.valueOf(rr.getId().getRrNodeId()));
      rrEntity.setRouterId(rr.getRrRouterId());
      return rrEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<String> getSpineNodeIdList(List<Node> nodes) {
    try {
      logger.methodStart();
      List<String> spineNodeIdList = new ArrayList<>();
      for (Node node : nodes) {
        spineNodeIdList.add(String.valueOf(node.getNodeId()));
      }
      return spineNodeIdList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<SpineEntity> getSpineEntities(List<Node> nodes) throws MsfException {
    try {
      logger.methodStart();
      List<SpineEntity> spineEntities = new ArrayList<>();
      for (Node node : nodes) {
        spineEntities.add(getSpineEntity(node));
      }
      return spineEntities;
    } finally {
      logger.methodEnd();
    }
  }

  protected SpineEntity getSpineEntity(Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      SpineEntity spine = new SpineEntity();
      spine.setNodeId(String.valueOf(node.getNodeId()));
      spine.setEquipmentTypeId(String.valueOf(node.getEquipment().getId().getEquipmentTypeId()));
      spine.setHostName(node.getHostName());
      spine.setMacAddr(node.getMacAddr());
      spine.setUsername(node.getUsername());
      spine.setProvisioning(node.getProvisioning());
      spine.setSnmpCommunity(node.getSnmpCommunity());
      spine.setNtpServerAddress(node.getNtpServerAddress());
      spine.setRpFlag(node.getRpFlag());
      spine.setRouterId(node.getRouterId());
      spine.setManagementIfAddress(node.getManagementIfAddress());
      spine.setProvisioningStatusEnum(node.getProvisioningStatusEnum());
      setPhysicalIfSpineList(spine, node);
      setLagIfSpineList(spine, node);
      spine.setRegisteredRrNodeIdList(getRrNodeIdList(node.getRrs()));
      return spine;
    } finally {
      logger.methodEnd();
    }
  }

  private void setPhysicalIfSpineList(SpineEntity spine, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "spine", "node" }, new Object[] { spine, node });
      List<PhysicalIfSpineEntity> physicalIfList = new ArrayList<>();
      for (PhysicalIf physicalIf : node.getPhysicalIfs()) {
        PhysicalIfSpineEntity physicalIfEntity = new PhysicalIfSpineEntity();
        physicalIfEntity.setPhysicalIfId(physicalIf.getPhysicalIfId());
        Node oppositeNode = physicalIf.getOppositeNode();
        if (null != oppositeNode) {
          OppositePhysicalIfSpineEntity oppositeIf = new OppositePhysicalIfSpineEntity();
          oppositeIf.setFabricType(oppositeNode.getNodeTypeEnum().getSingularMessage());
          oppositeIf.setNodeId(String.valueOf(oppositeNode.getNodeId()));
          oppositeIf.setPhysicalIfId(physicalIf.getOppositePhysicalIfId());
          physicalIfEntity.setOppositeIf(oppositeIf);
        }
        physicalIfEntity.setSpeed(physicalIf.getSpeed());
        physicalIfList.add(physicalIfEntity);
      }
      spine.setPhysicalIfList(physicalIfList);
    } finally {
      logger.methodEnd();
    }
  }

  private void setLagIfSpineList(SpineEntity spine, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "spine", "node" }, new Object[] { spine, node });
      List<LagIfSpineEntity> lagIfList = new ArrayList<>();
      List<InternalIfEntity> internalLinkIfList = new ArrayList<>();
      for (LagIf lagIf : node.getLagIfs()) {
        LagIfSpineEntity lagIfEntity = new LagIfSpineEntity();
        lagIfEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
        if (null != lagIf.getInternalLinkIf()) {
          InternalOptionsSpineEntity internalOption = new InternalOptionsSpineEntity();
          internalOption.setIpv4Address(lagIf.getIpv4Address());
          OppositeLagIfSpineEntity oppositeIf = new OppositeLagIfSpineEntity();
          if (lagIf.getOppositeNode() != null) {
            oppositeIf.setFabricType(NodeType.LEAF.getSingularMessage());
            oppositeIf.setNodeId(String.valueOf(lagIf.getOppositeNode().getNodeId()));
            oppositeIf.setLaglIfId(String.valueOf(lagIf.getOppositeLagIfId()));
          }
          internalOption.setOppositeIf(oppositeIf);
          lagIfEntity.setInternalOption(internalOption);
          InternalIfEntity internalIfEntity = new InternalIfEntity();
          internalIfEntity.setInternalIfId(String.valueOf(lagIf.getInternalLinkIf().getInternalLinkIfId()));
          internalIfEntity.setLaglIfId(String.valueOf(lagIf.getLagIfId()));
          internalIfEntity.setOperationStatusEnum(lagIf.getInternalLinkIf().getOperationStatusEnum());
          internalLinkIfList.add(internalIfEntity);
        }
        lagIfEntity.setMinimumLinks(lagIf.getMinimumLinks());
        lagIfEntity.setSpeed(lagIf.getSpeed());
        List<String> physicalIfIdList = new ArrayList<>();
        for (LagConstruction lagConstruction : lagIf.getLagConstructions()) {
          physicalIfIdList.add(lagConstruction.getPhysicalIf().getPhysicalIfId());
        }
        lagIfEntity.setPhysicalIfIdList(physicalIfIdList);
        lagIfList.add(lagIfEntity);
      }
      spine.setInternalLinkIfList(internalLinkIfList);
      spine.setLagIfList(lagIfList);
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeAfterLock(List<Node> nodes, NodeDao nodeDao, SessionWrapper sessionWrapper,
      String swClusterId, NodeType nodeType, String nodeId) throws MsfException {
    try {
      logger.methodStart();
      int spineNode = 0;
      int leafNode = 0;
      for (Node node : nodes) {
        if (!node.getProvisioningStatusEnum().equals(ProvisioningStatus.BOOT_COMPLETE)) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "There is an nodes of the other in a different state.");
        }
        switch (NodeType.getEnumFromCode(node.getNodeType())) {
          case SPINE:
            spineNode++;
            break;

          case LEAF:
            leafNode++;
            break;

          default:
            throw new IllegalArgumentException();
        }
      }
      SwCluster swCluster = swClusterDao.read(sessionWrapper, swClusterId);
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

        default:
          throw new IllegalArgumentException();
      }

      if (null != nodeDao.read(sessionWrapper, swClusterId, nodeType.getCode(), Integer.parseInt(nodeId))) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, "target resouece already exist. target = node");
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextLagIfId(SessionWrapper sessionWrapper, LagIfDao lagIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "lagIfDao" }, new Object[] { sessionWrapper, lagIfDao });
      LagIf biggestIdLagIf = lagIfDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdLagIf) {
        return FIRST_ID_NUM;
      } else {
        return biggestIdLagIf.getLagIfId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextInternalLinkIfId(SessionWrapper sessionWrapper, InternalLinkIfDao internalLinkIfDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "internalLinkIfDao" },
          new Object[] { sessionWrapper, internalLinkIfDao });
      InternalLinkIf biggestIdInternalLinkIf = internalLinkIfDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdInternalLinkIf) {
        return FIRST_ID_NUM;
      } else {
        return biggestIdInternalLinkIf.getInternalLinkIfId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected String getIpv4Address(SessionWrapper sessionWrapper, Node node, Integer oppositeNodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "node", "oppositeNodeId" },
          new Object[] { sessionWrapper, node, oppositeNodeId });
      int spineNodeId;
      int leafNodeId;
      NodeType nodeType = node.getNodeTypeEnum();
      switch (nodeType) {
        case SPINE:
          spineNodeId = node.getNodeId();
          leafNodeId = oppositeNodeId;
          break;

        case LEAF:
          spineNodeId = oppositeNodeId;
          leafNodeId = node.getNodeId();
          break;

        default:
          throw new IllegalArgumentException();
      }

      return createLinkIpAddress(sessionWrapper, node.getEquipment().getId().getSwClusterId(), nodeType, spineNodeId,
          leafNodeId, AddressType.IPV4);
    } finally {
      logger.methodEnd();
    }
  }

  protected void updateCreateNode(SessionWrapper sessionWrapper, NodeDao nodeDao, Node createNode,
      List<LagIf> oppositeLagIfs) throws MsfException {
    try {
      logger.methodStart();
      for (LagIf lagIf : createNode.getLagIfs()) {
        for (LagIf oppositeLagIf : oppositeLagIfs) {
          if (oppositeLagIf.getOppositeLagIfId().equals(lagIf.getLagIfId())) {
            lagIf.setOppositeNode(oppositeLagIf.getNode());
            lagIf.setOppositeLagIfId(oppositeLagIf.getLagIfId());
            break;
          }
        }
      }
      nodeDao.update(sessionWrapper, createNode);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body", "statusCode" }, new Object[] { body, statusCode });
      return createRestResponse((Object) body, statusCode);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractInternalResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body", "statusCode" }, new Object[] { body, statusCode });
      return createRestResponse((Object) body, statusCode);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createRestResponse(Object body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      String json = JsonUtil.toJson(body);

      RestResponseBase response = new RestResponseBase(statusCode, json);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodeCreateEcError(ErrorCode ecResponseStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecResponseStatus" }, new Object[] { ecResponseStatus });
      if (ecResponseStatus != null) {
        String errorMsg = "Check Node Create EC Error.";
        switch (ecResponseStatus) {
          case EC_CONNECTION_ERROR:
            throw new MsfException(ErrorCode.EC_CONNECTION_ERROR, errorMsg);
          case EC_CONTROL_ERROR:
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
          case EC_CONTROL_TIMEOUT:
            throw new MsfException(ErrorCode.EC_CONTROL_TIMEOUT, errorMsg);
          default:
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkProvisioningStatusBeforeNotify(Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      if (!node.getProvisioningStatusEnum().equals(ProvisioningStatus.STOPPED)) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Check ProvisioningStatus before notify.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkSwCluster(SessionWrapper sessionWrapper, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId" },
          new Object[] { sessionWrapper, swClusterId });
      SwCluster swCluster = swClusterDao.read(sessionWrapper, swClusterId);
      if (swCluster == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
    } finally {
      logger.methodEnd();
    }
  }

}
