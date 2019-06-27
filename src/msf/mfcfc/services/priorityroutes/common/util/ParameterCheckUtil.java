
package msf.mfcfc.services.priorityroutes.common.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Utility class for the parameter check procedure in the priority routes
 * control management function block.
 *
 * @author NTT
 *
 */
public class ParameterCheckUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(ParameterCheckUtil.class);

  private static final Pattern FABRIC_TYPE_PATTERN = Pattern.compile("^/nodes/(leafs|spines)/[a-zA-Z0-9-\\._~]*$");

  public static final Pattern FABRIC_TYPE_NUMERIC_PATTERN = Pattern.compile("^/nodes/(leafs|spines)/([1-9][0-9]*)$");

  public static final Pattern NODES_PATTERN = Pattern.compile("^/nodes$");

  /**
   * Check that the specified parameter is not NULL.
   *
   * @param checkTarget
   *          Check target instance
   * @throws MsfException
   *           If the parameter check is NG
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
   *           If the parameter check is NG
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
   *           If the parameter check is NG
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
   * Whether it matches the regular expression
   * "/nodes/(leafs|spines)/[1-9][0-9]*" or not.
   *
   * @param checkTargetString
   *          Target URI to check indicating a priority node.
   *
   * @param checkFabricTypePattern
   *          if true, uses "FABRIC_TYPE_NUMERIC_PATTERN" for matching,
   *          otherwise, uses "NODES_PATTERN".
   *
   * @throws MsfException
   *           If the parameter check is NG
   */
  public static void checkPriorityroutesResourcePathFormat(String checkTargetString, boolean checkFabricTypePattern)
      throws MsfException {
    if (checkFabricTypePattern) {
      try {
        logger.methodStart(new String[] { "checkTargetPrioritySystem" }, new Object[] { checkTargetString });

        checkNotNullAndLength(checkTargetString);
        if (!FABRIC_TYPE_PATTERN.matcher(checkTargetString).matches()) {
          String logMsg = MessageFormat.format("URI format not match. checkTargetString = {0}", checkTargetString);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        } else if (!FABRIC_TYPE_NUMERIC_PATTERN.matcher(checkTargetString).matches()) {
          String logMsg = MessageFormat.format("node_id format not match. checkTargetString = {0}", checkTargetString);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
        }
      } finally {
        logger.methodEnd();
      }
    } else {

      try {
        logger.methodStart(new String[] { "checkTargetPrioritySystem" }, new Object[] { checkTargetString });

        checkNotNullAndLength(checkTargetString);
        if (!NODES_PATTERN.matcher(checkTargetString).matches()) {
          checkPriorityroutesResourcePathFormat(checkTargetString, true);
        }
      } finally {
        logger.methodEnd();
      }
    }
  }
}
