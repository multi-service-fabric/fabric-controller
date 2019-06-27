
package msf.fc.failure.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.Failure;
import msf.fc.common.config.type.system.NoticeDestInfoFailure;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcVlanIf;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcVlanIfDao;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.logicalif.data.LogicalIfOperationRequestBody;
import msf.mfcfc.failure.logicalif.data.LogicalIfStatusRequest;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusIfEntity;
import msf.mfcfc.failure.logicalif.data.entity.LogicalIfStatusNodeEntity;
import msf.mfcfc.failure.status.data.FailureStatusNotifyRequestBody;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the failure information notification.
 *
 * @author NTT
 *
 */
public class FcFailureStatusNotifyScenario extends FcAbstractFailureStatusScenarioBase<LogicalIfStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFailureStatusNotifyScenario.class);

  private LogicalIfOperationRequestBody requestBody;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcFailureStatusNotifyScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(LogicalIfStatusRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      LogicalIfOperationRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          LogicalIfOperationRequestBody.class);

      requestBody.validate();

      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();

        Failure systemConfFailure = FcConfigManager.getInstance().getSystemConfFailure();

        if (systemConfFailure.getNoticeDestInfo().isEmpty()) {

          logger.info("Notification destination information is empty. execute skip.");
          return responseFailureNotifyData();
        }

        boolean isCluster = false;

        boolean isSlice = false;
        for (NoticeDestInfoFailure info : systemConfFailure.getNoticeDestInfo()) {
          isCluster |= info.isIsClusterUnit();
          isSlice |= info.isIsSliceUnit();
        }

        Map<String, FcNode> fcNodeMap = new HashMap<>();

        Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap = new HashMap<>();
        ifInfoEcMap.put(InterfaceType.PHYSICAL_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.BREAKOUT_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.LAG_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.VLAN_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());

        int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();

        int retry = systemConfFailure.getNoticeRetryNum();
        int timeout = systemConfFailure.getNoticeTimeout();

        List<FailureStatusNodeFailureEntity> nodeList = getNodeFailureList(session, fcNodeMap, clusterId,
            requestBody.getUpdateLogicalIfStatusOption().getNodeList());

        List<FailureStatusIfFailureEntity> ifList = getIfFailureList(session, fcNodeMap, clusterId,
            requestBody.getUpdateLogicalIfStatusOption().getIfList(), ifInfoEcMap);

        noticeFailureInfo(systemConfFailure.getNoticeDestInfo(), ifList, nodeList, new ArrayList<>(), new ArrayList<>(),
            null, retry, timeout);

        List<FailureStatusClusterFailureEntity> clusterList = new ArrayList<>();

        FailureStatusSliceUnitEntity sliceEntity = new FailureStatusSliceUnitEntity();

        if (isCluster | isSlice) {

          addIfFailureDownNode(session, fcNodeMap, requestBody.getUpdateLogicalIfStatusOption().getNodeList(),
              ifInfoEcMap);

          if (isSlice) {
            sliceEntity = createSliceNotifyInfo(session, ifInfoEcMap, null);

            noticeFailureInfo(systemConfFailure.getNoticeDestInfo(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), (sliceEntity == null) ? new ArrayList<>() : sliceEntity.getSliceList(),
                (sliceEntity == null) ? null : sliceEntity.getClusterLink(), retry, timeout);
          }

          if (isCluster) {

            Map<ClusterType, Map<String, FailureStatus>> failureInfoMap = createClusterNotifyInfo(session, ifInfoEcMap,
                null);

            clusterList.addAll(getClusterFailureEntityList(failureInfoMap, clusterId));

            noticeFailureInfo(systemConfFailure.getNoticeDestInfo(), new ArrayList<>(), new ArrayList<>(), clusterList,
                new ArrayList<>(), null, retry, timeout);
          }
        }

        return responseFailureNotifyData();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        session.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<FailureStatusNodeFailureEntity> getNodeFailureList(SessionWrapper session, Map<String, FcNode> fcNodeMap,
      int clusterId, List<LogicalIfStatusNodeEntity> ecNodes) throws NumberFormatException, MsfException {

    List<FailureStatusNodeFailureEntity> nodes = new ArrayList<>();
    if (ecNodes == null) {
      return nodes;
    }

    FcNodeDao fcNodeDao = new FcNodeDao();
    for (LogicalIfStatusNodeEntity ecNode : ecNodes) {

      String ecNodeId = ecNode.getNodeId();
      if (!fcNodeMap.containsKey(ecNodeId)) {
        FcNode fcNode = fcNodeDao.readByEcNodeId(session, Integer.valueOf(ecNodeId));
        fcNodeMap.put(ecNodeId, fcNode);
      }

      FcNode fcNode = fcNodeMap.get(ecNodeId);
      if (fcNode == null) {
        logger.info(
            "Failure Notify Skipped(Physical Unit :Node). Target Node Not Found. LogicalIfStatusNodeEntity={0}.",
            ecNode);
        continue;
      }

      nodes.add(getNodeFailureEntity(ecNode, fcNode, clusterId));
    }

    sortFailureStatusNodeFailureEntity(nodes);

    return nodes;
  }

  private List<FailureStatusIfFailureEntity> getIfFailureList(SessionWrapper session, Map<String, FcNode> fcNodeMap,
      int clusterId, List<LogicalIfStatusIfEntity> ecIfs,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap) throws MsfException {

    List<FailureStatusIfFailureEntity> ifs = new ArrayList<>();
    if (ecIfs == null) {
      return ifs;
    }

    FcNodeDao fcNodeDao = new FcNodeDao();
    for (LogicalIfStatusIfEntity ife : ecIfs) {

      String ecNodeId = ife.getNodeId();
      if (!fcNodeMap.containsKey(ecNodeId)) {
        FcNode fcNode = fcNodeDao.readByEcNodeId(session, Integer.valueOf(ecNodeId));
        fcNodeMap.put(ecNodeId, fcNode);
      }

      FcNode fcNode = fcNodeMap.get(ecNodeId);
      if (fcNode == null) {
        logger.info("Failure notify skipped(Physical Unit :If). Target Node not found. LogicalIfStatusIfEntity={0}.",
            ife);
        continue;
      }

      if (InterfaceType.VLAN_IF != ife.getIfTypeEnum()) {
        ifs.add(getIfFailureEntity(ife, fcNode, clusterId));
      }

      setMap(ifInfoEcMap.get(ife.getIfTypeEnum()), ife);
    }

    sortFailureStatusIfFailureEntity(ifs);

    return ifs;
  }

  private void noticeFailureInfo(List<NoticeDestInfoFailure> noticeDestInfo, List<FailureStatusIfFailureEntity> ifs,
      List<FailureStatusNodeFailureEntity> nodes, List<FailureStatusClusterFailureEntity> clusters,
      List<FailureStatusSliceFailureEntity> sliceList, FailureStatusSliceClusterLinkFailureEntity clusterLink,
      int retry, int timeout) {

    for (NoticeDestInfoFailure destInfo : noticeDestInfo) {

      RestRequestBase request = createRequest(destInfo, ifs, nodes, clusters, sliceList, clusterLink);
      if (request == null) {

        logger.debug("no data for notify. dest = {0}", ToStringBuilder.reflectionToString(destInfo));
        continue;
      }
      notifyFailureInfo(request, destInfo.getNoticeAddress(), destInfo.getNoticePort(), timeout, retry);
    }
  }

  @SuppressWarnings("unchecked")
  private RestRequestBase createRequest(NoticeDestInfoFailure destInfo, List<FailureStatusIfFailureEntity> ifList,
      List<FailureStatusNodeFailureEntity> nodeList, List<FailureStatusClusterFailureEntity> clusterList,
      List<FailureStatusSliceFailureEntity> sliceList, FailureStatusSliceClusterLinkFailureEntity clusterLink) {
    try {
      logger.methodStart(new String[] { "destInfo", "ifList", "nodeList", "clusterList", "sliceList", "clusterLink" },
          new Object[] { ToStringBuilder.reflectionToString(destInfo), ToStringBuilder.reflectionToString(ifList),
              ToStringBuilder.reflectionToString(nodeList), ToStringBuilder.reflectionToString(clusterList),
              ToStringBuilder.reflectionToString(sliceList), ToStringBuilder.reflectionToString(clusterLink) });

      boolean isNotice = false;

      List<FailureStatusClusterFailureEntity> clusterListCp = (List<FailureStatusClusterFailureEntity>) deepCopy(
          clusterList);
      List<FailureStatusSliceFailureEntity> sliceListCp = (List<FailureStatusSliceFailureEntity>) deepCopy(sliceList);

      FailureStatusNotifyRequestBody request = new FailureStatusNotifyRequestBody();

      if (destInfo.isIsPhysicalUnit()) {
        if (!ifList.isEmpty() || !nodeList.isEmpty()) {
          isNotice = true;
        }
        FailureStatusPhysicalUnitEntity physicalUnit = new FailureStatusPhysicalUnitEntity();
        physicalUnit.setIfList(ifList);
        physicalUnit.setNodeList(nodeList);
        request.setPhysicalUnit(physicalUnit);
      }

      if (destInfo.isIsClusterUnit()) {
        if (!clusterListCp.isEmpty()) {
          isNotice = true;
        }
        FailureStatusClusterUnitEntity clusterUnit = new FailureStatusClusterUnitEntity();

        updateInternalFailureStatusClusterUnit(destInfo, clusterListCp);

        clusterUnit.setClusterList(clusterListCp);
        request.setClusterUnit(clusterUnit);
      }

      if (destInfo.isIsSliceUnit()) {
        if (!sliceListCp.isEmpty() || clusterLink != null) {
          isNotice = true;
        }
        FailureStatusSliceUnitEntity sliceUnit = new FailureStatusSliceUnitEntity();

        updateInternalFailureStatusSliceUnit(destInfo, sliceListCp);
        sliceUnit.setSliceList(sliceListCp);
        sliceUnit.setClusterLink(clusterLink);
        request.setSliceUnit(sliceUnit);

      }

      RestRequestBase requestBase = null;
      if (isNotice) {
        requestBase = new RestRequestBase();
        requestBase.setRequestBody(JsonUtil.toJson(request));
      }

      return requestBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void updateInternalFailureStatusClusterUnit(NoticeDestInfoFailure destInfo,
      List<FailureStatusClusterFailureEntity> clusterList) {

    Integer failureLinkNum = destInfo.getFailureLinkNum();
    if (failureLinkNum == null) {
      return;
    }

    if (failureLinkNum.intValue() > getDownLinks()) {
      return;
    }

    for (FailureStatusClusterFailureEntity cluster : clusterList) {
      if (ClusterType.INTERNAL.getMessage().equals(cluster.getType())) {
        cluster.setFailureStatusEnum(FailureStatus.DOWN);
      }
    }
  }

  private void updateInternalFailureStatusSliceUnit(NoticeDestInfoFailure destInfo,
      List<FailureStatusSliceFailureEntity> sliceList) {

    Integer failureLinkNum = destInfo.getFailureLinkNum();
    if (failureLinkNum == null) {
      return;
    }

    if (failureLinkNum.intValue() > getDownLinks()) {
      return;
    }

    for (FailureStatusSliceFailureEntity slice : sliceList) {
      slice.setFailureStatusEnum(FailureStatus.DOWN);
    }
  }

  private RestResponseBase responseFailureNotifyData() {
    return new RestResponseBase(HttpStatus.OK_200, (String) null);
  }

  protected void addIfFailureDownNode(SessionWrapper session, Map<String, FcNode> fcNodeMap,
      List<LogicalIfStatusNodeEntity> nodes, Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap)
      throws MsfException {

    if (nodes == null) {
      return;
    }

    for (LogicalIfStatusNodeEntity logicalIfNode : nodes) {

      if (FailureStatus.DOWN != logicalIfNode.getFailureStatusEnum()) {
        continue;
      }

      String ecNodeId = logicalIfNode.getNodeId();
      FcNode fcNode = fcNodeMap.get(ecNodeId);
      if (fcNode == null) {

        continue;
      }

      List<FcPhysicalIf> physicalIfs = fcNode.getPhysicalIfs();
      List<LogicalIfStatusIfEntity> logicalPhysicalIfs = getLogicalIfPhysicalIf(logicalIfNode.getNodeId(), physicalIfs);
      setMapList(ifInfoEcMap.get(InterfaceType.PHYSICAL_IF), logicalPhysicalIfs);

      List<FcBreakoutIf> breakoutIfs = fcNode.getBreakoutIfs();
      List<LogicalIfStatusIfEntity> logicalBreakoutIfs = getLogicalIfBreakoutIf(logicalIfNode.getNodeId(), breakoutIfs);
      setMapList(ifInfoEcMap.get(InterfaceType.BREAKOUT_IF), logicalBreakoutIfs);

      List<FcLagIf> lagIfs = fcNode.getLagIfs();
      List<LogicalIfStatusIfEntity> logicalLagIfs = getLogicalIfLagIf(logicalIfNode.getNodeId(), lagIfs);
      setMapList(ifInfoEcMap.get(InterfaceType.LAG_IF), logicalLagIfs);

      FcVlanIfDao fcVlanIfDao = new FcVlanIfDao();
      List<FcVlanIf> vlanIfs = fcVlanIfDao.readList(session, fcNode.getNodeInfoId());
      List<LogicalIfStatusIfEntity> logicalVlanIfs = getLogicalIfVlanIf(logicalIfNode.getNodeId(), vlanIfs);
      setMapList(ifInfoEcMap.get(InterfaceType.VLAN_IF), logicalVlanIfs);
    }
  }

  private List<LogicalIfStatusIfEntity> getLogicalIfPhysicalIf(String ecNodeId, List<FcPhysicalIf> physicalIfs) {

    List<LogicalIfStatusIfEntity> failureIfList = new ArrayList<>();
    for (FcPhysicalIf physicalIf : physicalIfs) {
      LogicalIfStatusIfEntity logicalIf = getLogicalIf(ecNodeId, InterfaceType.PHYSICAL_IF,
          physicalIf.getPhysicalIfId());
      failureIfList.add(logicalIf);
    }
    return failureIfList;
  }

  private List<LogicalIfStatusIfEntity> getLogicalIfBreakoutIf(String ecNodeId, List<FcBreakoutIf> breakoutIfs) {

    List<LogicalIfStatusIfEntity> failureIfList = new ArrayList<>();
    for (FcBreakoutIf breakoutIf : breakoutIfs) {
      LogicalIfStatusIfEntity logicalIf = getLogicalIf(ecNodeId, InterfaceType.BREAKOUT_IF,
          breakoutIf.getBreakoutIfId());
      failureIfList.add(logicalIf);
    }
    return failureIfList;
  }

  private List<LogicalIfStatusIfEntity> getLogicalIfLagIf(String ecNodeId, List<FcLagIf> lagIfs) {

    List<LogicalIfStatusIfEntity> failureIfList = new ArrayList<>();
    for (FcLagIf lagIf : lagIfs) {
      LogicalIfStatusIfEntity logicalIf = getLogicalIf(ecNodeId, InterfaceType.LAG_IF, lagIf.getLagIfId().toString());
      failureIfList.add(logicalIf);
    }
    return failureIfList;
  }

  private List<LogicalIfStatusIfEntity> getLogicalIfVlanIf(String ecNodeId, List<FcVlanIf> vlanIfs) {

    List<LogicalIfStatusIfEntity> failureIfList = new ArrayList<>();
    for (FcVlanIf vlanIf : vlanIfs) {
      LogicalIfStatusIfEntity logicalIf = getLogicalIf(vlanIf.getId().getVlanIfId().toString(),
          FailureStatus.DOWN.getMessage(), ecNodeId, InterfaceType.VLAN_IF);
      failureIfList.add(logicalIf);
    }
    return failureIfList;
  }

  private LogicalIfStatusIfEntity getLogicalIf(String ecNodeId, InterfaceType ifType, String ifId) {

    LogicalIfStatusIfEntity ifFailure = new LogicalIfStatusIfEntity();
    ifFailure.setNodeId(ecNodeId);
    ifFailure.setIfTypeEnum(ifType);
    ifFailure.setStatusEnum(FailureStatus.DOWN);
    ifFailure.setIfId(ifId);
    return ifFailure;
  }
}
