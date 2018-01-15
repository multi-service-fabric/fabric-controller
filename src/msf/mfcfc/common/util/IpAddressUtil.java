
package msf.mfcfc.common.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * IP address-related utility class.
 *
 * @author NTT
 *
 */
public class IpAddressUtil {
  private static final MsfLogger logger = MsfLogger.getInstance(IpAddressUtil.class);

  public static final int FREE_IP_ADDRESS = 0;

  public static final int N_FCINI = 16;

  public static final String LEAF = "LEAF";
  public static final String SPINE = "SPINE";
  public static final String RR = "RR";

  /**
   * Enum on the IP address relative position for each service of controller.
   *
   * @author NTT
   */
  public enum ConIp {
    CTL_0(0),
    CTL_1(1),
    CTL_VIP(2),
    DB_0(3),
    DB_1(4),
    DB_VIP(5),
    OPPOSIT_LEAF1(6),
    OPPOSIT_LEAF2(7),
    OPPOSIT_LEAF_VIP(8);

    private int type;

    ConIp(int type) {
      this.type = type;
    }

    public int getType() {
      return type;
    }
  }

  /**
   * Convert IP address of the specified string to the int-type number.
   *
   * @param ipAddress
   *          IP address string to convert
   * @return digitized IP address
   * @throws MsfException
   *           Exception
   */
  public static int convertIpAddressToIntFromStr(String ipAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Convert IP address of the specified number to IP address of the string.
   *
   * @param ipAddress
   *          digitized IP address
   * @return IP address converted to string
   * @throws MsfException
   *           Exception
   */
  public static String convertIpAddressToStrFromInt(int ipAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      byte[] byteAddress = ByteBuffer.allocate(4).putInt(ipAddress).array();
      InetAddress inetAddress = InetAddress.getByAddress(byteAddress);
      return inetAddress.getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to parse ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check whether the specified IP address is a network address.
   *
   * @param ipAddress
   *          IP address string to check (IPv4 / IPv6 can be specified)
   * @param subnet
   *          Subnet value of IP address to check
   * @return true for broadcast address, false for non-broadcast address
   * @throws MsfException
   *           Occurs when IP address conversion fails
   */
  public static boolean isNetworkAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      byte[] bytes = inetAddress.getAddress();

      int bitSize = 0;
      if (bytes.length == 16) {
        bitSize = 128;
        if (subnet < 1 || subnet > 128) {
          String logMsg = MessageFormat.format("illegal range of subnet(1-128) : {0}", subnet);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
      } else {
        bitSize = 32;
        if (subnet < 1 || subnet > 32) {
          String logMsg = MessageFormat.format("illegal range of subnet(1-32) : {0}", subnet);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
      }

      BigInteger ipb = new BigInteger(bytes);
      BigInteger ipbr = ipb.shiftRight(bitSize - subnet);
      BigInteger ipN = ipbr.shiftLeft(bitSize - subnet);

      InetAddress networkAddress;
      if (ipN.equals(BigInteger.ZERO)) {
        if (bitSize == 128) {
          networkAddress = InetAddress.getByName("0::0");
        } else {
          networkAddress = InetAddress.getByName("0.0.0.0");
        }
      } else {
        networkAddress = InetAddress.getByAddress(ipN.toByteArray());
      }

      return inetAddress.equals(networkAddress);

    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

}
