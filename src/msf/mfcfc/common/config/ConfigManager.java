
package msf.mfcfc.common.config;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.constant.IpAddressType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of config management function block.
 *
 * @author NTT
 *
 */
public class ConfigManager implements FunctionBlockBase {

  protected static final String HIBERNATE_CONF_FILE_NAME = "hibernate.cfg.xml";

  private static final MsfLogger logger = MsfLogger.getInstance(ConfigManager.class);

  protected String confDir = "../conf/";

  protected static ConfigManager instance = null;

  protected String managementIpAddress;

  protected String restServerListeningAddress;

  protected int restServerListeningPort;

  protected int restWaitConnectionTimeout;

  protected int restClientRequestTimeout;

  protected int restClientResponseBufferSize;

  protected boolean restIsPrettyPrinting;

  protected boolean restIsSerializeNulls;

  protected int l2SlicesMaxNum;

  protected int l3SlicesMaxNum;

  protected int l2SlicesMagnificationNum;

  protected int l3SlicesMagnificationNum;

  protected int l3VniVlanIdStartPos;

  protected int l3VniVlanIdEndPos;

  protected int asyncOperationDataRetentionPeriod;

  protected int maxAsyncRunnerThreadNum;

  protected int invokeAllTimout;

  protected int waitOperationResultTimeout;

  protected int executingOperationCheckCycle;

  protected int lockRetryNum;

  protected int lockTimeout;

  protected int noticeRetryNum;

  protected ConfigManager() {
  }

