
package msf.mfcfc.core.log.scenario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.common.constant.MergeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.log.scenario.data.LogReadRequest;
import msf.mfcfc.core.log.scenario.data.LogReadResponseBody;
import msf.mfcfc.core.log.scenario.data.entity.LogClusterLogsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogConditionsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogEcLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogEmLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogFcLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMergedDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMergedLogEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMfcLogsEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogMsfLogEntity;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement the common process of log acquisition
 * related-processing in system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractLogScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractLogScenarioBase.class);

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

  private static final String FC_API_ACCESS_FORMAT = "'../logs/api_access/fc_api_access_'yyyyMMdd'.log'";

  private static final String FC_PROCESSING_FORMAT = "'../logs/processing/fc_processing_'yyyyMMdd'.log'";

  private static final String FC_API_ACCESS_FILENAME = "../logs/api_access/fc_api_access.log";

  private static final String FC_PROCESSING_FILENAME = "../logs/processing/fc_processing.log";

  private static final String MFC_API_ACCESS_FORMAT = "'../logs/api_access/mfc_api_access_'yyyyMMdd'.log'";

  private static final String MFC_PROCESSING_FORMAT = "'../logs/processing/mfc_processing_'yyyyMMdd'.log'";

  private static final String MFC_API_ACCESS_FILENAME = "../logs/api_access/mfc_api_access.log";

  private static final String MFC_PROCESSING_FILENAME = "../logs/processing/mfc_processing.log";

  private static final String MESSAGE_REGEX = "^\\[(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})\\]"
      + " \\[([A-Z]*) *\\] \\[tid=(.*?)\\] \\((.*?)::(.*?):(\\d*)\\):(.*)$";

  protected static final Pattern PATTERN = Pattern.compile(MESSAGE_REGEX, Pattern.DOTALL);

  protected LogReadRequest request;

  protected List<ControllerType> controllerTypeList;

  protected LogFcLogEntity readLog(String logType, List<String> logLevel, String startDate, String endDate,
      Integer limitNumber, String searchString, String controllerType) throws MsfException {
    try {
      logger.methodStart();
      dateFormat.setLenient(false);
      Date date = dateFormat.parse(startDate);
      Date lastDate = dateFormat.parse(endDate);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      String today = dateFormat.format(new Date());
      String fileName;
      String todayFile;
      String format;
      boolean overLimitNumber = false;

      if (LogType.API_ACCESS.getMessage().equals(logType)) {
        format = FC_API_ACCESS_FORMAT;
        todayFile = FC_API_ACCESS_FILENAME;
      } else {
        format = FC_PROCESSING_FORMAT;
        todayFile = FC_PROCESSING_FILENAME;
      }

      List<LogDataEntity> logs = new ArrayList<LogDataEntity>();
      Integer limit = limitNumber;

      while (!date.after(lastDate)) {

        if (!today.equals(dateFormat.format(date))) {
          fileName = new SimpleDateFormat(format).format(date);
        } else {
          fileName = todayFile;
        }

        logs.addAll(readLogFile(fileName, limit, controllerType));
        if (limitNumber != null) {
          if (logs.size() > limitNumber) {
            overLimitNumber = true;
            break;
          }

          limit = limitNumber - logs.size();
        }

        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
      }
      String serverName = getHostName();

      LogFcLogEntity fcLog = getFcLog(logs, logLevel, searchString, limitNumber, overLimitNumber, serverName);
      return fcLog;
    } catch (ParseException parseException) {
      String logMsg = MessageFormat.format("param is undefined.param = {0}, {1}, value = {2}, {3}", "start_date",
          "end_date", startDate, endDate);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  protected LogMfcLogsEntity readMfcLog(String logType, List<String> logLevel, String startDate, String endDate,
      Integer limitNumber, String searchString, String controllerType) throws MsfException {
    try {
      logger.methodStart();
      dateFormat.setLenient(false);
      Date date = dateFormat.parse(startDate);
      Date lastDate = dateFormat.parse(endDate);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      String today = dateFormat.format(new Date());
      String fileName;
      String todayFile;
      String format;
      boolean overLimitNumber = false;

      if (LogType.API_ACCESS.getMessage().equals(logType)) {
        format = MFC_API_ACCESS_FORMAT;
        todayFile = MFC_API_ACCESS_FILENAME;
      } else {
        format = MFC_PROCESSING_FORMAT;
        todayFile = MFC_PROCESSING_FILENAME;
      }

      List<LogDataEntity> logs = new ArrayList<LogDataEntity>();
      Integer limit = limitNumber;

      while (!date.after(lastDate)) {

        if (!today.equals(dateFormat.format(date))) {
          fileName = new SimpleDateFormat(format).format(date);
        } else {
          fileName = todayFile;
        }

        logs.addAll(readLogFile(fileName, limit, controllerType));
        if (limitNumber != null) {
          if (logs.size() > limitNumber) {
            overLimitNumber = true;
            break;
          }

          limit = limitNumber - logs.size();
        }

        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
      }
      String serverName = getHostName();

      LogMfcLogsEntity mfcLog = getMfcLog(logs, logLevel, searchString, limitNumber, overLimitNumber, serverName);
      return mfcLog;
    } catch (ParseException parseException) {
      String logMsg = MessageFormat.format("param is undefined.param = {0}, {1}, value = {2}, {3}", "start_date",
          "end_date", startDate, endDate);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  private List<LogDataEntity> readLogFile(String fileName, Integer limit, String controllerType) throws MsfException {
    logger.methodStart();

    List<LogDataEntity> logs = new ArrayList<LogDataEntity>();
    String line;
    BufferedReader bufferedReader = null;

    File file = new File(fileName);
    if (!file.exists()) {
      return logs;
    }

    String serverName = getHostName();

    try {
      bufferedReader = new BufferedReader(new FileReader(file));
      while ((line = bufferedReader.readLine()) != null) {

        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {

          if (limit != null) {
            if (logs.size() > limit) {
              break;
            }
          }

          logs.add(getLogData(matcher, serverName, controllerType));
        } else {
          if (!logs.isEmpty()) {

            logs.get(logs.size() - 1).appendMessage(new StringBuffer("\n").append(line).toString());
          }
        }
      }
      return logs;
    } catch (IOException ioe) {
      String logMsg = MessageFormat.format("file read error.param = {0}, value = {1}", "fileName", file.toString());
      logger.error(logMsg);
      throw new MsfException(ErrorCode.FILE_READ_ERROR, logMsg);
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException ioe) {

      }
      logger.methodEnd();
    }
  }

  protected LogMfcLogsEntity getMfcLog(List<LogDataEntity> logs, List<String> logLevel, String searchString,
      Integer limitNumber, boolean overLimitNumber, String serverName) {
    LogMfcLogsEntity mfcLog = new LogMfcLogsEntity();
    mfcLog.setLogDataList(getMatchLogData(logs, logLevel, searchString, limitNumber));
    mfcLog.setDataNumber(mfcLog.getLogDataList().size());
    mfcLog.setOverLimitNumber(overLimitNumber);
    mfcLog.setServerName(serverName);
    return mfcLog;
  }

  protected LogFcLogEntity getFcLog(List<LogDataEntity> logs, List<String> logLevel, String searchString,
      Integer limitNumber, boolean overLimitNumber, String serverName) {
    LogFcLogEntity fcLog = new LogFcLogEntity();
    fcLog.setLogDataList(getMatchLogData(logs, logLevel, searchString, limitNumber));
    fcLog.setDataNumber(fcLog.getLogDataList().size());
    fcLog.setOverLimitNumber(overLimitNumber);
    fcLog.setServerName(serverName);
    return fcLog;
  }

  protected LogDataEntity getLogData(Matcher matcher, String serverName, String controllerType) {
    LogDataEntity logDataEntity = new LogDataEntity();
    logDataEntity.setOccurredTime(matcher.group(1));
    logDataEntity.setLogLevel(matcher.group(2));
    logDataEntity.setThreadId(matcher.group(3));
    logDataEntity.setClassName(matcher.group(4));
    logDataEntity.setMethodName(matcher.group(5));
    logDataEntity.setLineNumber(Integer.parseInt(matcher.group(6)));
    logDataEntity.setMessage(matcher.group(7));
    logDataEntity.setController(controllerType);
    logDataEntity.setServerName(serverName);
    return logDataEntity;
  }

  protected List<LogDataEntity> getMatchLogData(List<LogDataEntity> logs, List<String> logLevels, String searchString,
      Integer limit) {
    List<LogDataEntity> matchLogData = new ArrayList<LogDataEntity>();
    for (LogDataEntity log : logs) {
      if (!logLevels.contains(log.getLogLevel())) {
        continue;
      }
      if (log.getMessage().indexOf(searchString) == -1) {
        continue;
      }
      matchLogData.add(log);
      if (limit != null) {
        if (matchLogData.size() >= limit) {
          break;
        }
      }
    }

    return matchLogData;
  }

  private String getHostName() throws MsfException {
    InputStream inputStream = null;
    BufferedReader bufferedReader = null;
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("hostname");
      Process process = processBuilder.start();
      inputStream = process.getInputStream();
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String hostName = bufferedReader.readLine();
      return hostName;
    } catch (IOException ioException) {
      String logMsg = MessageFormat.format("param is undefined.param = {0}", "hostname");
      logger.error(logMsg);
      throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException ioe) {

      }
    }
  }

  /**
   * Procedures to return the date of the day.
   *
   * @return the date of the day
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static String getDefaultDate() throws MsfException {
    try {
      logger.methodStart();
      Date date = new Date();
      SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
      return formatDate.format(date);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected LogConditionsEntity getConditions() {
    LogConditionsEntity conditions = new LogConditionsEntity();
    conditions.setLogType(request.getLogType());
    conditions.setLogLevelList(Arrays.asList(request.getLogLevel().split("\\+", 0)));
    conditions.setControllerList(getControllerStringList(controllerTypeList));

    if (request.getCluster() != null) {
      conditions.setClusterList(Arrays.asList(request.getCluster().split("\\+", 0)));
    } else {
      conditions.setClusterList(new ArrayList<String>());
    }
    conditions.setStartDate(request.getStartDate());
    conditions.setEndDate(request.getEndDate());
    conditions.setLimitNumber(request.getLimitNumber());
    conditions.setSearchString(request.getSearchString());
    conditions.setMergeType(request.getMergeType());
    return conditions;
  }

  protected List<String> getControllers(String controller) {

    List<String> controllers = Arrays.asList(controller.split("\\+", 0));
    return controllers;
  }

  protected List<ControllerType> getControllerEnumList(String controller) {
    List<ControllerType> controllers = new ArrayList<>();
    if (controller == null) {

      controllers.add(ControllerType.MFC);
      controllers.add(ControllerType.FC);
      controllers.add(ControllerType.EC);
      controllers.add(ControllerType.EM);
      return controllers;
    }

    List<String> controllerStrings = Arrays.asList(controller.split("\\+", 0));
    for (String controllerString : controllerStrings) {
      controllers.add(ControllerType.getEnumFromMessage(controllerString));
    }
    return controllers;
  }

  protected List<String> getControllerStringList(List<ControllerType> controllerTypeList) {

    List<String> controllerStrList = new ArrayList<>();

    if (controllerTypeList == null || controllerTypeList.isEmpty()) {
      return controllerStrList;
    }

    controllerStrList = controllerTypeList.stream().map(controllerType -> {
      return controllerType.getMessage();
    }).collect(Collectors.toList());

    return controllerStrList;
  }

  protected String getControllersStrings(List<ControllerType> controllers, ControllerType controllerType) {

    StringJoiner joiner = new StringJoiner("+");
    controllers.stream().filter(controller -> (controller.ordinal() - controllerType.ordinal()) > 0)
        .forEach(e -> joiner.add(e.getMessage()));
    return joiner.toString();
  }

  protected RestResponseBase responseLogReadData(LogMsfLogEntity logMsfLogEntity) {
    try {
      logger.methodStart(new String[] { "msf_log" }, new Object[] { logMsfLogEntity });
      LogReadResponseBody body = new LogReadResponseBody();
      body.setMsfLog(logMsfLogEntity);
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  protected List<LogMergedDataEntity> getLogMergedData(List<LogDataEntity> logList, Integer clusterId) {
    return getLogMergedData(logList, clusterId, true);
  }

  protected List<LogMergedDataEntity> getLogMergedData(List<LogDataEntity> logList, Integer clusterId,
      boolean isLimit) {
    List<LogMergedDataEntity> mergedLogList = new ArrayList<>();
    for (LogDataEntity log : logList) {
      LogMergedDataEntity mergedData = new LogMergedDataEntity();
      if (clusterId != null) {
        mergedData.setClusterId(clusterId.toString());
      }
      mergedData.setController(log.getController());
      mergedData.setServerName(log.getServerName());
      mergedData.setLogLevel(log.getLogLevel());
      mergedData.setOccurredTime(log.getOccurredTime());
      mergedData.setThreadId(log.getThreadId());
      mergedData.setClassName(log.getClassName());
      mergedData.setMethodName(log.getMethodName());
      mergedData.setLineNumber(log.getLineNumber());
      mergedData.setMessage(log.getMessage());
      mergedLogList.add(mergedData);
      if (isLimit && request.getLimitNumber() != null) {
        if (mergedLogList.size() >= request.getLimitNumber()) {
          break;
        }
      }
    }
    return mergedLogList;
  }

  protected LogMsfLogEntity getMsfLog(LogFcLogEntity fcLog, LogEcLogEntity ecLog, LogEmLogEntity emLog,
      LogMergedLogEntity mergedLog, String cluster) {
    try {
      logger.methodStart();

      LogMsfLogEntity msfLog = new LogMsfLogEntity();
      LogConditionsEntity conditions = getConditions();
      msfLog.setConditions(conditions);

      LogClusterLogsEntity logClusterLogsEntity = new LogClusterLogsEntity();

      if (MergeType.SEPARATE.getMessage().equals(request.getMergeType())) {
        logClusterLogsEntity.setFcLog(fcLog);
        logClusterLogsEntity.setEcLog(ecLog);
        logClusterLogsEntity.setEmLog(emLog);
        logClusterLogsEntity.setClusterId(cluster);
        List<LogClusterLogsEntity> clusterLogs = new ArrayList<>();
        clusterLogs.add(logClusterLogsEntity);
        msfLog.setClusterLogList(clusterLogs);
      }
      msfLog.setMfcLogList(null);
      msfLog.setMergedLog(mergedLog);

      return msfLog;

    } finally {
      logger.methodEnd();
    }
  }

  protected List<LogDataEntity> getSortData(LogFcLogEntity fcLog, LogEcLogEntity ecLog, LogEmLogEntity emLog) {
    List<LogDataEntity> logList = new ArrayList<>();
    if (fcLog != null) {

      fcLog.getLogDataList().stream().forEach(fcLogElement -> {

        fcLogElement.setController(ControllerType.FC.getMessage());

        fcLogElement.setServerName(fcLog.getServerName());

      });

      logList.addAll(fcLog.getLogDataList());
    }
    if (ecLog != null) {

      ecLog.getLogDataList().stream().forEach(ecLogElement -> {

        ecLogElement.setController(ControllerType.EC.getMessage());

        ecLogElement.setServerName(ecLog.getServerName());

      });

      logList.addAll(ecLog.getLogDataList());
    }
    if (emLog != null) {

      emLog.getLogDataList().stream().forEach(emLogElement -> {

        emLogElement.setController(ControllerType.EM.getMessage());

        emLogElement.setServerName(emLog.getServerName());

      });

      logList.addAll(emLog.getLogDataList());
    }

    Comparator<LogDataEntity> dateComparator = Comparator.comparing(d -> d.getOccurredTime());

    Comparator<LogDataEntity> controllerComparator = COMPARATOR_LOGCONTROLLER;
    Collections.sort(logList, dateComparator.thenComparing(controllerComparator));
    return logList;
  }

  protected List<String> getLogLevels(String loglevel) {

    List<String> logLevels = Arrays.asList(loglevel.toUpperCase().replace("WARNING", "WARN").split("\\+", 0));
    return logLevels;
  }

  protected static final Comparator<LogDataEntity> COMPARATOR_LOGCONTROLLER = new Comparator<LogDataEntity>() {
    @Override
    public int compare(LogDataEntity o1, LogDataEntity o2) {

      return ControllerType.getEnumFromMessage(o1.getController()).ordinal()
          - ControllerType.getEnumFromMessage(o2.getController()).ordinal();

    }
  };

  protected static final Comparator<LogMergedDataEntity> COMPARATOR_LOGDATA_LOGCONTROLLER = new Comparator<LogMergedDataEntity>() {
    @Override
    public int compare(LogMergedDataEntity o1, LogMergedDataEntity o2) {

      return ControllerType.getEnumFromMessage(o1.getController()).ordinal()
          - ControllerType.getEnumFromMessage(o2.getController()).ordinal();

    }
  };

  protected static final Comparator<LogMergedDataEntity> COMPARATOR_LOGDATA_CLUSTERID = new Comparator<LogMergedDataEntity>() {
    @Override
    public int compare(LogMergedDataEntity o1, LogMergedDataEntity o2) {

      Integer o1ClusterId = -1;

      Integer o2ClusterId = -1;

      if (o1.getClusterId() != null) {
        o1ClusterId = Integer.parseInt(o1.getClusterId());
      }

      if (o2.getClusterId() != null) {
        o2ClusterId = Integer.parseInt(o2.getClusterId());
      }

      return o1ClusterId - o2ClusterId;

    }
  };
}
