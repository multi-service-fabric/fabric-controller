
package msf.mfc.common.config;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import msf.mfc.common.config.type.data.DataConf;
import msf.mfc.common.config.type.develop.DevelopConf;
import msf.mfc.common.config.type.system.Failure;
import msf.mfc.common.config.type.system.Status;
import msf.mfc.common.config.type.system.SystemConf;
import msf.mfc.common.config.type.system.Traffic;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.IpAddressType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of config management function block.
 *
 * @author NTT
 *
 */
public class MfcConfigManager extends ConfigManager {

  private static final String SYSTEM_CONFIG_NAME = "mfc_system.xml";

  private static final String DATA_CONFIG_NAME = "mfc_data.xml";

  private static final String DEVELOP_CONFIG_NAME = "mfc_develop.xml";

  private static final String SYSTEM_CONFIG_XSD_NAME = "mfc_system_schema.xsd";

  private static final String DATA_CONFIG_XSD_NAME = "mfc_data_schema.xsd";

  private static final String DEVELOP_CONFIG_XSD_NAME = "mfc_develop_schema.xsd";

  private static final String XSD_DIR = "msf/mfc/common/config/xsd/";

  private static final MsfLogger logger = MsfLogger.getInstance(MfcConfigManager.class);

  private SystemConf systemConf;

  private DataConf dataConf;

  private DevelopConf developConf;

  private List<msf.mfc.common.config.type.system.SwClusterData> systemConfSwClusterDataList;

  private List<msf.mfc.common.config.type.data.SwClusterData> dataConfSwClusterDataList;

  private int maxSwClusterNum;

  private String clusterStartAddress;

  private Status systemConfStatus;

  private Failure systemConfFailure;

  private Traffic systemConfTraffic;

  private MfcConfigManager() {
  }

