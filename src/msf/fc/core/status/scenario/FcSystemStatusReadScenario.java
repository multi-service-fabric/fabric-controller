
package msf.fc.core.status.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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
import msf.mfcfc.common.constant.EcBlockadeStatus;
import msf.mfcfc.common.constant.EcEmServiceStatus;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.CommandUtil;
import msf.mfcfc.common.util.ControllerStatusUtil;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.AbstractStatusScenarioBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadRequest;
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
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for the status check.
 *
 * @author NTT
 *
 */
public class FcSystemStatusReadScenario extends AbstractStatusScenarioBase<SystemStatusReadRequest> {

  private SystemStatusReadRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcSystemStatusReadScenario.class);

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
        ParameterCheckUtil.checkClusterForFc(request.getCluster(),
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
      List<ControllerType> controllers = getControllers(request.getController());
      List<GetInfo> getInfos = getGetInfos(request.getGetInfo());
      List<SystemStatusInformationEntity> informationList = new ArrayList<>();
      List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList = new ArrayList<>();
      List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList = new ArrayList<>();
      String clusterId = String
          .valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());

      if (controllers.contains(ControllerType.EC) || controllers.contains(ControllerType.EM)) {
        ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody = sendStatusRead(controllers, getInfos);
        informationList = getLowInformationList(controllerStatusReadEcResponseBody, controllers, clusterId);
        controllerServiceStatusList = getLowControllerServiceStatusList(controllerStatusReadEcResponseBody, controllers,
            clusterId, getInfos);
        controllerBlockadeStatusList = getLowControllerBlockadeStatusList(controllerStatusReadEcResponseBody,
            controllers, clusterId, getInfos);
      }

      if (controllers.contains(ControllerType.FC)) {
        String pid = ControllerStatusUtil.getProcessId();
        ProcessBuilder hostNameCommand = new ProcessBuilder("env", "LANG=C", "hostname");
        String hostName = CommandUtil.getCommand(hostNameCommand).get(0);
        if (String.valueOf(CommandUtil.GET_FAILED).equals(hostName)) {
          String logMsg = MessageFormat.format("failed command. command = {0}", "hostname");
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
        String managementIpAddress = FcConfigManager.getInstance().getManagementIpAddress();

        List<String> topResult = new ArrayList<>();

        SystemStatusCpuEntity osCpu = getOsCpu(topResult, getInfos, pid);

        SystemStatusMemoryEntity osMem = getOsMem(topResult, getInfos, pid);

        SystemStatusDiskEntity osDisk = getOsDisk(getInfos);

        SystemStatusTrafficEntity osTraffic = getOsTraffic(getInfos);

        Float ctrCpu = getCtrCpu(topResult, getInfos, pid);

        Integer ctrMem = getCtrMem(topResult, getInfos, pid);

        Integer receiveRequest = getRecvRestConut(getInfos);

        Integer sendRequest = getSendRestConut(getInfos);

        SystemStatusControllerServiceStatusEntity serviceStatus = getControllerServiceStataus(clusterId, getInfos,
            ControllerType.FC);
        if (serviceStatus != null) {
          controllerServiceStatusList.add(serviceStatus);
        }

        SystemStatusControllerBlockadeStatusEntity blockadeStatus = getControllerBlockadeStataus(clusterId, getInfos,
            ControllerType.FC);
        if (blockadeStatus != null) {
          controllerBlockadeStatusList.add(blockadeStatus);
        }
        SystemStatusOsEntity osEntity = getSystemStatusOsEntity(osCpu, osMem, osDisk, osTraffic);
        SystemStatusControllerEntity controllerEntity = getSystemStatusControllerEntity(ctrCpu, ctrMem, receiveRequest,
            sendRequest, getInfos);
        SystemStatusInformationEntity fcInformationEntity = getFcInformationEntity(ControllerType.FC.getMessage(),
            clusterId, hostName, managementIpAddress, osEntity, controllerEntity);
        if (fcInformationEntity != null) {
          informationList.add(fcInformationEntity);
        }
      }
      if (informationList.isEmpty()) {
        informationList = null;
      }
      String allServiceStatus = getAllServiceStatus(controllerServiceStatusList, getInfos);
      if ((allServiceStatus == null) || (ControllerStatus.RUNNING.getMessage().equals(allServiceStatus))) {

        controllerServiceStatusList = null;
      }
      String allBlockadeStatus = getAllBlockadeStatus(controllerBlockadeStatusList, getInfos);
      if ((allBlockadeStatus == null) || (BlockadeStatus.NONE.getMessage().equals(allBlockadeStatus))) {

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
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<ControllerType> controllers,
      String clusterId) {
    SystemStatusInformationEntity lowInformationEntity = new SystemStatusInformationEntity();
    List<SystemStatusInformationEntity> informationList = new ArrayList<>();
    if (controllers.contains(ControllerType.EC)) {
      lowInformationEntity = getLowInformationEntity(controllerStatusReadEcResponseBody, ControllerType.EC.getMessage(),
          clusterId);
      if (lowInformationEntity != null) {
        informationList.add(lowInformationEntity);
      }
    }
    if (controllers.contains(ControllerType.EM)) {
      lowInformationEntity = getLowInformationEntity(controllerStatusReadEcResponseBody, ControllerType.EM.getMessage(),
          clusterId);
      if (lowInformationEntity != null) {
        informationList.add(lowInformationEntity);
      }
    }
    return informationList;
  }

  private List<SystemStatusControllerServiceStatusEntity> getLowControllerServiceStatusList(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<ControllerType> controllers,
      String clusterId, List<GetInfo> getInfos) {
    SystemStatusControllerServiceStatusEntity lowServiceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList = new ArrayList<>();
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return controllerServiceStatusList;
    }
    if (controllers.contains(ControllerType.EC)) {
      lowServiceStatusEntity = getLowServiceStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EC.getMessage(), clusterId);
      controllerServiceStatusList.add(lowServiceStatusEntity);
    }
    if (controllers.contains(ControllerType.EM)) {
      lowServiceStatusEntity = getLowServiceStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EM.getMessage(), clusterId);
      controllerServiceStatusList.add(lowServiceStatusEntity);
    }
    return controllerServiceStatusList;
  }

  private List<SystemStatusControllerBlockadeStatusEntity> getLowControllerBlockadeStatusList(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, List<ControllerType> controllers,
      String clusterId, List<GetInfo> getInfos) {
    SystemStatusControllerBlockadeStatusEntity blockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList = new ArrayList<>();
    if (!getInfos.contains(GetInfo.CTR_STATE)) {
      return controllerBlockadeStatusList;
    }
    if (controllers.contains(ControllerType.EC)) {
      blockadeStatusEntity = getLowBlockadeStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EC.getMessage(), clusterId);
      controllerBlockadeStatusList.add(blockadeStatusEntity);
    }
    if (controllers.contains(ControllerType.EM)) {
      blockadeStatusEntity = getLowBlockadeStatusEntity(controllerStatusReadEcResponseBody,
          ControllerType.EM.getMessage(), clusterId);
      controllerBlockadeStatusList.add(blockadeStatusEntity);
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
    SystemStatusControllerBlockadeStatusEntity blockadeStatusEntity = new SystemStatusControllerBlockadeStatusEntity();
    blockadeStatusEntity.setClusterId(clusterId);
    blockadeStatusEntity.setControllerType(controller);
    SystemStatusBlockadeEntity blockadeEntity = null;
    if (ControllerType.EC.getMessage().equals(controller)) {
      blockadeEntity = new SystemStatusBlockadeEntity();
      if (EcBlockadeStatus.INSERVICE.equals(controllerStatusReadEcResponseBody.getEcStatus().getBusyEnum())) {
        blockadeEntity.setBlockadeStatus(BlockadeStatus.NONE.getMessage());
      } else if (EcBlockadeStatus.BUSY.equals(controllerStatusReadEcResponseBody.getEcStatus().getBusyEnum())) {
        blockadeEntity.setBlockadeStatus(BlockadeStatus.BLOCKADE.getMessage());
      } else {
        blockadeEntity.setBlockadeStatus(BlockadeStatus.UNKNOWN.getMessage());
      }
    }
    blockadeStatusEntity.setStatus(blockadeEntity);
    return blockadeStatusEntity;
  }

  private SystemStatusControllerServiceStatusEntity getLowServiceStatusEntity(
      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody, String controller, String clusterId) {
    SystemStatusControllerServiceStatusEntity lowServiceStatusEntity = new SystemStatusControllerServiceStatusEntity();
    lowServiceStatusEntity.setControllerType(controller);
    lowServiceStatusEntity.setClusterId(clusterId);
    SystemStatusServiceStatusEntity serviceStatusEntity = new SystemStatusServiceStatusEntity();
    if (ControllerType.EC.getMessage().equals(controller)) {
      if (EcEmServiceStatus.INSERVICE.equals(controllerStatusReadEcResponseBody.getEcStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.STARTED);
      } else if (EcEmServiceStatus.STARTREADY
          .equals(controllerStatusReadEcResponseBody.getEcStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.INITIALIZING);
      } else if (EcEmServiceStatus.STOPREADY.equals(controllerStatusReadEcResponseBody.getEcStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.FINALIZING);
      } else if (EcEmServiceStatus.CHANGEOVER
          .equals(controllerStatusReadEcResponseBody.getEcStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.SWITCHING);
      } else {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.UNKNOWN);
      }
    } else {
      if (EcEmServiceStatus.INSERVICE.equals(controllerStatusReadEcResponseBody.getEmStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.STARTED);
      } else if (EcEmServiceStatus.STARTREADY
          .equals(controllerStatusReadEcResponseBody.getEmStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.INITIALIZING);
      } else if (EcEmServiceStatus.STOPREADY.equals(controllerStatusReadEcResponseBody.getEmStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.FINALIZING);
      } else if (EcEmServiceStatus.CHANGEOVER
          .equals(controllerStatusReadEcResponseBody.getEmStatus().getStatusEnum())) {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.SWITCHING);
      } else {
        serviceStatusEntity.setServiceStatusEnum(ServiceStatus.UNKNOWN);
      }
    }
    lowServiceStatusEntity.setStatus(serviceStatusEntity);
    return lowServiceStatusEntity;
  }

  private ControllerStatusReadEcResponseBody sendStatusRead(List<ControllerType> controllers, List<GetInfo> getInfos) {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      String targetUri = EcRequestUri.STATUS_READ.getUri();

      targetUri = targetUri + "?controller=" + getControllersStrings(controllers, ControllerType.FC) + "&get_info="
          + getInformationStrings(getInfos);

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.STATUS_READ.getHttpMethod(), targetUri,
          null, ecControlIpAddress, ecControlPort);

      ControllerStatusReadEcResponseBody controllerStatusReadEcResponseBody = new ControllerStatusReadEcResponseBody();
      controllerStatusReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          ControllerStatusReadEcResponseBody.class, ErrorCode.EC_CONNECTION_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          controllerStatusReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONNECTION_ERROR);

      return controllerStatusReadEcResponseBody;
    } catch (MsfException msfe) {
      logger.warn(msfe.getMessage(), msfe);

      logger.warn("Failed to get lower system status.");
      ControllerStatusEcStatusEcEntity ecEntity = new ControllerStatusEcStatusEcEntity();
      ecEntity.setStatus(ServiceStatus.UNKNOWN.getMessage());
      ecEntity.setBusy(BlockadeStatus.UNKNOWN.getMessage());
      ControllerStatusEmStatusEcEntity emEntity = new ControllerStatusEmStatusEcEntity();
      emEntity.setStatus(ServiceStatus.UNKNOWN.getMessage());

      ControllerStatusReadEcResponseBody ecResponseBody = new ControllerStatusReadEcResponseBody();
      ecResponseBody.setEcStatus(ecEntity);
      ecResponseBody.setEmStatus(emEntity);
      return ecResponseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
