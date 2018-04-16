
package msf.fc.core.log.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.rest.ec.log.data.LogReadEcResponseBody;
import msf.fc.rest.ec.log.data.entity.LogMessageEcEntity;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LogLevel;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.common.constant.MergeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.log.scenario.AbstractLogScenarioBase;
import msf.mfcfc.core.log.scenario.data.LogReadRequest;
import msf.mfcfc.core.log.scenario.data.entity.LogDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogEcLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogEmLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogFcLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMergedLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMsfLogEntity;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Log acquisition implementation class.
 *
 * @author NTT
 *
 */
public class FcLogReadScenario extends AbstractLogScenarioBase<LogReadRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLogReadScenario.class);

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
   */
  public FcLogReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      LogFcLogEntity fcLog = null;
      LogEcLogEntity ecLog = null;
      LogEmLogEntity emLog = null;
      LogMergedLogEntity mergedLog = null;

      if (request.getLogType() == null) {
        request.setLogType(LogType.PROCESSING.getMessage());
      }
      if (request.getLogLevel() == null) {
        request.setLogLevel(LogLevel.ERROR.getMessage());
      }

      controllerTypeList = getControllerEnumList(request.getController()).stream()
          .filter(controllerType -> !controllerType.equals(ControllerType.MFC)).collect(Collectors.toList());

      if (request.getCluster() == null) {
        request.setCluster(String.valueOf(clusterId));
      }
      String startDate;
      if (request.getStartDate() == null) {
        startDate = getDefaultDate();
      } else {
        startDate = request.getStartDate();
      }
      String endDate;
      if (request.getEndDate() == null) {
        endDate = getDefaultDate();
      } else {
        endDate = request.getEndDate();
      }
      String searchString;
      if (request.getSearchString() == null) {
        searchString = "";
      } else {
        searchString = request.getSearchString();
      }
      if (request.getMergeType() == null) {
        request.setMergeType(MergeType.SEPARATE.getMessage());
      }
      List<String> logLevels = getLogLevels(request.getLogLevel());

      if (LogType.API_ACCESS.getMessage().equals(request.getLogType())) {

        fcLog = readLog(request.getLogType(), logLevels, startDate, endDate, request.getLimitNumber(), searchString,
            ControllerType.FC.getMessage());
      } else {
        if (controllerTypeList.contains(ControllerType.EC) || controllerTypeList.contains(ControllerType.EM)) {
          LogReadEcResponseBody logReadEcResponseBody = sendLogRead(startDate, endDate);

          if (controllerTypeList.contains(ControllerType.EC)) {
            ecLog = getEcLogData(logReadEcResponseBody, logLevels, searchString);
          }

          if (controllerTypeList.contains(ControllerType.EM)) {
            emLog = getEmLogData(logReadEcResponseBody, logLevels, searchString);
          }
        }

        if (controllerTypeList.contains(ControllerType.FC)) {
          fcLog = readLog(request.getLogType(), logLevels, startDate, endDate, request.getLimitNumber(), searchString,
              ControllerType.FC.getMessage());
        }

        if (MergeType.MERGE.getMessage().equals(request.getMergeType())) {

          List<LogDataEntity> logList = getSortData(fcLog, ecLog, emLog);

          mergedLog = new LogMergedLogEntity();
          mergedLog.setLogDataList(getLogMergedData(logList, clusterId));
          mergedLog.setDataNumber(mergedLog.getLogDataList().size());
          Boolean overLimitNumber = false;
          if (request.getLimitNumber() != null) {
            if (logList.size() > request.getLimitNumber()) {
              overLimitNumber = true;
            }
          }
          mergedLog.setOverLimitNumber(overLimitNumber);
        }
      }

      LogMsfLogEntity logMsfLogEntity = getMsfLog(fcLog, ecLog, emLog, mergedLog, String.valueOf(clusterId));
      RestResponseBase responseBase = responseLogReadData(logMsfLogEntity);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(LogReadRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getLogType() != null) {
        ParameterCheckUtil.checkLogType(request.getLogType());
      }

      if (request.getLogLevel() != null) {
        ParameterCheckUtil.checkLogLevel(request.getLogLevel());
      }

      if (request.getController() != null) {
        ParameterCheckUtil.checkController(request.getController());
      }

      if (request.getMergeType() != null) {
        ParameterCheckUtil.checkMergeType(request.getMergeType());
      }

      if (request.getCluster() != null) {
        ParameterCheckUtil.checkClusterForFc(request.getCluster(),
            FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      }

      if (request.getStartDate() != null) {
        ParameterCheckUtil.checkDate(request.getStartDate());
      }

      if (request.getEndDate() != null) {
        ParameterCheckUtil.checkDate(request.getEndDate());
      }

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<LogDataEntity> getLowLog(List<LogMessageEcEntity> messages, String serverName, List<String> logLevels,
      String controllerType, String searchString) {
    List<LogDataEntity> lowLogList = new ArrayList<>();
    for (LogMessageEcEntity logs : messages) {
      LogDataEntity lowLogData = new LogDataEntity();
      String message = logs.getMessage();
      Matcher matcher = PATTERN.matcher(message);
      if (matcher.find()) {
        lowLogData = getLogData(matcher, serverName, controllerType);
        lowLogList.add(lowLogData);
      }
    }
    List<LogDataEntity> matchLowLogList = getMatchLogData(lowLogList, logLevels, searchString,
        request.getLimitNumber());
    return matchLowLogList;
  }

  private LogReadEcResponseBody sendLogRead(String startDate, String endDate) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();
      String ecController = getControllersStrings(controllerTypeList, ControllerType.FC);
      String targetUri = EcRequestUri.LOG_READ.getUri();
      Integer ecLimitNumber = request.getLimitNumber();
      if (ecLimitNumber == null) {
        ecLimitNumber = Integer.MAX_VALUE;
      } else if (ecLimitNumber < 1) {
        ecLimitNumber = 0;
      }

      targetUri = targetUri + "?controller=" + ecController + "&start_date=" + startDate + "&end_date=" + endDate
          + "&limit_number=" + String.valueOf(ecLimitNumber);
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LOG_READ.getHttpMethod(), targetUri, null,
          ecControlIpAddress, ecControlPort);

      LogReadEcResponseBody logReadEcResponseBody = new LogReadEcResponseBody();
      logReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), LogReadEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          logReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return logReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private LogEcLogEntity getEcLogData(LogReadEcResponseBody logReadEcResponseBody, List<String> logLevels,
      String searchString) {
    List<LogDataEntity> ecLogData = getLowLog(logReadEcResponseBody.getEcemLog().getEcLog().getLogDataList(),
        logReadEcResponseBody.getEcemLog().getEcLog().getServerName(), logLevels, ControllerType.EC.getMessage(),
        searchString);
    LogEcLogEntity ecLog = new LogEcLogEntity();
    ecLog.setLogDataList(ecLogData);
    ecLog.setDataNumber(ecLogData.size());
    ecLog.setOverLimitNumber(logReadEcResponseBody.getEcemLog().getEcLog().getOverLimitNumber());
    ecLog.setServerName(logReadEcResponseBody.getEcemLog().getEcLog().getServerName());
    return ecLog;
  }

  private LogEmLogEntity getEmLogData(LogReadEcResponseBody logReadEcResponseBody, List<String> logLevels,
      String searchString) {
    List<LogDataEntity> emLogData = getLowLog(logReadEcResponseBody.getEcemLog().getEmLog().getLogDataList(),
        logReadEcResponseBody.getEcemLog().getEmLog().getServerName(), logLevels, ControllerType.EM.getMessage(),
        searchString);
    LogEmLogEntity emLog = new LogEmLogEntity();
    emLog.setLogDataList(emLogData);
    emLog.setDataNumber(emLogData.size());
    emLog.setOverLimitNumber(logReadEcResponseBody.getEcemLog().getEmLog().getOverLimitNumber());
    emLog.setServerName(logReadEcResponseBody.getEcemLog().getEmLog().getServerName());
    return emLog;
  }

}
