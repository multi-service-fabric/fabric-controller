
package msf.mfcfc.common.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.IpAddressType;
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

  private static final int IPV4_BIT_SIZE = 32;

  private static final int IPV6_BIT_SIZE = 128;

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
   *          An IP address string to convert
   * @return digitized IP address
   * @throws MsfException
   *           If the IP address conversion fails
   */
  public static int convertIpAddressToIntFromStr(String ipAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      return ByteBuffer.wrap(inetAddress.getAddress()).getInt();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
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
   * @return An IP address converted to string
   * @throws MsfException
   *           If the IP address conversion fails
   */
  public static String convertIpAddressToStrFromInt(int ipAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      byte[] byteAddress = ByteBuffer.allocate(4).putInt(ipAddress).array();
      InetAddress inetAddress = InetAddress.getByAddress(byteAddress);
      return inetAddress.getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to parse ip address {0}", ipAddress);
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
   *           If the IP address conversion fails
   */
  public static boolean isNetworkAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      String networkAddress = getNetworkAddress(inetAddress.getHostAddress(), subnet);
      return inetAddress.getHostAddress().equals(networkAddress);
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to parse ip address {0}", ipAddress);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Convert an IP address to a Network address.
   *
   * @param ipAddress
   *          IP address string to check (IPv4 / IPv6 can be specified)
   * @param subnet
   *          Subnet value.
   * @return network address
   * @throws MsfException
   *           If the IP address conversion fails
   */
  public static String getNetworkAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      byte[] bytes = inetAddress.getAddress();

      int bitSize = getBitSize(bytes, subnet);

      BigInteger ipb = new BigInteger(bytes);
      BigInteger ipbr = ipb.shiftRight(bitSize - subnet);
      BigInteger ipN = ipbr.shiftLeft(bitSize - subnet);

      InetAddress networkAddress;
      if (ipN.equals(BigInteger.ZERO)) {
        if (bitSize == IPV6_BIT_SIZE) {
          networkAddress = InetAddress.getByName("0::0");
        } else {
          networkAddress = InetAddress.getByName("0.0.0.0");
        }
      } else {
        networkAddress = InetAddress.getByAddress(ipN.toByteArray());
      }

      return networkAddress.getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get an IP address resulting from adding the specified number of bits to the
   * specified IP address.
   *
   * @param ipAddress
   *          Target IP address
   * @param bits
   *          Number of bits to add
   * @return An IP address resulting from adding the specified number of bits
   * @throws MsfException
   *           If the IP address conversion fails
   */
  public static String getBitsAddedIpAddress(String ipAddress, int bits) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "bits" }, new Object[] { ipAddress, bits });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      byte[] bytes = inetAddress.getAddress();
      BigInteger ipb = new BigInteger(bytes);
      ipb = ipb.add(new BigInteger("" + bits));
      return InetAddress.getByAddress(ipb.toByteArray()).getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the last available host address on the network specified with an IP
   * address and a subnet mask.
   *
   * @param ipAddress
   *          IP address string to check (IPv4 / IPv6 can be specified)
   * @param subnet
   *          Subnet mask
   * @return the last host address of the subnet
   * @throws MsfException
   *           If the IP address conversion fails, or there can be no host
   *           address on the network.
   */
  public static String getMaxHostAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });

      String networkAddress = getNetworkAddress(ipAddress, subnet);

      InetAddress inetAddress = InetAddress.getByName(networkAddress);
      byte[] bytes = inetAddress.getAddress();
      BigInteger ipb = new BigInteger(bytes);

      int bitSize = getBitSize(bytes, subnet);
      checkExistHostAddress(subnet, bitSize == IPV4_BIT_SIZE);

      Double hostAllOne = Math.pow((double) 2, (double) (bitSize - subnet)) - 2;
      logger.debug("hostAllOne=" + hostAllOne);
      BigInteger hostAllOneBig = new BigInteger("" + hostAllOne.longValue());
      ipb = ipb.add(hostAllOneBig);

      return InetAddress.getByAddress(ipb.toByteArray()).getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the first available host address on the network specified with an IP
   * address and a subnet mask.
   *
   * @param ipAddress
   *          IP address string to check (IPv4 / IPv6 can be specified)
   * @param subnet
   *          Subnet mask
   * @return the first host address of the subnet
   * @throws MsfException
   *           If the IP address conversion fails, or there can be no host
   *           address on the network.
   */
  public static String getMinHostAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });

      String networkAddress = getNetworkAddress(ipAddress, subnet);

      InetAddress inetAddress = InetAddress.getByName(networkAddress);
      byte[] bytes = inetAddress.getAddress();

      int bitSize = getBitSize(bytes, subnet);
      checkExistHostAddress(subnet, bitSize == IPV4_BIT_SIZE);

      BigInteger ipb = new BigInteger(bytes);
      ipb = ipb.add(new BigInteger("1"));
      return InetAddress.getByAddress(ipb.toByteArray()).getHostAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  private static void checkExistHostAddress(int subnet, boolean isIpv4) throws MsfException {
    try {
      logger.methodStart(new String[] { "subnet", "isIpv4" }, new Object[] { subnet, isIpv4 });
      if (isIpv4) {
        if (subnet > IPV4_BIT_SIZE - 2) {
          String logMsg = MessageFormat.format("this network is without host address. subnet={0}", subnet);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
      } else {
        if (subnet > IPV6_BIT_SIZE - 2) {
          String logMsg = MessageFormat.format("this network is without host address. subnet={0}", subnet);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private static int getBitSize(byte[] ipAddressBytes, int subnet) throws MsfException {
    try {
      logger.methodStart();
      int bitSize = 0;
      if (ipAddressBytes.length == 16) {
        bitSize = IPV6_BIT_SIZE;
      } else {
        bitSize = IPV4_BIT_SIZE;
      }

      if (subnet < 1 || subnet > bitSize) {
        String logMsg = MessageFormat.format("illegal range of subnet(1-{0}) : {1}", bitSize, subnet);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      return bitSize;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check the format of Host address.
   *
   * @param host
   *          Host address to check
   * @param ipAddressType
   *          Type of IP address to check
   * @return Formatted address (but if the parameter is FQDN, it won't be
   *         reformatted).
   */
  public static String checkHost(String host, IpAddressType ipAddressType) {
    try {
      logger.methodStart();
      String resultString;

      switch (ipAddressType) {
        case IPV4:

          try {

            resultString = ParameterCheckUtil.checkIpv4Address(host);

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4.");
            resultString = null;

          }
          return resultString;

        case IPV4V6:

          try {

            resultString = ParameterCheckUtil.checkIpAddress(host);

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4/IPv6.");
            resultString = null;

          }
          return resultString;

        case IPV4V6FQDN:

          try {

            resultString = ParameterCheckUtil.checkIpAddress(host);
            return resultString;

          } catch (MsfException error1) {

          }

          try {

            ParameterCheckUtil.checkIdSpecifiedByUri(host);
            return host;

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4/IPv6/FQDN.");
            resultString = null;
            return resultString;
          }

        default:

          resultString = null;
          logger.warn("IP address type parameter is incorrect.");
      }

      return resultString;
    } finally {
      logger.methodEnd();
    }
  }
}
