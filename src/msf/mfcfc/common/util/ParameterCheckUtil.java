
package msf.mfcfc.common.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.constant.LogLevel;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.common.constant.MergeType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Utility class for parameter check procedure.
 *
 * @author NTT
 *
 */
public class ParameterCheckUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(ParameterCheckUtil.class);

  public static final String URI_PATTERN = "[a-zA-Z0-9-\\._~]+";

  public static final String URI_PATTERN_MATCHER = "(" + URI_PATTERN + ")";

  private static final Pattern idSpecifiedByUriPattern = Pattern.compile("^" + URI_PATTERN + "$");

  private static final Pattern numericIdPattern = Pattern.compile("^[1-9][0-9]*$");

  private static final Pattern macAddressPattern = Pattern.compile("^([0-9A-Fa-f]{2}[:]){5}[0-9A-Fa-f]{2}$");

  private static final Pattern clusterIdPattern = Pattern.compile("^[1-9][0-9]*(\\+[1-9][0-9]*)*$");

  private static final Pattern pathParameterPattern = Pattern.compile("^/(.*)$");

  private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

  private static final FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT);

  /**
   * Check that the specified parameter is not NULL.
   *
   * @param checkTarget
   *          Check target instance
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNotNull(Object checkTarget) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTarget" }, new Object[] { checkTarget });
      if (checkTarget == null) {
        String logMsg = "specified parameter is null";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check that the specified string parameter is not NULL, and that the string
   * length is not 0. <br>
   *
   * @param checkTargetStr
   *          String parameter to check
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNotNullAndLength(String checkTargetStr) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetStr" }, new Object[] { checkTargetStr });

      checkNotNull(checkTargetStr);
      if (checkTargetStr.length() == 0) {
        String logMsg = "specified string parameter length is 0.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check that the specified list type parameter is not NULL, and that the size
   * is not 0. <br>
   *
   * @param checkTargetList
   *          List parameter to check
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNotNullAndLength(List<?> checkTargetList) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetList" }, new Object[] { checkTargetList });

      checkNotNull(checkTargetList);
      if (checkTargetList.size() == 0) {
        String logMsg = "specified list parameter length is 0.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether it consists of "ALPHA / DIGIT / "-" / "." / "_" / "~""
   * (half-width alphanumeric characters and "-", ".", "_" and "~".)
   *
   * @param checkTargetId
   *          Check target ID string
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkIdSpecifiedByUri(String checkTargetId) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      checkNotNull(checkTargetId);
      if (!idSpecifiedByUriPattern.matcher(checkTargetId).matches()) {
        String logMsg = MessageFormat.format("id format invalid. checkTargetId = {0}", checkTargetId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Whether it matches the regular expression "[1-9] [0-9] *" or not.
   *
   * @param checkTargetId
   *          Numeric ID string to check
   * @param errorCode
   *          Raise error out on check NG
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNumericId(String checkTargetId, ErrorCode errorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetId", "errorCode" }, new Object[] { checkTargetId, errorCode });

      if (errorCode == null) {
        String logMsg = "errorCode is required.";
        logger.error(logMsg);
        throw new IllegalArgumentException(logMsg);
      }

      checkNotNullAndLength(checkTargetId);
      if (!numericIdPattern.matcher(checkTargetId).matches()) {
        String logMsg = MessageFormat.format("id format invalid. checkTargetId = {0}", checkTargetId);
        logger.error(logMsg);
        throw new MsfException(errorCode, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether the format is of "XX XX XX XX XX XX", and return the string
   * converted to uppercase characters. <br>
   * Note: Regarding the checked target string, be sure to manage it by
   * overwriting with returned string for unified notation in the whole system.
   *
   * @param checkTargetMacAddress
   *          MAC address string to check
   * @return MAC string that all the alphanumerics in the address got converted
   *         to uppercase characters
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static String checkMacAddress(String checkTargetMacAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetMacAddress" }, new Object[] { checkTargetMacAddress });

      checkNotNullAndLength(checkTargetMacAddress);
      if (!macAddressPattern.matcher(checkTargetMacAddress).matches()) {
        String logMsg = MessageFormat.format("mac address format invalid.checkTargetMacAddress = {0}",
            checkTargetMacAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      return checkTargetMacAddress.toUpperCase();
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Convert by means of InetAddress.getByName() and check whether an exception
   * occurs or not, then return the return value of
   * InetAddress.getHostAddress(). Note: Regarding the checked target string, be
   * sure to manage it by overwriting with returned string for unified notation
   * in the whole system.
   *
   * @param checkTargetIpAddress
   *          IPv4 / IPv6 address string to check
   * @return InetAddress.getByName().getHostAddress() response
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static String checkIpAddress(String checkTargetIpAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetIpAddress" }, new Object[] { checkTargetIpAddress });

      checkNotNullAndLength(checkTargetIpAddress);
      InetAddress inetAddress = InetAddress.getByName(checkTargetIpAddress);
      String modifiedIpAddress = inetAddress.getHostAddress();
      logger.debug("originalIpAddress = {0}, modifiedIpAddress = {1}", checkTargetIpAddress, modifiedIpAddress);
      return modifiedIpAddress;
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("checkTargetIpAddress = {0}", checkTargetIpAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether an exception occurs or not for converting by means of
   * InetAddress.getByName(), <br>
   * Or check whether the address is IPv4, and return the return value of
   * InetAddress.getHostAddress(). <br>
   * Note: Regarding the checked target string, be sure to manage it by
   * overwriting with returned string for unified notation in the whole system.
   *
   * @param checkTargetIpAddress
   *          IPv4 address string to check
   * @return InetAddress.getByName().getHostAddress() response
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static String checkIpv4Address(String checkTargetIpAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetIpAddress" }, new Object[] { checkTargetIpAddress });

      checkNotNullAndLength(checkTargetIpAddress);
      InetAddress inetAddress = InetAddress.getByName(checkTargetIpAddress);

      if (!(inetAddress instanceof Inet4Address)) {
        String logMsg = MessageFormat.format("target ipaddress is not IPv4. checkTargetIpAddress = {0}",
            checkTargetIpAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      String modifiedIpAddress = inetAddress.getHostAddress();
      logger.debug("originalIpAddress = {0}, modifiedIpAddress = {1}", checkTargetIpAddress, modifiedIpAddress);
      return modifiedIpAddress;
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("ip address is invalid. checkTargetIpAddress = {0}", checkTargetIpAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether an exception occurs or not for converting by means of
   * InetAddress.getByName(), <br>
   * Or check whether the address is IPv6, and return the return value of
   * InetAddress.getHostAddress(). <br>
   * Note: Regarding the checked target string, be sure to manage it by
   * overwriting with returned string for unified notation in the whole system.
   *
   * @param checkTargetIpAddress
   *          IPv6 address string to check
   * @return InetAddress.getByName().getHostAddress() response
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static String checkIpv6Address(String checkTargetIpAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetIpAddress" }, new Object[] { checkTargetIpAddress });

      checkNotNullAndLength(checkTargetIpAddress);
      InetAddress inetAddress = InetAddress.getByName(checkTargetIpAddress);

      if (!(inetAddress instanceof Inet6Address)) {
        String logMsg = MessageFormat.format("target ipaddress is not IPv6. checkTargetIpAddress = {0}",
            checkTargetIpAddress);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      String modifiedIpAddress = inetAddress.getHostAddress();
      logger.debug("originalIpAddress = {0}, modifiedIpAddress = {1}", checkTargetIpAddress, modifiedIpAddress);
      return modifiedIpAddress;
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("ip address is invalid. checkTargetIpAddress = {0}", checkTargetIpAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Whether it can be converted to the date type in "YYYYMMDD_hhmmss" format or
   * not.
   *
   * @param checkTargetDatetime
   *          Check target date
   * @return converted date instance
   *
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static Date checkDatetime(String checkTargetDatetime) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetDatetime" }, new Object[] { checkTargetDatetime });

      checkNotNullAndLength(checkTargetDatetime);

      Date date = DateUtils.parseDate(checkTargetDatetime, new String[] { DATE_FORMAT });

      String strTemp = dateFormat.format(date);

      if (!strTemp.equals(checkTargetDatetime)) {

        throw new ParseException(null, 0);

      }

      return date;

    } catch (ParseException exp) {
      String logMsg = MessageFormat.format("date format invalid. checkTargetDatetime = {0}", checkTargetDatetime);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check that the specified number is within the specified minimum value and
   * maximum value.
   *
   * @param checkTargetNumber
   *          Number to check
   * @param minValue
   *          Minimum value in a check range (If NULL, no need to check for the
   *          minimum value)
   *
   * @param maxValue
   *          Maximum value in a check range (If NULL, no need to check for the
   *          maximum value)
   *
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNumberRange(int checkTargetNumber, Integer minValue, Integer maxValue) throws MsfException {
    try {
      logger.methodStart(new String[] { "checkTargetNumber", "minValue", "maxValue" },
          new Object[] { checkTargetNumber, minValue, maxValue });
      if (minValue != null) {

        if (checkTargetNumber < minValue) {
          String logMsg = MessageFormat.format("value {0} is less than threshold {1}", checkTargetNumber, minValue);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_OUT_OF_RANGE, logMsg);
        }
      }
      if (maxValue != null) {

        if (checkTargetNumber > maxValue) {
          String logMsg = MessageFormat.format("value {0} is greater than threshold {1}", checkTargetNumber, maxValue);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_OUT_OF_RANGE, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether the target resource is not NULL. If NULL, throw an exception.
   *
   * @param targetResource
   *          Resource to check
   * @param keys
   *          Value names used for resource acquisition
   * @param values
   *          Values used for resource acquisition
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNotNullTargetResource(Object targetResource, String[] keys, Object[] values)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "targetResource", "keys", "values" },
          new Object[] { targetResource, keys, values });
      if (targetResource == null) {
        String logMsg = MessageFormat.format("target resource not found. parameters = {0}",
            makeValuesMessage(keys, values));
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether the related resource is not NULL. If NULL, throw an
   * exception.
   *
   * @param relatedResource
   *          Resource to check
   * @param keys
   *          Value names used for resource acquisition
   * @param values
   *          Values used for resource acquisition
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkNotNullRelatedResource(Object relatedResource, String[] keys, Object[] values)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "relatedResource", "keys", "values" },
          new Object[] { relatedResource, keys, values });
      if (relatedResource == null) {
        String logMsg = MessageFormat.format("related resource not found. parameters = {0}",
            makeValuesMessage(keys, values));
        logger.error(logMsg);
        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking port number.
   *
   * @param portStr
   *          Port number to check (string)
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkPortNumber(String portStr) throws MsfException {
    try {
      logger.methodStart();
      checkNumberRange(Integer.valueOf(portStr), 0, 65535);
    } catch (NumberFormatException ex) {
      String logMsg = MessageFormat.format("value {0} is not a number.", portStr);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_OUT_OF_RANGE, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking mixed-op in PATCH method.
   *
   * @param opList
   *          Op list to check
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkPatchOperationMix(List<PatchOperation> opList) throws MsfException {
    boolean existAdd = false;
    boolean existRemove = false;
    for (PatchOperation op : opList) {
      if (op.equals(PatchOperation.ADD)) {
        existAdd = true;
      }
      if (op.equals(PatchOperation.REMOVE)) {
        existRemove = true;
      }
    }
    if (existAdd && existRemove) {
      String logMsg = "patch operation 'add' and 'remove' are mixed.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, logMsg);
    }
  }

  private static String makeValuesMessage(String[] keys, Object[] values) {
    StringBuilder builder = new StringBuilder();
    if ((keys != null) && (values != null) && (keys.length == values.length)) {
      builder.append(" (");
      for (int i = 0; i < keys.length; i++) {
        builder.append(keys[i]);
        builder.append("=");
        builder.append(values[i]);

        if (i + 1 < values.length) {
          builder.append(", ");
        }
      }
      builder.append(").");
    } else {
      logger.warn("invalid key and value parameters.");
    }
    return builder.toString();
  }

  /**
   * Method for checking the format of log type.
   *
   * @param logType
   *          Log type parameter
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkLogType(String logType) throws MsfException {
    try {
      logger.methodStart(new String[] { "log_type" }, new Object[] { logType });
      LogType logtypeEnum = LogType.getEnumFromMessage(logType);
      if (logtypeEnum == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "log_type", logType);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the format of log level.
   *
   * @param logLevel
   *          Log level parameter
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkLogLevel(String logLevel) throws MsfException {
    try {
      logger.methodStart(new String[] { "log_level" }, new Object[] { logLevel });

      List<String> logLevels = Arrays.asList(logLevel.split("\\+", 0));
      for (String level : logLevels) {
        LogLevel loglevelEnum = LogLevel.getEnumFromMessage(level);
        if (loglevelEnum == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "log_level", logLevel);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the format of controller.
   *
   * @param controllerType
   *          Controller parameter
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkController(String controllerType) throws MsfException {
    try {
      logger.methodStart(new String[] { "controller" }, new Object[] { controllerType });

      List<String> controllers = Arrays.asList(controllerType.split("\\+", 0));
      for (String controller : controllers) {
        ControllerType controllerEnum = ControllerType.getEnumFromMessage(controller);
        if (controllerEnum == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "controller",
              controllerType);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the merge type format.
   *
   * @param mergeType
   *          Merge type parameter
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkMergeType(String mergeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "merge_type" }, new Object[] { mergeType });
      MergeType mergetypeEnum = MergeType.getEnumFromMessage(mergeType);
      if (mergetypeEnum == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "merge_type", mergeType);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the cluster IDs to be specified (multiple IDs can be
   * specified) at the time of request using regular expressions.
   *
   * @param cluster
   *          Cluster IDs specified from the upper layer system (multiple IDs
   *          can be specified and connected with "+")
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkClusterIdPattern(String cluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "cluster" }, new Object[] { cluster });
      List<String> clusters = Arrays.asList(cluster.split("\\+", 0));
      for (String id : clusters) {
        if (!clusterIdPattern.matcher(id).matches()) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "cluster", cluster);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the FC controller of cluster IDs to be specified
   * (multiple IDs can be specified) at the time of request.
   *
   * @param cluster
   *          Cluster IDs specified from the upper layer system (multiple IDs
   *          can be specified and connected with "+")
   * @param clusterId
   *          Cluster IDs to be checked whether it is included in "cluster"
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkClusterForFc(String cluster, int clusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "cluster" }, new Object[] { cluster });
      List<String> clusters = Arrays.asList(cluster.split("\\+", 0));

      checkClusterIdPattern(cluster);

      if (!clusters.contains(String.valueOf(clusterId))) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "cluster", cluster);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the cluster IDs to be specified (multiple IDs can be
   * specified) at the time of request.
   *
   * @param cluster
   *          Cluster IDs specified from the upper layer system (multiple IDs
   *          can be specified and connected with "+")
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkCluster(String cluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "cluster" }, new Object[] { cluster });
      List<String> clusters = Arrays.asList(cluster.split("\\+", 0));

      for (String id : clusters) {
        if (!clusterIdPattern.matcher(id).matches()) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "cluster", cluster);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking date.
   *
   * @param date
   *          Date
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkDate(String date) throws MsfException {
    try {
      logger.methodStart();
      SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
      formatDate.setLenient(false);
      formatDate.parse(date);
    } catch (ParseException parseException) {
      String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "date", date);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method for checking the acquired information of status acquisition.
   *
   * @param getInfo
   *          Information type to acquire
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkGetInfo(String getInfo) throws MsfException {
    try {
      logger.methodStart(new String[] { "get_info" }, new Object[] { getInfo });

      List<String> getInfos = Arrays.asList(getInfo.split("\\+", 0));
      for (String info : getInfos) {
        GetInfo getInfoEnum = GetInfo.getEnumFromMessage(info);
        if (getInfoEnum == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "controller", getInfo);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether it consists of "ALPHA / DIGIT / "-" / "." / "_" / "~""
   * (half-width alphanumeric characters and "-", ".", "_" and "~".)
   * 
   * Characters that are specified in the path parameter of the PATCH method
   * corresponds to this.
   *
   * @param checkTargetPath
   *          String to check
   * @param requiredId
   *          If the specified ID is essential
   * @throws MsfException
   *           If parameter check is of NG
   */
  public static void checkPatchPath(String checkTargetPath, boolean requiredId) throws MsfException {
    try {
      logger.methodStart();
      checkNotNull(checkTargetPath);

      Matcher matcher = pathParameterPattern.matcher(checkTargetPath);
      if (matcher.matches()) {

        String checkTargetId = matcher.group(1);
        if (checkTargetId.isEmpty()) {

          if (requiredId) {
            String logMsg = MessageFormat.format("path format invalid. checkTargetPath = {0}", checkTargetPath);
            logger.error(logMsg);
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          }
        } else {

          checkIdSpecifiedByUri(checkTargetId);
        }
      } else {
        String logMsg = MessageFormat.format("path format invalid. checkTargetPath = {0}", checkTargetPath);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }
}
