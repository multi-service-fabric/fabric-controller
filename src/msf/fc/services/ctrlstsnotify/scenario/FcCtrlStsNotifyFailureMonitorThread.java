
package msf.fc.services.ctrlstsnotify.scenario;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.ctrlstsnotify.FcCtrlStsNotifyManager;
import msf.fc.services.ctrlstsnotify.common.config.type.system.ControllerFailureNotification;
import msf.fc.services.ctrlstsnotify.common.config.type.system.Device;
import msf.fc.services.ctrlstsnotify.common.config.type.system.Monitored;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoCtlFailure;
import msf.mfcfc.common.constant.GetInfo;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.CommandUtil;
import msf.mfcfc.common.util.ControllerStatusUtil;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusDeviceEntity;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.ctrlstsnotify.CtrlStsNotifySender;
import msf.mfcfc.services.ctrlstsnotify.common.constant.ControllerErrorType;
import msf.mfcfc.services.ctrlstsnotify.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.ctrlstsnotify.common.constant.SystemType;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerErrorNotificationRequest;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerErrorNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerErrorNotificationEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.DeviceListEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.FailureInfoCpuEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.FailureInfoDiskEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.FailureInfoEntity;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.FailureInfoMemoryEntity;

/**
 * Main class for the controller status notification. To be executed
 * periodically from the CycleTimer class.
 *
 * @author NTT
 *
 */
