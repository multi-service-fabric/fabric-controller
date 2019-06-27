
package msf.fc.services.silentfailure.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.services.silentfailure.FcSilentFailureManager;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.PingBetweenDevicesEcRequestBody;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.PingBetweenDevicesEcResponseBody;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.entity.DstEcEntity;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.entity.PingTargetListEcEntity;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.entity.PingTargetListResponseEcEntity;
import msf.fc.services.silentfailure.rest.ec.pingbetweendevices.data.entity.SrcEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.silentfailure.common.constant.DetectionTriggerType;
import msf.mfcfc.services.silentfailure.common.constant.EcPingBetweenDevicesResult;
import msf.mfcfc.services.silentfailure.common.constant.EcRequestUri;
import msf.mfcfc.services.silentfailure.common.constant.MonitoringResultType;
import msf.mfcfc.services.silentfailure.scenario.data.SilentFaultNotificationUpdateRequestBody;

/**
 * Main class for the monitoring communication between devices by ping command.
 * This is executed periodically by the CycleTimer class.
 *
 * @author NTT
 *
 */
public class FcPingMonitorThread extends FcAbstractSilentFailureMonitorThread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPingMonitorThread.class);

  /**
   * Execution of the monitoring communication between devices by ping command.
   *
   * @throws MsfException
   *           If DB access error occurs, or sending notification fails.
   */
  public void executePingMonitor() throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap = getMonitorInternalLinkIfs(
            sessionWrapper);

        PingBetweenDevicesEcRequestBody requestBody = makePingBetweenDevicesRequestBody(internalLinkStatusMap);

        if (!requestBody.getPingTargetList().isEmpty()) {

          PingBetweenDevicesEcResponseBody responseBody = sendPingBetweenDevices(requestBody);

          Set<Map<NodeType, IfInfo>> failedResultIfInfoMapSet = getFailedResultIfInfoMapSet(sessionWrapper,
              responseBody);

          if (!failedResultIfInfoMapSet.isEmpty()) {

            boolean enableExecutingIfBlockade = FcSilentFailureManager.getInstance().getSystemConfData()
                .getSilentFailure().isEnableExecutingIfBlockade();

            if (enableExecutingIfBlockade) {
              if (failedResultIfInfoMapSet.size() == 1) {

                executeBlockingInterfaces(sessionWrapper, failedResultIfInfoMapSet.iterator().next());
              }

              setBlockadeStatusToNone(failedResultIfInfoMapSet);
            }

            SilentFaultNotificationUpdateRequestBody notifyRequestBody = makeSilentFaultNotificationUpdateRequestBody(
                internalLinkStatusMap.keySet(), failedResultIfInfoMapSet, DetectionTriggerType.PING);

            notifySilentFailureAll(notifyRequestBody);
          }
        }

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } catch (Exception ex) {

        logger.error("Some kind of Exception.", ex);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Some kind of Exception.");
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void run() {
    try {
      logger.methodStart();
      while (true) {
        try {

          silentFailureCommonData.checkForceStopPingMonitor();

          lock(silentFailureCommonData.isForceStopPingMonitor());

          silentFailureCommonData.checkForceStopPingMonitor();

          executePingMonitor();

        } catch (InterruptedException ie) {
          if (silentFailureCommonData.isForceStopPingMonitor()) {
            logger.info("Force Stop.");
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

  private PingBetweenDevicesEcRequestBody makePingBetweenDevicesRequestBody(
      Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkStatusMap" }, new Object[] { internalLinkStatusMap });
      List<PingTargetListEcEntity> pingTargetList = new ArrayList<>();

      for (Map.Entry<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> statusMapEntry : internalLinkStatusMap
          .entrySet()) {

        if (InterfaceOperationStatus.UP.equals(statusMapEntry.getValue())) {
          PingTargetListEcEntity pingTargetListEntity = makePingTargetListEntity(statusMapEntry.getKey());
          pingTargetList.add(pingTargetListEntity);
        }
      }

      PingBetweenDevicesEcRequestBody requestBody = new PingBetweenDevicesEcRequestBody();
      requestBody.setPingTargetList(pingTargetList);

      return requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private PingTargetListEcEntity makePingTargetListEntity(Map<NodeType, FcInternalLinkIf> internalLinkMap)
      throws MsfException {
    try {
      logger.methodStart();

      FcInternalLinkIf spineInternalLinkIf = internalLinkMap.get(NodeType.SPINE);
      IfInfo srcIfInfo = getIfInfoFromInternalLinkIf(spineInternalLinkIf);
      SrcEcEntity srcEntity = new SrcEcEntity();
      srcEntity.setNodeId(srcIfInfo.getEcNodeId());
      srcEntity.setIfType(srcIfInfo.getIfType());
      srcEntity.setIfId(srcIfInfo.getIfId());

      FcInternalLinkIf leafInternalLinkIf = internalLinkMap.get(NodeType.LEAF);
      IfInfo dstIfInfo = getIfInfoFromInternalLinkIf(leafInternalLinkIf);
      DstEcEntity dstEntity = new DstEcEntity();
      dstEntity.setNodeId(dstIfInfo.getEcNodeId());
      dstEntity.setIfType(dstIfInfo.getIfType());
      dstEntity.setIfId(dstIfInfo.getIfId());

      PingTargetListEcEntity pingTargetListEntity = new PingTargetListEcEntity();
      pingTargetListEntity.setSrc(srcEntity);
      pingTargetListEntity.setDst(dstEntity);

      return pingTargetListEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private PingBetweenDevicesEcResponseBody sendPingBetweenDevices(PingBetweenDevicesEcRequestBody requestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "requestBody" }, new Object[] { requestBody });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(requestBody));
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PING_BETWEEN_DEVICES.getHttpMethod(),
          EcRequestUri.PING_BETWEEN_DEVICES.getUri(), requestBase, ecControlIpAddress, ecControlPort);

      PingBetweenDevicesEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          PingBetweenDevicesEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkResponseFromEc(restResponseBase, responseBody, HttpStatus.OK_200);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Map<NodeType, IfInfo>> getFailedResultIfInfoMapSet(SessionWrapper sessionWrapper,
      PingBetweenDevicesEcResponseBody responseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "responseBody" }, new Object[] { responseBody });
      Set<Map<NodeType, IfInfo>> failedResultIfInfoMapSet = new HashSet<>();
      for (PingTargetListResponseEcEntity entity : responseBody.getPingTargetList()) {

        if (EcPingBetweenDevicesResult.FAILED.getMessage().equals(entity.getResult())) {
          Map<NodeType, IfInfo> internalLinkMap = new HashMap<>();
          IfInfo spineIfInfo = new IfInfo(entity.getSrc().getNodeId(), entity.getSrc().getIfType(),
              entity.getSrc().getIfId(),
              getFcInternalLinkIf(sessionWrapper, Integer.valueOf(entity.getSrc().getNodeId()),
                  entity.getSrc().getIfType(), entity.getSrc().getIfId()));
          spineIfInfo.setMonitoringResultType(MonitoringResultType.FAILED);
          internalLinkMap.put(NodeType.SPINE, spineIfInfo);
          IfInfo leafIfInfo = new IfInfo(entity.getDst().getNodeId(), entity.getDst().getIfType(),
              entity.getDst().getIfId(),
              getFcInternalLinkIf(sessionWrapper, Integer.valueOf(entity.getDst().getNodeId()),
                  entity.getDst().getIfType(), entity.getDst().getIfId()));
          leafIfInfo.setMonitoringResultType(MonitoringResultType.FAILED);
          internalLinkMap.put(NodeType.LEAF, leafIfInfo);
          failedResultIfInfoMapSet.add(internalLinkMap);
        } else if (!EcPingBetweenDevicesResult.SUCCESS.getMessage().equals(entity.getResult())) {
          logger.warn(
              "Ping between devices result is not expected. spine node-id for ec = {0},"
                  + " leaf node-id for ec = {1}, result = {2}",
              entity.getSrc().getNodeId(), entity.getDst().getNodeId(), entity.getResult());
        }
      }
      return failedResultIfInfoMapSet;
    } finally {
      logger.methodEnd();
    }
  }
}
