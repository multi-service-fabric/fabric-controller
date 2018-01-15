
package msf.fc.common.util;

import java.util.HashMap;
import java.util.Map;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.SwCluster;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;

/**
 * IP address-related utility class.
 *
 * @author NTT
 *
 */
public class FcIpAddressUtil extends IpAddressUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(FcIpAddressUtil.class);

  private static int nintrai;

  /**
   * Calculate IP addresses of Leaf (n) and Spine (m) on the internal link.
   *
   * @param leafId
   *          Leaf ID
   * @param spineId
   *          Spine ID
   *
   * @return Leaf/Spine IP address saved map.
   * @throws MsfException
   *           If SwClusterData can not be acquired from config
   */
  public static Map<String, String> getNintrai(int leafId, int spineId) throws MsfException {
    try {
      logger.methodStart(new String[] { "leafId", "spineId" }, new Object[] { leafId, spineId });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0intrai = swCluster.getInchannelStartAddress();

      int nsi = swCluster.getMaxSpineNum();

      int x0i = convertIpAddressToIntFromStr(x0intrai);

      int si = 4 * (nsi * (leafId - 1) + spineId - 1) + 2 + x0i;

      String ss = convertIpAddressToStrFromInt(si);

      int ti = 4 * (nsi * (leafId - 1) + spineId - 1) + 1 + x0i;
      String ts = convertIpAddressToStrFromInt(ti);

      Map<String, String> retMap = new HashMap<String, String>();
      retMap.put(SPINE, ss);
      retMap.put(LEAF, ts);

      return retMap;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate IP address of Leaf and RR on the internal link.
   *
   * @param rrId
   *          RR ID
   * @return Leaf/RR IP address saved map.
   * @throws MsfException
   *           If SwClusterData can not be acquired from config
   */
  public static Map<String, String> getNintrai(int rrId) throws MsfException {
    try {
      logger.methodStart(new String[] { "rrId" }, new Object[] { rrId });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0intrai = swCluster.getInchannelStartAddress();

      int x0i = convertIpAddressToIntFromStr(x0intrai);

      int nsi = swCluster.getMaxSpineNum();

      int nli = swCluster.getMaxLeafNum();

      int ui = 4 * (nsi * (nli - 1) + nsi - 1) + 2 + x0i;

      int si = ui + 4 * rrId - 1;

      int ti = ui + 4 * rrId;

      String ss = convertIpAddressToStrFromInt(si);
      String ts = convertIpAddressToStrFromInt(ti);

      Map<String, String> retMap = new HashMap<String, String>();
      retMap.put(LEAF, ss);
      retMap.put(RR, ts);

      return retMap;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return the number of internal link IP addresses.
   *
   * @return the number of internal IP addresses
   */
  public static int getNintrai() {
    try {
      logger.methodStart();
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      int nsi = swCluster.getMaxSpineNum();

      int nli = swCluster.getMaxLeafNum();

      int nri = swCluster.getMaxRrNum();

      int totalIps = 4 * nsi * nli + nri;

      double log2 = Math.log(totalIps) / Math.log(2);

      double log2c = Math.ceil(log2);

      nintrai = (int) Math.pow(2, log2c);
      return nintrai;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return Leaf loopback address.
   *
   * @param leafId
   *          Leaf ID
   * @return Leaf loopback address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getX0lloi(int leafId) throws MsfException {
    try {
      logger.methodStart(new String[] { "leafId" }, new Object[] { leafId });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0intrai = swCluster.getInchannelStartAddress();

      int x0i = convertIpAddressToIntFromStr(x0intrai);

      int nintrai = getNintrai();

      int x0lloii = x0i + nintrai + FREE_IP_ADDRESS + leafId;

      return convertIpAddressToStrFromInt(x0lloii);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return Spine loopback address
   *
   * @param spineId
   *          Spine ID
   * @return Spine loopback address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXslloi(int spineId) throws MsfException {
    try {
      logger.methodStart(new String[] { "spineId" }, new Object[] { spineId });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0lloi = getX0lloi(0);

      int x0lloii = convertIpAddressToIntFromStr(x0lloi);

      int nli = swCluster.getMaxLeafNum();

      double log2 = Math.log(nli) / Math.log(2);

      int nlloi = (int) Math.pow(2, Math.ceil(log2));

      int x0sloii = x0lloii + nlloi + spineId;

      return convertIpAddressToStrFromInt(x0sloii);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return RR loopback address
   *
   * @param rrId
   *          RR ID
   * @return RR loopback address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXrlloi(int rrId) throws MsfException {
    try {
      logger.methodStart(new String[] { "rrId" }, new Object[] { rrId });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0sloi = getXslloi(0);

      int x0sloii = convertIpAddressToIntFromStr(x0sloi);

      int nsi = swCluster.getMaxSpineNum();

      double log2 = Math.log(nsi) / Math.log(2);

      int nsloi = (int) Math.pow(2, Math.ceil(log2));

      int x0rioii = x0sloii + nsloi + rrId;

      return convertIpAddressToStrFromInt(x0rioii);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Return the aggregate address.
   *
   * @return aggregate address
   */
  public static String getXagri() {
    try {
      logger.methodStart();
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      return swCluster.getAggrigationStartAddress();
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Returns the prefix of aggregate address.
   *
   * @return prefix of aggregate address
   */
  public static int getPintrai() {
    try {
      logger.methodStart();
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      return swCluster.getAggrigationAddressPrefix();
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate FC address of inchannel.
   *
   * @param type
   *          FC controller type
   * @return FC inchannel address of specified type
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getX0fcinii(ConIp type) throws MsfException {
    try {
      logger.methodStart(new String[] { "type" }, new Object[] { type });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0rioi = getXrlloi(0);

      int x0rioii = convertIpAddressToIntFromStr(x0rioi);

      int nri = swCluster.getMaxRrNum();

      double log2 = Math.log(nri) / Math.log(2);

      int nrloi = (int) Math.pow(2, Math.ceil(log2));

      int x0fcini = x0rioii + nrloi + type.getType();

      return convertIpAddressToStrFromInt(x0fcini);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate EC address of inchannel.
   *
   * @param type
   *          EC controller type
   * @return EC inchannel address of specified type
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getX0ecinii(ConIp type) throws MsfException {
    try {
      logger.methodStart(new String[] { "type" }, new Object[] { type });

      String x0fcini = getX0fcinii(ConIp.CTL_0);

      int x0fcinii = convertIpAddressToIntFromStr(x0fcini);

      int x0ecinii = x0fcinii + N_FCINI + type.getType();

      return convertIpAddressToStrFromInt(x0ecinii);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Caluculate the outchannel Leaf address of the specified ID.
   *
   * @param id
   *          ID
   * @return outchannel Leaf address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXlmgi(int id) throws MsfException {
    try {
      logger.methodStart(new String[] { "id" }, new Object[] { id });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getLeafStartPos();
      return getOutchannel(start, id);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Caluculate the outchannel Spine address of the specified ID.
   *
   * @param id
   *          ID
   * @return outchannel Spine address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXsmgi(int id) throws MsfException {
    try {
      logger.methodStart(new String[] { "id" }, new Object[] { id });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getSpineStartPos();
      return getOutchannel(start, id);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate the outchannel RR address of specified ID.
   *
   * @param id
   *          ID
   * @return outchannel RR address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXrmgi(int id) throws MsfException {
    try {
      logger.methodStart(new String[] { "id" }, new Object[] { id });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getRrStartPos();
      return getOutchannel(start, id);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate the outchannel FC address of the specified ID.
   *
   * @param type
   *          Controller type
   * @return outchannel FC address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXfcmgi(ConIp type) throws MsfException {
    try {
      logger.methodStart(new String[] { "type" }, new Object[] { type });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getFcStartPos();
      return getOutchannel(start, type.getType());
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate the outchannel EC address of specified type.
   *
   * @param type
   *          Controller type
   * @return outchannel EC address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXecmgi(ConIp type) throws MsfException {
    try {
      logger.methodStart(new String[] { "type" }, new Object[] { type });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getEcStartPos();
      return getOutchannel(start, type.getType());
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Calculate the outchannel EM address of the specified type.
   *
   * @param type
   *          Controller type
   * @return outchannel EM address
   * @throws MsfException
   *           If value can't be acquired from config
   */
  public static String getXemmgi(ConIp type) throws MsfException {
    try {
      logger.methodStart(new String[] { "type" }, new Object[] { type });
      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();
      int start = swCluster.getEmStartPos();
      return getOutchannel(start, type.getType());
    } finally {
      logger.methodEnd();
    }
  }

  protected static String getOutchannel(int start, int id) throws MsfException {
    try {
      logger.methodStart(new String[] { "start", "id" }, new Object[] { start, id });

      SwCluster swCluster = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster();

      String x0out = swCluster.getOutchannelStartAddress();

      int x0i = convertIpAddressToIntFromStr(x0out);

      int ips = x0i + start + id;

      return convertIpAddressToStrFromInt(ips);
    } finally {
      logger.methodEnd();
    }
  }
}
