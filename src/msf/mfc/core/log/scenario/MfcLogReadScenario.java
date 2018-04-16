
package msf.mfc.core.log.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.util.UrlEncoded;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LogLevel;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.common.constant.MergeType;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.log.scenario.AbstractLogScenarioBase;
import msf.mfcfc.core.log.scenario.data.LogReadRequest;
import msf.mfcfc.core.log.scenario.data.LogReadResponseBody;
import msf.mfcfc.core.log.scenario.data.entity.LogClusterLogsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogConditionsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMergedDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMergedLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMfcLogsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMsfLogEntity;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Log acquisition implementation class.
 *
 * @author NTT
 *
 */
public class MfcLogReadScenario extends AbstractLogScenarioBase<LogReadRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcLogReadScenario.class);

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
  public MfcLogReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      LogMfcLogsEntity mfcLog = null;
      LogMergedLogEntity mergedLog = null;

      List<RestResponseData> restResponseDataList = null;

      if (request.getLogType() == null) {
        request.setLogType(LogType.PROCESSING.getMessage());
      }
      if (request.getLogLevel() == null) {
        request.setLogLevel(LogLevel.ERROR.getMessage());
      }

      controllerTypeList = getControllerEnumList(request.getController());

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

        mfcLog = readMfcLog(request.getLogType(), logLevels, startDate, endDate, request.getLimitNumber(), searchString,
            ControllerType.MFC.getMessage());
      } else {

        if (controllerTypeList.contains(ControllerType.MFC)) {

          mfcLog = readMfcLog(request.getLogType(), logLevels, startDate, endDate, request.getLimitNumber(),
              searchString, ControllerType.MFC.getMessage());

        }

        if (!(controllerTypeList.stream().allMatch(controllerType -> controllerType.equals(ControllerType.MFC)))) {

          SessionWrapper session = new SessionWrapper();

          try {

            session.openSession();

            List<MfcSwCluster> swClusterList = getMfcSwCluster(session, request.getCluster());

            request.setCluster(getClusterIdStr(swClusterList));

            restResponseDataList = sendControllerLogRead(swClusterList, startDate, endDate);

            if (!checkResponseAllSuccess(restResponseDataList)) {

              return createErrorResponse(restResponseDataList, null);

            }

          } catch (MsfException msfException) {

            logger.error(msfException.getMessage(), msfException);
            throw msfException;

          } finally {
            session.closeSession();

          }
        } else {

          request.setCluster(null);
        }

        if (MergeType.MERGE.getMessage().equals(request.getMergeType())) {

          mergedLog = new LogMergedLogEntity();

          List<LogMergedDataEntity> logDataList = new ArrayList<LogMergedDataEntity>();

          if (restResponseDataList != null) {

            logDataList.addAll(getLogMergedData(restResponseDataList));

          }

          if (mfcLog != null) {

            logDataList.addAll(getLogMergedData(mfcLog.getLogDataList(), (Integer) null, false));

          }

          Comparator<LogMergedDataEntity> dateComparator = Comparator.comparing(d -> d.getOccurredTime());
          logDataList.sort(dateComparator.thenComparing(COMPARATOR_LOGDATA_LOGCONTROLLER)
              .thenComparing(COMPARATOR_LOGDATA_CLUSTERID));

          if (request.getLimitNumber() != null) {

            mergedLog.setOverLimitNumber(logDataList.size() > request.getLimitNumber());

            if (request.getLimitNumber() < 1) {

              mergedLog.setLogDataList(new ArrayList<>());
            } else {
              mergedLog
                  .setLogDataList(logDataList.stream().limit(request.getLimitNumber()).collect(Collectors.toList()));
            }
          } else {

            mergedLog.setOverLimitNumber(false);
            mergedLog.setLogDataList(logDataList);
          }

          mergedLog.setDataNumber(mergedLog.getLogDataList().size());

          restResponseDataList = null;
          mfcLog = null;
        }
      }

      LogMsfLogEntity logMsfLogEntity = getMsfLog(mfcLog, restResponseDataList, mergedLog);
      RestResponseBase responseBase = responseLogReadData(logMsfLogEntity);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private List<LogMergedDataEntity> getLogMergedData(List<RestResponseData> restResponseDataList) throws MsfException {

    List<LogMergedDataEntity> resultList = new ArrayList<LogMergedDataEntity>();

    for (RestResponseData temp : restResponseDataList) {

      LogReadResponseBody body = JsonUtil.fromJson(temp.getResponse().getResponseBody(), LogReadResponseBody.class,
          ErrorCode.UNDEFINED_ERROR);

      List<LogClusterLogsEntity> clusterLogList = body.getMsfLog().getClusterLogList();

      for (LogClusterLogsEntity logClusterLogsEntity : clusterLogList) {

        List<LogDataEntity> logList = getSortData(logClusterLogsEntity.getFcLog(), logClusterLogsEntity.getEcLog(),
            logClusterLogsEntity.getEmLog());

        resultList.addAll(getLogMergedData(logList, Integer.parseInt(logClusterLogsEntity.getClusterId()), false));
      }

    }

    return resultList;

  }

  @Override
  protected void checkParameter(LogReadRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request.toString() });

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
        ParameterCheckUtil.checkCluster(request.getCluster());
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

  private List<MfcSwCluster> getMfcSwCluster(SessionWrapper session, String clusters) throws MsfException {

    try {

      logger.methodStart();
      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

      if (clusters == null) {

        return mfcSwClusterDao.readList(session);

      }

      List<String> clusterIdList = getClusterIds(clusters);
      List<MfcSwCluster> mfcSwClusterList = new ArrayList<MfcSwCluster>();

      for (String tempClusterId : clusterIdList) {

        MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(session, Integer.parseInt(tempClusterId));
        if (mfcSwCluster == null) {

          String logMsg = MessageFormat.format("target resource not found. parameters={0}, clusterId={1}", "cluster",
              clusterIdList);
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
        }
        mfcSwClusterList.add(mfcSwCluster);

      }
      return mfcSwClusterList;

    } finally {
      logger.methodEnd();
    }

  }

  private LogMsfLogEntity getMsfLog(LogMfcLogsEntity mfcLog, List<RestResponseData> restResponseDataList,
      LogMergedLogEntity mergedLog) throws MsfException {
    try {
      logger.methodStart();

      LogMsfLogEntity msfLog = new LogMsfLogEntity();
      LogConditionsEntity conditions = getConditions();
      msfLog.setConditions(conditions);

      if (mfcLog != null) {
        List<LogMfcLogsEntity> mfcList = new ArrayList<LogMfcLogsEntity>();
        mfcList.add(mfcLog);
        msfLog.setMfcLogList(mfcList);
      }

      if (restResponseDataList != null) {

        List<LogClusterLogsEntity> clusterLogList = new ArrayList<LogClusterLogsEntity>();

        for (RestResponseData temp : restResponseDataList) {

          LogReadResponseBody body = JsonUtil.fromJson(temp.getResponse().getResponseBody(), LogReadResponseBody.class,
              ErrorCode.UNDEFINED_ERROR);

          if (body.getMsfLog().getClusterLogList() != null) {
            clusterLogList.addAll(body.getMsfLog().getClusterLogList());
          }
        }

        msfLog.setClusterLogList(clusterLogList);

      }

      msfLog.setMergedLog(mergedLog);

      return msfLog;

    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getClusterIds(String clusterId) {

    List<String> clusterIds = Arrays.asList(clusterId.split("\\+", 0));
    return clusterIds;
  }

  private String getClusterIdStr(List<MfcSwCluster> clusterList) {
    String clusterId = "";

    if (clusterList.isEmpty()) {
      return clusterId;
    }

    for (MfcSwCluster tempId : clusterList) {
      clusterId += tempId.getSwClusterId() + "+";
    }

    return clusterId.substring(0, clusterId.length() - 1);
  }

  private List<RestResponseData> sendControllerLogRead(List<MfcSwCluster> clusterList, String startDate, String endDate)
      throws MsfException {

    try {
      logger.methodStart();

      List<RestRequestData> requestDataList = new ArrayList<>();

      for (MfcSwCluster cluster : clusterList) {

        Integer clusterId = cluster.getSwClusterId();

        String targetUri = MfcFcRequestUri.CONTROLLOR_LOG_READ.getUri() + "?" + "log_type=" + request.getLogType() + "&"
            + "log_level=" + request.getLogLevel() + "&" + "controller="
            + getControllersStrings(controllerTypeList, ControllerType.MFC) + "&" + "cluster=" + clusterId.toString()
            + "&" + "start_date=" + startDate + "&" + "end_date=" + endDate
            + ((request.getLimitNumber() != null) ? ("&" + "limit_number=" + request.getLimitNumber()) : "")
            + ((request.getSearchString() != null)
                ? ("&" + "search_string=" + UrlEncoded.encodeString(request.getSearchString())) : "");

        RestRequestData restRequestData = new RestRequestData(clusterId,
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort(),
            MfcFcRequestUri.CONTROLLOR_LOG_READ.getHttpMethod(), targetUri, null, HttpStatus.OK_200);

        requestDataList.add(restRequestData);
      }

      List<RestResponseData> restResponseDataList = sendRequest(requestDataList, RequestType.REQUEST);

      return restResponseDataList;

    } finally {
      logger.methodEnd();
    }

  }

}