public class FcCtrlStsNotifyFailureMonitorThread extends FcAbstractCtrlStsNotifyMonitorThread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCtrlStsNotifyFailureMonitorThread.class);

  List<GetInfo> getInfos = null;

  String pid = null;

  String logDirectory;

  Float osCpu = null;

  Float ctlCpu = null;

  List<Integer> osMem = null;

  Integer ctlMem = null;

  List<SystemStatusDeviceEntity> osDisk = null;

  List<SystemStatusDeviceEntity> ctlDisk = null;

  /**
   * Execution of the controller status monitoring.
   *
   * @throws MsfException
   *           If DB access error occurs, or sending notification fails.
   */
  public void executeFailureMonitor() throws MsfException {

    getInfos = getGetInfos();

    pid = ControllerStatusUtil.getProcessId();

    logDirectory = getLogDirectory();

    checkStatus(ControllerErrorType.FC_ACT, null);

    String sbyIpAddress = ControllerStatusUtil.getSbyIpAddress();
    if (!sbyIpAddress.equals(CommandUtil.GET_FAILED.toString())) {

      checkStatus(ControllerErrorType.FC_SBY, sbyIpAddress);
    }
  }

  @Override
  public void run() {
    try {
      logger.methodStart();
      while (true) {
        try {

          ctrlStsNotifyCommonData.checkForceStopFailureMonitor();

          lock(ctrlStsNotifyCommonData.isForceStopFauilureMonitor());

          ctrlStsNotifyCommonData.checkForceStopFailureMonitor();

          executeFailureMonitor();

        } catch (InterruptedException ie) {
          if (ctrlStsNotifyCommonData.isForceStopFauilureMonitor()) {
            logger.info("Force Stop.");
            return;
          }

          logger.debug("InterruptedException. Not ForceStop.");
        } catch (MsfException exp) {

        } finally {
          isRunning = false;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected List<GetInfo> getGetInfos() {
    List<GetInfo> getInfos = new ArrayList<>();
    getInfos.add(GetInfo.OS_CPU);
    getInfos.add(GetInfo.OS_MEM);
    getInfos.add(GetInfo.OS_DISK);
    getInfos.add(GetInfo.CTR_CPU);
    getInfos.add(GetInfo.CTR_MEM);
    getInfos.add(GetInfo.CTR_DISK);
    return getInfos;
  }

  protected String getLogDirectory() {
    String logDirectory = "";
    try {

      LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
      Configuration configuration = loggerContext.getConfiguration();
      RollingFileAppender appender = (RollingFileAppender) configuration.getAppender("FcRollingFile");

      String fileName = new File(appender.getFileName()).getCanonicalPath();
      logDirectory = new File(fileName).getParent();
    } catch (Exception exception) {
      logger.warn(MessageFormat.format("Exception occurred. error={0}", exception.getMessage()));
    }

    return logDirectory;
  }

  protected List<String> getDevice(List<Device> deviceList) {
    List<String> fsList = new ArrayList<>();
    for (Device device : deviceList) {
      if (device.getUsageThreshold() != 0) {
        fsList.add(device.getFileSystem());
      }
    }
    return fsList;
  }

  protected void checkStatus(ControllerErrorType ctrlErrType, String ipAddress) throws MsfException {

    osCpu = null;
    ctlCpu = null;
    osMem = new ArrayList<>();
    ctlMem = null;
    osDisk = new ArrayList<>();
    ctlDisk = null;

    List<String> topResult = new ArrayList<>();

    osCpu = ControllerStatusUtil.getOsCpu(topResult, getInfos, pid, ipAddress);

    osMem = ControllerStatusUtil.getOsMem(topResult, getInfos, pid, ipAddress);

    List<String> dfResult = new ArrayList<>();

    osDisk = ControllerStatusUtil.getOsDisk(dfResult, getInfos, null, ipAddress);

    if (ctrlErrType.equals(ControllerErrorType.FC_ACT)) {

      ctlCpu = ControllerStatusUtil.getCtlCpu(topResult, getInfos, pid);

      ctlMem = ControllerStatusUtil.getCtlMem(topResult, getInfos, pid);

      ctlDisk = osDisk;
    }

    ControllerFailureNotification config = FcCtrlStsNotifyManager.getInstance().getSystemConfData()
        .getControllerFailureNotification();
    int noticeRetryNum = config.getNoticeRetryNum();
    int noticeTimeout = config.getNoticeTimeout();
    List<NoticeDestInfoCtlFailure> noticeDistList = config.getNoticeDestInfo();

    for (NoticeDestInfoCtlFailure info : noticeDistList) {

      FailureInfoEntity failureNotifyOs = checkControllerStatusOs(info.getMonitored());
      if (failureNotifyOs != null) {
        sendNotify(noticeRetryNum, noticeTimeout, info, ctrlErrType, SystemType.OPERATING_SYSTEM, failureNotifyOs);
      }

      if (ctrlErrType.equals(ControllerErrorType.FC_ACT)) {

        FailureInfoEntity failureNotifyProcess = checkControllerStatusProcess(info.getMonitored());
        if (failureNotifyProcess != null) {
          sendNotify(noticeRetryNum, noticeTimeout, info, ctrlErrType, SystemType.CTL_PROCESS, failureNotifyProcess);
        }
      }
    }

  }

  protected FailureInfoEntity checkControllerStatusOs(Monitored threshold) throws MsfException {
    FailureInfoEntity returnInfo = new FailureInfoEntity();

    returnInfo.setCpu(getCpuInfo(osCpu, threshold.getOs().getCpuUseRateThreshold()));

    returnInfo.setMemory(getMemInfo(osMem, threshold.getOs().getMemoryUsageThreshold()));

    returnInfo.setDisk(getOsDiskInfo(osDisk, threshold.getOs().getDisk().getDevices()));

    if (returnInfo.getCpu() == null && returnInfo.getMemory() == null && returnInfo.getDisk() == null) {
      returnInfo = null;
    }
    return returnInfo;
  }

  protected FailureInfoEntity checkControllerStatusProcess(Monitored threshold) throws MsfException {
    FailureInfoEntity returnInfo = new FailureInfoEntity();

    returnInfo.setCpu(getCpuInfo(ctlCpu, threshold.getControllerProcess().getCpuUseRateThreshold()));

    List<Integer> checkMem = new ArrayList<>();
    checkMem.add(ctlMem);
    returnInfo.setMemory(getMemInfo(checkMem, threshold.getControllerProcess().getMemoryUsageThreshold()));

    returnInfo.setDisk(getCtlDiskInfo(ctlDisk, threshold.getControllerProcess().getDisk().getUsageThreshold()));

    if (returnInfo.getCpu() == null && returnInfo.getMemory() == null && returnInfo.getDisk() == null) {
      returnInfo = null;
    }
    return returnInfo;
  }

  protected FailureInfoCpuEntity getCpuInfo(Float cpu, float threshold) throws MsfException {
    FailureInfoCpuEntity cpuInfo = null;
    if (threshold != 0) {

      if (cpu.floatValue() > threshold) {
        cpuInfo = new FailureInfoCpuEntity();
        cpuInfo.setUseRate(cpu);
      }
    }
    return cpuInfo;
  }

  protected FailureInfoMemoryEntity getMemInfo(List<Integer> mem, int threshold) throws MsfException {
    FailureInfoMemoryEntity memInfo = null;
    if (threshold != 0) {

      if (mem.get(0).intValue() > threshold) {
        memInfo = new FailureInfoMemoryEntity();
        memInfo.setUsed(mem.get(0));

        if (mem.size() > 1) {
          memInfo.setFree(mem.get(1));
        }
      }
    }
    return memInfo;
  }

  protected FailureInfoDiskEntity getOsDiskInfo(List<SystemStatusDeviceEntity> disk, List<Device> deviceList)
      throws MsfException {
    FailureInfoDiskEntity diskInfo = null;
    List<DeviceListEntity> outputList = new ArrayList<>();
    if (disk != null && !disk.isEmpty()) {
      for (Device device : deviceList) {
        String fileSystem = device.getFileSystem();
        int usageThreshold = device.getUsageThreshold();
        if (usageThreshold != 0) {

          for (SystemStatusDeviceEntity deviceEntity : disk) {
            if (deviceEntity.getFileSystem().equals(fileSystem)) {
              if (deviceEntity.getUsed().intValue() > usageThreshold) {
                DeviceListEntity output = new DeviceListEntity();
                output.setFileSystem(deviceEntity.getFileSystem());
                output.setMountedOn(deviceEntity.getMountedOn());
                output.setSize(deviceEntity.getSize());
                output.setUsed(deviceEntity.getUsed());
                outputList.add(output);
                break;
              }
            }
          }
        }
      }
    }

    if (!outputList.isEmpty()) {
      diskInfo = new FailureInfoDiskEntity();
      diskInfo.setDeviceList(outputList);
    }
    return diskInfo;
  }

  protected FailureInfoDiskEntity getCtlDiskInfo(List<SystemStatusDeviceEntity> disk, int threshold)
      throws MsfException {
    FailureInfoDiskEntity diskInfo = null;
    List<DeviceListEntity> outputList = new ArrayList<>();
    if (disk != null && !disk.isEmpty()) {
      if (threshold != 0) {

        Comparator<SystemStatusDeviceEntity> desc = new Comparator<SystemStatusDeviceEntity>() {
          public int compare(SystemStatusDeviceEntity obj0, SystemStatusDeviceEntity obj1) {
            int obj0Len = obj0.getMountedOn().length();
            int obj1Len = obj1.getMountedOn().length();
            return (obj1Len - obj0Len);
          }
        };

        Collections.sort(disk, desc);

        for (SystemStatusDeviceEntity deviceEntity : disk) {
          String mountedOn = deviceEntity.getMountedOn();

          if (mountedOn.startsWith(logDirectory) && !mountedOn.equals(logDirectory)) {
            if (deviceEntity.getUsed() > threshold) {
              DeviceListEntity output = new DeviceListEntity();
              output.setFileSystem(deviceEntity.getFileSystem());
              output.setMountedOn(deviceEntity.getMountedOn());
              output.setSize(deviceEntity.getSize());
              output.setUsed(deviceEntity.getUsed());
              outputList.add(output);
            }
            continue;
          }

          if (logDirectory.equals(mountedOn) || logDirectory.startsWith(mountedOn + "/") || mountedOn.equals("/")) {

            if (deviceEntity.getUsed() > threshold) {
              DeviceListEntity output = new DeviceListEntity();
              output.setFileSystem(deviceEntity.getFileSystem());
              output.setMountedOn(deviceEntity.getMountedOn());
              output.setSize(deviceEntity.getSize());
              output.setUsed(deviceEntity.getUsed());
              outputList.add(output);
            }
            break;
          }
        }
      }
    }

    if (!outputList.isEmpty()) {
      diskInfo = new FailureInfoDiskEntity();
      diskInfo.setDeviceList(outputList);
    }
    return diskInfo;
  }

  protected void sendNotify(int noticeRetryNum, int noticeTimeout, NoticeDestInfoCtlFailure noticeInfo,
      ControllerErrorType ctrlErrType, SystemType systemType, FailureInfoEntity faulureInfo) {

    int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
    ControllerErrorNotificationEntity controller = new ControllerErrorNotificationEntity();
    controller.setClusterId(String.valueOf(clusterId));
    controller.setControllerTypeEnum(ctrlErrType);
    controller.setSystemTypeEnum(systemType);
    controller.setFailureInfo(faulureInfo);

    ControllerErrorNotificationRequestBody requestBody = new ControllerErrorNotificationRequestBody();
    requestBody.setController(controller);
    String bodyStr = JsonUtil.toJson(requestBody);

    ControllerErrorNotificationRequest request = new ControllerErrorNotificationRequest(bodyStr, null, null);
    request.setRequestBody(bodyStr);

    CtrlStsNotifySender.sendNotify(noticeInfo.getNoticeAddress(), noticeInfo.getNoticePort(), noticeRetryNum,
        noticeTimeout, request, MfcFcRequestUri.NOTEIFY_CONTOROLLER_FAILURE);
    return;
  }
}
