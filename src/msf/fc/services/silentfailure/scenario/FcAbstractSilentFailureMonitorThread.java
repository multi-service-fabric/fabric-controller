
package msf.fc.services.silentfailure.scenario;

import static java.util.Comparator.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.node.FcNodeManager;
import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.status.data.InterfaceChangeStateEcRequestBody;
import msf.fc.services.silentfailure.FcSilentFailureManager;
import msf.fc.services.silentfailure.common.config.type.system.NoticeDestInfo;
import msf.fc.services.silentfailure.common.config.type.system.SilentFailure;
import msf.mfcfc.common.constant.BlockadeStatusType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceOperationStatus;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractProcessThread;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.silentfailure.common.constant.BlockadeResultType;
import msf.mfcfc.services.silentfailure.common.constant.DetectionTriggerType;
import msf.mfcfc.services.silentfailure.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.silentfailure.common.constant.MonitoringResultType;
import msf.mfcfc.services.silentfailure.scenario.data.SilentFailureCommonData;
import msf.mfcfc.services.silentfailure.scenario.data.SilentFaultNotificationUpdateRequestBody;
import msf.mfcfc.services.silentfailure.scenario.data.entity.FailedInternalLinkListEntity;
import msf.mfcfc.services.silentfailure.scenario.data.entity.NodeOptionListEntity;

