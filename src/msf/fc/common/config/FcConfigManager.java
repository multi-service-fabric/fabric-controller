
package msf.fc.common.config;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.fc.common.config.type.data.DataConf;
import msf.fc.common.config.type.data.LeafRr;
import msf.fc.common.config.type.data.Rr;
import msf.fc.common.config.type.develop.DevelopConf;
import msf.fc.common.config.type.system.NoticeDestInfoFailure;
import msf.fc.common.config.type.system.NoticeDestInfoStatus;
import msf.fc.common.config.type.system.NoticeDestInfoTraffic;
import msf.fc.common.config.type.system.SystemConf;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.IpAddressType;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization function, living confirmation and
 * termination of config management function block.
 *
 * @author NTT
 *
 */
public class FcConfigManager extends ConfigManager {

  private static final String SYSTEM_CONFIG_NAME = "fc_system.xml";

  private static final String DATA_CONFIG_NAME = "fc_data.xml";

  private static final String DEVELOP_CONFIG_NAME = "fc_develop.xml";

  private static final String SYSTEM_CONFIG_XSD_NAME = "fc_system_schema.xsd";

  private static final String DATA_CONFIG_XSD_NAME = "fc_data_schema.xsd";

  private static final String DEVELOP_CONFIG_XSD_NAME = "fc_develop_schema.xsd";

  private static final String XSD_DIR = "msf/fc/common/config/xsd/";

  private static final MsfLogger logger = MsfLogger.getInstance(FcConfigManager.class);

  private SystemConf systemConf;

  private DataConf dataConf;

  private DevelopConf developConf;

  private msf.fc.common.config.type.system.SwClusterData systemConfSwClusterData;

  private msf.fc.common.config.type.data.SwClusterData dataConfSwClusterData;

  private msf.fc.common.config.type.system.Status systemConfStatus;

  private msf.fc.common.config.type.system.Failure systemConfFailure;

  private msf.fc.common.config.type.system.Traffic systemConfTraffic;

  private FcConfigManager() {
  }

