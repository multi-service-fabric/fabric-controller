
package msf.fc.services.nodeosupgrade;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.services.nodeosupgrade.common.config.type.system.ObjectFactory;
import msf.fc.services.nodeosupgrade.common.config.type.system.SystemConf;
import msf.fc.services.nodeosupgrade.scenario.upgrade.FcNodeOsUpgradeScenario;
import msf.fc.services.nodeosupgrade.scenario.upgrade.FcNodeOsUpgradeScheduler;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.nodeosupgrade.NodeOsUpgradeManager;
import msf.mfcfc.services.nodeosupgrade.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;

/**
 * Class to provide the initialization and termination function of the node OS
 * upgrade management function block.
 *
 * @author NTT
 *
 */
public final class FcNodeOsUpgradeManager extends NodeOsUpgradeManager {

  private static final String SYSTEM_CONFIG_NAME = "fc_nodeosupgrade_system.xml";

  private static final String SERVIUCES_CONF_DIR = "services/nodeosupgrade/";

  private static final String SYSTEM_CONFIG_XSD_NAME = "fc_nodeosupgrade_system.xsd";

  private static final String XSD_DIR = "msf/fc/services/nodeosupgrade/common/config/xsd/";

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeManager.class);

  private SystemConf systemConf;

  private static final Object FC_NODE_OS_UPGRADE_LOCK_OBJECT = new Object();

  private FcNodeOsUpgradeManager() {

  }

  protected FcNodeOsUpgradeScheduler fcNodeOsUpgradeScheduler = null;

  /**
   * Get the instance of FcNodeOsUpgradeManager
   *
   * @return FcNodeOsUpgradeManager instance
   */
  public static FcNodeOsUpgradeManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcNodeOsUpgradeManager();
      }
      return (FcNodeOsUpgradeManager) instance;
    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Get the system config information of the node OS upgrade management
   * function block.
   *
   * @return system config information
   */
  public SystemConf getSystemConfData() {
    return systemConf;
  }

  /**
   * Get the reservation scheduler for the node OS upgrade.
   *
   * @return The reservation scheduler for the node OS upgrade.
   */
  public FcNodeOsUpgradeScheduler getNodeOsUpgradeScheduler() {
    return fcNodeOsUpgradeScheduler;
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

      fcNodeOsUpgradeScheduler = new FcNodeOsUpgradeScheduler();

      CoreManager.getInstance().addReservationFunction(this);

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean startScheduler() {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();

        FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();

        List<Pattern> uriPatternList = Arrays.asList(MfcFcRequestUri.NODE_OS_UPGRADE.getUriPattern());
        List<FcAsyncRequest> readListReservationInfo = fcAsyncRequestsDao.readListReservationInfo(sessionWrapper,
            uriPatternList);

        for (FcAsyncRequest fcAsyncRequest : readListReservationInfo) {
          String portNumber = null;
          if (fcAsyncRequest.getNotificationPortNumber() != null) {
            portNumber = String.valueOf(fcAsyncRequest.getNotificationPortNumber());
          }
          NodeOsUpgradeRequest request = new NodeOsUpgradeRequest(fcAsyncRequest.getRequestBody(),
              fcAsyncRequest.getNotificationIpAddress(), portNumber, null);

          FcNodeOsUpgradeScenario scenario = new FcNodeOsUpgradeScenario(OperationType.NORMAL,
              SystemInterfaceType.EXTERNAL, fcAsyncRequest.getOperationId());
          scenario.execute(request);
        }

        return true;
      } catch (Exception ex) {
        logger.error("Failed to start scheduler FcNodeOsUpgrade Manager.", ex);
        return false;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stopScheduler() {
    try {
      logger.methodStart();

      if (fcNodeOsUpgradeScheduler != null) {
        fcNodeOsUpgradeScheduler.stopNodeOsUpgradeScheduler();
      }

      return true;
    } catch (Exception ex) {
      logger.error("Failed to stop scheduler FcNodeOsUpgrade Manager.", ex);
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

      logger.error("NodeOsUpgrade systemConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkConfigParameter() {
    try {
      logger.methodStart();

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for the node OS upgrade.
   *
   * @return FC_NODE_OS_UPGRADE_LOCK_OBJECT
   */
  public Object getFcNodeOsUpgradeLockObject() {
    try {
      logger.methodStart();
      return FC_NODE_OS_UPGRADE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

}
