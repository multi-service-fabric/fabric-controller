package msf.fc.node.nodes.spines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.EquipmentIfPK;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.nodes.AbstractNodeScenarioBase;
import msf.fc.node.nodes.spines.data.entity.PhysicalLinkSpineEntity;

public abstract class AbstractSpineNodeScenarioBase<T extends RestRequestBase> extends AbstractNodeScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSpineNodeScenarioBase.class);

  protected static final Integer RP_NUMBER_1 = 1;
  protected static final Integer RP_NUMBER_2 = 2;

  protected void checkNodeId(String nodeId) throws MsfException {

    ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.TARGET_RESOURCE_NOT_FOUND);

  }

  protected TreeMap<Integer, List<PhysicalLinkSpineEntity>> checkOppositeNode(List<Node> oppositeNodes,
      SessionWrapper sessionWrapper, List<PhysicalLinkSpineEntity> physicalLinkList) throws MsfException {
    try {
      logger.methodStart();
      boolean speedCapabilityFlag = false;
      TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap = new TreeMap<>();
      for (Node oppositeNode : oppositeNodes) {
        PhysicalIfDao physicalIfDao = new PhysicalIfDao();
        for (PhysicalLinkSpineEntity physicalLinkEntity : physicalLinkList) {
          speedCapabilityFlag = false;
          if (String.valueOf(oppositeNode.getNodeId()).equals(physicalLinkEntity.getRemoteLeafNodeId())) {
            PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, oppositeNode.getNodeInfoId(),
                physicalLinkEntity.getRemoteIfId());
            if (null == physicalIf) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Opposing interface is set not to exist.");
            } else if (null != physicalIf.getLagConstruction()) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
                  "The specified interface is used for other LagIF.");
            } else if (null != physicalIf.getOppositePhysicalIfId()) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified physical IF is used for other.");
            } else if (physicalIf.getPhysicalPortFlag()) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
                  "Specified physical IF is not one that has been registered for physical IF.");
            }
            for (EquipmentIf equipmentIf : oppositeNode.getEquipment().getEquipmentIfs()) {
              EquipmentIfPK equipmentIfId = equipmentIf.getId();
              if ((equipmentIfId.getPhysicalIfId().equals(physicalLinkEntity.getRemoteIfId()))
                  && (equipmentIfId.getSpeedCapability().equals(physicalLinkEntity.getSpeed()))) {
                speedCapabilityFlag = true;
                break;
              }
            }
            if (!speedCapabilityFlag) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
                  "All the physical IF that is specified in the same LagIF does not correspond to the specified speed.");
            }
            List<PhysicalLinkSpineEntity> spineEntities;
            if (null == (spineEntities = oppositeNodeMap.get(oppositeNode.getNodeId()))) {
              spineEntities = new ArrayList<>();
            }
            spineEntities.add(physicalLinkEntity);
            oppositeNodeMap.put(oppositeNode.getNodeId(), spineEntities);
          }
        }
        List<PhysicalLinkSpineEntity> spineEntities = oppositeNodeMap.get(oppositeNode.getNodeId());
        if (null == spineEntities) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "Link of all the opposing device for which currently exists is not defined.");
        } else {
          Set<String> checkRemoteIfId = new HashSet<>();
          String speed = null;
          for (PhysicalLinkSpineEntity physicalLinkEntity : spineEntities) {
            if (checkRemoteIfId.contains(physicalLinkEntity.getRemoteIfId())) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
                  "It is set to the same physical IF for the same device.");
            } else {
              checkRemoteIfId.add(physicalLinkEntity.getRemoteIfId());
            }

            if (speed == null) {
              speed = physicalLinkEntity.getSpeed();
            } else if (!speed.equals(physicalLinkEntity.getSpeed())) {
              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Speed information is different.");
            }
          }
        }
      }
      for (PhysicalLinkSpineEntity physicalLinkEntity : physicalLinkList) {
        boolean nodePresenceFlag = false;
        for (Node oppositeNode : oppositeNodes) {
          if (String.valueOf(oppositeNode.getNodeId()).equals(physicalLinkEntity.getRemoteLeafNodeId())) {
            nodePresenceFlag = true;
            break;
          }
        }
        if (!nodePresenceFlag) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "Nonexistent opposing node information is defined.");
        }
      }
      return oppositeNodeMap;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkExpansionNode(Equipment equipment, List<PhysicalLinkSpineEntity> physicalLinkList)
      throws MsfException {
    try {
      logger.methodStart();
      TreeMap<String, PhysicalLinkSpineEntity> physicalIfMap = new TreeMap<>();
      if (null == equipment) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is no information of the equipment.");
      }

      TreeMap<String, Set<String>> speedCapabilityMap = new TreeMap<>();
      for (EquipmentIf equipmentIf : equipment.getEquipmentIfs()) {
        String physicalIfId = equipmentIf.getId().getPhysicalIfId();
        Set<String> speedCapabilities;
        if (null == (speedCapabilities = speedCapabilityMap.get(physicalIfId))) {
          speedCapabilities = new TreeSet<String>();
        }
        speedCapabilities.add(equipmentIf.getId().getSpeedCapability());
        speedCapabilityMap.put(physicalIfId, speedCapabilities);
      }

      for (PhysicalLinkSpineEntity physicalLinkEntity : physicalLinkList) {
        String localIfId = physicalLinkEntity.getLocalIfId();
        if (speedCapabilityMap.get(localIfId) == null) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Nonexistent physical IF is defined.");
        } else if (!speedCapabilityMap.get(localIfId).contains(physicalLinkEntity.getSpeed())) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "All the physical IF that is specified in the same LagIF does not correspond to the specified speed.");
        }

        if (physicalIfMap.containsKey(physicalLinkEntity.getLocalIfId())) {
          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Is a duplicate of another ID.");
        }
        physicalIfMap.put(physicalLinkEntity.getLocalIfId(), physicalLinkEntity);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected List<PhysicalIf> createPhysicalIfs(Node node, List<Node> oppositeNodes, Equipment equipment,
      List<PhysicalLinkSpineEntity> physicalLinkList) throws MsfException {
    try {
      logger.methodStart();
      List<PhysicalIf> physicalIfs = new ArrayList<>();
      TreeSet<String> physicalIfIds = new TreeSet<String>();
      for (EquipmentIf equipmentIf : equipment.getEquipmentIfs()) {
        physicalIfIds.add(equipmentIf.getId().getPhysicalIfId());
      }
      for (String physicalIfId : physicalIfIds) {
        PhysicalIf physicalIf = new PhysicalIf();
        physicalIf.setNode(node);
        physicalIf.setPhysicalIfId(physicalIfId);
        physicalIf.setPhysicalPortFlag(false);
        for (PhysicalLinkSpineEntity physicalLinkEntity : physicalLinkList) {
          if (physicalIfId.equals(physicalLinkEntity.getLocalIfId())) {
            physicalIf.setOppositePhysicalIfId(physicalLinkEntity.getRemoteIfId());
            physicalIf.setSpeed(physicalLinkEntity.getSpeed());
            for (Node oppositeNode : oppositeNodes) {
              if (physicalLinkEntity.getRemoteLeafNodeId().equals(String.valueOf(oppositeNode.getNodeId()))) {
                physicalIf.setOppositeNode(oppositeNode);
              }
            }
            break;
          }
        }
        physicalIfs.add(physicalIf);
      }
      return physicalIfs;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LagIf> createLagIfs(SessionWrapper sessionWrapper, Node node,
      TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap, List<PhysicalIf> physicalIfs,
      boolean oppositeFlag) throws MsfException {
    try {
      logger.methodStart();
      List<LagIf> lagIfs = new ArrayList<>();
      LagIfDao lagIfDao = new LagIfDao();
      InternalLinkIfDao internalLinkIfDao = new InternalLinkIfDao();
      Integer nextLagIfId = getNextLagIfId(sessionWrapper, lagIfDao);
      Integer nextInternalLinkIfId = getNextInternalLinkIfId(sessionWrapper, internalLinkIfDao);
      logger.info("nextLagIfId = " + nextLagIfId + ", nextInternalLinkIfId = " + nextInternalLinkIfId + ".");
      for (Integer oppositeNodeId : oppositeNodeMap.keySet()) {
        lagIfs.add(createLagIf(sessionWrapper, node, oppositeNodeMap, physicalIfs, oppositeFlag, oppositeNodeId,
            nextLagIfId, nextInternalLinkIfId));
        nextLagIfId++;
        nextInternalLinkIfId++;
      }
      return lagIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private LagIf createLagIf(SessionWrapper sessionWrapper, Node node,
      TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap, List<PhysicalIf> physicalIfs,
      boolean oppositeFlag, Integer oppositeNodeId, Integer lagIfId, Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart();
      LagIf lagIf = new LagIf();
      lagIf.setLagIfId(lagIfId);
      lagIf.setNode(node);
      lagIf.setIpv4Address(getIpv4Address(sessionWrapper, node, oppositeNodeId));
      List<PhysicalLinkSpineEntity> spineEntities;
      if (oppositeFlag) {
        spineEntities = oppositeNodeMap.get(node.getNodeId());
      } else {
        spineEntities = oppositeNodeMap.get(oppositeNodeId);
      }
      lagIf.setMinimumLinks(spineEntities.size());
      lagIf.setSpeed(spineEntities.get(0).getSpeed());
      List<LagConstruction> lagConstructions = new ArrayList<>();
      for (PhysicalLinkSpineEntity spineEntity : spineEntities) {
        LagConstruction lagConstruction = new LagConstruction();
        lagConstruction.setLagIf(lagIf);
        for (PhysicalIf physicalIf : physicalIfs) {
          if (oppositeFlag) {
            if (spineEntity.getRemoteIfId().equals(physicalIf.getPhysicalIfId())) {
              lagConstruction.setPhysicalIf(physicalIf);
              lagConstruction.setPhysicalIfInfoId(physicalIf.getPhysicalIfInfoId());
              break;
            }
          } else {
            if (spineEntity.getLocalIfId().equals(physicalIf.getPhysicalIfId())) {
              lagConstruction.setPhysicalIf(physicalIf);
              break;
            }
          }
        }
        lagConstructions.add(lagConstruction);
      }
      lagIf.setLagConstructions(lagConstructions);
      InternalLinkIf internalLinkIf = new InternalLinkIf();
      internalLinkIf.setInternalLinkIfId(internalLinkIfId);
      internalLinkIf.setLagIf(lagIf);
      internalLinkIf.setOperationStatusEnum(InterfaceOperationStatus.UP);
      lagIf.setInternalLinkIf(internalLinkIf);
      return lagIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LagIf> createOppositeNode(SessionWrapper sessionWrapper, PhysicalIfDao physicalIfDao,
      LagIfDao lagIfDao, List<Node> oppositeNodes, TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap,
      Node createNode) throws MsfException {
    try {
      logger.methodStart();
      Integer nextLagIfId = 0;
      Integer nextInternalLinkIfId = 0;

      for (LagIf lagIf : createNode.getLagIfs()) {
        if (nextLagIfId <= lagIf.getLagIfId()) {
          nextLagIfId = lagIf.getLagIfId() + 1;
          nextInternalLinkIfId = lagIf.getInternalLinkIf().getInternalLinkIfId() + 1;
        }
      }

      List<LagIf> oppositeLagIfs = new ArrayList<>();
      for (Node node : oppositeNodes) {
        for (Integer oppositeNodeId : oppositeNodeMap.keySet()) {
          if (oppositeNodeId.equals(node.getNodeId())) {
            List<PhysicalLinkSpineEntity> spineEntities = oppositeNodeMap.get(oppositeNodeId);
            updatePhysicalIf(sessionWrapper, physicalIfDao, node, createNode, spineEntities);

            LagIf oppositeLagIf = createLagIf(sessionWrapper, node, oppositeNodeMap, node.getPhysicalIfs(), true,
                createNode.getNodeId(), nextLagIfId, nextInternalLinkIfId);
            nextLagIfId++;
            nextInternalLinkIfId++;
            oppositeLagIf.setOppositeNode(createNode);
            for (LagIf lagIf : createNode.getLagIfs()) {
              if (lagIf.getLagConstructions().get(0).getPhysicalIf().getPhysicalIfId()
                  .equals(spineEntities.get(0).getLocalIfId())) {
                oppositeLagIf.setOppositeLagIfId(lagIf.getLagIfId());
                break;
              }
            }
            lagIfDao.create(sessionWrapper, oppositeLagIf);
            oppositeLagIfs.add(oppositeLagIf);
          }
        }
      }
      return oppositeLagIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private void updatePhysicalIf(SessionWrapper sessionWrapper, PhysicalIfDao physicalIfDao, Node node, Node createNode,
      List<PhysicalLinkSpineEntity> spineEntities) throws MsfException {
    try {
      logger.methodStart();
      for (PhysicalLinkSpineEntity spineEntity : spineEntities) {
        for (PhysicalIf physicalIf : node.getPhysicalIfs()) {
          if (physicalIf.getPhysicalIfId().equals(spineEntity.getRemoteIfId())) {
            physicalIf.setOppositeNode(createNode);
            physicalIf.setOppositePhysicalIfId(spineEntity.getLocalIfId());
            physicalIf.setSpeed(spineEntity.getSpeed());
            physicalIfDao.update(sessionWrapper, physicalIf);
            break;
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

}