  /**
   * Get the instance of MfcConfigManager.
   *
   * @return MfcConfigManager instance
   */
  public static MfcConfigManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcConfigManager();
      }
      return (MfcConfigManager) instance;
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
   * @return boolean process result, true success, false fail
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

      setCommonData(systemConf.getController().getManagementIpAddress(),
          systemConf.getRest().getServer().getListeningAddress(), systemConf.getRest().getServer().getListeningPort(),
          systemConf.getRest().getClient().getWaitConnectionTimeout(),
          systemConf.getRest().getClient().getRequestTimeout(),
          systemConf.getRest().getClient().getResponseBufferSize(), systemConf.getRest().getJson().isIsPrettyPrinting(),
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

      systemConfSwClusterDataList = systemConf.getSwClustersData().getSwClusterData();

      dataConfSwClusterDataList = dataConf.getSwClustersData().getSwClusterData();

      maxSwClusterNum = dataConf.getSwClustersData().getMaxSwClusterNum();

      clusterStartAddress = dataConf.getSwClustersData().getClusterStartAddress();

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
          XSD_DIR + SYSTEM_CONFIG_XSD_NAME, msf.mfc.common.config.type.system.ObjectFactory.class);
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
          msf.mfc.common.config.type.data.ObjectFactory.class);
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
          XSD_DIR + DEVELOP_CONFIG_XSD_NAME, msf.mfc.common.config.type.develop.ObjectFactory.class);
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

      Set<Integer> dataConfigIdSet = new TreeSet<>();
      for (msf.mfc.common.config.type.data.SwClusterData data : dataConf.getSwClustersData().getSwClusterData()) {
        dataConfigIdSet.add(data.getSwCluster().getSwClusterId());
      }
      Set<Integer> systemConfigIdSet = new TreeSet<>();
      for (msf.mfc.common.config.type.system.SwClusterData data : systemConf.getSwClustersData().getSwClusterData()) {
        systemConfigIdSet.add(data.getSwCluster().getSwClusterId());
      }

      if (!dataConfigIdSet.equals(systemConfigIdSet)) {
        return false;
      }

      checkAddress = checkHost(dataConf.getSwClustersData().getClusterStartAddress(), IpAddressType.IPV4);
      if (checkAddress == null) {
        logger.error("Cluster Start Address NG :" + dataConf.getSwClustersData().getClusterStartAddress());
        return false;

      } else {
        logger.debug("Cluster Start Address OK.");

        dataConf.getSwClustersData().setClusterStartAddress(checkAddress);
      }

      for (msf.mfc.common.config.type.system.SwClusterData swClusterData : systemConfSwClusterDataList) {
        checkAddress = checkHost(swClusterData.getSwCluster().getFcControlAddress(), IpAddressType.IPV4V6FQDN);

        if (checkAddress == null) {
          logger.error("Fc Control Address NG :" + swClusterData.getSwCluster().getFcControlAddress());
          return false;

        } else {
          logger.debug("Fc Control Address OK.");

          swClusterData.getSwCluster().setFcControlAddress(checkAddress);
        }
      }

      for (msf.mfc.common.config.type.data.SwClusterData swClusterData : dataConfSwClusterDataList) {
        checkAddress = checkHost(swClusterData.getSwCluster().getInchannelStartAddress(), IpAddressType.IPV4);

        if (checkAddress == null) {
          logger.error("Inchannel Start Address NG :" + swClusterData.getSwCluster().getInchannelStartAddress());
          return false;

        } else {
          logger.debug("Inchannel Start Address OK.");

          swClusterData.getSwCluster().setInchannelStartAddress(checkAddress);
        }
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the maximum cluster number.
   *
   * @return maximum cluster number
   */
  public int getMaxSwClusterNum() {
    return maxSwClusterNum;
  }

  /**
   * Get the inter-cluster link IF starting IP address.
   *
   * @return inter-cluster link IF starting IP address
   */
  public String getClusterStartAddress() {
    return clusterStartAddress;
  }

  /**
   * Get the SW cluster information list of system config.
   *
   * @return SW cluster information list of system config
   */
  public List<msf.mfc.common.config.type.system.SwClusterData> getSystemConfSwClusterDataList() {
    return systemConfSwClusterDataList;
  }

  /**
   * Get the SW cluster information list of initial setting config.
   *
   * @return SW cluster information list of initial setting config
   */
  public List<msf.mfc.common.config.type.data.SwClusterData> getDataConfSwClusterDataList() {
    return dataConfSwClusterDataList;
  }

  /**
   * Get the SW cluster information of specified SW cluster ID in system config.
   *
   * @param clusterId
   *          SW cluster ID
   * @return SW cluster information in system config
   */
  public msf.mfc.common.config.type.system.SwClusterData getSystemConfSwClusterData(int clusterId) {
    try {
      logger.methodStart();
      for (msf.mfc.common.config.type.system.SwClusterData swClusterData : systemConfSwClusterDataList) {
        if (swClusterData.getSwCluster().getSwClusterId() == clusterId) {
          return swClusterData;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
      ;
    }
  }

  /**
   * Get the SW cluster information in initial setting config of specified SW
   * cluster ID.
   *
   * @param clusterId
   *          SW cluster ID
   * @return SW cluster information in initial setting config
   */
  public msf.mfc.common.config.type.data.SwClusterData getDataConfSwClusterData(int clusterId) {
    try {
      logger.methodStart();
      for (msf.mfc.common.config.type.data.SwClusterData swClusterData : dataConfSwClusterDataList) {
        if (swClusterData.getSwCluster().getSwClusterId() == clusterId) {
          return swClusterData;
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Reload the various config files and initialize the variables for getter.
   *
   * @throws MsfException
   *           Config reload failure exception
   */
  public void reloadConfig() throws MsfException {
    try {
      logger.methodStart();

      boolean checkReloadConfig = start();

      if (!checkReloadConfig) {
        logger.error("failed reload config file.");
        throw new MsfException(ErrorCode.FILE_READ_ERROR, "failed reload config file.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the status notification information in system config.
   *
   * @return systemConfStatus status notification information in system config
   */
  public Status getSystemConfStatus() {
    return systemConfStatus;
  }

  /**
   * Get the failure notification information in system config.
   *
   * @return systemConfFailure failure notification information in system config
   */
  public Failure getSystemConfFailure() {
    return systemConfFailure;
  }

  /**
   * Get the traffic notification information in system config.
   *
   * @return systemConfTraffic traffic notification information in system config
   */
  public Traffic getSystemConfTraffic() {
    return systemConfTraffic;
  }

}
