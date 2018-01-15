
package msf.fc.node.nodes.leafs;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.node.nodes.FcAbstractNodeRunnerBase;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutIfCreateEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLocalEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeMemberIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeOppositeEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalLinkEntity;

/**
 * Abstract class to implement common process of node management (Leaf)-related
 * asynchronous processing in configuration management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractLeafNodeRunnerBase extends FcAbstractNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractLeafNodeRunnerBase.class);

  protected List<FcNode> getOtherBorderLeafNodeList(List<FcNode> fcNodeList, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "nodeId" }, new Object[] { fcNodeList, nodeId });
      List<FcNode> borderLeafNodeList = new ArrayList<>();
      for (FcNode fcNode : fcNodeList) {
        if ((NodeType.getEnumFromCode(fcNode.getNodeType()).equals(NodeType.LEAF))
            && (LeafType.getEnumFromCode(fcNode.getLeafNode().getLeafType()).equals(LeafType.BORDER_LEAF))) {
          if (!fcNode.getNodeId().equals(nodeId)) {

            borderLeafNodeList.add(fcNode);
          }
        }
      }
      return borderLeafNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  protected TreeMap<Integer, LeafNodeOppositeEntity> createOppositeBreakoutIfMap(
      List<LeafNodeOppositeEntity> oppositeBreakoutList) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeBreakoutList" }, new Object[] { oppositeBreakoutList });

      TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap = new TreeMap<>();
      for (LeafNodeOppositeEntity leafNodeOppositeEntity : oppositeBreakoutList) {
        Integer oppositeNodeId = Integer.valueOf(leafNodeOppositeEntity.getOppositeNodeId());
        if (oppositeBreakoutIfMap.containsKey(oppositeNodeId)) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same node Id information.");
        }

        oppositeBreakoutIfMap.put(oppositeNodeId, leafNodeOppositeEntity);
      }
      return oppositeBreakoutIfMap;
    } finally {
      logger.methodEnd();
    }
  }

  protected TreeMap<Integer, Object> checkOppositeNode(List<FcNode> oppositeNodes, SessionWrapper sessionWrapper,
      List<LeafNodePhysicalLinkEntity> physicalLinkList, List<LeafNodeLagLinkEntity> lagLinkList,
      TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap) throws MsfException {

    try {
      logger.methodStart();

      FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

      TreeMap<Integer, Object> oppositeNodeMap = new TreeMap<>();
      for (FcNode oppositeNode : oppositeNodes) {

        ArrayList<String> createBreakoutIfIds = new ArrayList<>();
        ArrayList<String> usedPhysicalIfIds = new ArrayList<>();
        ArrayList<String> usedBreakoutIfIds = new ArrayList<>();

        createBreakoutIfIds = checkOppositeBreakoutIfList(sessionWrapper, oppositeNode, fcBreakoutIfDao,
            oppositeBreakoutIfMap);

        checkOppositePhysicalLinksList(sessionWrapper, oppositeNode, createBreakoutIfIds, usedPhysicalIfIds,
            usedBreakoutIfIds, physicalLinkList, oppositeNodeMap);

        checkOppositeLagLinksList(sessionWrapper, oppositeNode, createBreakoutIfIds, usedPhysicalIfIds,
            usedBreakoutIfIds, lagLinkList, oppositeNodeMap);

        if (!oppositeNodeMap.containsKey(oppositeNode.getNodeId())) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "Link of all the opposing device for which currently exists is not defined.");
        }
      }

      checkOppositeNodeIdForRequestData(oppositeNodes, physicalLinkList, lagLinkList);

      return oppositeNodeMap;
    } finally {
      logger.methodEnd();
    }
  }

  protected ArrayList<String> checkExpansionNode(List<LeafNodePhysicalLinkEntity> physicalLinkList,
      List<LeafNodeLagLinkEntity> lagLinkList, LeafNodeLocalEntity localBreakoutIf, ArrayList<String> physicalIfIds)
      throws MsfException {

    try {
      logger.methodStart();

      ArrayList<String> createBreakoutIfIds = new ArrayList<>();
      ArrayList<String> usedPhysicalIfIds = new ArrayList<>();
      ArrayList<String> usedBreakoutIfIds = new ArrayList<>();

      if (localBreakoutIf != null) {
        createBreakoutIfIds = checkLocalBreakoutIfList(localBreakoutIf, physicalIfIds);
      }

      checkLocalPhysicalLinksList(createBreakoutIfIds, usedPhysicalIfIds, usedBreakoutIfIds, physicalLinkList,
          physicalIfIds);

      checkLocalLagLinksList(createBreakoutIfIds, usedPhysicalIfIds, usedBreakoutIfIds, lagLinkList, physicalIfIds);

      return physicalIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkOppositeNodeIdForRequestData(List<FcNode> oppositeNodes,
      List<LeafNodePhysicalLinkEntity> physicalLinkList, List<LeafNodeLagLinkEntity> lagLinkList) throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNodes", "physicalLinkList", "lagLinkList" },
          new Object[] { oppositeNodes, physicalLinkList, lagLinkList });
      boolean nodePresenceFlag = false;

      for (LeafNodePhysicalLinkEntity physicalLinkEntity : physicalLinkList) {
        nodePresenceFlag = false;
        for (FcNode oppositeNode : oppositeNodes) {

          if (String.valueOf(oppositeNode.getNodeId()).equals(physicalLinkEntity.getOppositeNodeId())) {
            nodePresenceFlag = true;
            break;
          }
        }

        if (!nodePresenceFlag) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "Nonexistent opposing node information is defined. parameter = PhysicalLink.");
        }
      }

      for (LeafNodeLagLinkEntity lagLinkEntity : lagLinkList) {
        nodePresenceFlag = false;
        for (FcNode oppositeNode : oppositeNodes) {

          if (String.valueOf(oppositeNode.getNodeId()).equals(lagLinkEntity.getOppositeNodeId())) {
            nodePresenceFlag = true;
            break;
          }
        }

        if (!nodePresenceFlag) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "Nonexistent opposing node information is defined. parameter = LagLink.");
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkOppositeBreakoutIfList(SessionWrapper sessionWrapper, FcNode oppositeNode,
      FcBreakoutIfDao fcBreakoutIfDao, TreeMap<Integer, LeafNodeOppositeEntity> oppositeBreakoutIfMap)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNode", "fcBreakoutIfDao", "oppositeBreakoutIfMap" },
          new Object[] { oppositeNode, fcBreakoutIfDao, oppositeBreakoutIfMap });
      ArrayList<String> createBreakoutIfIds = new ArrayList<>();
      LeafNodeOppositeEntity leafNodeOppositeEntity = oppositeBreakoutIfMap.get(oppositeNode.getNodeId());
      if (leafNodeOppositeEntity != null) {
        List<LeafNodeBreakoutIfCreateEntity> oppositeBreakoutIfList = leafNodeOppositeEntity.getBreakoutIfList();

        createBreakoutIfIds = checkCreateBreakoutIfParamerter(sessionWrapper, oppositeNode, oppositeBreakoutIfList);

        checkBreakoutIfCreateFromDb(sessionWrapper, oppositeNode, createBreakoutIfIds, fcBreakoutIfDao);
      }
      return createBreakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkCreateBreakoutIfParamerter(SessionWrapper sessionWrapper, FcNode oppositeNode,
      List<LeafNodeBreakoutIfCreateEntity> breakoutIfCreateRequestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "breakoutIfCreateRequestBody" }, new Object[] { breakoutIfCreateRequestBody });

      ArrayList<String> breakoutIfIds = new ArrayList<>();

      for (LeafNodeBreakoutIfCreateEntity leafNodeBreakoutIfCreateEntity : breakoutIfCreateRequestBody) {
        for (String breakoutIfId : leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList()) {
          if (breakoutIfIds.contains(breakoutIfId)) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "same breakoutIF ID has been specified.");
          }
          breakoutIfIds.add(breakoutIfId);
        }

        if (!leafNodeBreakoutIfCreateEntity.getDivisionNumber()
            .equals(leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList().size())) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "breaoutif ID of the specified number is not specified.");
        }

        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
        FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, oppositeNode.getNodeType(),
            oppositeNode.getNodeId(), leafNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId());
        if (fcPhysicalIf == null) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "target resource not found. parameters = base_physicalIf");
        }
      }
      return breakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkBreakoutIfCreateFromDb(SessionWrapper sessionWrapper, FcNode oppositeNode,
      ArrayList<String> breakoutIfIds, FcBreakoutIfDao fcBreakoutIfDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "breakoutIfIds", "fcBreakoutIfDao" },
          new Object[] { oppositeNode, breakoutIfIds, fcBreakoutIfDao });
      for (String breakoutIfId : breakoutIfIds) {
        FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, oppositeNode.getNodeType(),
            oppositeNode.getNodeId(), breakoutIfId);
        if (fcBreakoutIf != null) {

          throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
              "target resouece already exist. target = breakoutIF");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkOppositePhysicalLinksList(SessionWrapper sessionWrapper, FcNode oppositeNode,
      ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds, ArrayList<String> usedBreakoutIfIds,
      List<LeafNodePhysicalLinkEntity> physicalLinkList, TreeMap<Integer, Object> oppositeNodeMap) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "oppositeNode", "usedPhysicalIfIds", "usedBreakoutIfIds", "physicalLinkList",
              "oppositeNodeMap" },
          new Object[] { oppositeNode, usedPhysicalIfIds, usedBreakoutIfIds, physicalLinkList, oppositeNodeMap });

      for (LeafNodePhysicalLinkEntity physicalLinkEntity : physicalLinkList) {
        if (String.valueOf(oppositeNode.getNodeId()).equals(physicalLinkEntity.getOppositeNodeId())) {
          if (physicalLinkEntity.getInternalLinkIf().getOpposite().getPhysicalIf() != null) {

            String physicalIfId = physicalLinkEntity.getInternalLinkIf().getOpposite().getPhysicalIf()
                .getPhysicalIfId();
            checkOppositePhysicalIfForInternalLinks(sessionWrapper, oppositeNode, physicalIfId, usedPhysicalIfIds, null,
                null);
          } else {

            String breakoutIfId = physicalLinkEntity.getInternalLinkIf().getOpposite().getBreakoutIf()
                .getBreakoutIfId();
            checkOppositeBreakoutIfForInternalLinks(sessionWrapper, oppositeNode, breakoutIfId, createBreakoutIfIds,
                usedBreakoutIfIds);
          }

          if (oppositeNodeMap.containsKey(oppositeNode.getNodeId())) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same node Id information.");
          }
          oppositeNodeMap.put(oppositeNode.getNodeId(), physicalLinkEntity);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkOppositeLagLinksList(SessionWrapper sessionWrapper, FcNode oppositeNode,
      ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds, ArrayList<String> usedBreakoutIfIds,
      List<LeafNodeLagLinkEntity> lagLinkList, TreeMap<Integer, Object> oppositeNodeMap) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "oppositeNode", "usedPhysicalIfIds", "usedBreakoutIfIds", "lagLinkList", "oppositeNodeMap" },
          new Object[] { oppositeNode, usedPhysicalIfIds, usedBreakoutIfIds, lagLinkList, oppositeNodeMap });
      for (LeafNodeLagLinkEntity leafNodeLagLinkEntity : lagLinkList) {
        if (String.valueOf(oppositeNode.getNodeId()).equals(leafNodeLagLinkEntity.getOppositeNodeId())) {

          checkOppositeLagLinkMember(sessionWrapper, oppositeNode, createBreakoutIfIds, usedPhysicalIfIds,
              usedBreakoutIfIds, leafNodeLagLinkEntity);

          if (oppositeNodeMap.containsKey(oppositeNode.getNodeId())) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same node Id information.");
          }
          oppositeNodeMap.put(oppositeNode.getNodeId(), leafNodeLagLinkEntity);
        }

      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkOppositeLagLinkMember(SessionWrapper sessionWrapper, FcNode oppositeNode,
      ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds, ArrayList<String> usedBreakoutIfIds,
      LeafNodeLagLinkEntity leafNodeLagLinkEntity) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "oppositeNode", "usedPhysicalIfIds", "usedBreakoutIfIds", "leafNodeLagLinkEntity" },
          new Object[] { oppositeNode, usedPhysicalIfIds, usedBreakoutIfIds, leafNodeLagLinkEntity });
      String basePhysicalIfSpeed = null;
      for (LeafNodeMemberIfEntity leafNodeMemberIfEntity : leafNodeLagLinkEntity.getMemberIfList()) {
        if (leafNodeMemberIfEntity.getOpposite().getPhysicalIf() != null) {

          String physicalIfId = leafNodeMemberIfEntity.getOpposite().getPhysicalIf().getPhysicalIfId();
          String physicalIfSpeed = leafNodeMemberIfEntity.getOpposite().getPhysicalIf().getPhysicalIfSpeed();
          basePhysicalIfSpeed = checkOppositePhysicalIfForInternalLinks(sessionWrapper, oppositeNode, physicalIfId,
              usedPhysicalIfIds, basePhysicalIfSpeed, physicalIfSpeed);
        } else {

          String breakoutIfId = leafNodeMemberIfEntity.getOpposite().getBreakoutIf().getBreakoutIfId();
          checkOppositeBreakoutIfForInternalLinks(sessionWrapper, oppositeNode, breakoutIfId, createBreakoutIfIds,
              usedBreakoutIfIds);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private String checkOppositePhysicalIfForInternalLinks(SessionWrapper sessionWrapper, FcNode oppositeNode,
      String physicalIfId, ArrayList<String> usedPhysicalIfIds, String basePhysicalIfSpeed, String physicalIfSpeed)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNode", "physicalIfId", "usedPhysicalIfIds" },
          new Object[] { oppositeNode, physicalIfId, usedPhysicalIfIds });

      if (usedPhysicalIfIds.contains(physicalIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same physicalIf Id information.");
      }
      usedPhysicalIfIds.add(physicalIfId);

      if (basePhysicalIfSpeed == null) {
        basePhysicalIfSpeed = physicalIfSpeed;
      } else if (!basePhysicalIfSpeed.equals(physicalIfSpeed)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Speed information is different.");
      }

      checkPhisicalIf(sessionWrapper, oppositeNode, physicalIfId);

      return basePhysicalIfSpeed;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkOppositeBreakoutIfForInternalLinks(SessionWrapper sessionWrapper, FcNode oppositeNode,
      String breakoutIfId, ArrayList<String> createBreakoutIfIds, ArrayList<String> usedBreakoutIfIds)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "oppositeNode", "breakoutIfId", "createBreakoutIfIds", "usedPhysicalIfIds" },
          new Object[] { oppositeNode, breakoutIfId, createBreakoutIfIds, usedBreakoutIfIds });

      if (usedBreakoutIfIds.contains(breakoutIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same breakoutIf Id information.");
      }
      usedBreakoutIfIds.add(breakoutIfId);

      if (!createBreakoutIfIds.remove(breakoutIfId)) {

        checkBreakoutIf(sessionWrapper, oppositeNode, breakoutIfId);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkLocalBreakoutIfList(LeafNodeLocalEntity localBreakoutIf,
      ArrayList<String> physicalIfIds) throws MsfException {
    try {
      logger.methodStart(new String[] { "loscalBreakoutList" }, new Object[] { localBreakoutIf });
      ArrayList<String> createBreakoutIfIds = new ArrayList<>();

      List<LeafNodeBreakoutIfCreateEntity> oppositeBreakoutIfList = localBreakoutIf.getBreakoutIfList();

      createBreakoutIfIds = checkLocalCreateBreakoutIfParamerter(oppositeBreakoutIfList, physicalIfIds);
      return createBreakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> checkLocalCreateBreakoutIfParamerter(
      List<LeafNodeBreakoutIfCreateEntity> breakoutIfCreateRequestBody, ArrayList<String> physicalIfIds)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "breakoutIfCreateRequestBody" }, new Object[] { breakoutIfCreateRequestBody });

      ArrayList<String> breakoutIfIds = new ArrayList<>();

      for (LeafNodeBreakoutIfCreateEntity leafNodeBreakoutIfCreateEntity : breakoutIfCreateRequestBody) {
        for (String breakoutIfId : leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList()) {
          if (breakoutIfIds.contains(breakoutIfId)) {

            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "same breakoutIF ID has been specified.");
          }
          breakoutIfIds.add(breakoutIfId);
        }

        if (!leafNodeBreakoutIfCreateEntity.getDivisionNumber()
            .equals(leafNodeBreakoutIfCreateEntity.getBreakoutIfIdList().size())) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "breaoutif ID of the specified number is not specified.");
        }

        if (!physicalIfIds.remove(leafNodeBreakoutIfCreateEntity.getBaseIf().getPhysicalIfId())) {

          throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
              "target resource not found. parameters = base_physicalIf");
        }
      }
      return breakoutIfIds;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLocalPhysicalLinksList(ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds,
      ArrayList<String> usedBreakoutIfIds, List<LeafNodePhysicalLinkEntity> physicalLinkList,
      ArrayList<String> physicalIfIds) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "createBreakoutIfIds", "usedPhysicalIfIds", "usedBreakoutIfIds", "physicalLinkList" },
          new Object[] { createBreakoutIfIds, usedPhysicalIfIds, usedBreakoutIfIds, physicalLinkList });

      for (LeafNodePhysicalLinkEntity physicalLinkEntity : physicalLinkList) {
        if (physicalLinkEntity.getInternalLinkIf().getLocal().getPhysicalIf() != null) {

          String physicalIfId = physicalLinkEntity.getInternalLinkIf().getLocal().getPhysicalIf().getPhysicalIfId();
          checkLocalPhysicalIfForInternalLinks(physicalIfId, usedPhysicalIfIds, physicalIfIds, null, null);
        } else {

          String breakoutIfId = physicalLinkEntity.getInternalLinkIf().getLocal().getBreakoutIf().getBreakoutIfId();
          checkLocalBreakoutIfForInternalLinks(breakoutIfId, usedBreakoutIfIds, createBreakoutIfIds);
        }

      }
    } finally {
      logger.methodEnd();
    }
  }

  private String checkLocalPhysicalIfForInternalLinks(String physicalIfId, ArrayList<String> usedPhysicalIfIds,
      ArrayList<String> physicalIfIds, String basePhysicalIfSpeed, String physicalIfSpeed) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIfId", "usedPhysicalIfIds", "physicalIfIds" },
          new Object[] { physicalIfId, usedPhysicalIfIds, physicalIfIds });

      if (usedPhysicalIfIds.contains(physicalIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same physicalIf Id information.");
      }
      usedPhysicalIfIds.add(physicalIfId);

      if (basePhysicalIfSpeed == null) {
        basePhysicalIfSpeed = physicalIfSpeed;
      } else if (!basePhysicalIfSpeed.equals(physicalIfSpeed)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Speed information is different.");
      }

      if (!physicalIfIds.remove(physicalIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "It is not specified except the physiccal IF ID which FC paid out.");
      }

      return basePhysicalIfSpeed;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLocalBreakoutIfForInternalLinks(String breakoutIfId, ArrayList<String> usedBreakoutIfIds,
      ArrayList<String> createBreakoutIfIds) throws MsfException {
    try {
      logger.methodStart(new String[] { "breakoutIfId", "usedBreakoutIfIds", "createBreakoutIfIds" },
          new Object[] { breakoutIfId, usedBreakoutIfIds, createBreakoutIfIds });
      if (usedBreakoutIfIds.contains(breakoutIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "There is the same breakoutIf Id information.");
      }
      usedBreakoutIfIds.add(breakoutIfId);

      if (!createBreakoutIfIds.remove(breakoutIfId)) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "breakoutIF which is not created is not specified as an internal link.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLocalLagLinksList(ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds,
      ArrayList<String> usedBreakoutIfIds, List<LeafNodeLagLinkEntity> lagLinkList, ArrayList<String> physicalIfIds)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "usedPhysicalIfIds", "usedBreakoutIfIds", "lagLinkList" },
          new Object[] { usedPhysicalIfIds, usedBreakoutIfIds, lagLinkList });
      for (LeafNodeLagLinkEntity leafNodeLagLinkEntity : lagLinkList) {

        checkLocalLagLinkMember(leafNodeLagLinkEntity, createBreakoutIfIds, usedPhysicalIfIds, usedBreakoutIfIds,
            physicalIfIds);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLocalLagLinkMember(LeafNodeLagLinkEntity leafNodeLagLinkEntity,
      ArrayList<String> createBreakoutIfIds, ArrayList<String> usedPhysicalIfIds, ArrayList<String> usedBreakoutIfIds,
      ArrayList<String> physicalIfIds) throws MsfException {
    try {
      logger.methodStart(
          new String[] { "leafNodeLagLinkEntity", "createBreakoutIfIds", "usedPhysicalIfIds", "usedBreakoutIfIds",
              "physicalIfIds" },
          new Object[] { leafNodeLagLinkEntity, createBreakoutIfIds, usedPhysicalIfIds, usedBreakoutIfIds,
              physicalIfIds });
      String basePhysicalIfSpeed = null;
      for (LeafNodeMemberIfEntity leafNodeMemberIfEntity : leafNodeLagLinkEntity.getMemberIfList()) {
        if (leafNodeMemberIfEntity.getLocal().getPhysicalIf() != null) {

          String physicalIfId = leafNodeMemberIfEntity.getLocal().getPhysicalIf().getPhysicalIfId();
          String physicalIfSpeed = leafNodeMemberIfEntity.getLocal().getPhysicalIf().getPhysicalIfSpeed();
          basePhysicalIfSpeed = checkLocalPhysicalIfForInternalLinks(physicalIfId, usedPhysicalIfIds, physicalIfIds,
              basePhysicalIfSpeed, physicalIfSpeed);
        } else {

          String breakoutIfId = leafNodeMemberIfEntity.getLocal().getBreakoutIf().getBreakoutIfId();
          checkLocalBreakoutIfForInternalLinks(breakoutIfId, usedBreakoutIfIds, createBreakoutIfIds);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

}
