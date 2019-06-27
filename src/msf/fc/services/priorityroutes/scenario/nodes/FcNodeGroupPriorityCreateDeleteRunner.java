
package msf.fc.services.priorityroutes.scenario.nodes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.rest.ec.node.interfaces.internallink.data.InternalLinkIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.internallink.data.entity.InternalLinkIfEcEntity;
import msf.fc.rest.ec.node.interfaces.internallink.data.entity.UpdateInternalLinkIfsEcEntity;
import msf.fc.services.priorityroutes.FcPriorityRoutesManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.priorityroutes.common.constant.EcRequestUri;
import msf.mfcfc.services.priorityroutes.common.util.ParameterCheckUtil;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityCreateDeleteRequestBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityRequest;

/**
 * Class to implement the asynchronous processing in the nodes addition/deletion
 * to the priority node group .
 *
 * @author NTT
 *
 */
public class FcNodeGroupPriorityCreateDeleteRunner extends FcAbstractNodeGroupPriorityRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeGroupPriorityCreateDeleteRunner.class);

  @SuppressWarnings("unused")
  private NodeGroupPriorityRequest request;
  private List<NodeGroupPriorityCreateDeleteRequestBody> requestBody;

  /**
   * <p>
   * Set the request information from scenario as arguments
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcNodeGroupPriorityCreateDeleteRunner(NodeGroupPriorityRequest request,
      List<NodeGroupPriorityCreateDeleteRequestBody> requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

        TreeMap<Integer, FcNode> existPriorityLeafsEntityMap = new TreeMap<>();

        TreeMap<Integer, FcNode> existPrioritySpinesEntityMap = new TreeMap<>();

        List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

        PatchOperation patchOperation = PatchOperation.getEnumFromMessage(requestBody.get(0).getOp());
        if (patchOperation.equals(PatchOperation.ADD)) {

          if (!isExistPriorityGroup(fcNodeList, existPriorityLeafsEntityMap, existPrioritySpinesEntityMap)) {

            checkAllNodeInternalLinkIfPriority(fcNodeList);
          }

          TreeMap<Integer, FcNode> addPriorityLeafsEntityMap = new TreeMap<>();

          TreeMap<Integer, FcNode> addPrioritySpinesEntityMap = new TreeMap<>();

          checkAddPriorityNodes(fcNodeList, addPriorityLeafsEntityMap, addPrioritySpinesEntityMap);

          logger.performance("start get leaf/spine resources lock.");
          sessionWrapper.beginTransaction();
          List<FcNode> fcTableLockLeafNodes = new ArrayList<>();
          List<FcNode> fcTableLockSpineNodes = new ArrayList<>();

          fcTableLockLeafNodes.addAll(addPriorityLeafsEntityMap.values());
          fcTableLockSpineNodes.addAll(addPrioritySpinesEntityMap.values());

          if (!addPriorityLeafsEntityMap.isEmpty()) {
            fcTableLockSpineNodes.addAll(existPrioritySpinesEntityMap.values());
          }
          if (!addPrioritySpinesEntityMap.isEmpty()) {
            fcTableLockLeafNodes.addAll(existPriorityLeafsEntityMap.values());
          }
          FcDbManager.getInstance().getResourceLock(null, null, fcTableLockLeafNodes, fcTableLockSpineNodes,
              sessionWrapper);
          logger.performance("end get leaf/spine resources lock.");

          updateInternalLinkIfList.addAll(nodeGroupPriorityAddProcess(addPriorityLeafsEntityMap.values(),
              addPrioritySpinesEntityMap, existPrioritySpinesEntityMap, fcNodeDao, sessionWrapper, true));

          updateInternalLinkIfList.addAll(nodeGroupPriorityAddProcess(addPrioritySpinesEntityMap.values(),
              addPriorityLeafsEntityMap, existPriorityLeafsEntityMap, fcNodeDao, sessionWrapper, false));

          if (!updateInternalLinkIfList.isEmpty()) {

            sendInternalLinkIfPriorityUpdateForNodeGroupPriority(updateInternalLinkIfList);
          }

        } else {

          TreeMap<Integer, FcNode> deletePriorityLeafsEntityMap = new TreeMap<>();

          TreeMap<Integer, FcNode> deletePrioritySpinesEntityMap = new TreeMap<>();

          isExistPriorityGroup(fcNodeList, existPriorityLeafsEntityMap, existPrioritySpinesEntityMap);

          checkDeletePriorityNodes(fcNodeList, deletePriorityLeafsEntityMap, deletePrioritySpinesEntityMap,
              existPriorityLeafsEntityMap, existPrioritySpinesEntityMap);

          if (!deletePriorityLeafsEntityMap.isEmpty() || (!deletePrioritySpinesEntityMap.isEmpty())) {

            logger.performance("start get leaf/spine resources lock.");
            sessionWrapper.beginTransaction();
            List<FcNode> fcTableLockLeafNodes = new ArrayList<>();
            List<FcNode> fcTableLockSpineNodes = new ArrayList<>();
            if (!deletePriorityLeafsEntityMap.isEmpty() && (!deletePrioritySpinesEntityMap.isEmpty())) {

              fcTableLockLeafNodes.addAll(existPriorityLeafsEntityMap.values());
              fcTableLockSpineNodes.addAll(existPrioritySpinesEntityMap.values());
            } else if (!deletePriorityLeafsEntityMap.isEmpty()) {

              fcTableLockLeafNodes.addAll(deletePriorityLeafsEntityMap.values());
              fcTableLockSpineNodes.addAll(existPrioritySpinesEntityMap.values());
            } else {

              fcTableLockLeafNodes.addAll(existPriorityLeafsEntityMap.values());
              fcTableLockSpineNodes.addAll(deletePrioritySpinesEntityMap.values());
            }
            FcDbManager.getInstance().getResourceLock(null, null, fcTableLockLeafNodes, fcTableLockSpineNodes,
                sessionWrapper);
            logger.performance("end get leaf/spine resources lock.");

            updateInternalLinkIfList.addAll(nodeGroupPriorityDeleteProcess(deletePriorityLeafsEntityMap.values(),
                deletePrioritySpinesEntityMap, existPrioritySpinesEntityMap, fcNodeDao, sessionWrapper, true));

            updateInternalLinkIfList.addAll(nodeGroupPriorityDeleteProcess(deletePrioritySpinesEntityMap.values(),
                deletePriorityLeafsEntityMap, existPriorityLeafsEntityMap, fcNodeDao, sessionWrapper, false));

            if (!updateInternalLinkIfList.isEmpty()) {

              sendInternalLinkIfPriorityUpdateForNodeGroupPriority(updateInternalLinkIfList);
            }
          }
        }

        responseBase = responseNodeGroupPriorityCreateDeleteData();

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private boolean isExistPriorityGroup(List<FcNode> fcNodeList, TreeMap<Integer, FcNode> existPriorityLeafsEntityMap,
      TreeMap<Integer, FcNode> existPrioritySpinesEntityMap) {
    try {
      logger.methodStart();
      for (FcNode fcNode : fcNodeList) {
        if (fcNode.getIsPriorityNodeGroupMember()) {
          if (NodeType.LEAF.equals(fcNode.getNodeTypeEnum())) {
            existPriorityLeafsEntityMap.put(fcNode.getNodeId(), fcNode);
          } else {
            existPrioritySpinesEntityMap.put(fcNode.getNodeId(), fcNode);
          }
        }
      }
      if ((existPriorityLeafsEntityMap.isEmpty()) && (existPrioritySpinesEntityMap.isEmpty())) {

        return false;
      } else {

        return true;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkAllNodeInternalLinkIfPriority(List<FcNode> fcNodeList) throws MsfException {

    try {
      logger.methodStart();
      for (FcNode fcNode : fcNodeList) {
        for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
          List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
          checkInternalLinkNormalIgpCost(fcInternalLinkIfList);
        }

        for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
          List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
          checkInternalLinkNormalIgpCost(fcInternalLinkIfList);
        }

        for (FcBreakoutIf fcBreakoutIf : fcNode.getBreakoutIfs()) {
          List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
          checkInternalLinkNormalIgpCost(fcInternalLinkIfList);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkInternalLinkNormalIgpCost(List<FcInternalLinkIf> fcInternalLinkIfList) throws MsfException {
    try {
      logger.methodStart();

      Integer internalLinkNormalIgpCost = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster()
          .getInternalLinkNormalIgpCost();
      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        Integer igpCost = fcInternalLinkIfList.get(0).getIgpCost();
        if (!internalLinkNormalIgpCost.equals(igpCost)) {

          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
              MessageFormat.format(
                  "There already have a other cost internal link. igpCost = {0}, internalLinkIfId = {1}.", igpCost,
                  fcInternalLinkIfList.get(0).getInternalLinkIfId()));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkAddPriorityNodes(List<FcNode> fcNodeList, TreeMap<Integer, FcNode> addPriorityLeafsEntityMap,
      TreeMap<Integer, FcNode> addPrioritySpinesEntityMap) throws MsfException {
    try {
      logger.methodStart();

      for (NodeGroupPriorityCreateDeleteRequestBody nodeGroupPriorityCreateDeleteRequestBody : requestBody) {
        Matcher fabricTypeNumericMatcher = ParameterCheckUtil.FABRIC_TYPE_NUMERIC_PATTERN
            .matcher(nodeGroupPriorityCreateDeleteRequestBody.getPath());
        if (fabricTypeNumericMatcher.matches()) {
          NodeType fabricTypeEnum = NodeType.getEnumFromPluralMessage(fabricTypeNumericMatcher.group(1));
          String nodeId = fabricTypeNumericMatcher.group(2);
          FcNode targetNode = null;
          for (FcNode fcNode : fcNodeList) {

            targetNode = null;

            if ((fcNode.getNodeTypeEnum().equals(fabricTypeEnum)
                && (fcNode.getNodeId().equals(Integer.valueOf(nodeId))))) {
              if (fcNode.getIsPriorityNodeGroupMember()) {

                throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, MessageFormat.format(
                    "The target resource is already a member of priority node group. fabricType = {0}, nodeId = {1}.",
                    fabricTypeNumericMatcher.group(1), nodeId));
              }
              targetNode = fcNode;
              break;
            }
          }
          if (targetNode == null) {

            throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
                MessageFormat.format("target resource is not found. fabricType = {0}, nodeId = {1}.",
                    fabricTypeNumericMatcher.group(1), nodeId));
          }

          if (NodeType.LEAF.equals(targetNode.getNodeTypeEnum())) {
            addPriorityLeafsEntityMap.put(targetNode.getNodeId(), targetNode);
          } else {
            addPrioritySpinesEntityMap.put(targetNode.getNodeId(), targetNode);
          }

        } else {

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter. path = " + nodeGroupPriorityCreateDeleteRequestBody.getPath());
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkDeletePriorityNodes(List<FcNode> fcNodeList, TreeMap<Integer, FcNode> deletePriorityLeafsEntityMap,
      TreeMap<Integer, FcNode> deletePrioritySpinesEntityMap, TreeMap<Integer, FcNode> existPriorityLeafsEntityMap,
      TreeMap<Integer, FcNode> existPrioritySpinesEntityMap) throws MsfException {
    try {
      logger.methodStart();

      for (NodeGroupPriorityCreateDeleteRequestBody nodeGroupPriorityCreateDeleteRequestBody : requestBody) {
        Matcher nodesMatcher = ParameterCheckUtil.NODES_PATTERN
            .matcher(nodeGroupPriorityCreateDeleteRequestBody.getPath());
        if (nodesMatcher.matches()) {

          deletePriorityLeafsEntityMap.putAll(existPriorityLeafsEntityMap);
          deletePrioritySpinesEntityMap.putAll(existPrioritySpinesEntityMap);
          continue;
        }
        Matcher fabricTypeNumericMatcher = ParameterCheckUtil.FABRIC_TYPE_NUMERIC_PATTERN
            .matcher(nodeGroupPriorityCreateDeleteRequestBody.getPath());
        if (fabricTypeNumericMatcher.matches()) {
          NodeType fabricTypeEnum = NodeType.getEnumFromPluralMessage(fabricTypeNumericMatcher.group(1));
          String nodeId = fabricTypeNumericMatcher.group(2);
          FcNode targetNode = null;
          for (FcNode fcNode : fcNodeList) {

            targetNode = null;

            if ((fcNode.getNodeTypeEnum().equals(fabricTypeEnum)
                && (fcNode.getNodeId().equals(Integer.valueOf(nodeId))))) {
              if (!fcNode.getIsPriorityNodeGroupMember()) {

                throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, MessageFormat.format(
                    "The target resource is not a member of priority node group already. fabricType = {0}, nodeId = {1}.",
                    fabricTypeNumericMatcher.group(1), nodeId));
              }
              targetNode = fcNode;
              break;
            }
          }
          if (targetNode == null) {

            throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
                MessageFormat.format("target resource is not found. fabricType = {0}, nodeId = {1}.",
                    fabricTypeNumericMatcher.group(1), nodeId));
          }

          if (NodeType.LEAF.equals(targetNode.getNodeTypeEnum())) {
            deletePriorityLeafsEntityMap.put(targetNode.getNodeId(), targetNode);
          } else {
            deletePrioritySpinesEntityMap.put(targetNode.getNodeId(), targetNode);
          }

        } else {

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter. path = " + nodeGroupPriorityCreateDeleteRequestBody.getPath());
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<UpdateInternalLinkIfsEcEntity> nodeGroupPriorityAddProcess(Collection<FcNode> addNodes,
      TreeMap<Integer, FcNode> addPriorityOppositeEntityMap, TreeMap<Integer, FcNode> existPriorityOppositeEntityMap,
      FcNodeDao fcNodeDao, SessionWrapper sessionWrapper, boolean isOppositeAdd) throws MsfException {
    try {
      logger.methodStart();

      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

      for (FcNode fcAddNode : addNodes) {

        fcAddNode.setIsPriorityNodeGroupMember(true);
        fcNodeDao.update(sessionWrapper, fcAddNode);
        if (!addPriorityOppositeEntityMap.isEmpty() || (!existPriorityOppositeEntityMap.isEmpty())) {
          for (FcPhysicalIf fcPhysicalIf : fcAddNode.getPhysicalIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
            addPriorityGroupNodeInternalLinkIf(fcAddNode, fcInternalLinkIfList, addPriorityOppositeEntityMap,
                existPriorityOppositeEntityMap, updateInternalLinkIfList, sessionWrapper, isOppositeAdd);
          }

          for (FcLagIf fcLagIf : fcAddNode.getLagIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
            addPriorityGroupNodeInternalLinkIf(fcAddNode, fcInternalLinkIfList, addPriorityOppositeEntityMap,
                existPriorityOppositeEntityMap, updateInternalLinkIfList, sessionWrapper, isOppositeAdd);
          }

          for (FcBreakoutIf fcBreakoutIf : fcAddNode.getBreakoutIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
            addPriorityGroupNodeInternalLinkIf(fcAddNode, fcInternalLinkIfList, addPriorityOppositeEntityMap,
                existPriorityOppositeEntityMap, updateInternalLinkIfList, sessionWrapper, isOppositeAdd);
          }
        }
      }
      return updateInternalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private void addPriorityGroupNodeInternalLinkIf(FcNode fcAddNode, List<FcInternalLinkIf> fcInternalLinkIfList,
      TreeMap<Integer, FcNode> addPriorityOppositeEntityMap, TreeMap<Integer, FcNode> existPriorityOppositeEntityMap,
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList, SessionWrapper sessionWrapper,
      boolean isOppositeAdd) throws MsfException {
    try {
      logger.methodStart();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        FcInternalLinkIf fcTargetInternalLinkIf = fcInternalLinkIfList.get(0);
        FcInternalLinkIf fcOppositInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);

        FcNode oppositNode = null;
        if (fcOppositInternalLinkIf.getLagIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getLagIf().getNode();
        } else if (fcOppositInternalLinkIf.getPhysicalIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getPhysicalIf().getNode();
        } else {
          oppositNode = fcOppositInternalLinkIf.getBreakoutIf().getNode();
        }

        Integer internalLinkPriorityIgpCost = FcPriorityRoutesManager.getInstance().getSystemConfData()
            .getPriorityRoutes().getInternalLinkPriorityIgpCost();

        if (isOppositeAdd) {

          updatePriorityOppositeInternalLinkIfProcess(fcAddNode, addPriorityOppositeEntityMap, oppositNode,
              fcTargetInternalLinkIf, fcOppositInternalLinkIf, updateInternalLinkIfList, sessionWrapper,
              internalLinkPriorityIgpCost);
        }

        updatePriorityOppositeInternalLinkIfProcess(fcAddNode, existPriorityOppositeEntityMap, oppositNode,
            fcTargetInternalLinkIf, fcOppositInternalLinkIf, updateInternalLinkIfList, sessionWrapper,
            internalLinkPriorityIgpCost);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<UpdateInternalLinkIfsEcEntity> nodeGroupPriorityDeleteProcess(Collection<FcNode> deleteNodes,
      TreeMap<Integer, FcNode> deletePriorityOppositeEntityMap, TreeMap<Integer, FcNode> existPriorityOppositeEntityMap,
      FcNodeDao fcNodeDao, SessionWrapper sessionWrapper, boolean isOppositeDelete) throws MsfException {
    try {
      logger.methodStart();

      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

      if (!isOppositeDelete) {
        for (Integer nodeId : deletePriorityOppositeEntityMap.keySet()) {
          existPriorityOppositeEntityMap.remove(nodeId);
        }
      }

      for (FcNode fcDeleteNode : deleteNodes) {

        fcDeleteNode.setIsPriorityNodeGroupMember(false);
        fcNodeDao.update(sessionWrapper, fcDeleteNode);
        if (!existPriorityOppositeEntityMap.isEmpty()) {
          for (FcPhysicalIf fcPhysicalIf : fcDeleteNode.getPhysicalIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
            deletePriorityGroupNodeInternalLinkIf(fcDeleteNode, fcInternalLinkIfList, existPriorityOppositeEntityMap,
                updateInternalLinkIfList, sessionWrapper);
          }

          for (FcLagIf fcLagIf : fcDeleteNode.getLagIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
            deletePriorityGroupNodeInternalLinkIf(fcDeleteNode, fcInternalLinkIfList, existPriorityOppositeEntityMap,
                updateInternalLinkIfList, sessionWrapper);
          }

          for (FcBreakoutIf fcBreakoutIf : fcDeleteNode.getBreakoutIfs()) {
            List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
            deletePriorityGroupNodeInternalLinkIf(fcDeleteNode, fcInternalLinkIfList, existPriorityOppositeEntityMap,
                updateInternalLinkIfList, sessionWrapper);
          }
        }
      }
      return updateInternalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private void deletePriorityGroupNodeInternalLinkIf(FcNode fcDeleteNode, List<FcInternalLinkIf> fcInternalLinkIfList,
      TreeMap<Integer, FcNode> existPriorityOppositeEntityMap,
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        FcInternalLinkIf fcTargetInternalLinkIf = fcInternalLinkIfList.get(0);
        FcInternalLinkIf fcOppositInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);

        FcNode oppositNode = null;
        if (fcOppositInternalLinkIf.getLagIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getLagIf().getNode();
        } else if (fcOppositInternalLinkIf.getPhysicalIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getPhysicalIf().getNode();
        } else {
          oppositNode = fcOppositInternalLinkIf.getBreakoutIf().getNode();
        }

        Integer internalLinkNormalIgpCost = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster()
            .getInternalLinkNormalIgpCost();

        updatePriorityOppositeInternalLinkIfProcess(fcDeleteNode, existPriorityOppositeEntityMap, oppositNode,
            fcTargetInternalLinkIf, fcOppositInternalLinkIf, updateInternalLinkIfList, sessionWrapper,
            internalLinkNormalIgpCost);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updatePriorityOppositeInternalLinkIfProcess(FcNode fcNode,
      TreeMap<Integer, FcNode> priorityOppositeEntityMap, FcNode oppositNode, FcInternalLinkIf fcTargetInternalLinkIf,
      FcInternalLinkIf fcOppositInternalLinkIf, List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList,
      SessionWrapper sessionWrapper, Integer internalLinkIgpCost) throws MsfException {

    try {
      logger.methodStart();
      if (priorityOppositeEntityMap.containsKey(oppositNode.getNodeId())) {

        fcTargetInternalLinkIf.setIgpCost(internalLinkIgpCost);
        updateInternalLinkIfList
            .add(internalLinkIfPriorityUpdateProcess(fcNode, fcTargetInternalLinkIf, sessionWrapper));

        fcOppositInternalLinkIf.setIgpCost(internalLinkIgpCost);
        updateInternalLinkIfList
            .add(internalLinkIfPriorityUpdateProcess(oppositNode, fcOppositInternalLinkIf, sessionWrapper));

      }
    } finally {
      logger.methodEnd();
    }
  }

  private UpdateInternalLinkIfsEcEntity internalLinkIfPriorityUpdateProcess(FcNode fcNode,
      FcInternalLinkIf fcInternalLinkIf, SessionWrapper sessionWrapper) throws MsfException {

    try {
      logger.methodStart();

      FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
      fcInternalLinkIfDao.update(sessionWrapper, fcInternalLinkIf);

      UpdateInternalLinkIfsEcEntity updateInternalLinkIfsEcEntity = new UpdateInternalLinkIfsEcEntity();
      updateInternalLinkIfsEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));

      InternalLinkIfEcEntity internalLinkIf = new InternalLinkIfEcEntity();

      if (fcInternalLinkIf.getPhysicalIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.PHYSICAL_IF);
        internalLinkIf.setIfId(fcInternalLinkIf.getPhysicalIf().getPhysicalIfId());
      } else if (fcInternalLinkIf.getLagIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.LAG_IF);
        internalLinkIf.setIfId(String.valueOf(fcInternalLinkIf.getLagIf().getLagIfId()));
      } else {
        internalLinkIf.setIfTypeEnum(InterfaceType.BREAKOUT_IF);
        internalLinkIf.setIfId(fcInternalLinkIf.getBreakoutIf().getBreakoutIfId());
      }

      internalLinkIf.setCost(fcInternalLinkIf.getIgpCost());

      updateInternalLinkIfsEcEntity.setInternalLinkIf(internalLinkIf);

      return updateInternalLinkIfsEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendInternalLinkIfPriorityUpdateForNodeGroupPriority(
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateInternalLinkIfList" }, new Object[] { updateInternalLinkIfList });

      InternalLinkIfUpdateEcRequestBody internalLinkIfUpdateEcRequestBody = new InternalLinkIfUpdateEcRequestBody();
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfsEcEntities = new ArrayList<>();
      updateInternalLinkIfsEcEntities.addAll(updateInternalLinkIfList);
      internalLinkIfUpdateEcRequestBody.setUpdateInternalLinkIfList(updateInternalLinkIfsEcEntities);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(internalLinkIfUpdateEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.INTERNAL_LINK_IF_UPDATE.getHttpMethod(),
          EcRequestUri.INTERNAL_LINK_IF_UPDATE.getUri(), restRequest, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody nodeCreateDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = nodeCreateDeleteEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseNodeGroupPriorityCreateDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
