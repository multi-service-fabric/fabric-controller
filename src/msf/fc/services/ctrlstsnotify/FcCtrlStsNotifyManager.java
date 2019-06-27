
package msf.fc.services.ctrlstsnotify;

import java.util.Date;
import java.util.Timer;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.ctrlstsnotify.common.config.type.system.Device;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoCtlFailure;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoLog;
import msf.fc.services.ctrlstsnotify.common.config.type.system.ObjectFactory;
import msf.fc.services.ctrlstsnotify.common.config.type.system.SystemConf;
import msf.fc.services.ctrlstsnotify.scenario.FcCtrlStsNotifyFailureCycleWaker;
import msf.mfcfc.common.constant.IpAddressType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.services.ctrlstsnotify.CtrlStsNotifyManager;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.CtrlStsNotifyCommonData;

/**
 * Class to provide the initialization and termination function of the
 * controller status notification function block.
 *
 * @author NTT
 *
 */
public final class FcCtrlStsNotifyManager extends CtrlStsNotifyManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCtrlStsNotifyManager.class);

  private static final long SLEEPTIME = 1000;
  private static final int SLEEPCOUNT = 60;

  private static final String SYSTEM_CONFIG_NAME = "fc_ctrlstsnotify_system.xml";

  private static final String SERVIUCES_CONF_DIR = "services/ctrlstsnotify/";

  private static final String SYSTEM_CONFIG_XSD_NAME = "fc_ctrlstsnotify_system.xsd";

  private static final String XSD_DIR = "msf/fc/services/ctrlstsnotify/common/config/xsd/";

  private SystemConf systemConf;

  private static CtrlStsNotifyCommonData crtlStsNotifyCommonData = CtrlStsNotifyCommonData.getInstance();

  private FcCtrlStsNotifyFailureCycleWaker failureMonitorCycleWaker = null;
  private Timer failureMonitorCycleWakerTimer = null;

  private FcCtrlStsNotifyManager() {

  }

  /**
   * Get the instance of FcCtrlStsNotifyManager.<br>
   * This method does not guarantee the uniqueness of the returned instance if
   * it is called by multi-threads simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return CtrlStsNotifyManager instance
   */
  public static FcCtrlStsNotifyManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcCtrlStsNotifyManager();
      }
      return (FcCtrlStsNotifyManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean start() {
    try {
      logger.methodStart();
      boolean checkLoadFile = true;

      checkLoadFile = loadSystemConfig();
      if (!checkLoadFile) {

        return false;
      }

      boolean checkConfig = checkConfigParameter();
      if (!checkConfig) {
        return false;
      }

      final int monitoringPeriod = systemConf.getControllerFailureNotification().getMonitoringPeriod();

      if (monitoringPeriod != 0) {
        failureMonitorCycleWaker = new FcCtrlStsNotifyFailureCycleWaker(monitoringPeriod);
        failureMonitorCycleWaker.start();
        failureMonitorCycleWakerTimer = new Timer();
        failureMonitorCycleWakerTimer.scheduleAtFixedRate(failureMonitorCycleWaker, new Date(),
            (monitoringPeriod * 1000));
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();
      boolean isOkFailureMonitorStatus = true;
      if (failureMonitorCycleWaker != null) {
        isOkFailureMonitorStatus = failureMonitorCycleWaker.checkStatus();
      } else {
        logger.debug("failure monitor is not scheduled.");
      }

      return isOkFailureMonitorStatus;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();
      boolean isStopFailureMonitor = true;

      if (failureMonitorCycleWakerTimer != null) {
        failureMonitorCycleWakerTimer.cancel();
        failureMonitorCycleWakerTimer = null;

        crtlStsNotifyCommonData.setForceStopFailureMonitor(true);

        failureMonitorCycleWaker.executeInterrupt();

        isStopFailureMonitor = checkStopCycleWaker();
      }

      return isStopFailureMonitor;

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkStopCycleWaker() {
    try {
      logger.methodStart();

      for (int count = 1; count <= SLEEPCOUNT; count++) {

        if (!failureMonitorCycleWaker.isThreadAlive()) {
          return true;
        }
        try {
          Thread.sleep(SLEEPTIME);
        } catch (InterruptedException ie) {
          logger.warn("Sleep has been interrupted during stop {0}.",
              failureMonitorCycleWaker.getClass().getSimpleName());
        }
      }
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean loadSystemConfig() {
    try {
      logger.methodStart();

      this.systemConf = (SystemConf) FcConfigManager.getInstance().loadConfig(
          FcConfigManager.getInstance().getConfigPath() + SERVIUCES_CONF_DIR + SYSTEM_CONFIG_NAME,
          XSD_DIR + SYSTEM_CONFIG_XSD_NAME, ObjectFactory.class);
      return true;
    } catch (SAXException | JAXBException error1) {

      logger.error("CtrlStsNotify systemConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkConfigParameter() {
    try {
      logger.methodStart();

      for (NoticeDestInfoLog noticeDestInfoLog : systemConf.getLogNotification().getNoticeDestInfo()) {

        String checkAddress1 = IpAddressUtil.checkHost(noticeDestInfoLog.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress1 == null) {
          logger.error("ControllerLogNoticeAddress is NG because the format is invalid.:"
              + noticeDestInfoLog.getNoticeAddress());
          return false;
        } else {
          logger.debug("ControllerLogNoticeAddress is OK.");

          noticeDestInfoLog.setNoticeAddress(checkAddress1);
        }
      }

      for (NoticeDestInfoCtlFailure noticeDestInfoCtlFailure : systemConf.getControllerFailureNotification()
          .getNoticeDestInfo()) {

        String checkAddress2 = IpAddressUtil.checkHost(noticeDestInfoCtlFailure.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress2 == null) {
          logger.error("ControllerFailureNoticeAddress is NG because the format is invalid.:"
              + noticeDestInfoCtlFailure.getNoticeAddress());
          return false;
        } else {
          logger.debug("ControllerFailureNoticeAddress is OK.");

          noticeDestInfoCtlFailure.setNoticeAddress(checkAddress2);
        }

        for (Device device : noticeDestInfoCtlFailure.getMonitored().getOs().getDisk().getDevices()) {
          String fileSystem = device.getFileSystem();
          if (!fileSystem.startsWith("/")) {
            logger.error("Controller Failure Notice FileSystem NG :" + device.getFileSystem());
            return false;
          }
        }
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the system config information of the controller status notification
   * function block.
   *
   * @return system config information
   */
  public SystemConf getSystemConfData() {
    return systemConf;
  }

}