  /**
   * Get the instance of ConfigManager. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return ConfigManager instance
   */
  public static ConfigManager getInstance() {
    try {
      logger.methodStart();
      return instance;

    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Set the config file directory path.
   *
   * @param configPath
   *          Config file path
   */
  public void setConfigPath(String configPath) {

    if (configPath.charAt(configPath.length() - 1) != '/') {

      this.confDir = configPath + "/";

    } else {

      this.confDir = configPath;
    }
  }

  /**
   * Get the config file directory path.
   *
   * @return Config file path
   */
  public String getConfigPath() {
    return this.confDir;
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

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Returns the status of the configuration management function block.
   *
   * @return process result true
   */
  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Terminate the config management function block.
   *
   * @return process result true
   */
  @Override
  public boolean stop() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Returns the config file path of Hibernate.
   *
   * @return Hibernate config file path
   */
  public String getHibernateConfFilePath() {
    return confDir + HIBERNATE_CONF_FILE_NAME;

  }

  protected String checkHost(String host, IpAddressType ipAddressType) {
    try {
      logger.methodStart();
      return IpAddressUtil.checkHost(host, ipAddressType);
    } finally {
      logger.methodEnd();
    }

  }

  protected boolean loadSystemConfig() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  protected boolean loadDataConfig() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  protected boolean loadDevelopConfig() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  /**
   * Load config files.
   *
   * @param confFile
   *          Config file path
   *
   * @param xsdPath
   *          XSD config file path
   *
   * @param objectFactory
   *          ObjectFactory class
   *
   * @return JAXBIntrospctor object
   *
   * @throws SAXException
   *           Config file read error
   *
   * @throws JAXBException
   *           Config file read error
   */
  public Object loadConfig(String confFile, String xsdPath, Class<?> objectFactory) throws SAXException, JAXBException {

    File conf = new File(confFile);

    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getClassLoader().getResource(xsdPath));
    JAXBContext jc = JAXBContext.newInstance(objectFactory);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    unmarshaller.setSchema(schema);

    return JAXBIntrospector.getValue(unmarshaller.unmarshal(conf));

  }

  protected void setCommonData(String managementIpAddress, String restServerListeningAddress,
      int restServerListeningPort, int restWaitConnectionTimeout, int restClientRequestTimeout,
      int restClientResponseBufferSize, boolean restIsPrettyPrinting, boolean restIsSerializeNulls, int l2SlicesMaxNum,
      int l3SlicesMaxNum, int l2SlicesMagnificationNum, int l3SlicesMagnificationNum, int l3VniVlanIdStartPos,
      int l3VniVlanIdEndPos, int asyncOperationDataRetentionPeriod, int maxAsyncRunnerThreadNum, int invokeAllTimout,
      int waitOperationResultTimeout, int executingOperationCheckCycle, int lockRetryNum, int lockTimeout,
      int noticeRetryNum) {
    try {
      logger.methodStart();
      this.managementIpAddress = managementIpAddress;

      this.restServerListeningAddress = restServerListeningAddress;

      this.restServerListeningPort = restServerListeningPort;

      this.restWaitConnectionTimeout = restWaitConnectionTimeout;

      this.restClientRequestTimeout = restClientRequestTimeout;

      this.restClientResponseBufferSize = restClientResponseBufferSize;

      this.restIsPrettyPrinting = restIsPrettyPrinting;

      this.restIsSerializeNulls = restIsSerializeNulls;

      this.l2SlicesMaxNum = l2SlicesMaxNum;

      this.l3SlicesMaxNum = l3SlicesMaxNum;

      this.l2SlicesMagnificationNum = l2SlicesMagnificationNum;

      this.l3SlicesMagnificationNum = l3SlicesMagnificationNum;

      this.l3VniVlanIdStartPos = l3VniVlanIdStartPos;

      this.l3VniVlanIdEndPos = l3VniVlanIdEndPos;

      this.asyncOperationDataRetentionPeriod = asyncOperationDataRetentionPeriod;

      this.maxAsyncRunnerThreadNum = maxAsyncRunnerThreadNum;

      this.invokeAllTimout = invokeAllTimout;

      this.waitOperationResultTimeout = waitOperationResultTimeout;

      this.executingOperationCheckCycle = executingOperationCheckCycle;

      this.lockRetryNum = lockRetryNum;

      this.lockTimeout = lockTimeout;

      this.noticeRetryNum = noticeRetryNum;

    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkConfigParameter() {
    try {
      logger.methodStart();

      String checkAddress = null;

      checkAddress = checkHost(managementIpAddress, IpAddressType.IPV4V6);

      if (checkAddress == null) {
        logger.error("ManagementIpAddress is NG because the format is invalid.:" + managementIpAddress);
        return false;

      } else {
        logger.debug("ManagementIpAddress is OK.");

        managementIpAddress = checkAddress;
      }

      checkAddress = checkHost(restServerListeningAddress, IpAddressType.IPV4V6);

      if (checkAddress == null) {
        logger.error("RestServerListeningAddress is NG because the format is invalid.:" + restServerListeningAddress);
        return false;

      } else {
        logger.debug("RestServerListeningAddress is OK.");

        restServerListeningAddress = checkAddress;
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  public String getManagementIpAddress() {
    return managementIpAddress;
  }

  public String getRestServerListeningAddress() {
    return restServerListeningAddress;
  }

  public int getRestServerListeningPort() {
    return restServerListeningPort;
  }

  public int getRestWaitConnectionTimeout() {
    return restWaitConnectionTimeout;
  }

  public int getRestClientRequestTimeout() {
    return restClientRequestTimeout;
  }

  public int getRestClientResponseBufferSize() {
    return restClientResponseBufferSize;
  }

  public boolean isPrettyPrinting() {
    return restIsPrettyPrinting;
  }

  public boolean isSerializeNulls() {
    return restIsSerializeNulls;
  }

  public int getL2SlicesMaxNum() {
    return l2SlicesMaxNum;
  }

  public int getL3SlicesMaxNum() {
    return l3SlicesMaxNum;
  }

  public int getL2SlicesMagnificationNum() {
    return l2SlicesMagnificationNum;
  }

  public int getL3SlicesMagnificationNum() {
    return l3SlicesMagnificationNum;
  }

  public int getAsyncOperationDataRetentionPeriod() {
    return asyncOperationDataRetentionPeriod;
  }

  public int getMaxAsyncRunnerThreadNum() {
    return maxAsyncRunnerThreadNum;
  }

  public int getInvokeAllTimout() {
    return invokeAllTimout;
  }

  public int getWaitOperationResultTimeout() {
    return waitOperationResultTimeout;
  }

  public int getExecutingOperationCheckCycle() {
    return executingOperationCheckCycle;
  }

  public int getLockRetryNum() {
    return lockRetryNum;
  }

  public int getLockTimeout() {
    return lockTimeout;
  }

  public int getNoticeRetryNum() {
    return noticeRetryNum;
  }

  /**
   * Get the minimum value of VLAN IDs' range in the system config, which are
   * assigned for creating L3VNI.
   *
   * @return The minimum value of VLAN IDs assigned for L3VNI creating
   */
  public int getL3VniVlanIdStartPos() {
    return l3VniVlanIdStartPos;
  }

  /**
   * Get the maximum value of VLAN IDs' range in the system config, which are
   * assigned for creating L3VNI.
   *
   * @return The maximum value of VLAN IDs assigned for the L3VNI creating
   */
  public int getL3VniVlanIdEndPos() {
    return l3VniVlanIdEndPos;
  }
}
