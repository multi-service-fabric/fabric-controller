
package msf.mfc.core.status.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ControllerStatus;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.core.status.scenario.AbstractStatusScenarioBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadRequest;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadResponseBody;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusBlockadeEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerBlockadeStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerServiceStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusCpuEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusDiskEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusInformationEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusMemoryEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusOsEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusServiceStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusTrafficEntity;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for status confirmation.
 *
 * @author NTT
 *
 */
public class MfcSystemStatusReadScenario extends AbstractStatusScenarioBase<SystemStatusReadRequest> {

  private SystemStatusReadRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcSystemStatusReadScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public MfcSystemStatusReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {

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
        ParameterCheckUtil.checkClusterIdPattern(request.getCluster());
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

      if (controllers.contains(ControllerType.FC) || controllers.contains(ControllerType.EC)
          || controllers.contains(ControllerType.EM)) {
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

          List<MfcSwCluster> mfcSwClusters = getSwClusterList(sessionWrapper, mfcSwClusterDao);

          List<MfcSwCluster> targetClusters = getTargetClusters(request.getCluster(), mfcSwClusters);

          if (!targetClusters.isEmpty()) {

            List<RestRequestData> requestDataList = createSystemStatusReadRequestData(targetClusters, controllers,
                getInfos);

            List<RestResponseData> restResponseDatas = new ArrayList<>();
            for (RestRequestData restRequestData : requestDataList) {
              restResponseDatas.add(sendSystemStatusRead(restRequestData));
            }

            for (RestResponseData restResponseData : restResponseDatas) {
              if (checkResponseSuccess(restResponseData)) {

                SystemStatusReadResponseBody systemStatusReadResponseBody = JsonUtil.fromJson(
                    restResponseData.getResponse().getResponseBody(), SystemStatusReadResponseBody.class,
                    ErrorCode.FC_CONTROL_ERROR);

                if (CollectionUtils.isNotEmpty(systemStatusReadResponseBody.getInformationList())) {
                  informationList.addAll(systemStatusReadResponseBody.getInformationList());
                }

                if (CollectionUtils.isNotEmpty(systemStatusReadResponseBody.getControllerServiceStatusList())) {
                  controllerServiceStatusList.addAll(systemStatusReadResponseBody.getControllerServiceStatusList());
                }

                if (CollectionUtils.isNotEmpty(systemStatusReadResponseBody.getControllerBlockadeStatusList())) {
                  controllerBlockadeStatusList.addAll(systemStatusReadResponseBody.getControllerBlockadeStatusList());
                }
              } else {

                controllerServiceStatusList.addAll(createUnknownControllerServiceStatusList(controllers,
                    restResponseData.getRequest().getClusterId()));
                controllerBlockadeStatusList
                    .addAll(createUnknownBlockadeStatusList(controllers, restResponseData.getRequest().getClusterId()));
              }
            }
          }
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }
      }

