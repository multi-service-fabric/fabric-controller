
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
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of config management function block.
 *
 * @author NTT
 *
 */
public class ConfigManager implements FunctionBlockBase {

  protected static final String HIBERNATE_CONF_FILE_NAME = "hibernate.cfg.xml";

  protected static final MsfLogger logger = MsfLogger.getInstance(ConfigManager.class);

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
   * Start the config management function block. <br>
   * <br>
   * Read the several config files and initialize the variables for getter
   * methods. <br>
   *
   * @return boolean process result, true success, false fail
   */
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
   * Return the config file path of Hibernate.
   *
   * @return Hibernate config file path
   */
  public String getHibernateConfFilePath() {
    return confDir + HIBERNATE_CONF_FILE_NAME;

  }

  protected String checkHost(String host, IpAddressType ipAddressType) {
    try {
      logger.methodStart();
      String resultString;

      switch (ipAddressType) {
        case IPV4:

          try {

            resultString = ParameterCheckUtil.checkIpv4Address(host);

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4.");
            resultString = null;

          }
          return resultString;

        case IPV4V6:

          try {

            resultString = ParameterCheckUtil.checkIpAddress(host);

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4/IPv6.");
            resultString = null;

          }
          return resultString;

        case IPV4V6FQDN:

          try {

            resultString = ParameterCheckUtil.checkIpAddress(host);
            return resultString;

          } catch (MsfException error1) {

          }

          try {

            ParameterCheckUtil.checkIdSpecifiedByUri(host);
            return host;

          } catch (MsfException error1) {
            logger.warn(host + " is not IPv4/IPv6/FQDN.");
            resultString = null;
            return resultString;
          }

        default:

          resultString = null;
          logger.warn("IP address type parameter is incorrect.");
      }

      return resultString;
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

  protected Object loadConfig(String confFile, String xsdPath, Class<?> objectFactory)
      throws SAXException, JAXBException {

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
      int l3SlicesMaxNum, int l2SlicesMagnificationNum, int l3SlicesMagnificationNum,
      int asyncOperationDataRetentionPeriod, int maxAsyncRunnerThreadNum, int invokeAllTimout,
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
        logger.error("Management Ip Address NG :" + managementIpAddress);
        return false;

      } else {
        logger.debug("Management Ip Address OK.");

        managementIpAddress = checkAddress;
      }

      checkAddress = checkHost(restServerListeningAddress, IpAddressType.IPV4V6);

      if (checkAddress == null) {
        logger.error("Rest Server Listening Address NG :" + restServerListeningAddress);
        return false;

      } else {
        logger.debug("Listening Address OK.");

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

}
