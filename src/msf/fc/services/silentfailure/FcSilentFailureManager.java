
package msf.fc.services.silentfailure;

import java.util.Date;
import java.util.Timer;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.silentfailure.common.config.type.system.NoticeDestInfo;
import msf.fc.services.silentfailure.common.config.type.system.ObjectFactory;
import msf.fc.services.silentfailure.common.config.type.system.SystemConf;
import msf.fc.services.silentfailure.scenario.FcOspfNeighborMonitorCycleWaker;
import msf.fc.services.silentfailure.scenario.FcPingMonitorCycleWaker;
import msf.mfcfc.common.constant.IpAddressType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.core.scenario.AbstractCycleWaker;
import msf.mfcfc.services.silentfailure.SilentFailureManager;
import msf.mfcfc.services.silentfailure.scenario.data.SilentFailureCommonData;

/**
 * Class to provide the initialization and termination function of the silent
 * failure detection function block.
 *
 * @author NTT
 *
 */
public final class FcSilentFailureManager extends SilentFailureManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSilentFailureManager.class);

  private static final long SLEEPTIME = 1000;
  private static final int SLEEPCOUNT = 60;

  private static final String SYSTEM_CONFIG_NAME = "fc_silentfailure_system.xml";

  private static final String SERVIUCES_CONF_DIR = "services/silentfailure/";

  private static final String SYSTEM_CONFIG_XSD_NAME = "fc_silentfailure_system.xsd";

  private static final String XSD_DIR = "msf/fc/services/silentfailure/common/config/xsd/";

  private SystemConf systemConf;

  private static SilentFailureCommonData silentFailureCommonData = SilentFailureCommonData.getInstance();

  private FcPingMonitorCycleWaker pingMonitorCycleWaker = null;

  private FcOspfNeighborMonitorCycleWaker ospfNeighborMonitorCycleWaker = null;

  private Timer pingMonitorCycleWakerTimer = null;

  private Timer ospfNeighborMonitorCycleWakerTimer = null;

  private FcSilentFailureManager() {
  }

  /**
   * Get the instance of FcSilentFailureManager
   *
   * @return FcSilentFailureManager instance
   */
  public static FcSilentFailureManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcSilentFailureManager();
      }
      return (FcSilentFailureManager) instance;
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

      final int pingMonitorCycle = systemConf.getSilentFailure().getPingMonitorCycle();

      if (pingMonitorCycle != 0) {
        pingMonitorCycleWaker = new FcPingMonitorCycleWaker(pingMonitorCycle);
        pingMonitorCycleWaker.start();
        pingMonitorCycleWakerTimer = new Timer();
        pingMonitorCycleWakerTimer.scheduleAtFixedRate(pingMonitorCycleWaker, new Date(), (pingMonitorCycle * 1000));
      }

      final int ospfNeighborMonitorCycle = systemConf.getSilentFailure().getOspfNeighborMonitorCycle();

      if (ospfNeighborMonitorCycle != 0) {
        ospfNeighborMonitorCycleWaker = new FcOspfNeighborMonitorCycleWaker(ospfNeighborMonitorCycle);
        ospfNeighborMonitorCycleWaker.start();
        ospfNeighborMonitorCycleWakerTimer = new Timer();
        ospfNeighborMonitorCycleWakerTimer.scheduleAtFixedRate(ospfNeighborMonitorCycleWaker, new Date(),
            (ospfNeighborMonitorCycle * 1000));
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
      boolean isOkPingMonitorStatus = true;
      if (pingMonitorCycleWaker != null) {
        isOkPingMonitorStatus = pingMonitorCycleWaker.checkStatus();
      } else {
        logger.debug("ping monitor is not scheduled.");
      }

      boolean isOkOspfNeighborMonitorStatus = true;
      if (ospfNeighborMonitorCycleWaker != null) {
        isOkOspfNeighborMonitorStatus = ospfNeighborMonitorCycleWaker.checkStatus();
      } else {
        logger.debug("ospf neighbor monitor is not scheduled.");
      }

      logger.debug("pingMonitorStatus = " + isOkPingMonitorStatus);
      logger.debug("ospfNeighborMonitorStatus = " + isOkOspfNeighborMonitorStatus);

      if (!isOkPingMonitorStatus || !isOkOspfNeighborMonitorStatus) {
        return false;
      } else {
        return true;
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();
      boolean isStopPingMonitor = true;
      boolean isStopOspfNeighborMonitor = true;

      if (pingMonitorCycleWakerTimer != null) {
        pingMonitorCycleWakerTimer.cancel();
        pingMonitorCycleWakerTimer = null;

        silentFailureCommonData.setForceStopPingMonitor(true);

        pingMonitorCycleWaker.executeInterrupt();

        isStopPingMonitor = checkStopCycleWaker(pingMonitorCycleWaker);
      } else {
        logger.debug("skip stopping ping monitoring");
      }

      if (ospfNeighborMonitorCycleWakerTimer != null) {
        ospfNeighborMonitorCycleWakerTimer.cancel();
        ospfNeighborMonitorCycleWakerTimer = null;

        silentFailureCommonData.setForceStopOspfNeighborMonitor(true);

        ospfNeighborMonitorCycleWaker.executeInterrupt();

        isStopOspfNeighborMonitor = checkStopCycleWaker(ospfNeighborMonitorCycleWaker);
      } else {
        logger.debug("skip stopping ospf neighbor monitoring");
      }

      if (!isStopPingMonitor || !isStopOspfNeighborMonitor) {
        return false;
      } else {
        return true;
      }

    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkStopCycleWaker(AbstractCycleWaker cycleWaker) {
    try {
      logger.methodStart(new String[] { "cycleWaker" }, new Object[] { cycleWaker });

      for (int count = 1; count <= SLEEPCOUNT; count++) {

        if (!cycleWaker.isThreadAlive()) {
          return true;
        }
        try {
          Thread.sleep(SLEEPTIME);
        } catch (InterruptedException ie) {
          logger.warn("Sleep has been interrupted during stop {0}.", cycleWaker.getClass().getSimpleName());
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

      logger.error("SilentFailure systemConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkConfigParameter() {
    try {
      logger.methodStart();

      for (NoticeDestInfo noticeDestInfo : systemConf.getSilentFailure().getNoticeDestInfo()) {
        String checkAddress = IpAddressUtil.checkHost(noticeDestInfo.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress == null) {
          logger.error(
              "SilentFailureNoticeAddress is NG because the format is invalid.:" + noticeDestInfo.getNoticeAddress());
          return false;
        } else {
          logger.debug("SilentFailureNoticeAddress is OK.");

          noticeDestInfo.setNoticeAddress(checkAddress);
        }
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the system config information of the silent failure detection function
   * block.
   *
   * @return system config information
   */
  public SystemConf getSystemConfData() {
    return systemConf;
  }
}
