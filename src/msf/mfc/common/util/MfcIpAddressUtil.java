
package msf.mfc.common.util;

import java.text.MessageFormat;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;

/**
 * IP address-related utility class.
 *
 * @author NTT
 *
 */
public class MfcIpAddressUtil extends IpAddressUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcIpAddressUtil.class);

  /**
   * Calculate the IP address of inter-cluster link IFs and return the IP
   * address of cluster N. Can be called in MFC only.
   *
   * @param clusterN
   *          Cluster N ID for calculation target (generated side)
   * @param clusterM
   *          Cluster M ID for calculation target
   * @return IP address of cluster N
   * @throws MsfException
   *           If the inter-cluster link IF starting IP address does not conform
   *           to IPv4 format. If cluster N and cluster M have the same value.
   */
  public static String getClusterIpAddress(int clusterN, int clusterM) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterN", "clusterM" }, new Object[] { clusterN, clusterM });
      MfcConfigManager configManager = MfcConfigManager.getInstance();
      String x0inter = configManager.getClusterStartAddress();
      int nclu = configManager.getMaxSwClusterNum();
      int x0i = convertIpAddressToIntFromStr(x0inter);

      boolean swFlag = false;
      if (clusterN == clusterM) {
        String logMsg = MessageFormat.format("Invalid use : clusterN({0}) and clusterM({1}) is same value.", clusterN,
            clusterM);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      } else if (clusterN > clusterM) {
        int tmp = clusterM;
        clusterM = clusterN;
        clusterN = tmp;
        swFlag = true;
      }

      int si = 4 * (clusterM - clusterN + (clusterN - 1) * (nclu - 1) - 1) + 1 + x0i;

      String ss = convertIpAddressToStrFromInt(si);

      int ti = 4 * (clusterM - clusterN + (clusterN - 1) * (nclu - 1) - 1) + 2 + x0i;

      String ts = convertIpAddressToStrFromInt(ti);

      if (swFlag) {
        String tmp = ss;
        ss = ts;
        ts = tmp;
      }

      return ss;
    } finally {
      logger.methodEnd();
    }
  }

}
