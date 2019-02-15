
package msf.fc.traffic.traffics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.NoticeDestInfoTraffic;
import msf.fc.common.config.type.system.Traffic;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectAllTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoSwitchTrafficCollectAllEcEntity;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.data.IfTrafficNotifyRequestBody;
import msf.mfcfc.traffic.traffics.data.TrafficCommonData;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficClusterEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficClusterUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficNotifyEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficPhysicalUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficSliceEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficSliceUnitEntity;

/**
 * Main class for traffic information notification. To be executed periodically
 * from the CycleTimer class.
 *
 * @author NTT
 *
 */
public class FcTrafficNoticeThread extends Thread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcTrafficNoticeThread.class);

  private TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();

  private boolean isRunning = false;

  private boolean wakeupFlag = false;

  private static Date startTime = new Date();

  private static Date lastStartTime = new Date();

  private static Date endTime = new Date();

  private boolean isInternal = true;

  /**
   * Returns execution of the traffic information notification
   *
   * @return whether or not execution of the traffic information notification
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Set the wakeup flag.
   *
   * @param wakeupFlag
   *          Wakeup flag
   */
  public void setWakeupFlag(boolean wakeupFlag) {
    this.wakeupFlag = wakeupFlag;
  }

  protected synchronized void lock() throws InterruptedException {
    try {
      logger.methodStart();

      while ((!wakeupFlag) && (!trafficCommonData.isForceStop())) {
        wait();
      }

      logger.trace("Wakeup FcTrafficNoticeThread.");

      isRunning = true;
      wakeupFlag = false;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execution of the traffic information notification.
   */
  @Override
  public void run() {
    try {
      logger.methodStart();
      while (true) {
        try {

          trafficCommonData.checkForceStop();

          lock();

          trafficCommonData.checkForceStop();

          executeNoticeTraffic();

        } catch (InterruptedException ie) {
          if (trafficCommonData.isForceStop()) {
            logger.warn("Force Stop.");
            return;
          }

          logger.debug("InterruptedException. Not ForceStop.");
        } catch (MsfException exp) {

        } finally {
          isRunning = false;
        }
      }
    } finally {
      logger.methodEnd();
    }

  }

  /**
   * Execution of the traffic information notification.
   *
   * @throws MsfException
   *           Exception
   */
  public void executeNoticeTraffic() throws MsfException {

    try {
      logger.methodStart();

      List<IfTrafficNotifyEntity> physicalUnitList = new ArrayList<>();

      List<IfTrafficClusterEntity> clusterUnitList = new ArrayList<>();

      List<IfTrafficSliceEntity> sliceUnitList = new ArrayList<>();
      isInternal = true;

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();

        Traffic systemConfTraffic = FcConfigManager.getInstance().getSystemConfTraffic();
        int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();

        if (systemConfTraffic.getNoticeDestInfo().isEmpty()) {

          logger.info("Notification destination information is empty. execute skip.");
          return;
        }

        boolean isSlice = false;
        for (NoticeDestInfoTraffic destInfo : systemConfTraffic.getNoticeDestInfo()) {
          isSlice |= destInfo.isIsSliceUnit();
        }

        TrafficInfoCollectAllTrafficEcResponseBody responseBody = sendTrafficReadList();
        if (!responseBody.getIsSuccess()) {

          logger.info("Failed get all traffic info from EC. is_success={0}", responseBody.getIsSuccess());
          return;
        }

        if (responseBody.getSwitchTrafficList() == null) {

          logger.info("switch_traffics is null.");
          return;
        }

        for (TrafficInfoSwitchTrafficCollectAllEcEntity switchTraffic : responseBody.getSwitchTrafficList()) {
          Integer ecNodeId = Integer.valueOf(switchTraffic.getNodeId());
          FcNode fcNode = new FcNodeDao().readByEcNodeId(session, ecNodeId);

          if (fcNode == null) {
            logger.info(
                "Traffic notify skipped. Target node not found. TrafficInfoSwitchTrafficCollectAllEcEntity={0}.",
                switchTraffic);
            continue;
          }

          Integer nodeId = fcNode.getNodeId();
          NodeType fabric = fcNode.getNodeTypeEnum();

          if (switchTraffic.getTrafficValueList() == null) {

            logger.warn("Traffic values is null. ecNodeId={0}, nodeId={1}", ecNodeId, nodeId);
            continue;
          }

          createNoticeUnit(session, switchTraffic.getTrafficValueList(), ecNodeId, nodeId, clusterId, fabric, isSlice,
              physicalUnitList, clusterUnitList, sliceUnitList);
        }

        for (NoticeDestInfoTraffic destInfo : systemConfTraffic.getNoticeDestInfo()) {
          RestRequestBase request = createRequest(destInfo, physicalUnitList, clusterUnitList, sliceUnitList);

          if (request == null) {

            continue;
          }

          notifyTrafficInfo(request, destInfo.getNoticeAddress(), destInfo.getNoticePort(),
              systemConfTraffic.getNoticeTimeout(), systemConfTraffic.getNoticeRetryNum());
        }

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } catch (Exception ex) {

        logger.error("Some kind of Exception.", ex);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Some kind of Exception.");
      } finally {
        session.closeSession();
      }

    } finally {
      logger.methodEnd();
    }

  }

  private void createNoticeUnit(SessionWrapper session, List<TrafficInfoTrafficValueEcEntity> trafficValues,
      Integer ecNodeId, Integer nodeId, int clusterId, NodeType fabricType, boolean isSlice,
      List<IfTrafficNotifyEntity> physicalUnitList, List<IfTrafficClusterEntity> clusterUnitList,
      List<IfTrafficSliceEntity> sliceUnitList) throws MsfException {

    for (TrafficInfoTrafficValueEcEntity trafficValue : trafficValues) {
      String ifType = trafficValue.getIfType();
      String ifId = trafficValue.getIfId();

      if (InterfaceType.VLAN_IF.getMessage().equals(ifType)) {

        getIfTrafficNotifyVlanIf(session, trafficValue, isSlice, ecNodeId, ifId, sliceUnitList);
        continue;
      }

      FcEdgePoint trafficEdgePoint = getEdgePoint(session, ifType, ecNodeId, ifId);
      if (trafficEdgePoint != null) {

        getIfTrafficNotifyEdgePoint(trafficEdgePoint, trafficValue, clusterId, nodeId, fabricType, physicalUnitList,
            clusterUnitList);
        continue;
      }

      FcInternalLinkIf trafficInternalLinkIf = getInternalLinkIf(session, ifType, ecNodeId, ifId);
      if (trafficInternalLinkIf != null) {

        getIfTrafficNotifyInternalLinkIf(trafficInternalLinkIf, trafficValue, clusterId, nodeId, fabricType,
            physicalUnitList, clusterUnitList);
        continue;
      }

      FcClusterLinkIf trafficClusterLinkIf = getClusterLinkIf(session, ifType, nodeId, ifId);
      if (trafficClusterLinkIf != null) {

        getIfTrafficNotifyClusterLinkIf(trafficClusterLinkIf, trafficValue, clusterId, nodeId, fabricType,
            physicalUnitList, clusterUnitList);
        continue;
      }
    }

    groupSliceUnitList(sliceUnitList);
  }

  private void groupSliceUnitList(List<IfTrafficSliceEntity> sliceUnitList) {

    Map<String, List<String>> l2SliceMap = new HashMap<>();
    Map<String, List<String>> l3SliceMap = new HashMap<>();

    for (IfTrafficSliceEntity sliceUnit : sliceUnitList) {
      if (SliceType.L2_SLICE == sliceUnit.getSliceTypeEnum()) {
        setSliceMap(l2SliceMap, sliceUnit.getSliceId(), sliceUnit.getCpIdList());
      } else if (SliceType.L3_SLICE == sliceUnit.getSliceTypeEnum()) {
        setSliceMap(l3SliceMap, sliceUnit.getSliceId(), sliceUnit.getCpIdList());
      }
    }

    sliceUnitList.clear();

    sliceUnitList.addAll(getSliceUnitGroup(l2SliceMap, SliceType.L2_SLICE));

    sliceUnitList.addAll(getSliceUnitGroup(l3SliceMap, SliceType.L3_SLICE));
  }

  private void setSliceMap(Map<String, List<String>> sliceMap, String sliceId, List<String> cpIds) {

    if (cpIds.isEmpty()) {
      return;
    }

    if (sliceMap.get(sliceId) == null) {
      sliceMap.put(sliceId, new ArrayList<String>());
    }
    sliceMap.get(sliceId).addAll(cpIds);
  }

  private List<IfTrafficSliceEntity> getSliceUnitGroup(Map<String, List<String>> sliceMap, SliceType sliceType) {

    List<IfTrafficSliceEntity> groupSliceUnitList = new ArrayList<>();
    for (Entry<String, List<String>> sliceEntry : sliceMap.entrySet()) {
      IfTrafficSliceEntity ifTrafficSliceEntity = new IfTrafficSliceEntity();
      ifTrafficSliceEntity.setSliceTypeEnum(sliceType);
      ifTrafficSliceEntity.setSliceId(sliceEntry.getKey());
      ifTrafficSliceEntity.setCpIdList(sliceEntry.getValue());
      groupSliceUnitList.add(ifTrafficSliceEntity);
    }
    return groupSliceUnitList;
  }

  private void getIfTrafficNotifyVlanIf(SessionWrapper session, TrafficInfoTrafficValueEcEntity trafficValue,
      boolean isSlice, Integer ecNodeId, String ifId, List<IfTrafficSliceEntity> sliceUnitList) throws MsfException {

    if (isSlice) {

      FcL2CpDao fcL2CpDao = new FcL2CpDao();
      FcL2Cp fcL2Cp = fcL2CpDao.read(session, ecNodeId, ifId);

      if (fcL2Cp == null) {

        FcL3CpDao fcL3CpDao = new FcL3CpDao();
        FcL3Cp fcL3Cp = fcL3CpDao.read(session, ecNodeId, ifId);

        if (fcL3Cp == null) {
          logger.info(
              "Traffic notify skipped(Slice Unit:L2Cp/L3Cp). Target L2Cp/L3Cp not found. ecNodeId={0}, ifId={1}.",
              ecNodeId, ifId);
          return;
        } else {

          if (isOverThreshold(fcL3Cp.getTrafficThreshold(), trafficValue.getReceiveRate(),
              trafficValue.getSendRate())) {
            sliceUnitList.add(getIfTrafficSliceEntity(fcL3Cp));
          }
        }
      } else {

        if (isOverThreshold(fcL2Cp.getTrafficThreshold(), trafficValue.getReceiveRate(), trafficValue.getSendRate())) {
          sliceUnitList.add(getIfTrafficSliceEntity(fcL2Cp));
        }
      }
    }
  }

  private void getIfTrafficNotifyEdgePoint(FcEdgePoint edgePoint, TrafficInfoTrafficValueEcEntity trafficValue,
      int clusterId, Integer nodeId, NodeType fabricType, List<IfTrafficNotifyEntity> physicalUnitList,
      List<IfTrafficClusterEntity> clusterUnitList) throws MsfException {

    if (isOverThreshold(edgePoint.getTrafficThreshold(), trafficValue.getReceiveRate(), trafficValue.getSendRate())) {

      physicalUnitList.add(getIfTrafficNotifyEntity(trafficValue, clusterId, nodeId, fabricType));

      clusterUnitList.add(
          getIfTrafficClusterEntity(clusterId, ClusterType.EDGE_POINT, String.valueOf(edgePoint.getEdgePointId())));

    }
  }

  private void getIfTrafficNotifyInternalLinkIf(FcInternalLinkIf internalLinkIf,
      TrafficInfoTrafficValueEcEntity trafficValue, int clusterId, Integer nodeId, NodeType fabricType,
      List<IfTrafficNotifyEntity> physicalUnitList, List<IfTrafficClusterEntity> clusterUnitList) {

    if (isOverThreshold(internalLinkIf.getTrafficThreshold(), trafficValue.getReceiveRate(),
        trafficValue.getSendRate())) {

      physicalUnitList.add(getIfTrafficNotifyEntity(trafficValue, clusterId, nodeId, fabricType));

      if (isInternal) {

        clusterUnitList.add(getIfTrafficClusterEntity(clusterId, ClusterType.INTERNAL, null));
        isInternal = false;
      }
    }
  }

  private void getIfTrafficNotifyClusterLinkIf(FcClusterLinkIf clusterLinkIf,
      TrafficInfoTrafficValueEcEntity trafficValue, int clusterId, Integer nodeId, NodeType fabricType,
      List<IfTrafficNotifyEntity> physicalUnitList, List<IfTrafficClusterEntity> clusterUnitList) {

    if (isOverThreshold(clusterLinkIf.getTrafficThreshold(), trafficValue.getReceiveRate(),
        trafficValue.getSendRate())) {

      physicalUnitList.add(getIfTrafficNotifyEntity(trafficValue, clusterId, nodeId, fabricType));

      clusterUnitList.add(getIfTrafficClusterEntity(clusterId, ClusterType.CLUSTER_LINK_IF,
          String.valueOf(clusterLinkIf.getClusterLinkIfId())));
    }
  }

  private FcEdgePoint getEdgePoint(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcEdgePoint fcEdgePoint = null;
    FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcEdgePoint = fcEdgePointDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcEdgePoint;
  }

  private FcInternalLinkIf getInternalLinkIf(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcInternalLinkIf fcInternalLinkIf = null;
    FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcInternalLinkIf = fcInternalLinkIfDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcInternalLinkIf;
  }

  private FcClusterLinkIf getClusterLinkIf(SessionWrapper session, String ifType, Integer ecNodeId, String ifId)
      throws MsfException {

    FcClusterLinkIf fcClusterLinkIf = null;
    FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();
    if (InterfaceType.PHYSICAL_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByPhysicalIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.LAG_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByLagIfId(session, ecNodeId, ifId);
    } else if (InterfaceType.BREAKOUT_IF.getMessage().equals(ifType)) {
      fcClusterLinkIf = fcClusterLinkIfDao.readByBreakoutIfId(session, ecNodeId, ifId);
    }
    return fcClusterLinkIf;
  }

  private IfTrafficNotifyEntity getIfTrafficNotifyEntity(TrafficInfoTrafficValueEcEntity trafficValue, int clusterId,
      Integer nodeId, NodeType fabricType) {

    IfTrafficNotifyEntity ifTrafficNotifyEntity = new IfTrafficNotifyEntity();
    ifTrafficNotifyEntity.setClusterId(String.valueOf(clusterId));
    ifTrafficNotifyEntity.setFabricType(fabricType.getPluralMessage());
    ifTrafficNotifyEntity.setNodeId(String.valueOf(nodeId));
    ifTrafficNotifyEntity.setIfType(trafficValue.getIfType());
    ifTrafficNotifyEntity.setIfId(trafficValue.getIfId());
    return ifTrafficNotifyEntity;
  }

  private IfTrafficClusterEntity getIfTrafficClusterEntity(int clusterId, ClusterType type, String ifId) {

    IfTrafficClusterEntity ifTrafficClusterEntity = new IfTrafficClusterEntity();
    ifTrafficClusterEntity.setClusterId(String.valueOf(clusterId));
    ifTrafficClusterEntity.setType(type.getMessage());
    ifTrafficClusterEntity.setIfId(ifId);
    return ifTrafficClusterEntity;
  }

  private IfTrafficSliceEntity getIfTrafficSliceEntity(FcL2Cp l2Cp) {

    IfTrafficSliceEntity ifTraffic = new IfTrafficSliceEntity();
    ifTraffic.setSliceType(SliceType.L2_SLICE.getMessage());
    ifTraffic.setSliceId(l2Cp.getId().getSliceId());
    List<String> cpIdList = new ArrayList<>();
    cpIdList.add(l2Cp.getId().getCpId());
    ifTraffic.setCpIdList(cpIdList);
    return ifTraffic;
  }

  private IfTrafficSliceEntity getIfTrafficSliceEntity(FcL3Cp l3Cp) {

    IfTrafficSliceEntity ifTraffic = new IfTrafficSliceEntity();
    ifTraffic.setSliceType(SliceType.L3_SLICE.getMessage());
    ifTraffic.setSliceId(l3Cp.getId().getSliceId());
    List<String> cpIdList = new ArrayList<>();
    cpIdList.add(l3Cp.getId().getCpId());
    ifTraffic.setCpIdList(cpIdList);
    return ifTraffic;
  }

  private RestRequestBase createRequest(NoticeDestInfoTraffic destInfo, List<IfTrafficNotifyEntity> physicalUnitList,
      List<IfTrafficClusterEntity> clusterUnitList, List<IfTrafficSliceEntity> sliceUnitList) {

    try {
      logger.methodStart(new String[] { "destInfo", "physicalUnitList", "clusterUnitList", "sliceUnitList" },
          new Object[] { ToStringBuilder.reflectionToString(destInfo),
              ToStringBuilder.reflectionToString(physicalUnitList), ToStringBuilder.reflectionToString(clusterUnitList),
              ToStringBuilder.reflectionToString(sliceUnitList) });

      boolean isNotice = false;
      IfTrafficNotifyRequestBody request = new IfTrafficNotifyRequestBody();
      if (destInfo.isIsPhysicalUnit()) {
        if (!physicalUnitList.isEmpty()) {
          isNotice = true;
        }

        IfTrafficPhysicalUnitEntity physicalUnit = new IfTrafficPhysicalUnitEntity();
        physicalUnit.setIfList(physicalUnitList);
        request.setPhysicalUnit(physicalUnit);
      }
      if (destInfo.isIsClusterUnit()) {
        if (!clusterUnitList.isEmpty()) {
          isNotice = true;
        }

        IfTrafficClusterUnitEntity clusterUnit = new IfTrafficClusterUnitEntity();
        clusterUnit.setClusterList(clusterUnitList);
        request.setClusterUnit(clusterUnit);
      }
      if (destInfo.isIsSliceUnit()) {
        if (!sliceUnitList.isEmpty()) {
          isNotice = true;
        }

        IfTrafficSliceUnitEntity sliceUnit = new IfTrafficSliceUnitEntity();
        sliceUnit.setSliceList(sliceUnitList);
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

  private boolean isOverThreshold(Double threshold, Double receiveRate, Double sendRate) {

    if (threshold == null) {

      return false;
    }
    if (threshold.compareTo(receiveRate) < 0) {
      return true;
    }
    if (threshold.compareTo(sendRate) < 0) {
      return true;
    }
    return false;
  }

  private TrafficInfoCollectAllTrafficEcResponseBody sendTrafficReadList() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.TRAFFIC_READ_LIST.getHttpMethod(),
          EcRequestUri.TRAFFIC_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      TrafficInfoCollectAllTrafficEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          TrafficInfoCollectAllTrafficEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (HttpStatus.OK_200 != restResponseBase.getHttpStatusCode()) {

        String errorMsg = MessageFormat.format("HttpStatusCode={0}, ErrorCode={1}",
            restResponseBase.getHttpStatusCode(), responseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private void notifyTrafficInfo(RestRequestBase request, String ipAddress, int port, int timeout, int retryNum) {

    try {
      logger.methodStart(new String[] { "request", "ipAddress", "port", "timeout", "retryNum" },
          new Object[] { request, ipAddress, port, timeout, retryNum });

      for (int cnt = 0; cnt <= retryNum; cnt++) {
        try {
          RestClient.sendRequest(MfcFcRequestUri.TRAFFIC_NOTIFY.getHttpMethod(),
              MfcFcRequestUri.TRAFFIC_NOTIFY.getUri(), request, ipAddress, port);
          break;
        } catch (MsfException msfException) {

          try {
            Thread.sleep(timeout);
          } catch (InterruptedException ie) {

          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Returns the start time of the traffic information notification.
   *
   * @return traffic information notification start time
   */
  public static Date getStartTime() {
    return startTime;
  }

  /**
   * Set the start time of the traffic information notification.
   *
   * @param startTime
   *          Start time
   */
  public static void setStartTime(Date startTime) {

    setLastStartTime(FcTrafficNoticeThread.startTime);

    FcTrafficNoticeThread.startTime = startTime;
  }

  /**
   * Returns the previous start time of the traffic information notification.
   *
   * @return traffic information notification start time
   */
  public static Date getLastStartTime() {
    return lastStartTime;
  }

  /**
   * Set the previous start time of the traffic information notification.
   *
   * @param lastStartTime
   *          Start time
   */
  public static void setLastStartTime(Date lastStartTime) {
    FcTrafficNoticeThread.lastStartTime = lastStartTime;
  }

  /**
   * Returns the end time of the traffic information notification.
   *
   * @return traffic information notification end time
   */
  public static Date getEndTime() {
    return endTime;
  }

  /**
   * Set the end time of the traffic information notification.
   *
   * @param endTime
   *          Time
   */
  public static void setEndTime(Date endTime) {
    FcTrafficNoticeThread.endTime = endTime;
  }

}
