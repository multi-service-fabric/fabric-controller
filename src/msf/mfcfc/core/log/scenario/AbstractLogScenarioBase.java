
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.log.scenario.data.entity.LogDataEntity;
import msf.mfcfc.core.log.scenario.data.entity.LogFcLogEntity;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement common process of log acquisition
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

  private static final String API_ACCESS_FORMAT = "'../logs/api_access/fc_api_access_'yyyyMMdd'.log'";

  private static final String PROCESSING_FORMAT = "'../logs/processing/fc_processing_'yyyyMMdd'.log'";

  private static final String API_ACCESS_FILENAME = "../logs/api_access/fc_api_access.log";

  private static final String PROCESSING_FILENAME = "../logs/processing/fc_processing.log";

  private static final String MESSAGE_REGEX = "^\\[(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})\\]"
      + " \\[([A-Z]*) *\\] \\[tid=(.*?)\\] \\((.*?)::(.*?):(\\d*)\\):(.*)$";

  protected static final Pattern PATTERN = Pattern.compile(MESSAGE_REGEX, Pattern.DOTALL);

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
        format = API_ACCESS_FORMAT;
        todayFile = API_ACCESS_FILENAME;
      } else {
        format = PROCESSING_FORMAT;
        todayFile = PROCESSING_FILENAME;
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

}