/**
 * Abstract class to implement the common process of the monitoring in the
 * silent failure detection function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractSilentFailureMonitorThread extends AbstractProcessThread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractSilentFailureMonitorThread.class);
  protected SilentFailureCommonData silentFailureCommonData = SilentFailureCommonData.getInstance();

  protected Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> getMonitorInternalLinkIfs(
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      FcNodeDao nodeDao = new FcNodeDao();
      List<FcNode> nodeList = nodeDao.readList(sessionWrapper);

      Map<Long, Map<FcInternalLinkIf, InterfaceOperationStatus>> spineInternalLinkIfMap = new TreeMap<>();

      Map<Long, Map<FcInternalLinkIf, InterfaceOperationStatus>> leafInternalLinkIfMap = new TreeMap<>();

      FcInternalLinkIfDao internalLinkIfDao = new FcInternalLinkIfDao();

      for (FcNode node : nodeList) {

        List<FcInternalLinkIf> internalLinkIfList = internalLinkIfDao.readList(sessionWrapper, node.getNodeType(),
            node.getNodeId());

        InterfaceReadListEcResponseBody responseBody = sendInterfaceReadList(node.getEcNodeId());
        InterfacesEcEntity interfacesEcEntity = responseBody.getIfs();

        Map<FcInternalLinkIf, InterfaceOperationStatus> internalLinkIfStatusMap = getInternalLinkIfStatusMap(
            internalLinkIfList, interfacesEcEntity);

        if (NodeType.LEAF.equals(node.getNodeTypeEnum())) {
          leafInternalLinkIfMap.put(node.getNodeInfoId(), internalLinkIfStatusMap);
        } else if (NodeType.SPINE.equals(node.getNodeTypeEnum())) {
          spineInternalLinkIfMap.put(node.getNodeInfoId(), internalLinkIfStatusMap);
        }
      }

      return getInternalLinkStatusMap(spineInternalLinkIfMap, leafInternalLinkIfMap);
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean isAllInternalLinkIfsUp(
      Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap) {
    try {
      logger.methodStart(new String[] { "internalLinkStatusMap" }, new Object[] { internalLinkStatusMap });
      for (InterfaceOperationStatus internalLinkStatus : internalLinkStatusMap.values()) {
        if (!InterfaceOperationStatus.UP.equals(internalLinkStatus)) {
          return false;
        }
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private InterfaceReadListEcResponseBody sendInterfaceReadList(Integer ecNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecNodeId" }, new Object[] { ecNodeId });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.IF_READ_LIST.getHttpMethod(),
          EcRequestUri.IF_READ_LIST.getUri(String.valueOf(ecNodeId)), null, ecControlIpAddress, ecControlPort);

      InterfaceReadListEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          InterfaceReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkResponseFromEc(restResponseBase, responseBody, HttpStatus.OK_200);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected BlockadeResultType sendBlockInterface(String ecNodeId, String interfaceType, String ifId) {
    try {
      logger.methodStart(new String[] { "ecNodeId", "interfaceType", "ifId" },
          new Object[] { ecNodeId, interfaceType, ifId });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      InterfaceChangeStateEcRequestBody requestBody = new InterfaceChangeStateEcRequestBody();
      requestBody.setStatus(BlockadeStatusType.DOWN.getMessage());
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(requestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.CHANGE_IF_STATUS.getHttpMethod(),
          EcRequestUri.CHANGE_IF_STATUS.getUri(ecNodeId, interfaceType, ifId), requestBase, ecControlIpAddress,
          ecControlPort);

      ErrorInternalResponseBody responseBody = new ErrorInternalResponseBody();
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), ErrorInternalResponseBody.class,
            ErrorCode.EC_CONTROL_ERROR);
      }

      checkResponseFromEc(restResponseBase, responseBody, HttpStatus.OK_200);
      return BlockadeResultType.COMPLETED;
    } catch (MsfException exp) {
      String errorMsg = MessageFormat.format(
          "failed to block internal link if. ecNodeId = {0}, interfaceType = {1}, ifId = {2}", ecNodeId, interfaceType,
          ifId);
      logger.warn(errorMsg, exp);
      return BlockadeResultType.FAILED;
    } finally {
      logger.methodEnd();
    }
  }

  private Map<FcInternalLinkIf, InterfaceOperationStatus> getInternalLinkIfStatusMap(
      List<FcInternalLinkIf> internalLinkIfList, InterfacesEcEntity interfacesEcEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIfList", "interfacesEcEntity" },
          new Object[] { internalLinkIfList, interfacesEcEntity });
      Map<String, String> physicalIfStateMap = new HashMap<>();
      Map<String, String> lagIfStateMap = new HashMap<>();
      Map<String, String> breakoutIfStateMap = new HashMap<>();

      for (PhysicalIfEcEntity entity : interfacesEcEntity.getPhysicalIfList()) {
        physicalIfStateMap.put(entity.getPhysicalIfId(), entity.getIfState());
      }

      for (LagIfEcEntity entity : interfacesEcEntity.getLagIfList()) {
        lagIfStateMap.put(entity.getLagIfId(), entity.getIfState());
      }

      for (BreakoutIfEcEntity entity : interfacesEcEntity.getBreakoutIfList()) {
        breakoutIfStateMap.put(entity.getBreakoutIfId(), entity.getIfState());
      }

      Map<FcInternalLinkIf, InterfaceOperationStatus> internalLinkIfStatusMap = new HashMap<>();
      for (FcInternalLinkIf internalLinkIf : internalLinkIfList) {
        String ifState = null;
        if (internalLinkIf.getPhysicalIf() != null) {

          ifState = physicalIfStateMap.get(internalLinkIf.getPhysicalIf().getPhysicalIfId());
        } else if (internalLinkIf.getLagIf() != null) {

          ifState = lagIfStateMap.get(internalLinkIf.getLagIf().getLagIfId().toString());
        } else if (internalLinkIf.getBreakoutIf() != null) {

          ifState = breakoutIfStateMap.get(internalLinkIf.getBreakoutIf().getBreakoutIfId());
        }

        if (ifState != null) {
          internalLinkIfStatusMap.put(internalLinkIf, InterfaceOperationStatus.getEnumFromMessage(ifState));
        } else {

          String logMsg = MessageFormat.format("could not get internal link if state from ec. internalLinkIfId={0}",
              internalLinkIf.getInternalLinkIfId());
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }

      }

      return internalLinkIfStatusMap;
    } finally {
      logger.methodEnd();
    }
  }

  private Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> getInternalLinkStatusMap(
      Map<Long, Map<FcInternalLinkIf, InterfaceOperationStatus>> spineInternalLinkIfMap,
      Map<Long, Map<FcInternalLinkIf, InterfaceOperationStatus>> leafInternalLinkIfMap) throws MsfException {
    try {
      logger.methodStart(new String[] { "spineInternalLinkIfMap", "leafInternalLinkIfMap" },
          new Object[] { spineInternalLinkIfMap, leafInternalLinkIfMap });

      Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap = new HashMap<>();

      for (Map.Entry<Long, Map<FcInternalLinkIf, InterfaceOperationStatus>> spineIfMapEntry : spineInternalLinkIfMap
          .entrySet()) {
        Map<FcInternalLinkIf, InterfaceOperationStatus> spineIfStatusMap = spineIfMapEntry.getValue();

        for (Map.Entry<FcInternalLinkIf, InterfaceOperationStatus> spineIfStatusMapEntry : spineIfStatusMap
            .entrySet()) {

          FcInternalLinkIf oppositeInternalLinkIf = spineIfStatusMapEntry.getKey().getOppositeInternalLinkIfs().get(0);

          Long oppositeLeafNodeInfoId = getNodeFromInternalLinkIf(oppositeInternalLinkIf).getNodeInfoId();

          Map<FcInternalLinkIf, InterfaceOperationStatus> leafInternalLinkIfStatusMap = leafInternalLinkIfMap
              .get(oppositeLeafNodeInfoId);
          if (leafInternalLinkIfStatusMap != null) {
            InterfaceOperationStatus leafInternalLinkIfStatus = leafInternalLinkIfStatusMap.get(oppositeInternalLinkIf);

            if (leafInternalLinkIfStatus != null) {
              Map<NodeType, FcInternalLinkIf> internalLinkIfPairMap = new HashMap<>();
              internalLinkIfPairMap.put(NodeType.LEAF, oppositeInternalLinkIf);
              internalLinkIfPairMap.put(NodeType.SPINE, spineIfStatusMapEntry.getKey());

              if (InterfaceOperationStatus.UP.equals(spineIfStatusMapEntry.getValue())
                  && InterfaceOperationStatus.UP.equals(leafInternalLinkIfStatus)) {
                internalLinkStatusMap.put(internalLinkIfPairMap, InterfaceOperationStatus.UP);
              } else {
                internalLinkStatusMap.put(internalLinkIfPairMap, InterfaceOperationStatus.DOWN);
              }
            }
          }
        }
      }
      logger.debug(ReflectionToStringBuilder.reflectionToString(internalLinkStatusMap));
      return internalLinkStatusMap;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcNode getNodeFromInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      if (internalLinkIf.getPhysicalIf() != null) {
        return internalLinkIf.getPhysicalIf().getNode();
      } else if (internalLinkIf.getLagIf() != null) {
        return internalLinkIf.getLagIf().getNode();
      } else {
        return internalLinkIf.getBreakoutIf().getNode();
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected IfInfo getIfInfoFromInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      if (internalLinkIf.getPhysicalIf() != null) {
        return new IfInfo(internalLinkIf.getPhysicalIf().getNode().getEcNodeId().toString(),
            InterfaceType.PHYSICAL_IF.getMessage(), internalLinkIf.getPhysicalIf().getPhysicalIfId(), internalLinkIf);
      } else if (internalLinkIf.getLagIf() != null) {
        return new IfInfo(internalLinkIf.getLagIf().getNode().getEcNodeId().toString(),
            InterfaceType.LAG_IF.getMessage(), internalLinkIf.getLagIf().getLagIfId().toString(), internalLinkIf);
      } else {
        return new IfInfo(internalLinkIf.getBreakoutIf().getNode().getEcNodeId().toString(),
            InterfaceType.BREAKOUT_IF.getMessage(), internalLinkIf.getBreakoutIf().getBreakoutIfId(), internalLinkIf);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkResponseFromEc(RestResponseBase restResponseBase, AbstractInternalResponseBody responseBody,
      int httpStatusCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseBase", "responseBody", "httpStatusCode" },
          new Object[] { restResponseBase, responseBody, httpStatusCode });

      if (httpStatusCode != restResponseBase.getHttpStatusCode()) {

        String errorMsg = MessageFormat.format("HttpStatusCode={0}, ErrorCode={1}",
            restResponseBase.getHttpStatusCode(), responseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected SilentFaultNotificationUpdateRequestBody makeSilentFaultNotificationUpdateRequestBody(
      Set<Map<NodeType, FcInternalLinkIf>> internalLinkIfPairMapSet,
      Set<Map<NodeType, IfInfo>> notifyTargetIfInfoMapSet, DetectionTriggerType detectionTriggerType)
      throws MsfException {
    try {
      logger.methodStart(
          new String[] { "internalLinkIfPairMapSet", "notifyTargetIfInfoMapSet", "detectionTriggerType" },
          new Object[] { internalLinkIfPairMapSet, notifyTargetIfInfoMapSet, detectionTriggerType });
      SilentFaultNotificationUpdateRequestBody requestBody = new SilentFaultNotificationUpdateRequestBody();

      requestBody.setClusterId(
          String.valueOf(FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getSwClusterId()));

      requestBody.setDetectionTriggerEnum(detectionTriggerType);

      List<FailedInternalLinkListEntity> failedInternalLinkListEntityList = new ArrayList<>();
      for (Map<NodeType, IfInfo> notifyTargetIfInfoMap : notifyTargetIfInfoMapSet) {

        List<NodeOptionListEntity> nodeOptionListEntityList = new ArrayList<>();
        for (Map.Entry<NodeType, IfInfo> targetIfMapEntry : notifyTargetIfInfoMap.entrySet()) {
          NodeOptionListEntity nodeOptionListEntity = makeNodeOptionListEntity(targetIfMapEntry.getValue(),
              targetIfMapEntry.getKey());
          nodeOptionListEntityList.add(nodeOptionListEntity);
        }

        if (notifyTargetIfInfoMap.size() == 1) {
          IfInfo ifInfo = notifyTargetIfInfoMap.get(nodeOptionListEntityList.get(0).getFabricTypeEnum());
          for (Map<NodeType, FcInternalLinkIf> fcInternalLinkMap : internalLinkIfPairMapSet) {

            if (fcInternalLinkMap.containsValue(ifInfo.getInternalLinkIf())) {
              FcInternalLinkIf oppositeInternalLinkIf = ifInfo.getInternalLinkIf().getOppositeInternalLinkIfs().get(0);
              NodeOptionListEntity nodeOptionListEntity = makeNodeOptionListEntity(oppositeInternalLinkIf);
              nodeOptionListEntityList.add(nodeOptionListEntity);
            }
          }
        }

        nodeOptionListEntityList.sort(comparing(NodeOptionListEntity::getFabricType)
            .thenComparing(NodeOptionListEntity::getNodeId).thenComparing(NodeOptionListEntity::getInternalLinkIfId)
            .thenComparing(NodeOptionListEntity::getIfType).thenComparing(NodeOptionListEntity::getIfId));

        FailedInternalLinkListEntity failedInternalLinkListEntity = new FailedInternalLinkListEntity();
        failedInternalLinkListEntity.setNodeOptionList(nodeOptionListEntityList);
        failedInternalLinkListEntityList.add(failedInternalLinkListEntity);
      }

      requestBody.setFailedInternalLinkList(failedInternalLinkListEntityList);

      return requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeOptionListEntity makeNodeOptionListEntity(IfInfo ifInfo, NodeType nodeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "ifInfo", "nodeType" }, new Object[] { ifInfo, nodeType });
      NodeOptionListEntity entity = new NodeOptionListEntity();
      entity.setBlockadeResultEnum(ifInfo.getBlockadeResultType());
      entity.setMonitoringResultEnum(ifInfo.getMonitoringResultType());

      entity.setFabricTypeEnum(nodeType);
      entity.setIfId(ifInfo.getIfId());
      entity.setIfType(ifInfo.getIfType());

      entity.setInternalLinkIfId(ifInfo.getInternalLinkIf().getInternalLinkIfId().toString());
      entity.setNodeId(getNodeFromInternalLinkIf(ifInfo.getInternalLinkIf()).getNodeId().toString());

      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  private NodeOptionListEntity makeNodeOptionListEntity(FcInternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      NodeOptionListEntity entity = new NodeOptionListEntity();

      if (FcSilentFailureManager.getInstance().getSystemConfData().getSilentFailure().isEnableExecutingIfBlockade()) {
        entity.setBlockadeResultEnum(BlockadeResultType.NONE);
      } else {
        entity.setBlockadeResult(null);
      }

      entity.setMonitoringResultEnum(MonitoringResultType.SUCCEEDED);

      FcNode node = getNodeFromInternalLinkIf(internalLinkIf);
      IfInfo ifInfo = getIfInfoFromInternalLinkIf(internalLinkIf);

      entity.setFabricTypeEnum(node.getNodeTypeEnum());
      entity.setIfId(ifInfo.getIfId());
      entity.setIfType(ifInfo.getIfType());

      entity.setInternalLinkIfId(internalLinkIf.getInternalLinkIfId().toString());
      entity.setNodeId(node.getNodeId().toString());

      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void executeBlockingInterfaces(SessionWrapper sessionWrapper, Map<NodeType, IfInfo> targetIfToBlock)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "targetIfToBlock" }, new Object[] { targetIfToBlock });
      synchronized (FcNodeManager.getInstance().getFcIfStateChangeLockObject()) {
        Boolean isAllInternalLinkIfsUp = null;
        try {

          Map<Map<NodeType, FcInternalLinkIf>, InterfaceOperationStatus> internalLinkStatusMap = getMonitorInternalLinkIfs(
              sessionWrapper);
          isAllInternalLinkIfsUp = isAllInternalLinkIfsUp(internalLinkStatusMap);
        } catch (MsfException exp) {

          logger.debug("do not block interface because internal link interfaces state check processing failed.");
          return;
        }

        if (isAllInternalLinkIfsUp) {

          for (Map.Entry<NodeType, IfInfo> targetIfToBlockEntry : targetIfToBlock.entrySet()) {
            IfInfo targetIfInfo = targetIfToBlock.get(targetIfToBlockEntry.getKey());
            BlockadeResultType blockadeResult = sendBlockInterface(targetIfInfo.getEcNodeId(),
                targetIfInfo.getIfTypeEnum().getPluralMessage(), targetIfInfo.getIfId());
            targetIfInfo.setBlockadeResultType(blockadeResult);
          }
        } else {
          logger.debug("do not block interface because all internal link interfaces are not linked up.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void setBlockadeStatusToNone(Set<Map<NodeType, IfInfo>> ifInfoMapSet) {

    for (Map<NodeType, IfInfo> ifInfoMap : ifInfoMapSet) {
      for (IfInfo ifInfo : ifInfoMap.values()) {
        if (ifInfo.getBlockadeResultType() == null) {
          ifInfo.setBlockadeResultType(BlockadeResultType.NONE);
        }
      }
    }
  }

  protected void notifySilentFailureAll(SilentFaultNotificationUpdateRequestBody requestBody) {
    try {
      logger.methodStart();

      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(requestBody));

      SilentFailure silentFailureConf = FcSilentFailureManager.getInstance().getSystemConfData().getSilentFailure();

      List<NoticeDestInfo> noticeDestInfoList = silentFailureConf.getNoticeDestInfo();

      for (NoticeDestInfo noticeDestInfo : noticeDestInfoList) {
        notifySilentFailure(requestBase, noticeDestInfo.getNoticeAddress(), noticeDestInfo.getNoticePort(),
            silentFailureConf.getNoticeTimeout(), silentFailureConf.getNoticeRetryNum());
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void notifySilentFailure(RestRequestBase request, String ipAddress, int port, int timeout, int retryNum) {

    try {
      logger.methodStart(new String[] { "request", "ipAddress", "port", "timeout", "retryNum" },
          new Object[] { request, ipAddress, port, timeout, retryNum });

      for (int cnt = 0; cnt <= retryNum; cnt++) {
        try {
          RestClient.sendRequest(MfcFcRequestUri.SILENT_FAULT_NOTIFY.getHttpMethod(),
              MfcFcRequestUri.SILENT_FAULT_NOTIFY.getUri(), request, ipAddress, port);
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

  protected FcInternalLinkIf getFcInternalLinkIf(SessionWrapper sessionWrapper, int ecNodeId, String ifType,
      String ifId) throws MsfException {
    try {
      logger.methodStart(new String[] { "ecNodeId", "ifType", "ifId" }, new Object[] { ecNodeId, ifType, ifId });
      FcInternalLinkIfDao internalLinkIfDao = new FcInternalLinkIfDao();
      FcInternalLinkIf internalLinkIf = null;
      if (ifType.equals(InterfaceType.PHYSICAL_IF.getMessage())) {
        internalLinkIf = internalLinkIfDao.readByPhysicalIfId(sessionWrapper, ecNodeId, ifId);
      } else if (ifType.equals(InterfaceType.LAG_IF.getMessage())) {
        internalLinkIf = internalLinkIfDao.readByLagIfId(sessionWrapper, ecNodeId, ifId);
      } else if (ifType.equals(InterfaceType.BREAKOUT_IF.getMessage())) {
        internalLinkIf = internalLinkIfDao.readByBreakoutIfId(sessionWrapper, ecNodeId, ifId);
      }

      if (internalLinkIf == null) {
        String logMsg = MessageFormat.format(
            "internal link if does not exist. ecNodeId = {0}, ifType = {1}, ifId = {2}", ecNodeId, ifType, ifId);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      return internalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected class IfInfo {

    private String ecNodeId;

    private String ifType;

    private String ifId;

    private FcInternalLinkIf internalLinkIf = null;

    private MonitoringResultType monitoringResultType = null;

    private BlockadeResultType blockadeResultType = null;

    /**
     * Constructor.
     *
     * @param ecNodeId
     *          EC Node ID
     * @param ifType
     *          IF type
     * @param ifId
     *          IF ID
     * @param internalLinkIf
     *          Internal link IF information
     */
    public IfInfo(String ecNodeId, String ifType, String ifId, FcInternalLinkIf internalLinkIf) {
      this.ecNodeId = ecNodeId;
      this.ifType = ifType;
      this.ifId = ifId;
      this.internalLinkIf = internalLinkIf;
    }

    public String getEcNodeId() {
      return ecNodeId;
    }

    public String getIfType() {
      return ifType;
    }

    public String getIfId() {
      return ifId;
    }

    public FcInternalLinkIf getInternalLinkIf() {
      return internalLinkIf;
    }

    public void setInternalLinkIf(FcInternalLinkIf internalLinkIf) {
      this.internalLinkIf = internalLinkIf;
    }

    public MonitoringResultType getMonitoringResultType() {
      return monitoringResultType;
    }

    public void setMonitoringResultType(MonitoringResultType monitoringResultType) {
      this.monitoringResultType = monitoringResultType;
    }

    public BlockadeResultType getBlockadeResultType() {
      return blockadeResultType;
    }

    public void setBlockadeResultType(BlockadeResultType blockadeResultType) {
      this.blockadeResultType = blockadeResultType;
    }

    public InterfaceType getIfTypeEnum() {
      return InterfaceType.getEnumFromMessage(ifType);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((ecNodeId == null) ? 0 : ecNodeId.hashCode());
      result = prime * result + ((ifId == null) ? 0 : ifId.hashCode());
      result = prime * result + ((ifType == null) ? 0 : ifType.hashCode());
      result = prime * result + ((internalLinkIf == null) ? 0 : internalLinkIf.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof IfInfo)) {
        return false;
      }
      IfInfo other = (IfInfo) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }
      if (ecNodeId == null) {
        if (other.ecNodeId != null) {
          return false;
        }
      } else if (!ecNodeId.equals(other.ecNodeId)) {
        return false;
      }
      if (ifId == null) {
        if (other.ifId != null) {
          return false;
        }
      } else if (!ifId.equals(other.ifId)) {
        return false;
      }
      if (ifType == null) {
        if (other.ifType != null) {
          return false;
        }
      } else if (!ifType.equals(other.ifType)) {
        return false;
      }
      if (internalLinkIf == null) {
        if (other.internalLinkIf != null) {
          return false;
        }
      } else if (!internalLinkIf.equals(other.internalLinkIf)) {
        return false;
      }
      return true;
    }

    private FcAbstractSilentFailureMonitorThread getOuterType() {
      return FcAbstractSilentFailureMonitorThread.this;
    }

  }
}