  /**
   * Get the instance of FcConfigManager.
   *
   * @return FcConfigManager instance
   */
  public static FcConfigManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcConfigManager();
      }
      return (FcConfigManager) instance;
    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Start the config management function block. <br>
   * <br>
   * Read the several config files and initialize the variables for getter
   * methods. <br>
   *
   * @return boolean process result, true: success, false: fail
   */
  @Override
  public boolean start() {
    try {
      logger.methodStart();
      boolean checkLoadFile = true;

      checkLoadFile = loadSystemConfig();
      if (!checkLoadFile) {

        return false;
      }
      checkLoadFile = loadDataConfig();
      if (!checkLoadFile) {

        return false;
      }
      checkLoadFile = loadDevelopConfig();
      if (!checkLoadFile) {

        return false;
      }

      setCommonData(systemConf.getRest().getServer().getListeningAddress(),
          systemConf.getRest().getServer().getListeningPort(),
          systemConf.getRest().getClient().getWaitConnectionTimeout(),
          systemConf.getRest().getClient().getRequestTimeout(), systemConf.getRest().getJson().isIsPrettyPrinting(),
          systemConf.getRest().getJson().isIsSerializeNulls(), systemConf.getSlice().getL2MaxSlicesNum(),
          systemConf.getSlice().getL3MaxSlicesNum(), systemConf.getSlice().getL2SlicesMagnificationNum(),
          systemConf.getSlice().getL3SlicesMagnificationNum(),
          developConf.getSystem().getAsyncOperation().getDataRetentionPeriod(),
          developConf.getSystem().getAsyncOperation().getMaxAsyncRunnerThreadNum(),
          developConf.getSystem().getAsyncOperation().getInvokeAllTimout(),
          developConf.getSystem().getAsyncOperation().getWaitOperationResultTimeout(),
          developConf.getSystem().getExecutingOperationCheckCycle(),
          developConf.getSystem().getLock().getLockRetryNum(), developConf.getSystem().getLock().getLockTimeout(),
          developConf.getSystem().getNotice().getNoticeRetryNum());

      systemConfSwClusterData = systemConf.getSwClustersData().getSwClusterData();

      dataConfSwClusterData = dataConf.getSwClustersData().getSwClusterData();

      systemConfStatus = systemConf.getStatus();

      systemConfFailure = systemConf.getFailure();

      systemConfTraffic = systemConf.getTraffic();

      boolean checkConfig = checkConfigParameter();
      if (!checkConfig) {
        return false;
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean loadSystemConfig() {
    try {
      logger.methodStart();

      this.systemConf = (SystemConf) this.loadConfig(this.confDir + SYSTEM_CONFIG_NAME,
          XSD_DIR + SYSTEM_CONFIG_XSD_NAME, msf.fc.common.config.type.system.ObjectFactory.class);
      return true;
    } catch (SAXException | JAXBException error1) {

      logger.error("SystemConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean loadDataConfig() {
    try {
      logger.methodStart();

      this.dataConf = (DataConf) this.loadConfig(this.confDir + DATA_CONFIG_NAME, XSD_DIR + DATA_CONFIG_XSD_NAME,
          msf.fc.common.config.type.data.ObjectFactory.class);
      return true;
    } catch (SAXException | JAXBException error1) {

      logger.error("DataConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean loadDevelopConfig() {
    try {
      logger.methodStart();

      this.developConf = (DevelopConf) this.loadConfig(this.confDir + DEVELOP_CONFIG_NAME,
          XSD_DIR + DEVELOP_CONFIG_XSD_NAME, msf.fc.common.config.type.develop.ObjectFactory.class);
      return true;
    } catch (SAXException | JAXBException error1) {

      logger.error("DevelopConfig file could not read.", error1);
      return false;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean checkConfigParameter() {
    try {
      logger.methodStart();

      String checkAddress = null;

      if (!super.checkConfigParameter()) {
        return false;
      }

      if (systemConf.getSwClustersData().getSwClusterData().getSwCluster().getSwClusterId() != dataConf
          .getSwClustersData().getSwClusterData().getSwCluster().getSwClusterId()) {
        logger.error("SW Cluster ID(Config) is wrong.");
        return false;
      }

      checkAddress = checkHost(systemConf.getSwClustersData().getSwClusterData().getSwCluster().getEcControlAddress(),
          IpAddressType.IPV4V6FQDN);

      if (checkAddress == null) {
        logger.error("Ec Control Address NG :"
            + systemConf.getSwClustersData().getSwClusterData().getSwCluster().getEcControlAddress());
        return false;

      } else {
        logger.debug("Ec Control Address OK.");

        systemConf.getSwClustersData().getSwClusterData().getSwCluster().setEcControlAddress(checkAddress);
      }

      checkAddress = checkHost(
          dataConf.getSwClustersData().getSwClusterData().getSwCluster().getInchannelStartAddress(),
          IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Inchannel Start Address NG :"
            + dataConf.getSwClustersData().getSwClusterData().getSwCluster().getInchannelStartAddress());
        return false;

      } else {
        logger.debug("Inchannel Start Address OK.");

        dataConf.getSwClustersData().getSwClusterData().getSwCluster().setInchannelStartAddress(checkAddress);
      }

      checkAddress = checkHost(
          dataConf.getSwClustersData().getSwClusterData().getSwCluster().getOutchannelStartAddress(),
          IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Outchannel Start Address NG :"
            + dataConf.getSwClustersData().getSwClusterData().getSwCluster().getOutchannelStartAddress());
        return false;

      } else {
        logger.debug("Outchannel Start Address OK.");

        dataConf.getSwClustersData().getSwClusterData().getSwCluster().setOutchannelStartAddress(checkAddress);
      }

      checkAddress = checkHost(
          dataConf.getSwClustersData().getSwClusterData().getSwCluster().getAggrigationStartAddress(),
          IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Aggrigation Start Address NG :"
            + dataConf.getSwClustersData().getSwClusterData().getSwCluster().getAggrigationStartAddress());
        return false;

      } else {
        logger.debug("Aggrigation Start Address OK.");

        dataConf.getSwClustersData().getSwClusterData().getSwCluster().setAggrigationStartAddress(checkAddress);
      }

      for (Rr rr : dataConf.getSwClustersData().getSwClusterData().getRrs().getRr()) {
        checkAddress = checkHost(rr.getRrRouterId(), IpAddressType.IPV4);

        if (checkAddress == null) {
          logger.error("Rr Router Id NG :" + rr.getRrRouterId());
          return false;

        } else {
          logger.debug("Rr Router Id OK.");

          rr.setRrRouterId(checkAddress);
        }
      }

      for (LeafRr leafRr : dataConf.getSwClustersData().getSwClusterData().getRrs().getLeafRr()) {
        checkAddress = checkHost(leafRr.getLeafRrRouterId(), IpAddressType.IPV4);

        if (checkAddress == null) {
          logger.error("Leaf Rr Router Id NG :" + leafRr.getLeafRrNodeId());
          return false;

        } else {
          logger.debug("Leaf Rr Router Id OK.");

          leafRr.setLeafRrRouterId(checkAddress);
        }
      }

      for (NoticeDestInfoStatus status : systemConf.getStatus().getNoticeDestInfo()) {
        checkAddress = checkHost(status.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress == null) {
          logger.error("Status Notice Address NG :" + status.getNoticeAddress());
          return false;
        } else {
          logger.debug("Status Notice Address OK.");

          status.setNoticeAddress(checkAddress);
        }
      }

      for (NoticeDestInfoFailure failure : systemConf.getFailure().getNoticeDestInfo()) {
        checkAddress = checkHost(failure.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress == null) {
          logger.error("Failure Notice Address NG :" + failure.getNoticeAddress());
          return false;
        } else {
          logger.debug("Failure Notice Address OK.");

          failure.setNoticeAddress(checkAddress);
        }
      }

      for (NoticeDestInfoTraffic traffic : systemConf.getTraffic().getNoticeDestInfo()) {
        checkAddress = checkHost(traffic.getNoticeAddress(), IpAddressType.IPV4);
        if (checkAddress == null) {
          logger.error("Traffic Notice Address NG :" + traffic.getNoticeAddress());
          return false;
        } else {
          logger.debug("Traffic Notice Address OK.");

          traffic.setNoticeAddress(checkAddress);
        }
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the SW cluster information in system config.
   *
   * @return SW cluster information in system config
   */
  public msf.fc.common.config.type.system.SwClusterData getSystemConfSwClusterData() {
    return systemConfSwClusterData;
  }

  /**
   * Get the SW cluster information in initial setting config.
   *
   * @return SW cluster information in initial setting config
   */
  public msf.fc.common.config.type.data.SwClusterData getDataConfSwClusterData() {
    return dataConfSwClusterData;
  }

  /**
   * Get the status notification information in system config.
   *
   * @return systemConfStatus status notification information in system config
   */
  public msf.fc.common.config.type.system.Status getSystemConfStatus() {
    return systemConfStatus;
  }

  /**
   * Get the failure notification information in system config.
   *
   * @return systemConfFailure failure notification information in system config
   */
  public msf.fc.common.config.type.system.Failure getSystemConfFailure() {
    return systemConfFailure;
  }

  /**
   * Get the traffic notification information in system config.
   *
   * @return systemConfTraffic traffic notification information in system config
   */
  public msf.fc.common.config.type.system.Traffic getSystemConfTraffic() {
    return systemConfTraffic;
  }

}
