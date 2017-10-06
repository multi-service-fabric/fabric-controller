package msf.fc.common.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;

public class ParameterCheckUtil {
  private static final MsfLogger logger = MsfLogger.getInstance(ParameterCheckUtil.class);
  private static final Pattern idSpecifiedByUriPattern = Pattern.compile("^[a-zA-Z0-9-\\._~]+$");
  private static final Pattern numericIdPattern = Pattern.compile("^[1-9][0-9]*$");
  private static final Pattern macAddressPattern = Pattern.compile("^([0-9A-Fa-f]{2}[:]){5}[0-9A-Fa-f]{2}$");

  private static final String DATE_FORMAT = "yyyyMMdd_HHmm";

  private static final FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT);


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
}
