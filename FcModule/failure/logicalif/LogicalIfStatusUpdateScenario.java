package msf.fc.failure.logicalif;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.failure.logicalif.data.LogicalIfStatusRequest;
import msf.fc.failure.logicalif.data.LogicalIfStatusUpdateRequestBody;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusCpBaseData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusInternalLinkIfData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusNodeData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusSliceData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusVpnBaseData;
import msf.fc.rest.common.JsonUtil;

public class LogicalIfStatusUpdateScenario extends AbstractLogicalIfStatusScenarioBase<LogicalIfStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(LogicalIfStatusUpdateScenario.class);
  private LogicalIfStatusUpdateRequestBody requestBody;

  public LogicalIfStatusUpdateScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(LogicalIfStatusRequest request) throws MsfException {

    logger.methodStart();
    try {
      this.requestBody = JsonUtil.fromJson(request.getRequestBody(), LogicalIfStatusUpdateRequestBody.class);

      this.requestBody.validate();

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: check parameter error.", me, getOperationId());
      throw me;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.methodStart();

    boolean isLinkSucceeded = true;
    boolean isL2SliceSucceeded = true;
    boolean isL3SliceSucceeded = true;
    SessionWrapper session = new SessionWrapper();

    try {
      LogicalIfStatusData updateOptionData = this.requestBody.getUpdateOptionData();
      String clusterId = this.requestBody.getUpdateOptionData().getClusterId();
      List<LogicalIfStatusNodeData> nodes = updateOptionData.getNodes();
      LogicalIfStatusSliceData slices = updateOptionData.getSlices();

      session.openSession();

      if (nodes != null) {
        isLinkSucceeded = updateInternalLinkIfStatus(session, clusterId, nodes);
      }

      if (slices != null) {
        if (slices.getL2Vpn() != null) {
          isL2SliceSucceeded = updateL2CpStatus(session, clusterId, slices.getL2Vpn());
        }
        if (slices.getL3Vpn() != null) {
          isL3SliceSucceeded = updateL3CpStatus(session, clusterId, slices.getL3Vpn());
        }
      }
      if (!(isLinkSucceeded && isL2SliceSucceeded && isL3SliceSucceeded)) {
        logger.warn("[ope_id={0}]: Update Error Exists.", getOperationId());
        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "Update Error Exists.");
      }

      return new RestResponseBase(HttpStatus.OK_200, (String) null);

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}", me, getOperationId(), me.getMessage());
      throw me;

    } finally {
      session.closeSession();
      logger.methodEnd();
    }

  }

  private boolean updateInternalLinkIfStatus(SessionWrapper session, String clusterId,
      List<LogicalIfStatusNodeData> nodes) throws MsfException {
    logger.methodStart();

    boolean resultFlag = true;

    try {
      HashMap<String, HashMap<Integer, Node>> dbNodeMap = createDbNodeMap(session, clusterId);

      for (LogicalIfStatusNodeData node : nodes) {
        String nodeType = node.getNodeType();
        Integer nodeId = Integer.valueOf(node.getNodeId());
        logger.debug("[ope_id={0}]: NodeType={1}, NodeId={2}. update Node.", getOperationId(), nodeType, nodeId);

        try {
          if (!getNodeLock(session, dbNodeMap, node)) {
            continue;
          }
          logger.debug("[ope_id={0}]: NodeType={1}, NodeId={2}. Get Lock.", getOperationId(), nodeType, nodeId);

          updateNode(session, clusterId, node);
          logger.debug("[ope_id={0}]: NodeType={1}, NodeId={2}. Commit Complete.", getOperationId(), nodeType, nodeId);

        } catch (MsfException me) {
          logger.warn("[ope_id={0}]: {1}.", getOperationId(), me, me.getMessage());
          resultFlag = false;
          rollbackUpdate(session, me);
          continue;
        }
      }
      return resultFlag;

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}.", getOperationId(), me, me.getMessage());
      throw me;
    } finally {
      logger.methodEnd(MessageFormat.format("result = {0}", resultFlag));
    }
  }

  private boolean updateL2CpStatus(SessionWrapper session, String clusterId, List<LogicalIfStatusVpnBaseData> l2vpns)
      throws MsfException {

    logger.methodStart();

    boolean resultFlag = true;

    try {
      HashMap<String, L2Slice> dbL2SliceMap = createDbL2SliceMap(session);

      for (LogicalIfStatusVpnBaseData l2vpn : l2vpns) {
        String sliceId = l2vpn.getSliceId();
        logger.debug("[ope_id={0}]: sliceId={1}. update L2Slice.", getOperationId(), sliceId);

        try {
          if (!dbL2SliceMap.containsKey(sliceId)) {
            logger.warn("[ope_id={0}]: sliceId={1}. Slice Not Found.", getOperationId(), sliceId);
            continue;
          }

          DbManager.getInstance().getL2SlicesLock(Arrays.asList(dbL2SliceMap.get(sliceId)), session);
          logger.debug("[ope_id={0}]: sliceId={1}. Get Lock.", getOperationId(), sliceId);

          updateL2Slice(session, clusterId, l2vpn);
          logger.debug("[ope_id={0}]: sliceId={1}. Commit Complete.", getOperationId(), sliceId);

        } catch (MsfException me) {
          logger.warn("[ope_id={0}]: {1}", me, me.getMessage());
          resultFlag = false;
          rollbackUpdate(session, me);
          continue;
        }
      }
      return resultFlag;

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}", me, me.getMessage());
      throw me;
    } finally {
      logger.methodEnd(MessageFormat.format("result = {0}", resultFlag));
    }
  }

  private boolean updateL3CpStatus(SessionWrapper session, String clusterId, List<LogicalIfStatusVpnBaseData> l3vpns)
      throws MsfException {

    logger.methodStart();

    boolean resultFlag = true;

    try {
      HashMap<String, L3Slice> dbL3SliceMap = createDbL3SliceMap(session);

      for (LogicalIfStatusVpnBaseData l3vpn : l3vpns) {
        String sliceId = l3vpn.getSliceId();
        logger.debug("[ope_id={0}]: sliceId={1}. update L3Slice.", getOperationId(), sliceId);

        try {
          if (!dbL3SliceMap.containsKey(sliceId)) {
            logger.warn("[ope_id={0}]: sliceId={1}. Slice Not Found.", getOperationId(), sliceId);
            continue;
          }

          DbManager.getInstance().getL3SlicesLock(Arrays.asList(dbL3SliceMap.get(sliceId)), session);
          logger.debug("[ope_id={0}]: sliceId={1}. Get Lock.", getOperationId(), sliceId);

          updateL3Slice(session, clusterId, l3vpn);
          logger.debug("[ope_id={0}]: sliceId={1}. Commit Complete.", getOperationId(), sliceId);

        } catch (MsfException me) {
          logger.warn("[ope_id={0}]: {1}", me, me.getMessage());
          resultFlag = false;
          rollbackUpdate(session, me);
          continue;
        }
      }
      return resultFlag;

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}", me, me.getMessage());
      throw me;
    } finally {
      logger.methodEnd(MessageFormat.format("result = {0}", resultFlag));
    }
  }

  private HashMap<String, HashMap<Integer, Node>> createDbNodeMap(SessionWrapper session, String clusterId)
      throws MsfException {
    List<Node> dbNodeList = new NodeDao().readList(session, clusterId);
    HashMap<String, HashMap<Integer, Node>> dbNodeMap = new HashMap<>();
    for (NodeType type : NodeType.values()) {
      dbNodeMap.put(type.getSingularMessage(), new HashMap<>());
    }

    for (Node dbNode : dbNodeList) {
      String nodeType = dbNode.getNodeTypeEnum().getSingularMessage();
      dbNodeMap.get(nodeType).put(dbNode.getNodeId(), dbNode);
    }
    return dbNodeMap;
  }

  private boolean getNodeLock(SessionWrapper session, HashMap<String, HashMap<Integer, Node>> dbNodeMap,
      LogicalIfStatusNodeData node) throws MsfException {

    boolean res = true;
    String nodeType = node.getNodeType();
    Integer nodeId = Integer.valueOf(node.getNodeId());

    HashMap<Integer, Node> dbNodeTypeMap = dbNodeMap.get(nodeType);
    if (dbNodeTypeMap == null || !dbNodeTypeMap.containsKey(nodeId)) {
      logger.warn("[ope_id={0}]: NodeType={1}, NodeId={2}. Node Not Found.", getOperationId(), nodeType, nodeId);
      res = false;
    } else {
      List<Node> lockNodes = Arrays.asList(dbNodeTypeMap.get(nodeId));
      NodeType nodeTypeEnum = NodeType.getEnumFromSingularMessage(nodeType);
      switch (nodeTypeEnum) {
        case LEAF:
          DbManager.getInstance().getLeafsLock(lockNodes, session);
          break;
        case SPINE:
          DbManager.getInstance().getSpinesLock(lockNodes, session);
          break;
        default:
          throw new IllegalArgumentException(MessageFormat.format("nodeType={0}", nodeTypeEnum.getSingularMessage()));
      }
    }
    return res;
  }

  private void updateNode(SessionWrapper session, String clusterId, LogicalIfStatusNodeData node) throws MsfException {

    session.beginTransaction();

    NodeType nodeTypeEnum = NodeType.getEnumFromSingularMessage(node.getNodeType());
    Integer nodeId = Integer.valueOf(node.getNodeId());
    InternalLinkIfDao internalLinkIfDao = new InternalLinkIfDao();
    List<InternalLinkIf> dbInternalLinkIfList = internalLinkIfDao.readList(session, clusterId, nodeTypeEnum.getCode(),
        nodeId);

    HashMap<Integer, InternalLinkIf> dbInternalLinkIfMap = new HashMap<>();
    for (InternalLinkIf internalLinkIf : dbInternalLinkIfList) {
      dbInternalLinkIfMap.put(internalLinkIf.getInternalLinkIfId(), internalLinkIf);
    }

    for (LogicalIfStatusInternalLinkIfData internalLinkIfData : node.getInternalLinkIfs()) {
      Integer internalLinkIfId = Integer.valueOf(internalLinkIfData.getInternalLinkIfId());
      if (dbInternalLinkIfMap.containsKey(internalLinkIfId)) {
        InternalLinkIf updateInfo = dbInternalLinkIfMap.get(internalLinkIfId);
        updateInfo.setOperationStatusEnum(internalLinkIfData.getStatusEnum());
        internalLinkIfDao.update(session, updateInfo);

        logger.debug("[ope_id={0}]: InternalLinkIfId={1}. OperationStatus Update.", getOperationId(), internalLinkIfId);
      } else {
        logger.warn("[ope_id={0}]: InternalLinkIfId={1}. InternalLinkIf Not Found.", getOperationId(),
            internalLinkIfId);
        continue;
      }
    }
    session.commit();
  }

  private HashMap<String, L2Slice> createDbL2SliceMap(SessionWrapper session) throws MsfException {
    List<L2Slice> dbL2SliceList = new L2SliceDao().readList(session);
    HashMap<String, L2Slice> dbL2SliceMap = new HashMap<>();
    for (L2Slice dbL2Slice : dbL2SliceList) {
      dbL2SliceMap.put(dbL2Slice.getSliceId(), dbL2Slice);
    }
    return dbL2SliceMap;
  }

  private void updateL2Slice(SessionWrapper session, String clusterId, LogicalIfStatusVpnBaseData l2vpn)
      throws MsfException {
    String sliceId = l2vpn.getSliceId();
    session.beginTransaction();
    L2CpDao l2CpDao = new L2CpDao();
    List<L2Cp> dbL2CpList = l2CpDao.readList(session, sliceId, clusterId, ActiveStatus.ACTIVE.getCode());
    HashMap<String, L2Cp> dbL2CpMap = new HashMap<>();
    for (L2Cp l2Cp : dbL2CpList) {
      dbL2CpMap.put(l2Cp.getId().getCpId(), l2Cp);
    }

    for (LogicalIfStatusCpBaseData l2Cp : l2vpn.getCps()) {
      String cpId = l2Cp.getCpId();
      if (dbL2CpMap.containsKey(cpId)) {
        L2Cp updateInfo = dbL2CpMap.get(cpId);
        updateInfo.setOperationStatusEnum(l2Cp.getStatusEnum());

        l2CpDao.update(session, updateInfo);
        logger.debug("[ope_id={0}]: sliceId={1}, cpId={2}. OperationStatus Update.", getOperationId(), sliceId, cpId);
      } else {
        logger.warn("[ope_id={0}]: sliceId={1}, cpId={2}. CP Not Found.", getOperationId(), sliceId, cpId);
        continue;
      }
    }
    session.commit();
  }

  private HashMap<String, L3Slice> createDbL3SliceMap(SessionWrapper session) throws MsfException {
    List<L3Slice> dbL3SliceList = new L3SliceDao().readList(session);
    HashMap<String, L3Slice> dbL3SliceMap = new HashMap<>();
    for (L3Slice dbL3Slice : dbL3SliceList) {
      dbL3SliceMap.put(dbL3Slice.getSliceId(), dbL3Slice);
    }
    return dbL3SliceMap;
  }

  private void updateL3Slice(SessionWrapper session, String clusterId, LogicalIfStatusVpnBaseData l3vpn)
      throws MsfException {
    String sliceId = l3vpn.getSliceId();
    session.beginTransaction();
    L3CpDao l3CpDao = new L3CpDao();
    List<L3Cp> dbL3CpList = l3CpDao.readList(session, sliceId, clusterId, ActiveStatus.ACTIVE.getCode());
    HashMap<String, L3Cp> dbL3CpMap = new HashMap<>();
    for (L3Cp l3Cp : dbL3CpList) {
      dbL3CpMap.put(l3Cp.getId().getCpId(), l3Cp);
    }

    for (LogicalIfStatusCpBaseData l3Cp : l3vpn.getCps()) {
      String cpId = l3Cp.getCpId();
      if (dbL3CpMap.containsKey(cpId)) {
        L3Cp updateInfo = dbL3CpMap.get(cpId);
        updateInfo.setOperationStatusEnum(l3Cp.getStatusEnum());

        l3CpDao.update(session, updateInfo);
        logger.debug("[ope_id={0}]: sliceId={1}, cpId={2}. OperationStatus Update.", getOperationId(), sliceId, cpId);
      } else {
        logger.warn("[ope_id={0}]: sliceId={1}, cpId={2}. CP Not Found.", getOperationId(), sliceId, cpId);
        continue;
      }
    }
    session.commit();
  }

  private void rollbackUpdate(SessionWrapper session, MsfException me) throws MsfException {
    if (me.getErrorCode().equals(ErrorCode.DATABASE_OPERATION_ERROR)
        || me.getErrorCode().equals(ErrorCode.EXCLUSIVE_CONTROL_ERROR)) {
      try {
        session.rollback();
      } catch (MsfException me2) {
        logger.error("[ope_id={0}]: {1}", me2, getOperationId(), me2.getMessage());
        throw me2;
      }
    } else {
      session.rollback();
      throw me;
    }
  }

}