      if (controllers.contains(ControllerType.MFC)) {
        String pid = getProcessId();
        ProcessBuilder hostNameCommand = new ProcessBuilder("env", "LANG=C", "hostname");
        List<String> hostnameResult = new ArrayList<>();
        String hostName = getCommand(hostNameCommand, hostnameResult).get(0);
        if (String.valueOf(GET_FAILED).equals(hostName)) {
          String logMsg = MessageFormat.format("failed command. command = {0}", "hostname");
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
        String managementIpAddress = MfcConfigManager.getInstance().getManagementIpAddress();

        List<String> topResult = new ArrayList<>();

        SystemStatusCpuEntity osCpu = getOsCpu(topResult, getInfos, pid);

        SystemStatusMemoryEntity osMem = getOsMem(topResult, getInfos, pid);

        SystemStatusDiskEntity osDisk = getOsDisk(getInfos);

        SystemStatusTrafficEntity osTraffic = getOsTraffic(getInfos);

        Float ctrCpu = getCtrCpu(topResult, getInfos, pid);

        Integer ctrMem = getCtrMem(topResult, getInfos, pid);

        Integer receiveRequest = getRecvRestConut(getInfos);

        Integer sendRequest = getSendRestConut(getInfos);

        SystemStatusControllerServiceStatusEntity serviceStatus = getControllerServiceStataus(null, getInfos,
            ControllerType.MFC);
        if (serviceStatus != null) {
          controllerServiceStatusList.add(serviceStatus);
        }

        SystemStatusControllerBlockadeStatusEntity blockadeStatus = getControllerBlockadeStataus(null, getInfos,
            ControllerType.MFC);
        if (blockadeStatus != null) {
          controllerBlockadeStatusList.add(blockadeStatus);
        }
        SystemStatusOsEntity osEntity = getSystemStatusOsEntity(osCpu, osMem, osDisk, osTraffic);
        SystemStatusControllerEntity controllerEntity = getSystemStatusControllerEntity(ctrCpu, ctrMem, receiveRequest,
            sendRequest, getInfos);
        SystemStatusInformationEntity fcInformationEntity = getFcInformationEntity(ControllerType.MFC.getMessage(),
            null, hostName, managementIpAddress, osEntity, controllerEntity);
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

  private List<MfcSwCluster> getSwClusterList(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao" }, new Object[] { mfcSwClusterDao });
      List<MfcSwCluster> mfcSwClusters = mfcSwClusterDao.readList(sessionWrapper);
      return mfcSwClusters;
    } finally {
      logger.methodEnd();
    }
  }

  private List<MfcSwCluster> getTargetClusters(String cluster, List<MfcSwCluster> mfcSwClusters) throws MsfException {
    if (cluster == null) {
      return mfcSwClusters;
    }

    List<MfcSwCluster> targetClusters = new ArrayList<>();

    List<String> targetClusterIds = Arrays.asList(cluster.split("\\+", 0));
    for (int i = 0; i < targetClusterIds.size(); i++) {
      String targetClusterId = targetClusterIds.get(i);

      for (MfcSwCluster mfcSwCluster : mfcSwClusters) {
        if (mfcSwCluster.getSwClusterId().equals(Integer.valueOf(targetClusterId))) {
          targetClusters.add(mfcSwCluster);
          break;
        }
      }

      if (targetClusters.size() != i + 1) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "related resource not found. targetClusterId = " + targetClusterId);
      }
    }
    return targetClusters;
  }

  private List<RestRequestData> createSystemStatusReadRequestData(List<MfcSwCluster> mfcSwClusters,
      List<ControllerType> controllers, List<GetInfo> getInfos) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusters" }, new Object[] { mfcSwClusters });
      List<RestRequestData> requestDataList = new ArrayList<>();
      for (MfcSwCluster mfcSwCluster : mfcSwClusters) {

        SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(mfcSwCluster.getSwClusterId())
            .getSwCluster();
        String fcControlAddress = swCluster.getFcControlAddress();
        int fcControlPort = swCluster.getFcControlPort();

        String targetUri = MfcFcRequestUri.STATUS_READ.getUri() + "?controller="
            + getControllersStrings(controllers, ControllerType.MFC) + "&cluster=" + mfcSwCluster.getSwClusterId()
            + "&get_info=" + getInformationStrings(getInfos);

        RestRequestData requestData = new RestRequestData(Integer.valueOf(mfcSwCluster.getSwClusterId()),
            fcControlAddress, fcControlPort, MfcFcRequestUri.STATUS_READ.getHttpMethod(), targetUri, null,
            HttpStatus.OK_200);

        requestDataList.add(requestData);
      }
      return requestDataList;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseData sendSystemStatusRead(RestRequestData restRequestData) {
    try {
      logger.methodStart();

      RestResponseBase restResponseBase = RestClient.sendRequest(restRequestData.getHttpMethod(),
          restRequestData.getTargetUri(), null, restRequestData.getIpAddress(), restRequestData.getPort());

      SystemStatusReadResponseBody statusReadResponseBody = new SystemStatusReadResponseBody();
      statusReadResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), SystemStatusReadResponseBody.class,
          ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          statusReadResponseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

      return new RestResponseData(restRequestData, restResponseBase);
    } catch (MsfException msfe) {
      logger.warn(msfe.getMessage(), msfe);

      logger.warn("Failed to get lower system status. ClusterId = " + restRequestData.getClusterId());
      RestResponseBase restErrorResponseBase = new RestResponseBase();
      restErrorResponseBase.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR_500);
      return new RestResponseData(restRequestData, restErrorResponseBase);
    } finally {
      logger.methodEnd();
    }
  }

  private List<SystemStatusControllerServiceStatusEntity> createUnknownControllerServiceStatusList(
      List<ControllerType> controllers, int clusterId) {
    try {
      logger.methodStart(new String[] { "controllers", "clusterId" }, new Object[] { controllers, clusterId });
      List<SystemStatusControllerServiceStatusEntity> unknownControllerServiceStatusList = new ArrayList<>();

      SystemStatusServiceStatusEntity serviceStatusEntity = new SystemStatusServiceStatusEntity();
      serviceStatusEntity.setServiceStatusEnum(ServiceStatus.UNKNOWN);

      if (controllers.contains(ControllerType.FC)) {
        unknownControllerServiceStatusList.add(createUnknownControllerServiceStatus(ControllerType.FC, clusterId));
      }
      if (controllers.contains(ControllerType.EC)) {
        unknownControllerServiceStatusList.add(createUnknownControllerServiceStatus(ControllerType.EC, clusterId));
      }
      if (controllers.contains(ControllerType.EM)) {
        unknownControllerServiceStatusList.add(createUnknownControllerServiceStatus(ControllerType.EM, clusterId));
      }
      return unknownControllerServiceStatusList;
    } finally {
      logger.methodEnd();
    }
  }

  private SystemStatusControllerServiceStatusEntity createUnknownControllerServiceStatus(ControllerType controllerType,
      int clusterId) {
    try {
      logger.methodStart(new String[] { "controllerType", "clusterId" }, new Object[] { controllerType, clusterId });

      SystemStatusControllerServiceStatusEntity serviceStatusEntity = new SystemStatusControllerServiceStatusEntity();
      serviceStatusEntity.setControllerType(controllerType.getMessage());
      serviceStatusEntity.setClusterId(String.valueOf(clusterId));

      SystemStatusServiceStatusEntity statusEntity = new SystemStatusServiceStatusEntity();
      statusEntity.setServiceStatusEnum(ServiceStatus.UNKNOWN);
      serviceStatusEntity.setStatus(statusEntity);
      return serviceStatusEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private List<SystemStatusControllerBlockadeStatusEntity> createUnknownBlockadeStatusList(
      List<ControllerType> controllers, int clusterId) {
    try {
      logger.methodStart(new String[] { "controllers", "clusterId" }, new Object[] { controllers, clusterId });
      List<SystemStatusControllerBlockadeStatusEntity> unknownBlockadeStatusList = new ArrayList<>();

      SystemStatusServiceStatusEntity serviceStatusEntity = new SystemStatusServiceStatusEntity();
      serviceStatusEntity.setServiceStatusEnum(ServiceStatus.UNKNOWN);

      if (controllers.contains(ControllerType.FC)) {
        unknownBlockadeStatusList.add(createUnknownBlockadeStatus(ControllerType.FC, clusterId));
      }
      if (controllers.contains(ControllerType.EC)) {
        unknownBlockadeStatusList.add(createUnknownBlockadeStatus(ControllerType.EC, clusterId));
      }
      if (controllers.contains(ControllerType.EM)) {
        unknownBlockadeStatusList.add(createUnknownBlockadeStatus(ControllerType.EM, clusterId));
      }
      return unknownBlockadeStatusList;
    } finally {
      logger.methodEnd();
    }
  }

  private SystemStatusControllerBlockadeStatusEntity createUnknownBlockadeStatus(ControllerType controllerType,
      int clusterId) {
    try {
      logger.methodStart(new String[] { "controllerType", "clusterId" }, new Object[] { controllerType, clusterId });

      SystemStatusControllerBlockadeStatusEntity blockadeEntity = new SystemStatusControllerBlockadeStatusEntity();
      blockadeEntity.setControllerType(controllerType.getMessage());
      blockadeEntity.setClusterId(String.valueOf(clusterId));
      if (!controllerType.equals(ControllerType.EM)) {

        SystemStatusBlockadeEntity statusBlockadeEntity = new SystemStatusBlockadeEntity();
        statusBlockadeEntity.setBlockadeStatusEnum(BlockadeStatus.UNKNOWN);
        blockadeEntity.setStatus(statusBlockadeEntity);
      }
      return blockadeEntity;
    } finally {
      logger.methodEnd();
    }
  }

}
