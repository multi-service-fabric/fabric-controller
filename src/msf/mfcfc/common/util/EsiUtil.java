
package msf.mfcfc.common.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Utility class to generate and analyze ESI value.
 *
 * @author NTT
 *
 */
public class EsiUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(EsiUtil.class);

  private static final String ESI_PREFIX = "00:99:99:99";

  private static final String ESI_SEPARATOR = ":";

  private static final Pattern esiPattern = Pattern
      .compile("^(\\d{2}:\\d{2}:\\d{2}:\\d{2}):(\\d{2}:\\d{2}):(\\d{2}:\\d{2}):(\\d{2}:\\d{2})$");

  public static final int ESI_SERIAL_MAX = 9999;

  /**
   * Generate ESI value from the specified lower number/higher number cluster ID
   * as well as serial number.
   *
   * @param lowerSwClusterId
   *          ID lower number cluster ID
   * @param higherSwClusterId
   *          higher number cluster ID
   * @param serialNumber
   *          Serial number
   * @return ESI value
   */
  public static String createEsi(int lowerSwClusterId, int higherSwClusterId, int serialNumber) {
    try {
      logger.methodStart(new String[] { "lowerSwClusterId", "higherSwClusterId", "serialNumber" },
          new Object[] { lowerSwClusterId, higherSwClusterId, serialNumber });

      StringBuilder lowerSb = new StringBuilder(String.format("%04d", lowerSwClusterId));

      StringBuilder higherSb = new StringBuilder(String.format("%04d", higherSwClusterId));

      StringBuilder serialSb = new StringBuilder(String.format("%04d", serialNumber));

      lowerSb.insert(2, ESI_SEPARATOR);
      higherSb.insert(2, ESI_SEPARATOR);
      serialSb.insert(2, ESI_SEPARATOR);
      return ESI_PREFIX + ESI_SEPARATOR + lowerSb + ESI_SEPARATOR + higherSb + ESI_SEPARATOR + serialSb;

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Extract and return LACP system ID string from the specified ESI value.
   * Return Null if specified so, or return "0" if specified so for indicating
   * ESI deletion.
   *
   * @param esi
   *          ESI value
   * @return LACP system ID
   */
  public static String getLacpSystemIdFromEsi(String esi) {
    try {
      logger.methodStart(new String[] { "esi" }, new Object[] { esi });
      if (esi == null) {
        return null;
      } else if (esi.equals("0")) {
        return "0";
      } else {
        return new StringBuilder(esi).delete(0, ESI_PREFIX.length() + 1).toString();
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return lower SW cluster ID from the specified ESI value.
   *
   * @param esi
   *          ESI value
   * @return lower cluster ID
   * @throws MsfException
   *           If ESI value violates the format
   */
  public static int getLowerSwClusterId(String esi) throws MsfException {
    return getEsiParameter(esi, 2);
  }

  /**
   * Return higher SW cluster ID from the specified ESI value.
   *
   * @param esi
   *          ESI value
   * @return higher cluster ID
   * @throws MsfException
   *           If ESI value violates the format
   */
  public static int getHigherSwClusterId(String esi) throws MsfException {
    return getEsiParameter(esi, 3);
  }

  /**
   * Return ESI serial number from the specified ESI value.
   *
   * @param esi
   *          ESI value
   * @return ESI serial number
   * @throws MsfException
   *           If ESI value violates the format
   */
  public static int getSerialNumber(String esi) throws MsfException {
    return getEsiParameter(esi, 4);
  }

  private static int getEsiParameter(String esi, int group) throws MsfException {
    try {
      logger.methodStart(new String[] { "esi" }, new Object[] { esi });
      Matcher matcher = esiPattern.matcher(esi);
      if (matcher.matches()) {
        return Integer.valueOf(matcher.group(group).replace(ESI_SEPARATOR, ""));
      } else {
        String errorMessage = MessageFormat.format("invalid format esi. esi={0}", esi);
        logger.warn(errorMessage);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
