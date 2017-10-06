package msf.fc.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;

public class IpAddressUtil {
  private static final MsfLogger logger = MsfLogger.getInstance(IpAddressUtil.class);

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

  public static boolean isLoopbackAddress(String ipAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      return inetAddress.isLoopbackAddress();
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  public static boolean isNetworkAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      String networkAddress = getNetworkAddress(inetAddress, subnet);
      return ipAddress.equals(networkAddress);
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  public static boolean isBroadcastAddress(String ipAddress, int subnet) throws MsfException {
    try {
      logger.methodStart(new String[] { "ipAddress", "subnet" }, new Object[] { ipAddress, subnet });
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      String broadcastAddress = getBroadcastAddress(inetAddress, subnet);
      return ipAddress.equals(broadcastAddress);
    } catch (UnknownHostException exp) {
      String logMsg = MessageFormat.format("failed to convert ip address {0}", ipAddress);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  private static String getNetworkAddress(InetAddress ipAddress, int subnet) throws UnknownHostException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });

      if (ipAddress instanceof Inet4Address) {
        if (subnet < 1 || subnet > 32) {
          throw new IllegalArgumentException("illegal range of subnet(0-32).");
        }
        int ip = ByteBuffer.wrap(ipAddress.getAddress()).getInt();
        int networkAddress = (ip >> (32 - subnet)) << (32 - subnet);
        byte[] byteAddress = ByteBuffer.allocate(4).putInt(networkAddress).array();
        return InetAddress.getByAddress(byteAddress).getHostAddress();
      } else {
        throw new IllegalArgumentException("unsupport ipv6");
      }

    } finally {
      logger.methodEnd();
    }
  }

  private static String getBroadcastAddress(InetAddress ipAddress, int subnet) throws UnknownHostException {
    try {
      logger.methodStart(new String[] { "ipAddress" }, new Object[] { ipAddress });
      if (ipAddress instanceof Inet4Address) {
        if (subnet < 1 || subnet > 32) {
          throw new IllegalArgumentException("illegal range of subnet(0-32).");
        }
        int ip = ByteBuffer.wrap(ipAddress.getAddress()).getInt();
        int broadcast = (((ip >> (32 - subnet)) + 1) << (32 - subnet)) - 1;
        byte[] byteAddress = ByteBuffer.allocate(4).putInt(broadcast).array();
        return InetAddress.getByAddress(byteAddress).getHostAddress();
      } else {
        throw new IllegalArgumentException("unsupport ipv6");
      }
    } finally {
      logger.methodEnd();
    }
  }

}
