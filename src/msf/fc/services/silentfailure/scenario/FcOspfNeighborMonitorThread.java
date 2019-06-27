
package msf.fc.services.silentfailure.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcNode;
import msf.fc.services.silentfailure.FcSilentFailureManager;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.OspfNeighborReadEcRequestBody;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.OspfNeighborReadEcResponseBody;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborIfListEcEntity;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborIfListResponseEcEntity;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborListEcEntity;
import msf.fc.services.silentfailure.rest.ec.ospfneighbor.data.entity.OspfNeighborListResponseEcEntity;
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
import msf.mfcfc.services.silentfailure.common.constant.EcOspfNeighborStatus;
import msf.mfcfc.services.silentfailure.common.constant.EcRequestUri;
import msf.mfcfc.services.silentfailure.common.constant.MonitoringResultType;
import msf.mfcfc.services.silentfailure.scenario.data.SilentFaultNotificationUpdateRequestBody;

/**
 * Main class for the OSPF neighbor monitoring. To be executed periodically from
 * the CycleTimer class.
 *
 * @author NTT
 *
 */
public class FcOspfNeighborMonitorThread extends FcAbstractSilentFailureMonitorThread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcOspfNeighborMonitorThread.class);

  /**
   * Execution of the OSPF neighbor monitoring.
   *
   * @throws MsfException
   *           If DB access error occurs, or sending notification fails.
   */
  public void executeOspfNeighborMonitor() throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap = getMonitorInternalLinkIfs(
            sessionWrapper);

        OspfNeighborReadEcRequestBody requestBody = makeOspfNeighborReadEcRequestBody(internalLinkStatusMap);

        if (!requestBody.getOspfNeighbors().isEmpty()) {

          OspfNeighborReadEcResponseBody responseBody = sendOspfNeighborRead(requestBody);

          Set<Map<NodeType, IfInfo>> linkNgIfInfoMapSet = getLinkNgIfInfoMapSet(sessionWrapper, responseBody,
              internalLinkStatusMap.keySet());

          if (!linkNgIfInfoMapSet.isEmpty()) {

            boolean enableExecutingIfBlockade = FcSilentFailureManager.getInstance().getSystemConfData()
                .getSilentFailure().isEnableExecutingIfBlockade();

            if (enableExecutingIfBlockade) {
              if (linkNgIfInfoMapSet.size() == 1) {

                executeBlockingInterfaces(sessionWrapper, linkNgIfInfoMapSet.iterator().next());
              }

              setBlockadeStatusToNone(linkNgIfInfoMapSet);
            }

            SilentFaultNotificationUpdateRequestBody notifyRequestBody = makeSilentFaultNotificationUpdateRequestBody(
                internalLinkStatusMap.keySet(), linkNgIfInfoMapSet, DetectionTriggerType.OSPFNBR);

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

          silentFailureCommonData.checkForceStopOspfNeighborMonitor();

          lock(silentFailureCommonData.isForceStopOspfNeighborMonitor());

          silentFailureCommonData.checkForceStopOspfNeighborMonitor();

          executeOspfNeighborMonitor();
        } catch (InterruptedException ie) {
          if (silentFailureCommonData.isForceStopOspfNeighborMonitor()) {
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

  private OspfNeighborReadEcRequestBody makeOspfNeighborReadEcRequestBody(
      Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkStatusMap" }, new Object[] { internalLinkStatusMap });

      OspfNeighborReadEcRequestBody requestBody = new OspfNeighborReadEcRequestBody();
      requestBody.setOspfNeighbors(makeOspfNeighborListEcEntityList(internalLinkStatusMap));

      return requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<OspfNeighborListEcEntity> makeOspfNeighborListEcEntityList(
      Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkStatusMap" }, new Object[] { internalLinkStatusMap });

      Map<Long, OspfNeighborListEcEntity> ospfNeighborListEcEntityTmpMap = new HashMap<>();
      for (Map.Entry<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> statusMapEntry : internalLinkStatusMap
          .entrySet()) {

        if (InterfaceOperationStatus.UP.equals(statusMapEntry.getValue())) {
          for (FcInternalLinkIf internalLinkIf : statusMapEntry.getKey().values()) {
            FcNode node = getNodeFromInternalLinkIf(internalLinkIf);

            OspfNeighborListEcEntity ospfNeighborListEcEntity = null;
            if (ospfNeighborListEcEntityTmpMap.containsKey(node.getNodeInfoId())) {

              ospfNeighborListEcEntity = ospfNeighborListEcEntityTmpMap.get(node.getNodeInfoId());
            } else {

              ospfNeighborListEcEntity = new OspfNeighborListEcEntity();
              ospfNeighborListEcEntity.setNodeId(node.getEcNodeId().toString());
              ospfNeighborListEcEntity.setOspfNeighborIfList(new ArrayList<>());
              ospfNeighborListEcEntityTmpMap.put(node.getNodeInfoId(), ospfNeighborListEcEntity);
            }

            OspfNeighborIfListEcEntity ospfNeighborIfListEcEntity = makeOspfNeighborIfListEcEntity(
                internalLinkIf.getOppositeInternalLinkIfs().get(0));

            ospfNeighborListEcEntity.getOspfNeighborIfList().add(ospfNeighborIfListEcEntity);
          }
        }
      }

      List<OspfNeighborListEcEntity> ospfNeighborList = new ArrayList<>(ospfNeighborListEcEntityTmpMap.values());
      return ospfNeighborList;
    } finally {
      logger.methodEnd();
    }
  }

  private OspfNeighborIfListEcEntity makeOspfNeighborIfListEcEntity(FcInternalLinkIf internalLinkIf)
      throws MsfException {
    try {
      logger.methodStart();
      OspfNeighborIfListEcEntity ospfNeighborIfListEcEntity = new OspfNeighborIfListEcEntity();

      IfInfo ifInfo = getIfInfoFromInternalLinkIf(internalLinkIf);
      ospfNeighborIfListEcEntity.setNodeId(ifInfo.getEcNodeId());
      ospfNeighborIfListEcEntity.setIfType(ifInfo.getIfType());
      ospfNeighborIfListEcEntity.setIfId(ifInfo.getIfId());
      return ospfNeighborIfListEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private OspfNeighborReadEcResponseBody sendOspfNeighborRead(OspfNeighborReadEcRequestBody requestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "requestBody" }, new Object[] { requestBody });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(requestBody));
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.OSPF_NEIGHBOR_READ.getHttpMethod(),
          EcRequestUri.OSPF_NEIGHBOR_READ.getUri(), requestBase, ecControlIpAddress, ecControlPort);

      OspfNeighborReadEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          OspfNeighborReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkResponseFromEc(restResponseBase, responseBody, HttpStatus.OK_200);

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Map<NodeType, IfInfo>> getLinkNgIfInfoMapSet(SessionWrapper sessionWrapper,
      OspfNeighborReadEcResponseBody responseBody, Set<Map<NodeType, FcInternalLinkIf>> internalLinkIfPairMapSet)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "responseBody" }, new Object[] { responseBody });
      Set<Map<NodeType, IfInfo>> linkNgIfInfoMapSet = new HashSet<>();

      Map<FcInternalLinkIf, IfInfo> linkNgIfInfoMap = new LinkedHashMap<>();
      for (OspfNeighborListResponseEcEntity entity : responseBody.getOspfNeighbors()) {
        for (OspfNeighborIfListResponseEcEntity ifEntity : entity.getOspfNeighborIfList()) {

          if (EcOspfNeighborStatus.DOWN.getMessage().equals(ifEntity.getStatus())) {
            IfInfo ifInfo = new IfInfo(ifEntity.getNodeId(), ifEntity.getIfType(), ifEntity.getIfId(),
                getFcInternalLinkIf(sessionWrapper, Integer.valueOf(ifEntity.getNodeId()), ifEntity.getIfType(),
                    ifEntity.getIfId()));
            ifInfo.setMonitoringResultType(MonitoringResultType.FAILED);
            linkNgIfInfoMap.put(ifInfo.getInternalLinkIf(), ifInfo);
          } else if (!EcOspfNeighborStatus.UP.getMessage().equals(ifEntity.getStatus())) {
            logger.warn(
                "Ospf neighbor status is not expected. node-id for ec = {0},"
                    + "opposite node-id for ec = {1}, status = {2}",
                entity.getNodeId(), ifEntity.getNodeId(), ifEntity.getStatus());
          }
        }
      }

      while (linkNgIfInfoMap.size() > 0) {

        IfInfo ifInfo = linkNgIfInfoMap.values().iterator().next();
        linkNgIfInfoMap.remove(ifInfo.getInternalLinkIf());

        Map<NodeType, IfInfo> ifInfoMap = new HashMap<>();
        ifInfoMap.put(getNodeFromInternalLinkIf(ifInfo.getInternalLinkIf()).getNodeTypeEnum(), ifInfo);
        linkNgIfInfoMapSet.add(ifInfoMap);

        FcInternalLinkIf searchTargetInternalLinkIf = ifInfo.getInternalLinkIf().getOppositeInternalLinkIfs().get(0);

        IfInfo oppositeIfInfo = linkNgIfInfoMap.get(searchTargetInternalLinkIf);
        if (oppositeIfInfo != null) {
          ifInfoMap.put(getNodeFromInternalLinkIf(searchTargetInternalLinkIf).getNodeTypeEnum(), oppositeIfInfo);
          linkNgIfInfoMap.remove(oppositeIfInfo.getInternalLinkIf());
        }
      }

      return linkNgIfInfoMapSet;
    } finally {
      logger.methodEnd();
    }
  }

}
