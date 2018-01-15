
package msf.fc.failure.status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
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
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for failure information notification.
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
        for (NoticeDestInfoFailure info : systemConfFailure.getNoticeDestInfo()) {
          isCluster |= info.isIsClusterUnit();
        }


        Map<String, FcNode> fcNodeMap = new HashMap<>();


        Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap = new HashMap<>();
        ifInfoEcMap.put(InterfaceType.PHYSICAL_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.BREAKOUT_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.LAG_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());
        ifInfoEcMap.put(InterfaceType.VLAN_IF, new HashMap<String, List<LogicalIfStatusIfEntity>>());

        int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();


        List<FailureStatusNodeFailureEntity> nodes = getNodeFailureList(session, fcNodeMap, clusterId,
            requestBody.getUpdateLogicalIfStatusOption().getNodeList());


        List<FailureStatusIfFailureEntity> ifs = getIfFailureList(session, fcNodeMap, clusterId,
            requestBody.getUpdateLogicalIfStatusOption().getIfList(), ifInfoEcMap);


        List<FailureStatusClusterFailureEntity> clusters = new ArrayList<>();
        if (isCluster) {


          addIfFailureDownNode(fcNodeMap, requestBody.getUpdateLogicalIfStatusOption().getNodeList(), ifInfoEcMap);


          Map<ClusterType, Map<String, FailureStatus>> failureInfoMap = getClusterNotifyInfo(session, ifInfoEcMap,
              null);
          clusters.addAll(getClusterFailureEntityList(failureInfoMap, clusterId));
        }

        int retry = systemConfFailure.getNoticeRetryNum();
        int timeout = systemConfFailure.getNoticeTimeout();

        noticeFailureInfo(systemConfFailure.getNoticeDestInfo(), ifs, nodes, clusters, retry, timeout);

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

    return ifs;
  }

  
  private void noticeFailureInfo(List<NoticeDestInfoFailure> noticeDestInfo, List<FailureStatusIfFailureEntity> ifs,
      List<FailureStatusNodeFailureEntity> nodes, List<FailureStatusClusterFailureEntity> clusters, int retry,
      int timeout) {

    for (NoticeDestInfoFailure destInfo : noticeDestInfo) {


      RestRequestBase request = createRequest(destInfo, ifs, nodes, clusters);
      if (request == null) {

        continue;
      }
      notifyFailureInfo(request, destInfo.getNoticeAddress(), destInfo.getNoticePort(), timeout, retry);
    }
  }

  
  private RestRequestBase createRequest(NoticeDestInfoFailure destInfo, List<FailureStatusIfFailureEntity> ifs,
      List<FailureStatusNodeFailureEntity> nodes, List<FailureStatusClusterFailureEntity> clusters) {
    try {
      logger.methodStart(new String[] { "destInfo", "ifs", "nodes", "clusters" },
          new Object[] { ToStringBuilder.reflectionToString(destInfo), ToStringBuilder.reflectionToString(ifs),
              ToStringBuilder.reflectionToString(nodes), ToStringBuilder.reflectionToString(clusters) });

      boolean isNotice = false;

      List<FailureStatusClusterFailureEntity> clustersCp = deepcopy(clusters);

      FailureStatusNotifyRequestBody request = new FailureStatusNotifyRequestBody();


      if (destInfo.isIsPhysicalUnit()) {
        if (!ifs.isEmpty() || !nodes.isEmpty()) {
          isNotice = true;
        }
        FailureStatusPhysicalUnitEntity physicalUnit = new FailureStatusPhysicalUnitEntity();
        physicalUnit.setIfList(ifs);
        physicalUnit.setNodeList(nodes);
        request.setPhysicalUnit(physicalUnit);
      }


      if (destInfo.isIsClusterUnit()) {
        if (!clustersCp.isEmpty()) {
          isNotice = true;
        }
        FailureStatusClusterUnitEntity clusterUnit = new FailureStatusClusterUnitEntity();
        updateInternalFailureStatus(destInfo, clustersCp);

        clusterUnit.setClusterList(clustersCp);
        request.setClusterUnit(clusterUnit);
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

  
  @SuppressWarnings("unchecked")
  private List<FailureStatusClusterFailureEntity> deepcopy(List<FailureStatusClusterFailureEntity> obj) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectOutputStream(baos).writeObject(obj);
      return (List<FailureStatusClusterFailureEntity>) new ObjectInputStream(
          new ByteArrayInputStream(baos.toByteArray())).readObject();
    } catch (IOException | ClassNotFoundException ioe) {
      throw new IllegalArgumentException();
    }
  }

  
  private void updateInternalFailureStatus(NoticeDestInfoFailure destInfo,
      List<FailureStatusClusterFailureEntity> clusters) {

    Integer failureLinkNum = destInfo.getFailureLinkNum();
    if (failureLinkNum == null) {
      return;
    }


    if (failureLinkNum.intValue() > getDownLinks()) {
      return;
    }


    for (FailureStatusClusterFailureEntity cluster : clusters) {
      if (ClusterType.INTERNAL.getMessage().equals(cluster.getType())) {
        cluster.setFailureStatusEnum(FailureStatus.DOWN);
      }
    }

  }

  
  private void notifyFailureInfo(RestRequestBase request, String ipAddress, int port, int timeout, int retryNum) {

    try {
      logger.methodStart(new String[] { "request", "ipAddress", "port", "timeout", "retryNum" },
          new Object[] { request, ipAddress, port, timeout, retryNum });

      for (int cnt = 0; cnt <= retryNum; cnt++) {
        try {
          RestClient.sendRequest(MfcFcRequestUri.FAILURE_NOTIFY.getHttpMethod(),
              MfcFcRequestUri.FAILURE_NOTIFY.getUri(), request, ipAddress, port);
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

  
  private RestResponseBase responseFailureNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  
  private void addIfFailureDownNode(Map<String, FcNode> fcNodeMap, List<LogicalIfStatusNodeEntity> nodes,
      Map<InterfaceType, Map<String, List<LogicalIfStatusIfEntity>>> ifInfoEcMap) throws MsfException {

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

  
  private LogicalIfStatusIfEntity getLogicalIf(String ecNodeId, InterfaceType ifType, String ifId) {

    LogicalIfStatusIfEntity ifFailure = new LogicalIfStatusIfEntity();
    ifFailure.setNodeId(ecNodeId);
    ifFailure.setIfTypeEnum(ifType);
    ifFailure.setStatusEnum(FailureStatus.DOWN);
    ifFailure.setIfId(ifId);
    return ifFailure;
  }
}
