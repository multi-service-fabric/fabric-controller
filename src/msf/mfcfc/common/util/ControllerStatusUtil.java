
package msf.mfcfc.common.util;

import static msf.mfcfc.common.util.CommandUtil.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusDeviceEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusInterfaceEntity;

/**
 * Utility class for controller status acquisition.
 *
 * @author NTT
 *
 */
public class ControllerStatusUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(ControllerStatusUtil.class);

  private static final String OS_CPU_REGEX = "^%Cpu\\(s\\): *\\d{0,}\\.\\d us, *\\d{0,}\\.\\d sy, *\\d{0,}\\.\\d ni, *"
      + "(\\d{0,}\\.\\d) id, *\\d{0,}\\.\\d wa, *\\d{0,}\\.\\d hi, *\\d{0,}\\.\\d si, *\\d{0,}\\.\\d st$";

  private static final String OS_MEM_REGEX = "^([KMGTPE]iB) Mem : *\\d{0,}\\.*\\d{0,} total, "
      + "*(\\d{0,}\\.*\\d{0,}) free, *(\\d{0,}\\.*\\d{0,}) used, *(\\d{0,}\\.*\\d{0,}) *buff/cache$";

  private static final String OS_SWAP_REGEX = "^([KMGTPE]iB) Swap: *\\d{0,}\\.*\\d{0,} total, "
      + "*\\d{0,}\\.*\\d{0,} free, *(\\d{0,}\\.*\\d{0,}) used\\. *\\d{0,}\\.*\\d{0,} *avail Mem $";

  private static final String CTR_CPU_MEM_REGEX = "^ *\\d{0,} *[\\w*\\-]* *\\d{0,} *\\d{0,} "
      + "*\\d{0,}\\.*\\d{0,}[mMgGtTpPeE]{0,} *(\\d{0,}\\.*\\d{0,}[mMgGtTpPeE]{0,}) *\\d{0,}\\.*\\d{0,}[mMgGtTpPeE]{0,} "
      + "*\\w *(\\d{0,}\\.\\d) *\\d{0,}\\.\\d";

  private static final String OS_DISK_REGEX = "^([/\\w*\\-\\.:]*) *(\\d{0,}) *(\\d{0,}) *(\\d{0,}) *\\d{0,}% "
      + "*([/\\w*\\-]*)";

  private static final String OS_TRAFFIC_REGEX = "\\d{2}:\\d{2}:\\d{2} *([/\\w*\\-]*) *(\\d{0,}\\.\\d{2})"
      + " *(\\d{0,}\\.\\d{2}) *(\\d{0,}\\.\\d{2}) *(\\d{0,}\\.\\d{2})";

  private static final String SCRIPT_PATH_RELATIVE_DIR = "../bin/";

  private static final String SBY_IPADDRESS_GET_SCRIPT = "fc_sby_ip_get.sh";

  private static final String SBY_TOP_COMMAND_SCRIPT = "fc_sby_top_cmd.sh";

  private static final String SBY_DF_COMMAND_SCRIPT = "fc_sby_df_cmd.sh";

  private static List<String> getTopCommandResult(String pid) throws MsfException {

    List<String> commandResultList = getCommand(
        new ProcessBuilder("env", "LANG=C", "top", "-b", "-d", "1", "-n", "2", "-p", pid), 17, null);

    if (commandResultList.size() == 17) {
      return commandResultList.subList(9, 17);
    } else {
      return commandResultList;
    }
  }

  private static List<String> getSbyTopCommandResult(String ipAddress) throws MsfException {

    List<String> commandResultList = getCommand(
        new ProcessBuilder("env", "LANG=C", SCRIPT_PATH_RELATIVE_DIR + SBY_TOP_COMMAND_SCRIPT, ipAddress), 17, null);

    if (commandResultList.size() == 17) {
      return commandResultList.subList(9, 17);
    } else {
      return commandResultList;
    }
  }

  private static List<String> getDfCommandResult() throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", "df", "-k"));

  }

  private static List<String> getSbyDfCommandResult(String ipAddress) throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", SCRIPT_PATH_RELATIVE_DIR + SBY_DF_COMMAND_SCRIPT, ipAddress));

  }

  private static List<String> getSarCommandResult() throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", "sar", "1", "1", "-n", "DEV"));

  }

  /**
   * Get the CPU usage rate by the OS.
   *
   * @param topResult
   *          Execution result of the top command
   * @param getInfos
   *          Acquired information
   * @param pid
   *          PID
   * @param ipAddress
   *          ACT: Empty / SBY : SBY Server IP Address
   * @return CPU usage rate by the OS
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static Float getOsCpu(List<String> topResult, List<GetInfo> getInfos, String pid, String ipAddress)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_CPU)) {
      return null;
    }

    if (topResult.isEmpty()) {
      if (ipAddress == null) {
        topResult.addAll(getTopCommandResult(pid));
      } else {
        topResult.addAll(getSbyTopCommandResult(ipAddress));
      }
    }
    Float osCpu = -1f;
    String line = topResult.get(2);
    if (!String.valueOf(GET_FAILED).equals(line)) {
      Float max = 100f;
      Pattern pattern = Pattern.compile(OS_CPU_REGEX);
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        osCpu = Float.parseFloat(matcher.group(1));
        BigDecimal bd = new BigDecimal(max - osCpu);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        osCpu = bd.floatValue();
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }
    return osCpu;
  }

  /**
   * Get the CPU usage rate by the controller.
   *
   * @param topResult
   *          Execution result of the top command
   * @param getInfos
   *          Acquired information
   * @param pid
   *          PID
   * @return CPU usage rate by the controller
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static Float getCtlCpu(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_CPU)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult.addAll(getTopCommandResult(pid));
    }
    Float ctrCpu = -1f;
    String line = topResult.get(7);
    if (!String.valueOf(GET_FAILED).equals(line)) {
      List<String> nprocResult = getCommand(new ProcessBuilder("env", "LANG=C", "nproc"));
      if (!String.valueOf(GET_FAILED).equals(nprocResult.get(0))) {
        Pattern pattern = Pattern.compile(CTR_CPU_MEM_REGEX);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          try {
            ctrCpu = Float.parseFloat(matcher.group(2));
            ctrCpu = ctrCpu / Float.parseFloat(nprocResult.get(0));
          } catch (NumberFormatException exception) {
            logger.warn("Result parsing failed. command=top or nproc");
          }
        } else {
          logger.warn("Result parsing failed. command=top");
        }
      }
    }
    return ctrCpu;
  }

  /**
   * Get the memory usage by the OS.
   *
   * @param topResult
   *          Execution result of the top command
   * @param getInfos
   *          Acquired information
   * @param pid
   *          PID
   * @param ipAddress
   *          ACT: Empty / SBY : SBY Server IP Address
   * @return Memory usage by the OS.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<Integer> getOsMem(List<String> topResult, List<GetInfo> getInfos, String pid, String ipAddress)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_MEM)) {
      return null;
    }

    if (topResult.isEmpty()) {
      if (ipAddress == null) {
        topResult.addAll(getTopCommandResult(pid));
      } else {
        topResult.addAll(getSbyTopCommandResult(ipAddress));
      }
    }
    String memLine = topResult.get(3);
    Integer used = GET_FAILED;
    Integer free = GET_FAILED;
    Integer buffCache = GET_FAILED;
    Integer swpd = GET_FAILED;
    if (!String.valueOf(GET_FAILED).equals(memLine)) {
      Pattern memPattern = Pattern.compile(OS_MEM_REGEX);
      Matcher memMatcher = memPattern.matcher(memLine);
      if (memMatcher.find()) {
        String unit = memMatcher.group(1);
        free = Integer.parseInt(convertOsMemSize(unit, memMatcher.group(2)));
        used = Integer.parseInt(convertOsMemSize(unit, memMatcher.group(3)));
        buffCache = Integer.parseInt(convertOsMemSize(unit, memMatcher.group(4)));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }

    String swapLine = topResult.get(4);
    if (!String.valueOf(GET_FAILED).equals(swapLine)) {
      Pattern swapPattern = Pattern.compile(OS_SWAP_REGEX);
      Matcher swapMatcher = swapPattern.matcher(swapLine);
      if (swapMatcher.find()) {
        String unit = swapMatcher.group(1);
        swpd = Integer.parseInt(convertOsMemSize(unit, swapMatcher.group(2)));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }

    List<Integer> resultList = new ArrayList<>();
    resultList.add(used);
    resultList.add(free);
    resultList.add(buffCache);
    resultList.add(swpd);
    return resultList;
  }

  /**
   * Get the memory usage by the the controller.
   *
   * @param topResult
   *          Execution result of the top command
   * @param getInfos
   *          Acquired information
   * @param pid
   *          PID
   * @return Memory usage by the the controller.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static Integer getCtlMem(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_MEM)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult.addAll(getTopCommandResult(pid));
    }
    Integer ctrMem = GET_FAILED;
    String line = topResult.get(7);
    if (!String.valueOf(GET_FAILED).equals(line)) {
      Pattern pattern = Pattern.compile(CTR_CPU_MEM_REGEX);
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        ctrMem = Integer.parseInt(convertProcessMemSize(matcher.group(1)));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }
    return ctrMem;
  }

  /**
   * Converts the process memory usage value by the top command to kilobytes.
   *
   * @param value
   *          Memory Usage
   * @return Memory usage by the the controller.
   */
  public static String convertProcessMemSize(String value) {
    String ret = value;

    try {
      String lastStr = value.substring(value.length() - 1);
      String numStr = value.substring(0, value.length() - 1);
      if (!lastStr.matches("^[0-9]*$")) {
        int exp = 0;
        if (lastStr.equals("m") || lastStr.equals("M")) {
          exp = 1;
        } else if (lastStr.equals("g") || lastStr.equals("G")) {
          exp = 2;
        } else if (lastStr.equals("t") || lastStr.equals("T")) {
          exp = 3;
        } else if (lastStr.equals("p") || lastStr.equals("P")) {
          exp = 4;
        } else if (lastStr.equals("e") || lastStr.equals("E")) {
          exp = 5;
        }
        float sourceValue = Float.parseFloat(numStr);
        float convertValue = sourceValue * (float) Math.pow(1024, exp);
        ret = new BigDecimal(convertValue).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
      }
    } catch (Exception exception) {
      logger.warn("Result parsing failed. command=top");
    }
    return ret;
  }

  /**
   * Converts the OS memory usage value by the top command to kilobytes.
   *
   * @param unit
   *          Unit of memory usage
   * @param value
   *          OS memory usage obtained by executing the top command
   * @return Memory usage by the OS (KB)
   */
  public static String convertOsMemSize(String unit, String value) {
    String ret = GET_FAILED.toString();
    try {
      int exp = 0;
      switch (unit) {
        case "MiB":
          exp = 1;
          break;
        case "GiB":
          exp = 2;
          break;
        case "TiB":
          exp = 3;
          break;
        case "PiB":
          exp = 4;
          break;
        case "EiB":
          exp = 5;
          break;
        default:
          exp = 0;
          break;
      }
      float sourceValue = Float.parseFloat(value);
      float convertValue = sourceValue * (float) Math.pow(1024, exp);
      ret = new BigDecimal(convertValue).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    } catch (Exception exception) {
      logger.warn("Result parsing failed. command=top");
    }

    return ret;
  }

  /**
   * Get the disk usage by the OS.
   *
   * @param dfResult
   *          Execution result of df command
   * @param getInfos
   *          Acquired information
   * @param deviceList
   *          Acquired Device information list
   * @param ipAddress
   *          ACT: Empty / SBY : SBY Server IP Address
   * @return Disk usage by the OS
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<SystemStatusDeviceEntity> getOsDisk(List<String> dfResult, List<GetInfo> getInfos,
      List<String> deviceList, String ipAddress) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_DISK)) {
      return null;
    }
    return getDisk(dfResult, deviceList, ipAddress);
  }

  /**
   * Get the disk usage by the controller.
   *
   * @param dfResult
   *          Execution result of df command
   * @param getInfos
   *          Acquired information
   * @param deviceList
   *          Acquired Device information list
   * @return Disk usage by the controller.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<SystemStatusDeviceEntity> getCtlDisk(List<String> dfResult, List<GetInfo> getInfos,
      List<String> deviceList) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_DISK)) {
      return null;
    }
    return getDisk(dfResult, deviceList, null);
  }

  /**
   * Get the disk usage by the controller.
   *
   * @param dfResult
   *          Execution result of df command
   * @param getInfos
   *          Acquired information
   * @param deviceList
   *          Acquired Device information list
   * @return Disk usage by the controller.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<SystemStatusDeviceEntity> getDisk(List<String> dfResult, List<String> deviceList, String ipAddress)
      throws MsfException {

    if (dfResult.isEmpty()) {
      if (ipAddress == null) {
        dfResult.addAll(getDfCommandResult());
      } else {
        dfResult.addAll(getSbyDfCommandResult(ipAddress));
      }
    }

    if (!dfResult.isEmpty()) {
      dfResult.remove(0);
    }
    List<SystemStatusDeviceEntity> deviceEntityList = new ArrayList<>();
    Pattern pattern = Pattern.compile(OS_DISK_REGEX);
    for (String line : dfResult) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find() && checkDevice(matcher.group(1), deviceList)) {
        SystemStatusDeviceEntity deviceEntity = new SystemStatusDeviceEntity();
        deviceEntity.setFileSystem(matcher.group(1));
        deviceEntity.setMountedOn((matcher.group(5)));
        deviceEntity.setSize(Integer.parseInt(matcher.group(2)));
        deviceEntity.setUsed(Integer.parseInt(matcher.group(3)));
        deviceEntity.setAvail(Integer.parseInt(matcher.group(4)));
        deviceEntityList.add(deviceEntity);
      } else if (!String.valueOf(GET_FAILED).equals(line)) {
        logger.warn("Result parsing failed. command=df");
        break;
      } else {
        break;
      }
    }
    if (deviceEntityList.isEmpty()) {
      deviceEntityList = null;
    }
    return deviceEntityList;
  }

  /**
   * Check whether the specified file system is in the acquired device
   * information list.
   *
   * @param fileSystem
   *          File System
   * @param deviceList
   *          Acquired Device information
   * @return true: When it's in the acquired device information list or there is
   *         no information list. false: When it's not in the acquired device
   *         information list.
   */
  public static boolean checkDevice(String fileSystem, List<String> deviceList) {
    boolean check = false;
    if (deviceList == null) {
      check = true;
    } else {
      for (String device : deviceList) {
        if (fileSystem.equals(device)) {
          check = true;
          break;
        }
      }
    }
    return check;
  }

  /**
   * Get the network traffic volume by the OS.
   *
   * @param getInfos
   *          Acquired information
   * @return Network traffic volume by the OS.
   * @throws MsfException
   *           If the OS command execution fails.
   */
  public static List<SystemStatusInterfaceEntity> getOsTraffic(List<GetInfo> getInfos) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_TRAFFIC)) {
      return null;
    }
    List<String> sarResult = getSarCommandResult();

    if (sarResult.size() > 2) {
      sarResult.remove(2);
      sarResult.remove(1);
      sarResult.remove(0);
    }
    List<SystemStatusInterfaceEntity> interfaceEntityList = new ArrayList<>();
    Pattern pattern = Pattern.compile(OS_TRAFFIC_REGEX);
    for (String line : sarResult) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        SystemStatusInterfaceEntity interfaceEntity = new SystemStatusInterfaceEntity();
        interfaceEntity.setIfname(matcher.group(1));
        interfaceEntity.setRxpck(Float.parseFloat(matcher.group(2)));
        interfaceEntity.setTxpck(Float.parseFloat(matcher.group(3)));
        interfaceEntity.setRxkb(Float.parseFloat(matcher.group(4)));
        interfaceEntity.setTxkb(Float.parseFloat(matcher.group(5)));
        interfaceEntityList.add(interfaceEntity);
      } else if (!String.valueOf(GET_FAILED).equals(line) && !line.equals("")) {
        logger.warn("Result parsing failed. command=sar");
        break;
      } else {
        break;
      }
    }
    if (interfaceEntityList.isEmpty()) {
      interfaceEntityList = null;
    }
    return interfaceEntityList;
  }

  /**
   * Get the PID.
   *
   * @return Process ID
   */
  public static String getProcessId() {
    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
    String vmName = bean.getName();
    String pid = vmName.split("@")[0];
    return pid;
  }

  /**
   * Get the IP address of the standby server.
   *
   * @return IP address
   */
  public static String getSbyIpAddress() {
    String ipAddress = GET_FAILED.toString();
    try {
      List<String> resultList = getCommand(
          new ProcessBuilder("env", "LANG=C", SCRIPT_PATH_RELATIVE_DIR + SBY_IPADDRESS_GET_SCRIPT));
      ipAddress = resultList.get(0);
      if (String.valueOf(GET_FAILED).equals(ipAddress)) {
        logger.warn("Result parsing failed. command=" + SBY_IPADDRESS_GET_SCRIPT);
      }
    } catch (Exception exception) {
      logger.warn("Result parsing failed. command=sby.sh");
    }
    return ipAddress;
  }

}
