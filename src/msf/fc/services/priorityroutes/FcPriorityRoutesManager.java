
package msf.fc.services.priorityroutes;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.priorityroutes.common.config.type.system.ObjectFactory;
import msf.fc.services.priorityroutes.common.config.type.system.SystemConf;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.services.priorityroutes.PriorityRoutesManager;

/**
 * Class to provide the initialization and termination function of the priority
 * routes control management function block.
 *
 * @author NTT
 *
 */
public final class FcPriorityRoutesManager extends PriorityRoutesManager {

  private static final String SYSTEM_CONFIG_NAME = "fc_priorityroutes_system.xml";

  private static final String SERVIUCES_CONF_DIR = "services/priorityroutes/";

  private static final String SYSTEM_CONFIG_XSD_NAME = "fc_priorityroutes_system.xsd";

  private static final String XSD_DIR = "msf/fc/services/priorityroutes/common/config/xsd/";

  private static final MsfLogger logger = MsfLogger.getInstance(FcPriorityRoutesManager.class);

  private SystemConf systemConf;

  private FcPriorityRoutesManager() {
  }

  /**
   * Get the instance of FcPriorityRoutesManager.<br>
   * This method does not guarantee the uniqueness of the returned instance if
   * it is called by multi-threads simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcPriorityRoutesManager instance
   */
  public static FcPriorityRoutesManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcPriorityRoutesManager();
      }
      return (FcPriorityRoutesManager) instance;
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

      return true;
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

      logger.error("PriorityRoutes systemConfig file could not read.", error1);
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
   * Get the system config information of the priority routes control management
   * function block.
   *
   * @return system config information
   */
  public SystemConf getSystemConfData() {
    return systemConf;
  }

}
