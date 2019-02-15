
package msf.mfcfc.core.status.scenario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ControllerStatus;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.log.scenario.AbstractLogScenarioBase;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequest;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadResponseBody;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusBlockadeEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerBlockadeStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerServiceStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusCpuEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusDeviceEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusDiskEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusInformationEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusInterfaceEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusMemoryEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusOsEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusServiceStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusTrafficEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of the system status-related
 * processing in the system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractStatusScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractLogScenarioBase.class);

  private static final String OS_CPU_REGEX = "^%Cpu\\(s\\): *\\d{0,}\\.\\d us, *\\d{0,}\\.\\d sy, *\\d{0,}\\.\\d ni, *"
      + "(\\d{0,}\\.\\d) id, *\\d{0,}\\.\\d wa, *\\d{0,}\\.\\d hi, *\\d{0,}\\.\\d si, *\\d{0,}\\.\\d st$";

  private static final String OS_MEM_REGEX = "^KiB Mem : *\\d{0,} total, *(\\d{0,}) free, *(\\d{0,}) used, *(\\d{0,}) *"
      + "buff/cache$";

  private static final String OS_SWAP_REGEX = "^KiB Swap: *\\d{0,} total, *\\d{0,} free, *(\\d{0,}) used\\. *\\d{0,} *"
      + "avail Mem $";

  private static final String CTR_MEM_REGEX = "^ *\\d{0,} *[\\w*\\-]* *\\d{0,} *\\d{0,} *\\d{0,}\\.*\\d{0,}[mg]{0,} *(\\d{0,}) *\\d{0,} *\\w "
      + "*(\\d{0,}\\.\\d) *\\d{0,}\\.\\d";

  private static final String OS_DISK_REGEX = "^([/\\w*\\-\\.:]*) *(\\d{0,}) *(\\d{0,}) *(\\d{0,}) *\\d{0,}% *([/\\w*\\-]*)";

  private static final String OS_TRAFFIC_REGEX = "\\d{2}:\\d{2}:\\d{2} *([/\\w*\\-]*) *(\\d{0,}\\.\\d{2})"
      + " *(\\d{0,}\\.\\d{2}) *(\\d{0,}\\.\\d{2}) *(\\d{0,}\\.\\d{2})";

  protected static final Integer GET_FAILED = -1;

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<ControllerType> getControllers(String controller) {
    List<ControllerType> controllers = new ArrayList<>();
    if (controller == null) {

      controllers.add(ControllerType.MFC);
      controllers.add(ControllerType.FC);
      controllers.add(ControllerType.EC);
      controllers.add(ControllerType.EM);
      return controllers;
    }

    List<String> controllerStrings = Arrays.asList(controller.split("\\+", 0));
    for (String controllerString : controllerStrings) {
      controllers.add(ControllerType.getEnumFromMessage(controllerString));
    }
    return controllers;
  }

  protected String getControllersStrings(List<ControllerType> controllers, ControllerType controllerType) {

    StringJoiner joiner = new StringJoiner("+");
    controllers.stream().filter(controller -> (controller.ordinal() - controllerType.ordinal()) > 0)
        .forEach(e -> joiner.add(e.getMessage()));
    return joiner.toString();
  }

  protected List<GetInfo> getGetInfos(String getInfo) {
    List<GetInfo> getInfos = new ArrayList<>();
    if (getInfo == null) {

      getInfos.add(GetInfo.OS_CPU);
      getInfos.add(GetInfo.OS_MEM);
      getInfos.add(GetInfo.OS_DISK);
      getInfos.add(GetInfo.OS_TRAFFIC);
      getInfos.add(GetInfo.CTR_CPU);
      getInfos.add(GetInfo.CTR_MEM);
      getInfos.add(GetInfo.CTR_STATE);
      getInfos.add(GetInfo.CTR_RECEIVE_REQ);
      getInfos.add(GetInfo.CTR_SEND_REQ);
      return getInfos;
    }

    List<String> getInfosStrings = Arrays.asList(getInfo.split("\\+", 0));
    for (String getInfosString : getInfosStrings) {
      getInfos.add(GetInfo.getEnumFromMessage(getInfosString));
    }
    return getInfos;
  }

  protected String getInformationStrings(List<GetInfo> getInfos) {
    StringJoiner joiner = new StringJoiner("+");
    getInfos.stream().forEach(e -> joiner.add(e.getMessage()));
    return joiner.toString();
  }

  protected List<String> getCommand(ProcessBuilder command, List<String> result) throws MsfException {
    InputStream inputStream = null;
    BufferedReader bufferedReader = null;
    try {
      Process process = command.start();
      inputStream = process.getInputStream();
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
        result.add(line);
      }
      if (result.isEmpty()) {
        logger.warn(MessageFormat.format("Command failed. command={0}", command.command()));

        for (int i = 0; i < 8; i++) {
          result.add(String.valueOf(GET_FAILED));
        }
      }
      return result;
    } catch (IOException ioException) {
      logger.warn(MessageFormat.format("IOException occurred. command={0}", command.command()));

      for (int i = 0; i < 8; i++) {
        result.add(String.valueOf(GET_FAILED));
      }
      return result;
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException ioe) {

      }
    }
  }

  private List<String> getTopCommandResult(List<String> topResult, String pid) throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", "top", "-b", "-n", "1", "-p", pid), topResult);

  }

  private List<String> getDfCommandResult(List<String> dfResult) throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", "df", "-k"), dfResult);

  }

  private List<String> getSarCommandResult(List<String> sarResult) throws MsfException {
    return getCommand(new ProcessBuilder("env", "LANG=C", "sar", "1", "1", "-n", "DEV"), sarResult);

  }

  protected SystemStatusCpuEntity getOsCpu(List<String> topResult, List<GetInfo> getInfos, String pid)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_CPU)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getTopCommandResult(topResult, pid);
    }
    SystemStatusCpuEntity cpuEntity = new SystemStatusCpuEntity();
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
    cpuEntity.setUseRate(osCpu);
    return cpuEntity;
  }

  protected SystemStatusMemoryEntity getOsMem(List<String> topResult, List<GetInfo> getInfos, String pid)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_MEM)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getTopCommandResult(topResult, pid);
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
        free = Integer.parseInt(memMatcher.group(1));
        used = Integer.parseInt(memMatcher.group(2));
        buffCache = Integer.parseInt(memMatcher.group(3));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }

    String swapLine = topResult.get(4);
    if (!String.valueOf(GET_FAILED).equals(swapLine)) {
      Pattern swapPattern = Pattern.compile(OS_SWAP_REGEX);
      Matcher swapMatcher = swapPattern.matcher(swapLine);
      if (swapMatcher.find()) {
        swpd = Integer.parseInt(swapMatcher.group(1));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }

    SystemStatusMemoryEntity memoryEntity = new SystemStatusMemoryEntity();
    memoryEntity.setUsed(used);
    memoryEntity.setFree(free);
    memoryEntity.setBuffCache(buffCache);
    memoryEntity.setSwpd(swpd);
    return memoryEntity;
  }

  protected Integer getCtrMem(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_MEM)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getTopCommandResult(topResult, pid);
    }
    Integer ctrMem = GET_FAILED;
    String line = topResult.get(7);
    if (!String.valueOf(GET_FAILED).equals(line)) {
      Pattern pattern = Pattern.compile(CTR_MEM_REGEX);
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        ctrMem = Integer.parseInt(matcher.group(1));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }
    return ctrMem;
  }

  protected Float getCtrCpu(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_CPU)) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getTopCommandResult(topResult, pid);
    }
    Float ctrCpu = -1f;
    List<String> nprocResult = new ArrayList<>();
    nprocResult = getCommand(new ProcessBuilder("env", "LANG=C", "nproc"), nprocResult);
    String line = topResult.get(7);
    if (!String.valueOf(GET_FAILED).equals(line) && !String.valueOf(GET_FAILED).equals(nprocResult.get(0))) {
      Pattern pattern = Pattern.compile(CTR_MEM_REGEX);
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        ctrCpu = Float.parseFloat(matcher.group(2));
        ctrCpu = ctrCpu / Float.parseFloat(nprocResult.get(0));
      } else {
        logger.warn("Result parsing failed. command=top");
      }
    }
    return ctrCpu;
  }

  protected SystemStatusDiskEntity getOsDisk(List<GetInfo> getInfos) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_DISK)) {
      return null;
    }
    List<String> dfResult = new ArrayList<>();
    dfResult = getDfCommandResult(dfResult);

    if (!dfResult.isEmpty()) {
      dfResult.remove(0);
    }
    List<SystemStatusDeviceEntity> deviceEntityList = new ArrayList<>();
    Pattern pattern = Pattern.compile(OS_DISK_REGEX);
    for (String line : dfResult) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
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
    SystemStatusDiskEntity systemStatusDiskEntity = new SystemStatusDiskEntity();
    systemStatusDiskEntity.setDeviceList(deviceEntityList);
    return systemStatusDiskEntity;
  }

  protected SystemStatusTrafficEntity getOsTraffic(List<GetInfo> getInfos) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_TRAFFIC)) {
      return null;
    }
    List<String> sarResult = new ArrayList<>();
    sarResult = getSarCommandResult(sarResult);

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

    SystemStatusTrafficEntity systemStatusTrafficEntity = new SystemStatusTrafficEntity();
    systemStatusTrafficEntity.setInterfaceList(interfaceEntityList);
    return systemStatusTrafficEntity;
  }

  protected Integer getRecvRestConut(List<GetInfo> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_RECEIVE_REQ)) {
      return null;
    }
    return AbstractRestHandler.getRecvCount();
  }

  protected Integer getSendRestConut(List<GetInfo> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_SEND_REQ)) {
      return null;
    }
    return RestClient.getSendCount();
  }

  protected String getProcessId() {
    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
    String vmName = bean.getName();
    String pid = vmName.split("@")[0];
    return pid;
  }

  protected SystemStatusControllerServiceStatusEntity getControllerServiceStataus(String clusterId,
      List<GetInfo> getInfos, ControllerType controllerType) {
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return null;
    }

    SystemStatus status = SystemStatusManager.getInstance().getSystemStatus();
    SystemStatusControllerServiceStatusEntity serviceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    String serviceStatus = status.getServiceStatusEnum().getMessage();
    serviceStatusEntity.setClusterId(clusterId);
    serviceStatusEntity.setControllerType(controllerType.getMessage());
    SystemStatusServiceStatusEntity statusEntity = new SystemStatusServiceStatusEntity();
    statusEntity.setServiceStatus(serviceStatus);
    serviceStatusEntity.setStatus(statusEntity);
    return serviceStatusEntity;
  }

  protected SystemStatusControllerBlockadeStatusEntity getControllerBlockadeStataus(String clusterId,
      List<GetInfo> getInfos, ControllerType controllerType) {
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return null;
    }

    SystemStatus status = SystemStatusManager.getInstance().getSystemStatus();
    SystemStatusControllerBlockadeStatusEntity blockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    String blockadeStatus = status.getBlockadeStatusEnum().getMessage();
    blockadeStatusEntity.setClusterId(clusterId);
    blockadeStatusEntity.setControllerType(controllerType.getMessage());
    SystemStatusBlockadeEntity blockadeEntity = new SystemStatusBlockadeEntity();
    blockadeEntity.setBlockadeStatus(blockadeStatus);
    blockadeStatusEntity.setStatus(blockadeEntity);
    return blockadeStatusEntity;
  }

  protected SystemStatusOsEntity getSystemStatusOsEntity(SystemStatusCpuEntity cpuEntity,
      SystemStatusMemoryEntity memoryEntity, SystemStatusDiskEntity diskEntity,
      SystemStatusTrafficEntity trafficEntity) {
    if (cpuEntity == null && memoryEntity == null && diskEntity == null && trafficEntity == null) {
      return null;
    }
    SystemStatusOsEntity osEntity = new SystemStatusOsEntity();
    osEntity.setCpu(cpuEntity);
    osEntity.setMemory(memoryEntity);

    osEntity.setDisk(diskEntity);
    osEntity.setTraffic(trafficEntity);
    return osEntity;
  }

  protected SystemStatusControllerEntity getSystemStatusControllerEntity(Float cpu, Integer memory,
      Integer receiveRequest, Integer sendRequest, List<GetInfo> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_CPU) && !getInfos.contains(GetInfo.CTR_MEM)
        && !getInfos.contains(GetInfo.CTR_RECEIVE_REQ) && !getInfos.contains(GetInfo.CTR_SEND_REQ)) {
      return null;
    }
    SystemStatusControllerEntity controllerEntity = new SystemStatusControllerEntity();
    controllerEntity.setCpu(cpu);
    controllerEntity.setMemory(memory);
    controllerEntity.setReceiveRequest(receiveRequest);
    controllerEntity.setSendRequest(sendRequest);
    return controllerEntity;
  }

  protected SystemStatusInformationEntity getFcInformationEntity(String controllerType, String clusterId,
      String hostName, String managementpAddress, SystemStatusOsEntity osEntity,
      SystemStatusControllerEntity controllerEntity) {
    if (osEntity == null && controllerEntity == null) {
      return null;
    }
    SystemStatusInformationEntity informationEntity = new SystemStatusInformationEntity();
    informationEntity.setControllerType(controllerType);
    informationEntity.setClusterId(clusterId);
    informationEntity.setHostName(hostName);
    informationEntity.setManagementIpAddress(managementpAddress);
    informationEntity.setOs(osEntity);
    informationEntity.setController(controllerEntity);

    return informationEntity;
  }

  protected String getAllServiceStatus(List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList,
      List<GetInfo> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return null;
    }
    for (SystemStatusControllerServiceStatusEntity service : controllerServiceStatusList) {
      if (!ServiceStatus.STARTED.equals(service.getStatus().getServiceStatusEnum())) {
        return ControllerStatus.WARNING.getMessage();
      }
    }
    return ControllerStatus.RUNNING.getMessage();
  }

  protected String getAllBlockadeStatus(List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList,
      List<GetInfo> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return null;
    }
    boolean isBlockade = false;
    for (SystemStatusControllerBlockadeStatusEntity blockade : controllerBlockadeStatusList) {
      if (blockade.getStatus() != null) {
        if (BlockadeStatus.BLOCKADE.equals(blockade.getStatus().getBlockadeStatusEnum())) {
          isBlockade = true;
        } else if (!BlockadeStatus.NONE.equals(blockade.getStatus().getBlockadeStatusEnum())) {
          return BlockadeStatus.WARNING.getMessage();
        }
      }

    }
    if (isBlockade) {
      return BlockadeStatus.BLOCKADE.getMessage();
    }
    return BlockadeStatus.NONE.getMessage();
  }

  protected RestResponseBase responseSystemStatusReadData(String serviceStatus,
      List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList, String blockadeStatus,
      List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList,
      List<SystemStatusInformationEntity> informationList) {
    try {
      logger.methodStart();
      SystemStatusReadResponseBody body = createResponseBody(serviceStatus, controllerServiceStatusList, blockadeStatus,
          controllerBlockadeStatusList, informationList);
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private SystemStatusReadResponseBody createResponseBody(String serviceStatus,
      List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList, String blockadeStatus,
      List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList,
      List<SystemStatusInformationEntity> informationList) {
    try {
      logger.methodStart();
      SystemStatusReadResponseBody responseBody = new SystemStatusReadResponseBody();
      responseBody.setServiceStatus(serviceStatus);
      responseBody.setControllerServiceStatusList(controllerServiceStatusList);
      responseBody.setBlockadeStatus(blockadeStatus);
      responseBody.setControllerBlockadeStatusList(controllerBlockadeStatusList);
      responseBody.setInformationList(informationList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected void sendStatusNotify(String ipAddress, int port, int noticeRetryNum, int noticeTimeout,
      SystemStatusNotifyRequest request) {
    for (int retryNum = 0; retryNum < noticeRetryNum + 1; retryNum++) {
      if (sendStatus(ipAddress, port, noticeTimeout, request) != null) {
        break;
      }
    }
  }

  private RestResponseBase sendStatus(String ipAddress, int port, int noticeTimeout,
      SystemStatusNotifyRequest request) {
    RestResponseBase restResponseBase = new RestResponseBase();
    try {
      String targetUri = MfcFcRequestUri.STATUS_NOTIFY.getUri();
      restResponseBase = RestClient.sendRequest(MfcFcRequestUri.STATUS_NOTIFY.getHttpMethod(), targetUri, request,
          ipAddress, port);
    } catch (MsfException msf) {
      try {
        restResponseBase = null;
        TimeUnit.MILLISECONDS.sleep(noticeTimeout);
      } catch (InterruptedException ie) {

      }
    }
    return restResponseBase;
  }

  protected RestResponseBase responsStatusNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
