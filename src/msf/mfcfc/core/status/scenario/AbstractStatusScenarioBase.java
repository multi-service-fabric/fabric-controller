
package msf.mfcfc.core.status.scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

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
import msf.mfcfc.common.util.ControllerStatusUtil;
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

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractStatusScenarioBase.class);

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

  protected SystemStatusCpuEntity getOsCpu(List<String> topResult, List<GetInfo> getInfos, String pid)
      throws MsfException {
    Float osCpu = ControllerStatusUtil.getOsCpu(topResult, getInfos, pid, null);
    SystemStatusCpuEntity cpuEntity = new SystemStatusCpuEntity();
    cpuEntity.setUseRate(osCpu);
    return cpuEntity;
  }

  protected SystemStatusMemoryEntity getOsMem(List<String> topResult, List<GetInfo> getInfos, String pid)
      throws MsfException {
    List<Integer> memList = ControllerStatusUtil.getOsMem(topResult, getInfos, pid, null);
    SystemStatusMemoryEntity memoryEntity = new SystemStatusMemoryEntity();
    if (memList != null && !memList.isEmpty()) {
      memoryEntity.setUsed(memList.get(0));
      memoryEntity.setFree(memList.get(1));
      memoryEntity.setBuffCache(memList.get(2));
      memoryEntity.setSwpd(memList.get(3));
    }
    return memoryEntity;
  }

  protected Integer getCtrMem(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    Integer ctrMem = ControllerStatusUtil.getCtlMem(topResult, getInfos, pid);
    return ctrMem;
  }

  protected Float getCtrCpu(List<String> topResult, List<GetInfo> getInfos, String pid) throws MsfException {
    Float ctrCpu = ControllerStatusUtil.getCtlCpu(topResult, getInfos, pid);
    return ctrCpu;
  }

  protected SystemStatusDiskEntity getOsDisk(List<GetInfo> getInfos) throws MsfException {
    List<String> dfResult = new ArrayList<>();
    List<SystemStatusDeviceEntity> deviceEntityList = ControllerStatusUtil.getOsDisk(dfResult, getInfos, null, null);
    SystemStatusDiskEntity systemStatusDiskEntity = new SystemStatusDiskEntity();
    systemStatusDiskEntity.setDeviceList(deviceEntityList);
    return systemStatusDiskEntity;
  }

  protected SystemStatusTrafficEntity getOsTraffic(List<GetInfo> getInfos) throws MsfException {
    List<SystemStatusInterfaceEntity> interfaceEntityList = ControllerStatusUtil.getOsTraffic(getInfos);
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
