
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.NodeOsUpgradeEcRequestBody;
import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.entity.NodeOsUpgradeEcEntity;
import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.entity.NodeOsUpgradeEquipmentEcEntity;
import msf.fc.services.nodeosupgrade.scenario.detour.FcNodeDetourUpdateProcess;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.operation.scenario.data.OperationNotifyRequestBody;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.nodeosupgrade.common.constant.EcRequestUri;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeDetourUpdateAction;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeStatus;
import msf.mfcfc.services.nodeosupgrade.common.constant.OsUpgradeResultType;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.entity.NodeDetourUpdateOptionEntity;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeAsyncResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeAsyncSubStatusResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeNotifyRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeEntity;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeListEntity;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeSubEntity;

/**
 * A reservation scheduler for the node OS upgrade.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeScheduler {
  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeScheduler.class);

  private boolean newExecutableFlag = true;

  private LinkedHashMap<String, Map<Date, FcNodeOsUpgradeSchedulerTask>> schedulerTasks = new LinkedHashMap<>();

  private List<NodeOsUpgradeListEntity> currentOsUpgradeListEntities = new ArrayList<>();

  private NodeOsUpgradeListEntity currentOsUpgradeListEntity = null;

  private Timer timer = null;

  private boolean isRunning = false;

  private FcAsyncRequest currentAsyncRequest = null;

  private LinkedHashMap<Integer, NodeOsUpgradeSubEntity> currentOsUpgradeSubEntities = new LinkedHashMap<>();

  private NodeOsUpgradeSubEntity currentOsUpgradeSubEntity = null;

  private void initializeSettings() {
    initializeSettings(true);
  }

  private void initializeSettings(boolean listClear) {

    if (listClear) {
      schedulerTasks.clear();
    }
    currentOsUpgradeListEntities = new ArrayList<>();
    currentOsUpgradeListEntity = null;
    timer = null;
    isRunning = false;
    currentAsyncRequest = null;
    currentOsUpgradeSubEntities = new LinkedHashMap<>();
    currentOsUpgradeSubEntity = null;
  }

  /**
   * Register the execution task with the reservation scheduler.
   *
   * @param fcNodeOsUpgradeSchedulerTask
   *          Task to execute
   * @param operationId
   *          Operation ID
   * @throws MsfException
   *           When an unexpected error occurs.
   */
  public void setNodeOsUpgradeTask(FcNodeOsUpgradeSchedulerTask fcNodeOsUpgradeSchedulerTask, String operationId)
      throws MsfException {
    try {
      logger.methodStart();

      if (newExecutableFlag) {
        if (!schedulerTasks.isEmpty()) {

          if (isRunning) {

            schedulerTasks.put(operationId, createReservationTimeTaskMap(fcNodeOsUpgradeSchedulerTask));
          } else {

            Map<Date, FcNodeOsUpgradeSchedulerTask> earliestTimeTaskMap = getEarliestReservationTimeTaskMap();

            Date existReservationTime = earliestTimeTaskMap.keySet().iterator().next();

            Map<Date, FcNodeOsUpgradeSchedulerTask> reservationTimeTaskMap = createReservationTimeTaskMap(
                fcNodeOsUpgradeSchedulerTask);

            Date reservationTime = reservationTimeTaskMap.keySet().iterator().next();

            schedulerTasks.put(operationId, reservationTimeTaskMap);

            if (existReservationTime.after(reservationTime)) {

              FcNodeOsUpgradeSchedulerTask existReservationTask = earliestTimeTaskMap.values().iterator().next();
              FcNodeOsUpgradeSchedulerTask existReservationSchedulerTask = new FcNodeOsUpgradeSchedulerTask(
                  existReservationTask.request, existReservationTask.requestBody, existReservationTask.operationId);

              timer.cancel();
              schedulerTasks.remove(existReservationTask.operationId);
              schedulerTasks.put(existReservationTask.operationId,
                  createReservationTimeTaskMap(existReservationSchedulerTask));
              setTimerScheduleReservationTask(reservationTimeTaskMap);
            }

          }
        } else {

          initializeSettings();

          Map<Date, FcNodeOsUpgradeSchedulerTask> reservationTimeTaskMap = createReservationTimeTaskMap(
              fcNodeOsUpgradeSchedulerTask);
          schedulerTasks.put(operationId, reservationTimeTaskMap);

          setTimerScheduleReservationTask(reservationTimeTaskMap);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void setTimerScheduleReservationTask(Map<Date, FcNodeOsUpgradeSchedulerTask> reservationTimeTaskMap)
      throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        FcNodeOsUpgradeSchedulerTask earliestReservationTimeTask = reservationTimeTaskMap.values().iterator().next();

        Date reservationTime = reservationTimeTaskMap.keySet().iterator().next();

        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
        FcAsyncRequest fcAsyncRequest = fcAsyncRequestsDao.read(sessionWrapper,
            earliestReservationTimeTask.operationId);

        setCurrentAsyncRequest(fcAsyncRequest);

        timer = new Timer(false);
        timer.schedule(earliestReservationTimeTask, reservationTime);

      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Map<Date, FcNodeOsUpgradeSchedulerTask> createReservationTimeTaskMap(
      FcNodeOsUpgradeSchedulerTask fcNodeOsUpgradeSchedulerTask) throws MsfException {
    try {
      logger.methodStart();
      Map<Date, FcNodeOsUpgradeSchedulerTask> schedulerTaskMap = new HashMap<>();
      schedulerTaskMap.put(getFormatReservationTime(fcNodeOsUpgradeSchedulerTask.requestBody.getReservationTime()),
          fcNodeOsUpgradeSchedulerTask);
      return schedulerTaskMap;
    } finally {
      logger.methodEnd();
    }
  }

  private Date getFormatReservationTime(String reservationTime) throws MsfException {
    try {
      logger.methodStart();
      return DateUtils.parseDate(reservationTime, new String[] { ParameterCheckUtil.DATE_FORMAT });
    } catch (ParseException parseException) {

      throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter. reservationTime = " + reservationTime);
    } finally {
      logger.methodEnd();
    }
  }

  private Map<Date, FcNodeOsUpgradeSchedulerTask> getEarliestReservationTimeTaskMap() throws MsfException {
    try {
      logger.methodStart();
      Map<Date, FcNodeOsUpgradeSchedulerTask> earliestSchedulerTaskMap = null;
      Date earliestReservationTime = null;
      for (Map<Date, FcNodeOsUpgradeSchedulerTask> schedulerTask : schedulerTasks.values()) {
        if (earliestReservationTime == null) {

          earliestSchedulerTaskMap = schedulerTask;
          earliestReservationTime = schedulerTask.keySet().iterator().next();
        } else {
          Date reservationTime = schedulerTask.keySet().iterator().next();
          if (earliestReservationTime.after(reservationTime)) {

            earliestSchedulerTaskMap = schedulerTask;
            earliestReservationTime = reservationTime;
          }
        }
      }
      return earliestSchedulerTaskMap;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set the state of the asynchronous request to "running" and set the start
   * time to the current time.
   *
   * @param currentOsUpgradeListEntities
   *          List of target device information in the request from the
   *          higher-level system for the device OS upgrade.
   * @throws MsfException
   *           When a DB access error occurs
   */
  public void updateAsyncRequest(List<NodeOsUpgradeListEntity> currentOsUpgradeListEntities) throws MsfException {
    try {
      logger.methodStart();

      isRunning = true;

      this.currentOsUpgradeListEntities = currentOsUpgradeListEntities;
      this.currentOsUpgradeListEntity = currentOsUpgradeListEntities.get(0);

      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        currentAsyncRequest.setStatusEnum(AsyncProcessStatus.RUNNING);
        currentAsyncRequest.setStartTime(new Timestamp(System.currentTimeMillis()));
        currentAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
        fcAsyncRequestsDao.update(sessionWrapper, currentAsyncRequest);

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * When the currently executing process of the node OS upgrade fails, this
   * process is executed.
   *
   * @param isCancel
   *          Cancel Flag
   * @param errorCode
   *          Error code
   * @param errorMessage
   *          Error detail message
   * @throws MsfException
   *           When a DB access error occurs, or an unexpected error occurs.
   */
  public void runningOperationFailureProcess(boolean isCancel, ErrorCode errorCode, String errorMessage)
      throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      boolean nodeOsUpgradeNotifyFlag = false;

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        if (isCancel) {
          currentAsyncRequest.setStatusEnum(AsyncProcessStatus.CANCELED);
        } else {
          currentAsyncRequest.setStatusEnum(AsyncProcessStatus.FAILED);
        }

        if (currentOsUpgradeSubEntity != null) {
          switch (currentOsUpgradeSubEntity.getStatusEnum()) {
            case WAITING:

              currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_NOT_OS_UPGRADED_NOT_DETOURED);
              break;

            case DETOURED:

              currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_NOT_OS_UPGRADED_DETOURED);
              break;

            case OS_UPGRADING:

              if (isCancel) {
                currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_UNKNOWN_DETOURED);
              } else {
                currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_NOT_OS_UPGRADED_DETOURED);
              }
              break;

            case RECOVERING:
              if (currentOsUpgradeSubEntity.getOperatorCheckWaiting()) {

                currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_UNKNOWN_DETOURED);
              } else {

                if (isCancel) {
                  currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_UNKNOWN_DETOURED);
                } else {
                  currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_NOT_RECOVERED);

                  nodeOsUpgradeNotifyFlag = true;
                }
              }
              break;

            case RECOVERED:

              currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_RECOVERED_DETOURED);
              break;

            default:

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Illegal state. currentNodeStatus = " + currentOsUpgradeSubEntity.getStatus());
          }

          setAsyncRequestResponseBody(errorCode, errorMessage);
        }

        updateCurrentAsyncRequest(sessionWrapper);

        for (String reserveOperationId : schedulerTasks.keySet()) {
          if (!reserveOperationId.equals(currentAsyncRequest.getOperationId())) {
            FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
            FcAsyncRequest fcAsyncRequest = fcAsyncRequestsDao.read(sessionWrapper, reserveOperationId);

            fcAsyncRequest.setStatusEnum(AsyncProcessStatus.CANCELED);
            fcAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
            Map<Date, FcNodeOsUpgradeSchedulerTask> reservationTimeTaskMap = schedulerTasks.get(reserveOperationId);

            FcNodeOsUpgradeSchedulerTask earliestReservationTimeTask = reservationTimeTaskMap.values().iterator()
                .next();
            setOsUpgradeSubEntitiesForFailed(earliestReservationTimeTask.requestBody, ErrorCode.OPERATION_CANCELED,
                ErrorCode.OPERATION_CANCELED.getMessage(), false, fcAsyncRequest);
            fcAsyncRequestsDao.update(sessionWrapper, fcAsyncRequest);
          }
        }

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

        for (String operationId : schedulerTasks.keySet()) {
          OperationManager.getInstance().releaseOperationId(operationId);
        }

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

        initializeSettings();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      try {
        if ((nodeOsUpgradeNotifyFlag) && (currentOsUpgradeListEntity.getOperatorCheck())) {

          notifyOperationResult(
              createNodeOsUpgradeNotifyBody(currentAsyncRequest.getCommonEntity(), OsUpgradeResultType.FAILED));
        }

        notifyOperationResult(createOperationNotifyBody(currentAsyncRequest.getCommonEntity()));

        sessionWrapper = new SessionWrapper();
        sessionWrapper.openSession();
        for (String reserveOperationId : schedulerTasks.keySet()) {
          if (!reserveOperationId.equals(currentAsyncRequest.getOperationId())) {
            FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
            FcAsyncRequest fcAsyncRequest = fcAsyncRequestsDao.read(sessionWrapper, reserveOperationId);

            notifyOperationResult(createOperationNotifyBody(fcAsyncRequest.getCommonEntity()));
          }
        }

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);

        initializeSettings();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      initializeSettings();

    } finally {
      logger.methodEnd();
    }

  }

  private void setAsyncRequestResponseBody(ErrorCode errorCode, String errorMessage) {
    try {
      logger.methodStart();

      if (errorCode != null) {
        currentAsyncRequest.setResponseStatusCode(errorCode.getStatusCode());
      } else {

        currentAsyncRequest.setResponseStatusCode(HttpStatus.OK_200);
      }
      NodeOsUpgradeAsyncResponseBody nodeOsUpgradeAsyncResponseBody = new NodeOsUpgradeAsyncResponseBody();
      int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      nodeOsUpgradeAsyncResponseBody.setClusterId(String.valueOf(swClusterId));
      List<NodeOsUpgradeEntity> nodeList = new ArrayList<>();
      for (NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity : currentOsUpgradeSubEntities.values()) {
        NodeOsUpgradeEntity nodeOsUpgradeEntity = new NodeOsUpgradeEntity();
        nodeOsUpgradeEntity.setFabricType(nodeOsUpgradeSubEntity.getFabricType());
        nodeOsUpgradeEntity.setNodeId(nodeOsUpgradeSubEntity.getNodeId());
        nodeOsUpgradeEntity.setEquipmentTypeId(nodeOsUpgradeSubEntity.getEquipmentTypeId());
        nodeOsUpgradeEntity.setStatus(nodeOsUpgradeSubEntity.getStatus());
        if (errorCode != null) {
          if (currentOsUpgradeSubEntity.getFabricType().equals(nodeOsUpgradeSubEntity.getFabricType())
              && currentOsUpgradeSubEntity.getNodeId().equals(nodeOsUpgradeSubEntity.getNodeId())) {
            nodeOsUpgradeEntity.setErrorCode(errorCode.getCode());
            nodeOsUpgradeEntity.setErrorMessage(errorMessage);
          }
        }
        nodeList.add(nodeOsUpgradeEntity);
      }
      nodeOsUpgradeAsyncResponseBody.setNodeList(nodeList);
      currentAsyncRequest.setResponseBody(JsonUtil.toJson(nodeOsUpgradeAsyncResponseBody));
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * When the node OS upgrade is completed, this process is executed.
   *
   * @throws MsfException
   *           DBアクセスエラー発生時
   */
  public void processingAfterRecoverNode() throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      NodeOsUpgradeSubEntity waitingOsUpgradeSubEntity = null;
      NodeOsUpgradeEcRequestBody createNodeOsUpgradeData = null;
      FcNode updateNode = null;

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.COMPLETED);
        currentOsUpgradeSubEntity.setOperatorCheckWaiting(false);

        for (NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity : currentOsUpgradeSubEntities.values()) {
          if (nodeOsUpgradeSubEntity.getStatusEnum().equals(NodeOsUpgradeStatus.WAITING)) {

            waitingOsUpgradeSubEntity = nodeOsUpgradeSubEntity;
            break;
          }
        }

        if (waitingOsUpgradeSubEntity == null) {

          currentAsyncRequest.setStatusEnum(AsyncProcessStatus.COMPLETED);

          setAsyncRequestResponseBody(null, null);

        } else {

          currentOsUpgradeSubEntity = waitingOsUpgradeSubEntity;
          for (NodeOsUpgradeListEntity nodeOsUpgradeListEntity : currentOsUpgradeListEntities) {

            if (currentOsUpgradeSubEntity.getFabricType().equals(nodeOsUpgradeListEntity.getFabricType())
                && currentOsUpgradeSubEntity.getNodeId().equals(nodeOsUpgradeListEntity.getNodeId())) {
              currentOsUpgradeListEntity = nodeOsUpgradeListEntity;
              break;
            }
          }

          nodeDetourProcess();

          currentOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.DETOURED);

          updateNode = getNode(sessionWrapper, currentOsUpgradeListEntity.getFabricTypeEnum(),
              Integer.valueOf(currentOsUpgradeListEntity.getNodeId()));

          createNodeOsUpgradeData = createNodeOsUpgradeData(updateNode, currentOsUpgradeListEntity);
        }

        updateCurrentAsyncRequest(sessionWrapper);

        if (createNodeOsUpgradeData != null) {

          sessionWrapper.commit();

          sendNodeOsUpgrade(updateNode, createNodeOsUpgradeData);

          updateRunningOperationStatus(NodeOsUpgradeStatus.OS_UPGRADING);
        } else {

          FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

          OperationManager.getInstance().releaseOperationId(currentAsyncRequest.getOperationId());

          sessionWrapper.commit();
        }
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      if (waitingOsUpgradeSubEntity == null) {

        try {

          notifyOperationResult(createOperationNotifyBody(currentAsyncRequest.getCommonEntity()));

          logger.performance("start wait for node os upgrade process.");
          synchronized (FcNodeOsUpgradeManager.getInstance().getFcNodeOsUpgradeLockObject()) {
            logger.performance("end wait for node os upgrade process.");

            schedulerTasks.remove(currentAsyncRequest.getOperationId());

            initializeSettings(false);

            if (newExecutableFlag) {
              Map<Date, FcNodeOsUpgradeSchedulerTask> earliestTimeTaskMap = getEarliestReservationTimeTaskMap();
              if (earliestTimeTaskMap != null) {
                setTimerScheduleReservationTask(earliestTimeTaskMap);
              }
            }

          }

        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);

          throw msfException;
        }

      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Executes the detour process for the node on which the OS upgrade is
   * currently being executed.
   *
   * @throws MsfException
   *           When a node detour internal processing fails.
   */
  public void nodeDetourProcess() throws MsfException {
    try {
      logger.methodStart();

      NodeDetourUpdateRequestBody nodeDetourUpdateRequestBody = new NodeDetourUpdateRequestBody();
      nodeDetourUpdateRequestBody.setActionEnum(NodeDetourUpdateAction.UPDATE);
      NodeDetourUpdateOptionEntity updateOption = new NodeDetourUpdateOptionEntity();

      updateOption.setDetoured(true);
      nodeDetourUpdateRequestBody.setUpdateOption(updateOption);

      NodeType detourNodeType = NodeType.getEnumFromSingularMessage(currentOsUpgradeSubEntity.getFabricType());
      String detourNodeId = currentOsUpgradeSubEntity.getNodeId();
      int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      FcNodeDetourUpdateProcess fcNodeDetourUpdateProcess = new FcNodeDetourUpdateProcess(String.valueOf(swClusterId),
          detourNodeType.getPluralMessage(), detourNodeId, nodeDetourUpdateRequestBody);
      fcNodeDetourUpdateProcess.nodeDetourUpdateProcess();

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the node information table record.
   *
   * @param sessionWrapper
   *          Session management instance
   * @param nodeType
   *          Node type
   * @param nodeId
   *          Acquisition target Node ID
   * @return Node information table record
   * @throws MsfException
   *           When a DB access error occurs
   */
  public FcNode getNode(SessionWrapper sessionWrapper, NodeType nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeType", "nodeId" }, new Object[] { nodeType, nodeId });
      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType.getCode(), nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, MessageFormat.format(
            "target resource is not found. nodeType = {0}, nodeId = {1}.", nodeType.getSingularMessage(), nodeId));
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Generate information to be transmitted to the EC when upgrading the node
   * OS.
   *
   * @param updateNode
   *          Node information record.
   * @param currentNodeOsUpgradeListEntity
   *          Node information in the node OS upgrade that is currently being
   *          executed.
   * @return Node addition with service restoration request body
   * @throws MsfException
   *           When an information acquisition error occurs.
   */
  public NodeOsUpgradeEcRequestBody createNodeOsUpgradeData(FcNode updateNode,
      NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "currentNodeOsUpgradeListEntity" },
          new Object[] { updateNode, currentNodeOsUpgradeListEntity });

      NodeOsUpgradeEcRequestBody nodeOsUpgradeEcRequestBody = new NodeOsUpgradeEcRequestBody();
      NodeOsUpgradeEquipmentEcEntity equipment = new NodeOsUpgradeEquipmentEcEntity();
      equipment.setEquipmentTypeId(currentNodeOsUpgradeListEntity.getEquipmentTypeId());
      nodeOsUpgradeEcRequestBody.setEquipment(equipment);

      NodeOsUpgradeEcEntity node = new NodeOsUpgradeEcEntity();
      switch (updateNode.getNodeTypeEnum()) {
        case LEAF:

          boolean isBorderLeaf = LeafType.getEnumFromCode(updateNode.getLeafNode().getLeafType())
              .equals(LeafType.BORDER_LEAF);
          node.setNodeType(isBorderLeaf ? InternalNodeType.B_LEAF.getMessage() : InternalNodeType.LEAF.getMessage());
          break;
        case SPINE:

          node.setNodeType(InternalNodeType.SPINE.getMessage());
          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter. nodeType = " + updateNode.getNodeType());
      }
      node.setUpgradeScriptPath(currentNodeOsUpgradeListEntity.getOsUpgrade().getUpgradeScriptPath());
      node.setZtpFlag(currentNodeOsUpgradeListEntity.getOsUpgrade().getZtpFlag());
      node.setUpgradeCompleteMsg(currentNodeOsUpgradeListEntity.getOsUpgrade().getUpgradeCompleteMsg());
      node.setUpgradeErrorMsgList(currentNodeOsUpgradeListEntity.getOsUpgrade().getUpgradeErrorMsgList());
      nodeOsUpgradeEcRequestBody.setNode(node);

      return nodeOsUpgradeEcRequestBody;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Send the node OS upgrade request to the EC.
   *
   * @param updateNode
   *          Target Node information
   * @param nodeOsUpgradeEcRequestBody
   *          Upgrade node information
   * @return Response information of Leaf node service restoration.
   * @throws MsfException
   *           When an unexpected error occurs in sending the request to the EC,
   *           or a connection error occurs.
   */
  public RestResponseBase sendNodeOsUpgrade(FcNode updateNode, NodeOsUpgradeEcRequestBody nodeOsUpgradeEcRequestBody)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "nodeOsUpgradeEcRequestBody" },
          new Object[] { updateNode, nodeOsUpgradeEcRequestBody });

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(nodeOsUpgradeEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.NODE_OS_UPGRADE.getHttpMethod(),
          EcRequestUri.NODE_OS_UPGRADE.getUri(String.valueOf(updateNode.getEcNodeId())), restRequest,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody recoverNodeEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = recoverNodeEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.ACCEPTED_202, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkRestResponseHttpStatusCode(int httpStatusCode, int expectHttpStatusCode, String errorCode,
      ErrorCode throwErrorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "httpStatusCode", "expectHttpStatusCode", "errorCode", "throwErrorCode" },
          new Object[] { httpStatusCode, expectHttpStatusCode, errorCode, throwErrorCode });

      if (httpStatusCode != expectHttpStatusCode) {

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}", httpStatusCode, errorCode);
        logger.error(errorMsg);
        throw new MsfException(throwErrorCode, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Procedure for the node OS upgrade completion notification.
   *
   * @param osUpgradeResult
   *          Result of the node OS upgrade completion notification.
   */
  public void notifyNodeOsUpgradeResult(OsUpgradeResultType osUpgradeResult) {
    try {
      logger.methodStart();

      notifyOperationResult(createNodeOsUpgradeNotifyBody(currentAsyncRequest.getCommonEntity(), osUpgradeResult));
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Status modification processing of the node OS upgrade currently being
   * executed.
   *
   * @param currentStatus
   *          Upgrade node status
   * @throws MsfException
   *           When a DB access error occurs
   */
  public void updateRunningOperationStatus(NodeOsUpgradeStatus currentStatus) throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        if (NodeOsUpgradeStatus.OPERATOR_CHECKING.equals(currentStatus)) {

          currentOsUpgradeSubEntity.setOperatorCheckWaiting(true);
        } else {
          currentOsUpgradeSubEntity.setStatusEnum(currentStatus);
        }

        updateCurrentAsyncRequest(sessionWrapper);

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void updateCurrentAsyncRequest(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      NodeOsUpgradeAsyncSubStatusResponseBody subStatusResponseBody = new NodeOsUpgradeAsyncSubStatusResponseBody();
      List<NodeOsUpgradeSubEntity> subEntities = new ArrayList<>();
      for (NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity : currentOsUpgradeSubEntities.values()) {
        subEntities.add(nodeOsUpgradeSubEntity);
      }
      int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      subStatusResponseBody.setClusterId(String.valueOf(swClusterId));
      subStatusResponseBody.setNodeList(subEntities);

      currentAsyncRequest.setSubStatus(JsonUtil.toJson(subStatusResponseBody));
      currentAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));

      FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
      fcAsyncRequestsDao.update(sessionWrapper, currentAsyncRequest);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * An unexecuted operation cancellation.
   *
   * @param fcAsyncRequest
   *          Information for asynchronous request to be canceled
   * @throws MsfException
   *           When a DB access error occurs
   */
  public void cancelReservationRequest(FcAsyncRequest fcAsyncRequest) throws MsfException {
    try {
      logger.methodStart();
      boolean isRunningTimerCancel = false;

      if (fcAsyncRequest.getOperationId().equals(currentAsyncRequest.getOperationId())) {

        timer.cancel();

        isRunningTimerCancel = true;
      }

      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        fcAsyncRequest.setStatusEnum(AsyncProcessStatus.CANCELED);
        fcAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
        Map<Date, FcNodeOsUpgradeSchedulerTask> reservationTimeTaskMap = schedulerTasks
            .get(fcAsyncRequest.getOperationId());

        FcNodeOsUpgradeSchedulerTask earliestReservationTimeTask = reservationTimeTaskMap.values().iterator().next();
        setOsUpgradeSubEntitiesForFailed(earliestReservationTimeTask.requestBody, ErrorCode.OPERATION_CANCELED,
            ErrorCode.OPERATION_CANCELED.getMessage(), isRunningTimerCancel, fcAsyncRequest);

        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
        fcAsyncRequestsDao.update(sessionWrapper, fcAsyncRequest);

        OperationManager.getInstance().releaseOperationId(fcAsyncRequest.getOperationId());

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      notifyOperationResult(createOperationNotifyBody(fcAsyncRequest.getCommonEntity()));

      schedulerTasks.remove(fcAsyncRequest.getOperationId());

      if (isRunningTimerCancel) {

        initializeSettings(false);

        Map<Date, FcNodeOsUpgradeSchedulerTask> earliestTimeTaskMap = getEarliestReservationTimeTaskMap();
        if (earliestTimeTaskMap != null) {
          setTimerScheduleReservationTask(earliestTimeTaskMap);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * A process that set the response information and sub status information for
   * the asynchronous request information about the unexecuted operation
   * cancellation or failure.
   *
   * @param requestBody
   *          Request information for the node OS upgrade
   * @param errorCode
   *          Error code
   * @param errorMessage
   *          Error Message
   * @param isCurrent
   *          When specifying the earliest reservation request
   * @param fcAsyncRequest
   *          Information of the asynchronous request.
   * @throws MsfException
   *           When an unexpected node type is specified.
   */
  public void setOsUpgradeSubEntitiesForFailed(NodeOsUpgradeRequestBody requestBody, ErrorCode errorCode,
      String errorMessage, boolean isCurrent, FcAsyncRequest fcAsyncRequest) throws MsfException {
    try {
      logger.methodStart();
      LinkedHashMap<Integer, NodeOsUpgradeSubEntity> createOsUpgradeSubEntities = new LinkedHashMap<>();

      List<NodeOsUpgradeEntity> currentOsUpgradeEntities = new ArrayList<>();
      for (NodeOsUpgradeListEntity nodeOsUpgradeListEntity : requestBody.getNodeList()) {

        NodeOsUpgradeEntity nodeOsUpgradeSubEntity = new NodeOsUpgradeEntity();
        nodeOsUpgradeSubEntity.setFabricType(nodeOsUpgradeListEntity.getFabricType());
        nodeOsUpgradeSubEntity.setNodeId(nodeOsUpgradeListEntity.getNodeId());
        nodeOsUpgradeSubEntity.setEquipmentTypeId(nodeOsUpgradeListEntity.getEquipmentTypeId());
        nodeOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.WAITING);
        nodeOsUpgradeSubEntity.setErrorCode(errorCode.getCode());
        nodeOsUpgradeSubEntity.setErrorMessage(errorMessage);
        currentOsUpgradeEntities.add(nodeOsUpgradeSubEntity);
      }

      for (NodeOsUpgradeEntity nodeOsUpgradeEntity : currentOsUpgradeEntities) {

        NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity = new NodeOsUpgradeSubEntity();
        nodeOsUpgradeSubEntity.setFabricType(nodeOsUpgradeEntity.getFabricType());
        nodeOsUpgradeSubEntity.setNodeId(nodeOsUpgradeEntity.getNodeId());
        nodeOsUpgradeSubEntity.setEquipmentTypeId(nodeOsUpgradeEntity.getEquipmentTypeId());
        nodeOsUpgradeSubEntity.setStatusEnum(nodeOsUpgradeEntity.getStatusEnum());
        nodeOsUpgradeSubEntity.setOperatorCheckWaiting(false);

        Integer ecNodeId = ParameterCheckUtil.getEcNodeId(Integer.valueOf(nodeOsUpgradeEntity.getNodeId()),
            NodeType.getEnumFromSingularMessage(nodeOsUpgradeEntity.getFabricType()));
        createOsUpgradeSubEntities.put(ecNodeId, nodeOsUpgradeSubEntity);
      }
      if (isCurrent) {
        this.currentOsUpgradeSubEntities = createOsUpgradeSubEntities;
      }

      fcAsyncRequest.setResponseStatusCode(errorCode.getStatusCode());
      NodeOsUpgradeAsyncResponseBody nodeOsUpgradeAsyncResponseBody = new NodeOsUpgradeAsyncResponseBody();
      int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      nodeOsUpgradeAsyncResponseBody.setClusterId(String.valueOf(swClusterId));
      nodeOsUpgradeAsyncResponseBody.setNodeList(currentOsUpgradeEntities);
      fcAsyncRequest.setResponseBody(JsonUtil.toJson(nodeOsUpgradeAsyncResponseBody));

      NodeOsUpgradeAsyncSubStatusResponseBody subStatusResponseBody = new NodeOsUpgradeAsyncSubStatusResponseBody();
      List<NodeOsUpgradeSubEntity> subEntities = new ArrayList<>();
      for (NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity : createOsUpgradeSubEntities.values()) {
        subEntities.add(nodeOsUpgradeSubEntity);
      }
      subStatusResponseBody.setClusterId(String.valueOf(swClusterId));
      subStatusResponseBody.setNodeList(subEntities);
      fcAsyncRequest.setSubStatus(JsonUtil.toJson(subStatusResponseBody));
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Using the EC device ID as a key, Get the the target node state from the
   * asynchronous response node state list of the node OS upgrade, that is
   * currently being executed.
   *
   * @param ecNodeId
   *          EC node ID
   * @return Node state of the target node ID
   */
  public Integer getTargetNodeStatus(String ecNodeId) {

    try {
      logger.methodStart();
      NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity = currentOsUpgradeSubEntities.get(Integer.valueOf(ecNodeId));
      if (nodeOsUpgradeSubEntity != null) {

        return nodeOsUpgradeSubEntity.getStatusEnum().getCode();
      } else {

        return null;
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the operator check state of the node OS upgrade currently being
   * executed.
   *
   * @return The operator check state of the node OS upgrade currently being
   *         executed.
   */
  public boolean isOperatorCheckWaiting() {
    try {
      logger.methodStart();
      if (currentOsUpgradeSubEntity == null) {

        return false;
      }
      return currentOsUpgradeSubEntity.getOperatorCheckWaiting();
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the asynchronous information of the node OS upgrade set in the
   * reservation scheduler.
   *
   * @return An asynchronous information for the node OS upgrade
   */
  public FcAsyncRequest getCurrentAsyncRequest() {
    try {
      logger.methodStart();
      return currentAsyncRequest;
    } finally {
      logger.methodEnd();
    }
  }

  private void setCurrentAsyncRequest(FcAsyncRequest fcAsyncRequest) {
    try {
      logger.methodStart();
      this.currentAsyncRequest = fcAsyncRequest;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the currently upgrade executing node information in the request from
   * the higher-level system.
   *
   * @return Target node information that was requested from the host system.
   */
  public NodeOsUpgradeListEntity getCurrentNodeOsUpgradeListEntity() {
    try {
      logger.methodStart();
      return currentOsUpgradeListEntity;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the asynchronous request information with the specified operation ID
   * from the reservation scheduler.
   *
   * @param operationId
   *          Operation ID
   * @return An asynchronous request information with the specified operation
   *         ID, managed by the reservation scheduler.
   * @throws MsfException
   *           When a DB access error occurs
   */
  public FcAsyncRequest getTargetAsyncRequest(String operationId) throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      if (!schedulerTasks.containsKey(operationId)) {

        logger.warn("taeget operation id is not node os upgrade reservation request.");
        return null;
      }

      try {
        sessionWrapper.openSession();
        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
        return fcAsyncRequestsDao.read(sessionWrapper, operationId);
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * [Common function]The reservation scheduler stop processing in controller
   * stop processing.
   *
   */
  public void stopNodeOsUpgradeScheduler() {
    try {
      logger.methodStart();

      logger.performance("start wait for node os upgrade process.");
      synchronized (FcNodeOsUpgradeManager.getInstance().getFcNodeOsUpgradeLockObject()) {
        logger.performance("end wait for node os upgrade process.");

        newExecutableFlag = false;

        if (currentAsyncRequest != null) {
          if (AsyncProcessStatus.WAITING.equals(currentAsyncRequest.getStatusEnum())) {

            timer.cancel();

            initializeSettings(false);
          }

        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set node status list for the node OS upgrade.
   *
   * @param currentOsUpgradeEntities
   *          Node status list of the node OS upgrade currently being executed.
   * @throws MsfException
   *           When there is no node or model information for the specified node
   *           in the request.
   *
   */
  public void setCurrentOsUpgradeSubEntities(List<NodeOsUpgradeEntity> currentOsUpgradeEntities) throws MsfException {
    try {
      logger.methodStart();
      LinkedHashMap<Integer, NodeOsUpgradeSubEntity> createOsUpgradeSubEntities = new LinkedHashMap<>();
      boolean isErrorOccurred = false;

      for (NodeOsUpgradeEntity nodeOsUpgradeEntity : currentOsUpgradeEntities) {

        NodeOsUpgradeSubEntity nodeOsUpgradeSubEntity = new NodeOsUpgradeSubEntity();
        nodeOsUpgradeSubEntity.setFabricType(nodeOsUpgradeEntity.getFabricType());
        nodeOsUpgradeSubEntity.setNodeId(nodeOsUpgradeEntity.getNodeId());
        nodeOsUpgradeSubEntity.setEquipmentTypeId(nodeOsUpgradeEntity.getEquipmentTypeId());
        nodeOsUpgradeSubEntity.setStatusEnum(nodeOsUpgradeEntity.getStatusEnum());
        nodeOsUpgradeSubEntity.setOperatorCheckWaiting(false);
        if (nodeOsUpgradeEntity.getErrorCode() != null) {
          isErrorOccurred = true;
        }

        Integer ecNodeId = ParameterCheckUtil.getEcNodeId(Integer.valueOf(nodeOsUpgradeEntity.getNodeId()),
            NodeType.getEnumFromSingularMessage(nodeOsUpgradeEntity.getFabricType()));
        createOsUpgradeSubEntities.put(ecNodeId, nodeOsUpgradeSubEntity);
      }
      this.currentOsUpgradeSubEntities = createOsUpgradeSubEntities;

      if (isErrorOccurred) {

        currentAsyncRequest.setResponseStatusCode(ErrorCode.TARGET_RESOURCE_NOT_FOUND.getStatusCode());
        NodeOsUpgradeAsyncResponseBody nodeOsUpgradeAsyncResponseBody = new NodeOsUpgradeAsyncResponseBody();
        int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
        nodeOsUpgradeAsyncResponseBody.setClusterId(String.valueOf(swClusterId));
        nodeOsUpgradeAsyncResponseBody.setNodeList(currentOsUpgradeEntities);
        currentAsyncRequest.setResponseBody(JsonUtil.toJson(nodeOsUpgradeAsyncResponseBody));

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "Temporary error message.");
      }

      this.currentOsUpgradeSubEntity = createOsUpgradeSubEntities.values().iterator().next();
    } finally {
      logger.methodEnd();
    }
  }

  private final SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

  private RestRequestBase createOperationNotifyBody(AsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart();
      OperationNotifyRequestBody body = new OperationNotifyRequestBody();
      body.setLastUpdateTime(writeDateFormat.format(asyncRequest.getLastUpdateTime()));
      body.setOccurredTime(writeDateFormat.format(asyncRequest.getOccurredTime()));
      if (asyncRequest.getReservationTime() != null) {
        body.setReservationTime(writeDateFormat.format(asyncRequest.getReservationTime()));
      }
      if (asyncRequest.getStartTime() != null) {
        body.setStartTime(writeDateFormat.format(asyncRequest.getStartTime()));
      }
      body.setOperationId(asyncRequest.getOperationId());
      body.setStatusEnum(asyncRequest.getStatusEnum());
      body.setSubStatus(asyncRequest.getSubStatus());

      String requestBody = asyncRequest.getRequestBody() == null ? "" : asyncRequest.getRequestBody();
      OperationRequestEntity request = new OperationRequestEntity(asyncRequest.getRequestUri(),
          asyncRequest.getRequestMethod(), requestBody);
      body.setRequest(request);

      String responseBody = asyncRequest.getResponseBody() == null ? "" : asyncRequest.getResponseBody();
      OperationResponseEntity response = new OperationResponseEntity(asyncRequest.getResponseStatusCode(),
          responseBody);
      body.setResponse(response);

      RestRequestBase restRequestBase = new RestRequestBase();
      restRequestBase.setRequestBody(JsonUtil.toJson(body));
      restRequestBase.setNotificationAddress(asyncRequest.getNotificationIpAddress());
      if (asyncRequest.getNotificationPortNumber() != null) {
        restRequestBase.setNotificationPort(String.valueOf(asyncRequest.getNotificationPortNumber()));
      }
      restRequestBase.setRequestMethodEnum(MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getHttpMethod());
      restRequestBase.setRequestUri(MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getUri(asyncRequest.getOperationId()));

      return restRequestBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestRequestBase createNodeOsUpgradeNotifyBody(AsyncRequest asyncRequest, OsUpgradeResultType resultType) {
    try {
      logger.methodStart();
      NodeOsUpgradeNotifyRequestBody body = new NodeOsUpgradeNotifyRequestBody();
      int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      body.setClusterId(String.valueOf(swClusterId));
      body.setFabricType(currentOsUpgradeSubEntity.getFabricType());
      body.setNodeId(currentOsUpgradeSubEntity.getNodeId());
      body.setOsUpgradeResultEnum(resultType);

      RestRequestBase restRequestBase = new RestRequestBase();
      restRequestBase.setRequestBody(JsonUtil.toJson(body));
      restRequestBase.setNotificationAddress(asyncRequest.getNotificationIpAddress());
      if (asyncRequest.getNotificationPortNumber() != null) {
        restRequestBase.setNotificationPort(String.valueOf(asyncRequest.getNotificationPortNumber()));
      }
      restRequestBase.setRequestMethodEnum(
          msf.mfcfc.services.nodeosupgrade.common.constant.MfcFcRequestUri.NOTIFY_NODE_OS_UPGRADE.getHttpMethod());
      restRequestBase.setRequestUri(
          msf.mfcfc.services.nodeosupgrade.common.constant.MfcFcRequestUri.NOTIFY_NODE_OS_UPGRADE.getUri());

      return restRequestBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void notifyOperationResult(RestRequestBase request) {
    try {
      logger.methodStart(new String[] { "request", "operationId" }, new Object[] { request });

      if (request.getNotificationAddress() == null || request.getNotificationPort() == null) {
        logger.debug("do not notify operation result because not specified ip address or port.");
        return;
      }

      int noticeRetryNum = ConfigManager.getInstance().getNoticeRetryNum();
      logger.debug("noticeRetryNum = " + noticeRetryNum);
      int retryNum = 0;
      while (true) {
        try {

          RestResponseBase response = RestClient.sendRequest(request.getRequestMethodEnum(), request.getRequestUri(),
              request, request.getNotificationAddress(), Integer.valueOf(request.getNotificationPort()));

          if (HttpStatus.isSuccess(response.getHttpStatusCode())) {
            break;
          } else {
            logger.debug("failed to send operation result notification. retry number =" + retryNum + " response code = "
                + response.getHttpStatusCode());
            retryNum++;
          }
        } catch (MsfException exp) {
          logger.debug("failed to send operation result notification. retry number =" + retryNum, exp);
          retryNum++;
        }

        if (retryNum > noticeRetryNum) {
          logger.warn("retry count to send reaches {0}.", noticeRetryNum);
          break;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

}
