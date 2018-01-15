
package msf.fc.core.status.scenario;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.rest.ec.status.data.ControllerStatusReadEcResponseBody;
import msf.fc.rest.ec.status.data.entity.ControllerStatusControllerEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusDeviceEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusEcStatusEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusEmStatusEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusInformationsEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusInterfaceEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusOsEcEntity;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ControllerStatus;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.core.status.scenario.AbstractStatusScenarioBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadRequest;
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
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for status confirmation.
 *
 * @author NTT
 *
 */
public class FcSystemStatusReadScenario extends AbstractStatusScenarioBase<SystemStatusReadRequest> {

  private SystemStatusReadRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcSystemStatusReadScenario.class);

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

  private static final Integer GET_FAILED = -1;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcSystemStatusReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(SystemStatusReadRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getController() != null) {
        ParameterCheckUtil.checkController(request.getController());
      }

      if (request.getCluster() != null) {
        ParameterCheckUtil.checkCluster(request.getCluster(),
            FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      }

      if (request.getGetInfo() != null) {
        ParameterCheckUtil.checkGetInfo(request.getGetInfo());
      }

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      List<String> controllers = getControllers(request.getController());
      List<String> getInfos = getGetInfos(request.getGetInfo());
      List<SystemStatusInformationEntity> informationList = new ArrayList<>();
      List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList = new ArrayList<>();
      List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList = new ArrayList<>();
      String clusterId = String
          .valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());

      if (controllers.contains(ControllerType.EC.getMessage())
          || controllers.contains(ControllerType.EM.getMessage())) {
        ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody = sendLogRead();
        informationList = getLowInformationList(controllerStatusReadEcResponseBody, controllers, clusterId);
        controllerServiceStatusList = getLowControllerServiceStatusList(controllerStatusReadEcResponseBody, controllers,
            clusterId, getInfos);
        controllerBlockadeStatusList = getLowControllerBlockadeStatusList(controllerStatusReadEcResponseBody,
            controllers, clusterId, getInfos);
      }

      if (controllers.contains(ControllerType.FC.getMessage())) {
        String pid = getProcessId();
        ProcessBuilder hostNameCommand = new ProcessBuilder("hostname");
        List<String> hostnameResult = new ArrayList<>();
        String hostName = getCommand(hostNameCommand, hostnameResult).get(0);
        if (String.valueOf(GET_FAILED).equals(hostName)) {
          String logMsg = MessageFormat.format("failed command. command = {0}", "hostname");
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
        String listeningAddress = FcConfigManager.getInstance().getRestServerListeningAddress();

        List<String> topResult = new ArrayList<>();

        SystemStatusCpuEntity osCpu = getOsCpu(topResult, getInfos, pid);

        SystemStatusMemoryEntity osMem = getOsMem(topResult, getInfos, pid);

        SystemStatusDiskEntity osDisk = getOsDisk(getInfos);

        SystemStatusTrafficEntity osTraffic = getOsTraffic(getInfos);

        Float ctrCpu = getCtrCpu(topResult, getInfos, pid);

        Integer ctrMem = getCtrMem(topResult, getInfos, pid);

        Integer receiveRequest = getRecvRestConut(getInfos);

        Integer sendRequest = getSendRestConut(getInfos);

        SystemStatusControllerServiceStatusEntity serviceStatus = getControllerServiceStataus(clusterId, getInfos);
        if (serviceStatus != null) {
          controllerServiceStatusList.add(serviceStatus);
        }

        SystemStatusControllerBlockadeStatusEntity blockadeStatus = getControllerBlockadeStataus(clusterId, getInfos);
        if (serviceStatus != null) {
          controllerBlockadeStatusList.add(blockadeStatus);
        }
        SystemStatusOsEntity osEntity = getSystemStatusOsEntity(osCpu, osMem, osDisk, osTraffic);
        SystemStatusControllerEntity controllerEntity = getSystemStatusControllerEntity(ctrCpu, ctrMem, receiveRequest,
            sendRequest, getInfos);
        SystemStatusInformationEntity fcInformationEntity = getFcInformationEntity(ControllerType.FC.getMessage(),
            clusterId, hostName, listeningAddress, osEntity, controllerEntity);
        if (fcInformationEntity != null) {
          informationList.add(fcInformationEntity);
        }
      }
      if (informationList.isEmpty()) {
        informationList = null;
      }
      String allServiceStatus = getAllServiceStatus(controllerServiceStatusList, getInfos);
      if (!ControllerStatus.WARNING.getMessage().equals(allServiceStatus)) {
        controllerServiceStatusList = null;
      }
      String allBlockadeStatus = getAllBlockadeStatus(controllerBlockadeStatusList, getInfos);
      if (!ControllerStatus.WARNING.getMessage().equals(allBlockadeStatus)) {
        controllerBlockadeStatusList = null;
      }

      RestResponseBase response = responseSystemStatusReadData(allServiceStatus, controllerServiceStatusList,
          allBlockadeStatus, controllerBlockadeStatusList, informationList);

      return response;
    } finally {
      logger.methodEnd();
    }
  }

  private List<SystemStatusInformationEntity> getLowInformationList(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<String> controllers,
      String clusterId) {
    SystemStatusInformationEntity lowInformationEntity = new SystemStatusInformationEntity();
    List<SystemStatusInformationEntity> informationList = new ArrayList<>();
    if (controllers.contains(ControllerType.EC.getMessage())) {
      lowInformationEntity = getLowInformationEntity(controllerStatusReadEcResponseBody, ControllerType.EC.getMessage(),
          clusterId);
      if (lowInformationEntity != null) {
        informationList.add(lowInformationEntity);
      }
    }
    if (controllers.contains(ControllerType.EM.getMessage())) {
      lowInformationEntity = getLowInformationEntity(controllerStatusReadEcResponseBody, ControllerType.EM.getMessage(),
          clusterId);
      if (lowInformationEntity != null) {
        informationList.add(lowInformationEntity);
      }
    }
    return informationList;
  }

  private List<SystemStatusControllerServiceStatusEntity> getLowControllerServiceStatusList(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<String> controllers, String clusterId,
      List<String> getInfos) {
    SystemStatusControllerServiceStatusEntity lowServiceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList = new ArrayList<>();
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return controllerServiceStatusList;
    }
    if (controllers.contains(ControllerType.EC.getMessage())) {
      lowServiceStatusEntity = getLowServiceStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EC.getMessage(), clusterId);
      controllerServiceStatusList.add(lowServiceStatusEntity);
    }
    if (controllers.contains(ControllerType.EM.getMessage())) {
      lowServiceStatusEntity = getLowServiceStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EM.getMessage(), clusterId);
      controllerServiceStatusList.add(lowServiceStatusEntity);
    }
    return controllerServiceStatusList;
  }

  private List<SystemStatusControllerBlockadeStatusEntity> getLowControllerBlockadeStatusList(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<String> controllers, String clusterId,
      List<String> getInfos) {
    SystemStatusControllerBlockadeStatusEntity lowBlockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList = new ArrayList<>();
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return controllerBlockadeStatusList;
    }
    if (controllers.contains(ControllerType.EC.getMessage())) {
      lowBlockadeStatusEntity = getLowBlockadeStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EC.getMessage(), clusterId);
      controllerBlockadeStatusList.add(lowBlockadeStatusEntity);
    }
    if (controllers.contains(ControllerType.EM.getMessage())) {
      lowBlockadeStatusEntity = getLowBlockadeStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EM.getMessage(), clusterId);
      controllerBlockadeStatusList.add(lowBlockadeStatusEntity);
    }
    return controllerBlockadeStatusList;
  }

  private SystemStatusInformationEntity getLowInformationEntity(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, String controllerType, String clusterId) {
    SystemStatusInformationEntity lowInformationEntity = null;
    if (controllerStatusReadEcResponseBody.getInformationList() != null) {
      lowInformationEntity = new SystemStatusInformationEntity();
      for (ControllerStatusInformationsEcEntity informationsEcEntity : controllerStatusReadEcResponseBody
          .getInformationList()) {
        if (controllerType.equals(informationsEcEntity.getControllerType())) {
          lowInformationEntity.setControllerType(informationsEcEntity.getControllerType());
          lowInformationEntity.setClusterId(clusterId);
          lowInformationEntity.setHostName(informationsEcEntity.getHostName());
          lowInformationEntity.setManagementIpAddress(informationsEcEntity.getManagementIpAddress());
          lowInformationEntity.setOs(getLowOsEntity(informationsEcEntity.getOs()));
          lowInformationEntity.setController(getLowControllerEntity(informationsEcEntity.getController()));
          break;
        }
      }
    }
    return lowInformationEntity;
  }

  private SystemStatusOsEntity getLowOsEntity(ControllerStatusOsEcEntity osLowEntity) {
    SystemStatusOsEntity osEntity = null;
    if (osLowEntity != null) {
      osEntity = new SystemStatusOsEntity();
      osEntity.setCpu(getLowOsCpuEntity(osLowEntity));
      osEntity.setMemory(getLowOsMemEntity(osLowEntity));
      osEntity.setDisk(getLowOsDiskEntity(osLowEntity));
      osEntity.setTraffic(getLowOsTrafficEntity(osLowEntity));
    }
    return osEntity;
  }

  private SystemStatusCpuEntity getLowOsCpuEntity(ControllerStatusOsEcEntity osLowEntity) {
    SystemStatusCpuEntity osCpuEntity = null;
    if (osLowEntity.getCpu() != null) {
      osCpuEntity = new SystemStatusCpuEntity();
      osCpuEntity.setUseRate(osLowEntity.getCpu().getUseRate());
    }
    return osCpuEntity;
  }

  private SystemStatusMemoryEntity getLowOsMemEntity(ControllerStatusOsEcEntity osLowEntity) {
    SystemStatusMemoryEntity osMemEntity = null;
    if (osLowEntity.getMemory() != null) {
      osMemEntity = new SystemStatusMemoryEntity();
      osMemEntity.setUsed(osLowEntity.getMemory().getUsed());
      osMemEntity.setFree(osLowEntity.getMemory().getFree());
      osMemEntity.setBuffCache(osLowEntity.getMemory().getBuffCache());
      osMemEntity.setSwpd(osLowEntity.getMemory().getSwpd());
    }
    return osMemEntity;
  }

  private SystemStatusDiskEntity getLowOsDiskEntity(ControllerStatusOsEcEntity osLowEntity) {
    if (osLowEntity.getDisk() == null) {
      return null;
    }
    SystemStatusDiskEntity osDiskEntity = new SystemStatusDiskEntity();
    List<SystemStatusDeviceEntity> deviceEntityList = null;
    if (osLowEntity.getDisk().getDeviceList() != null) {
      deviceEntityList = new ArrayList<>();
      for (ControllerStatusDeviceEcEntity lowDeviceEntity : osLowEntity.getDisk().getDeviceList()) {
        SystemStatusDeviceEntity deviceEntity = new SystemStatusDeviceEntity();
        deviceEntity.setFileSystem(lowDeviceEntity.getFileSystem());
        deviceEntity.setMountedOn(lowDeviceEntity.getMountedOn());
        deviceEntity.setSize(lowDeviceEntity.getSize());
        deviceEntity.setUsed(lowDeviceEntity.getUsed());
        deviceEntity.setAvail(lowDeviceEntity.getAvail());
        deviceEntityList.add(deviceEntity);
      }
    }
    osDiskEntity.setDeviceList(deviceEntityList);
    return osDiskEntity;
  }

  private SystemStatusTrafficEntity getLowOsTrafficEntity(ControllerStatusOsEcEntity osLowEntity) {
    if (osLowEntity.getTraffic() == null) {
      return null;
    }
    SystemStatusTrafficEntity osTrafficEntity = new SystemStatusTrafficEntity();
    List<SystemStatusInterfaceEntity> trafficEntityList = null;
    if (osLowEntity.getTraffic().getInterfaceList() != null) {
      trafficEntityList = new ArrayList<>();
      for (ControllerStatusInterfaceEcEntity lowDeviceEntity : osLowEntity.getTraffic().getInterfaceList()) {
        SystemStatusInterfaceEntity trafficEntity = new SystemStatusInterfaceEntity();
        trafficEntity.setIfname(lowDeviceEntity.getIfname());
        trafficEntity.setRxpck(lowDeviceEntity.getRxpck());
        trafficEntity.setTxpck(lowDeviceEntity.getTxpck());
        trafficEntity.setRxkb(lowDeviceEntity.getRxkb());
        trafficEntity.setTxkb(lowDeviceEntity.getTxkb());
        trafficEntityList.add(trafficEntity);
      }
    }
    osTrafficEntity.setInterfaceList(trafficEntityList);
    return osTrafficEntity;
  }

  private SystemStatusControllerEntity getLowControllerEntity(ControllerStatusControllerEcEntity controllerLowEntity) {
    SystemStatusControllerEntity controllerEntity = null;
    if (controllerLowEntity != null) {
      controllerEntity = new SystemStatusControllerEntity();
      controllerEntity.setCpu(controllerLowEntity.getCpu());
      controllerEntity.setMemory(controllerLowEntity.getMemory());
      controllerEntity.setReceiveRequest(controllerLowEntity.getReceiveRestRequest());
      controllerEntity.setSendRequest(controllerLowEntity.getSendRestRequest());
    }
    return controllerEntity;
  }

  private SystemStatusControllerBlockadeStatusEntity getLowBlockadeStatusEntity(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, String controller, String clusterId) {
    SystemStatusControllerBlockadeStatusEntity lowBlockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    lowBlockadeStatusEntity.setClusterId(clusterId);
    lowBlockadeStatusEntity.setControllerType(controller);
    SystemStatusBlockadeEntity blockadeEntity = null;
    if (ControllerType.EC.getMessage().equals(controller)) {
      blockadeEntity = new SystemStatusBlockadeEntity();
      if (controllerStatusReadEcResponseBody.getEcStatus().getBusy().equals("inservice")) {
        blockadeEntity.setBlockadeStatus("none");
      } else if (controllerStatusReadEcResponseBody.getEcStatus().getBusy().equals("busy")) {
        blockadeEntity.setBlockadeStatus("blockade");
      } else {
        blockadeEntity.setBlockadeStatus("unknown");
      }
    }
    lowBlockadeStatusEntity.setStatus(blockadeEntity);
    return lowBlockadeStatusEntity;
  }

  private SystemStatusControllerServiceStatusEntity getLowServiceStatusEntity(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, String controller, String clusterId) {
    SystemStatusControllerServiceStatusEntity lowServiceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    lowServiceStatusEntity.setControllerType(controller);
    lowServiceStatusEntity.setClusterId(clusterId);
    SystemStatusServiceStatusEntity serviceStatusEntity = new SystemStatusServiceStatusEntity();
    if (ControllerType.EC.getMessage().equals(controller)) {
      if (controllerStatusReadEcResponseBody.getEcStatus().getStatus().equals("inservice")) {
        serviceStatusEntity.setServiceStatus("running");
      } else if (controllerStatusReadEcResponseBody.getEcStatus().getStatus().equals("startready")) {
        serviceStatusEntity.setServiceStatus("start-up in progress");
      } else if (controllerStatusReadEcResponseBody.getEcStatus().getStatus().equals("stopready")) {
        serviceStatusEntity.setServiceStatus("shutdown in progress");
      } else if (controllerStatusReadEcResponseBody.getEcStatus().getStatus().equals("changeover")) {
        serviceStatusEntity.setServiceStatus("system switching");
      } else {
        serviceStatusEntity.setServiceStatus("unknown");
      }
    } else {
      if (controllerStatusReadEcResponseBody.getEmStatus().getStatus().equals("inservice")) {
        serviceStatusEntity.setServiceStatus("running");
      } else if (controllerStatusReadEcResponseBody.getEmStatus().getStatus().equals("startready")) {
        serviceStatusEntity.setServiceStatus("start-up in progress");
      } else if (controllerStatusReadEcResponseBody.getEmStatus().getStatus().equals("stopready")) {
        serviceStatusEntity.setServiceStatus("shutdown in progress");
      } else if (controllerStatusReadEcResponseBody.getEmStatus().getStatus().equals("changeover")) {
        serviceStatusEntity.setServiceStatus("system switching");
      } else {
        serviceStatusEntity.setServiceStatus("unknown");
      }
    }
    lowServiceStatusEntity.setStatus(serviceStatusEntity);
    return lowServiceStatusEntity;
  }

  private SystemStatusControllerEntity getSystemStatusControllerEntity(Float cpu, Integer memory,
      Integer receiveRequest, Integer sendRequest, List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_CPU.getMessage()) && !getInfos.contains(GetInfo.CTR_MEM.getMessage())
        && !getInfos.contains(GetInfo.CTR_RECEIVE_REG.getMessage())
        && !getInfos.contains(GetInfo.CTR_SEND_REG.getMessage())) {
      return null;
    }
    SystemStatusControllerEntity controllerEntity = new SystemStatusControllerEntity();
    controllerEntity.setCpu(cpu);
    controllerEntity.setMemory(memory);
    controllerEntity.setReceiveRequest(receiveRequest);
    controllerEntity.setSendRequest(sendRequest);
    return controllerEntity;
  }

  private SystemStatusOsEntity getSystemStatusOsEntity(SystemStatusCpuEntity cpuEntity,
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

  private SystemStatusInformationEntity getFcInformationEntity(String controllerType, String clusterId, String hostName,
      String managementpAddress, SystemStatusOsEntity osEntity, SystemStatusControllerEntity controllerEntity) {
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

  private ControllerStatusReadEcResponseBody sendLogRead() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();
      String ecController = request.getController().replace("+mfc", "").replace("mfc+", "").replace("+fc", "")
          .replace("fc+", "");
      String targetUri = EcRequestUri.STATUS_READ.getUri();

      targetUri = targetUri + "?controller=" + ecController + "&get_info=" + request.getGetInfo();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.STATUS_READ.getHttpMethod(), targetUri,
          null, ecControlIpAddress, ecControlPort);

      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody = new ControllerStatusReadEcResponseBody();
      controllerStatusReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          ControllerStatusReadEcResponseBody.class, ErrorCode.EC_CONNECTION_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          controllerStatusReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONNECTION_ERROR);

      return controllerStatusReadEcResponseBody;
    } catch (MsfException msfe) {
      ControllerStatusReadEcResponseBody ecResponseBody = new ControllerStatusReadEcResponseBody();
      ControllerStatusEcStatusEcEntity ecEntity = new ControllerStatusEcStatusEcEntity();
      ecEntity.setStatus("unknown");
      ecEntity.setBusy("unknown");
      ControllerStatusEmStatusEcEntity emEntity = new ControllerStatusEmStatusEcEntity();
      emEntity.setStatus("unknown");
      ecResponseBody.setEcStatus(ecEntity);
      ecResponseBody.setEmStatus(emEntity);
      return ecResponseBody;
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

  private List<String> getControllers(String controller) {
    if (controller == null) {
      controller = "fc+ec+em";
      request.setController(controller);
    }

    List<String> logLevels = Arrays.asList(controller.split("\\+", 0));
    return logLevels;
  }

  private List<String> getGetInfos(String getInfo) {
    if (getInfo == null) {

      getInfo = "os-cpu+os-mem+os-disk+os-traffic+ctr-cpu+ctr-mem+ctr-state+ctr-receive_req+ctr-send_req";
      request.setGetInfo(getInfo);
    }

    List<String> getInfos = Arrays.asList(getInfo.split("\\+", 0));
    return getInfos;
  }

  private List<String> getCommand(ProcessBuilder command, List<String> result) throws MsfException {
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

  private SystemStatusCpuEntity getOsCpu(List<String> topResult, List<String> getInfos, String pid)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_CPU.getMessage())) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getCommand(new ProcessBuilder("env", "LANG=C", "top", "-b", "-n", "1", "-p", pid), topResult);
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

  private SystemStatusMemoryEntity getOsMem(List<String> topResult, List<String> getInfos, String pid)
      throws MsfException {
    if (!getInfos.contains(GetInfo.OS_MEM.getMessage())) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getCommand(new ProcessBuilder("env", "LANG=C", "top", "-b", "-n", "1", "-p", pid), topResult);
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

  private Integer getCtrMem(List<String> topResult, List<String> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_MEM.getMessage())) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getCommand(new ProcessBuilder("env", "LANG=C", "top", "-b", "-n", "1", "-p", pid), topResult);
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

  private Float getCtrCpu(List<String> topResult, List<String> getInfos, String pid) throws MsfException {
    if (!getInfos.contains(GetInfo.CTR_CPU.getMessage())) {
      return null;
    }

    if (topResult.isEmpty()) {
      topResult = getCommand(new ProcessBuilder("env", "LANG=C", "top", "-b", "-n", "1", "-p", pid), topResult);
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

  private SystemStatusDiskEntity getOsDisk(List<String> getInfos) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_DISK.getMessage())) {
      return null;
    }
    SystemStatusDiskEntity systemStatusDiskEntity = new SystemStatusDiskEntity();
    List<String> dfResult = new ArrayList<>();
    dfResult = getCommand(new ProcessBuilder("env", "LANG=C", "df", "-k"), dfResult);

    if (dfResult.size() > 0) {
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
    systemStatusDiskEntity.setDeviceList(deviceEntityList);
    return systemStatusDiskEntity;
  }

  private SystemStatusTrafficEntity getOsTraffic(List<String> getInfos) throws MsfException {
    if (!getInfos.contains(GetInfo.OS_TRAFFIC.getMessage())) {
      return null;
    }
    SystemStatusTrafficEntity systemStatusTrafficEntity = new SystemStatusTrafficEntity();
    List<String> sarResult = new ArrayList<>();
    sarResult = getCommand(new ProcessBuilder("env", "LANG=C", "sar", "1", "1", "-n", "DEV"), sarResult);

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
    systemStatusTrafficEntity.setInterfaceList(interfaceEntityList);
    return systemStatusTrafficEntity;
  }

  private SystemStatusControllerServiceStatusEntity getControllerServiceStataus(String clusterId,
      List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return null;
    }

    SystemStatus status = SystemStatusManager.getInstance().getSystemStatus();
    SystemStatusControllerServiceStatusEntity fcServiceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    String serviceStatus = status.getServiceStatusEnum().getMessage();
    fcServiceStatusEntity.setClusterId(clusterId);
    fcServiceStatusEntity.setControllerType(ControllerType.FC.getMessage());
    SystemStatusServiceStatusEntity serviceStatusEntity = new SystemStatusServiceStatusEntity();
    serviceStatusEntity.setServiceStatus(serviceStatus);
    fcServiceStatusEntity.setStatus(serviceStatusEntity);
    return fcServiceStatusEntity;
  }

  private SystemStatusControllerBlockadeStatusEntity getControllerBlockadeStataus(String clusterId,
      List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return null;
    }

    SystemStatus status = SystemStatusManager.getInstance().getSystemStatus();
    SystemStatusControllerBlockadeStatusEntity fcBlockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    String blockadeStatus = status.getBlockadeStatusEnum().getMessage();
    fcBlockadeStatusEntity.setClusterId(clusterId);
    fcBlockadeStatusEntity.setControllerType(ControllerType.FC.getMessage());
    SystemStatusBlockadeEntity blockadeEntity = new SystemStatusBlockadeEntity();
    blockadeEntity.setBlockadeStatus(blockadeStatus);
    fcBlockadeStatusEntity.setStatus(blockadeEntity);
    return fcBlockadeStatusEntity;
  }

  private String getAllServiceStatus(List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList,
      List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return null;
    }
    for (SystemStatusControllerServiceStatusEntity service : controllerServiceStatusList) {
      if (!ServiceStatus.STARTED.getMessage().equals(service.getStatus().getServiceStatus())) {
        return "warning";
      }
    }
    return "running";
  }

  private String getAllBlockadeStatus(List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList,
      List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_STATE.getMessage())) {
      return null;
    }
    boolean isBlockade = false;
    for (SystemStatusControllerBlockadeStatusEntity blockade : controllerBlockadeStatusList) {
      if (blockade.getStatus() != null) {
        if (BlockadeStatus.BLOCKADE.getMessage().equals(blockade.getStatus().getBlockadeStatus())) {
          isBlockade = true;
        } else if (!BlockadeStatus.NONE.getMessage().equals(blockade.getStatus().getBlockadeStatus())) {
          return "warning";
        }
      }

    }
    if (isBlockade) {
      return "blockade";
    }
    return "none";
  }

  private Integer getRecvRestConut(List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_RECEIVE_REG.getMessage())) {
      return null;
    }
    return AbstractRestHandler.getRecvCount();
  }

  private Integer getSendRestConut(List<String> getInfos) {
    if (!getInfos.contains(GetInfo.CTR_SEND_REG.getMessage())) {
      return null;
    }
    return RestClient.getSendCount();
  }

  private String getProcessId() {
    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
    String vmName = bean.getName();
    String pid = vmName.split("@")[0];
    return pid;
  }

  private RestResponseBase responseSystemStatusReadData(String serviceStatus,
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

}
