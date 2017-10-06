package msf.fc.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.config.type.data.DataConf;
import msf.fc.common.config.type.develop.DevelopConf;
import msf.fc.common.config.type.system.SystemConf;
import msf.fc.common.constant.IpAddressType;
import msf.fc.common.data.Rr;
import msf.fc.common.data.RrPK;
import msf.fc.common.data.SliceManagerBaseInfo;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.RrDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.db.dao.slices.SliceManagerBaseInfoDao;

public class ConfigManager implements FunctionBlockBase {

  private static final String SYSTEM_CONFIG_NAME = "fc_system.xml";

  private static final String DEVELOP_CONFIG_NAME = "fc_develop.xml";

  private static final String DATA_CONFIG_XSD_NAME = "fc_data_schema.xsd";

  private static final String HIBERNATE_CONF_FILE_PATH = "../conf/hibernate.cfg.xml";

  private static final String XSD_DIR = "msf/fc/common/config/xsd/";

  private static final ConfigManager instance = new ConfigManager();

  private SystemConf systemConf;

  private DevelopConf developConf;

  private int restServerListeningPort;

  private int restClientRequestTimeout;

  private boolean restIsSerializeNulls;

  private int l2SlicesMaxNum;

  private int l2SlicesMagnificationNum;

  private int trafficDataRetentionPeriod;

  private String trafficTmInputFilePath;

  private int asyncOperationDataRetentionPeriod;

  private int executingOperationCheckCycle;

  private int lockRetryNum;

  private Map<Integer, msf.fc.common.config.type.system.SwClusterData> systemSwClusterMap;

  private ConfigManager() {
  }

  public static ConfigManager getInstance() {
    try {
      logger.methodStart();

      return instance;

    } finally {
      logger.methodEnd();

    }
  }

  public void setConfigPath(String configPath) {

      this.confDir = configPath + "/";

    } else {
      this.confDir = configPath;
    }
  }

  public boolean start() {
    try {
      logger.methodStart();

      boolean checkLoadFile = true;

      try {
        this.systemConf = (SystemConf) this.loadConfig(this.confDir + SYSTEM_CONFIG_NAME,
            XSD_DIR + SYSTEM_CONFIG_XSD_NAME, msf.fc.common.config.type.system.ObjectFactory.class);

      } catch (SAXException error1) {
        logger.error("SystemConfig file could not read.", error1);
        checkLoadFile = false;

      } catch (JAXBException error1) {
        logger.error("SystemConfig file could not read.", error1);
        checkLoadFile = false;

      }

      try {
        this.dataConf = (DataConf) this.loadConfig(this.confDir + DATA_CONFIG_NAME, XSD_DIR + DATA_CONFIG_XSD_NAME,
            msf.fc.common.config.type.data.ObjectFactory.class);

      } catch (SAXException error1) {
        logger.error("DataConfig file could not read.", error1);
        checkLoadFile = false;

      } catch (JAXBException error1) {
        logger.error("DataConfig file could not read.", error1);
        checkLoadFile = false;

      }

      try {
        this.developConf = (DevelopConf) this.loadConfig(this.confDir + DEVELOP_CONFIG_NAME,
            XSD_DIR + DEVELOP_CONFIG_XSD_NAME, msf.fc.common.config.type.develop.ObjectFactory.class);

      } catch (SAXException error1) {
        logger.error("DevelopConfig file could not read.", error1);
        checkLoadFile = false;

      } catch (JAXBException error1) {
        logger.error("DevelopConfig file could not read.", error1);
        checkLoadFile = false;

      }

      if (!checkLoadFile) {
        return false;

      }

      restServerListeningAddress = this.systemConf.getRest().getServer().getListeningAddress();

      String checkAddress = restServerListeningAddress;
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4V6);

      if (checkAddress == null) {
        logger.error("Rest Server Listening Address NG :" + restServerListeningAddress);
        return false;

      } else {
        logger.debug("Listenig Address OK.");
        this.systemConf.getRest().getServer().setListeningAddress(checkAddress);
      }

      this.restServerListeningPort = this.systemConf.getRest().getServer().getListeningPort();

      this.restWaitConnectionTimeout = this.systemConf.getRest().getClient().getWaitConnectionTimeout();

      this.restClientRequestTimeout = this.systemConf.getRest().getClient().getRequestTimeout();

      this.restIsPrettyPrinting = this.systemConf.getRest().getJson().isPrettyPrinting();

      this.restIsSerializeNulls = this.systemConf.getRest().getJson().isSerializeNulls();

      this.l2SlicesMaxNum = this.systemConf.getSlice().getL2MaxSlicesNum();

      this.l3SlicesMaxNum = this.systemConf.getSlice().getL3MaxSlicesNum();

      this.l2SlicesMagnificationNum = this.systemConf.getSlice().getL2SlicesMagnificationNum();

      this.l3SlicesMagnificationNum = this.systemConf.getSlice().getL3SlicesMagnificationNum();

      this.trafficInterval = this.systemConf.getTraffic().getInterval();

      this.trafficDataRetentionPeriod = this.systemConf.getTraffic().getDataRetentionPeriod();

      this.trafficTmToolPath = this.systemConf.getTraffic().getTmToolPath();

      this.trafficTmInputFilePath = this.systemConf.getTraffic().getTmInputFilePath();

      this.trafficTmOutputFilePath = this.systemConf.getTraffic().getTmOutputFilePath();

      this.maxAsyncRunnerThreadNum = this.developConf.getSystem().getAsyncOperation().getMaxAsyncRunnerThreadNum();

      this.asyncOperationDataRetentionPeriod = this.developConf.getSystem().getAsyncOperation()
          .getDataRetentionPeriod();

      this.executingOperationCheckCycle = this.developConf.getSystem().getExecutingOperationCheckCycle();

      this.lockRetryNum = this.developConf.getSystem().getLock().getLockRetryNum();

      this.lockTimeout = this.developConf.getSystem().getLock().getLockTimeout();

      this.systemSwClusterMap = new HashMap<>();

      for (msf.fc.common.config.type.system.SwClusterData target : this.systemConf.getSwClustersData()
          .getSwClusterData()) {
        this.systemSwClusterMap.put(target.getSwCluster().getSwClusterId(), target);

      }

      this.dataSwClusterMap = new HashMap<>();

      for (msf.fc.common.config.type.data.SwClusterData target : this.dataConf.getSwClustersData().getSwClusterData()) {
        this.dataSwClusterMap.put(target.getSwCluster().getSwClusterId(), target);

      }

      if (!this.checkSwClusterParameter()) {
        logger.error("SW Cluster Info is wrong.");
        return false;

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

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();

      return true;

    } finally {
      logger.methodEnd();

    }
  }

  public boolean insertDbRecords() {

    try {
      logger.methodStart();

      boolean result = true;

      Set<Integer> systemSwClusterIdSet = this.systemSwClusterMap.keySet();

      SessionWrapper session = new SessionWrapper();

      try {
        session.openSession();

        SwClusterDao swClusterDao = new SwClusterDao();
        if (!this.checkConfigSwClusterId(session, swClusterDao)) {
          return false;

        }

        session.beginTransaction();

        Iterator<Integer> systemIt = systemSwClusterIdSet.iterator();

        while (systemIt.hasNext()) {
          Integer swClusterId = systemIt.next();

          msf.fc.common.config.type.system.SwCluster systemSwCluster = this.systemSwClusterMap.get(swClusterId)
              .getSwCluster();

          msf.fc.common.config.type.data.SwCluster dataSwCluster = this.dataSwClusterMap.get(swClusterId)
              .getSwCluster();

          SwCluster dbSwCluster = swClusterDao.read(session, swClusterId.toString());

          logger.debug("SW Cluster info insertion processing start.");
          if (!this.setSwClusterDbRecord(session, systemSwCluster, dataSwCluster, swClusterDao, dbSwCluster)) {
            logger.warn("SW Cluster info insertion processing failed.");
            result = false;
            break;

          }

          logger.debug("RR info insertion processing start.");
          if (!this.setRrDbRecords(session, dataSwCluster)) {
            logger.warn("RR info insertion processing failed.");
            result = false;
            break;

          }
        }

        if (result) {
          logger.debug("L2VPN MulticastAddressBase insertion processing start.");
          if (!this.setL2vpnMulicastAddressBaseDbRecord(session)) {
            logger.warn("L2VPN MulticastAddressBase insertion processing failed.");
            result = false;

          }
        }

        if (result) {
          session.commit();

        } else {
          session.rollback();

        }
      } catch (MsfException error1) {
        logger.warn("DB processing failed.", error1);
        result = false;

        try {
          session.rollback();

        } catch (MsfException error2) {
          logger.warn("DB roll back processing failed.", error2);

        }
      } finally {
        session.closeSession();

      }

      if (result) {
        logger.debug("Insertion to DB processing succeed.");
      } else {
        logger.warn("Insertion to DB processing failed.");
      }
      return result;

    } finally {
      logger.methodEnd();

    }
  }

  public String getRestServerListeningAddress() {
    return this.restServerListeningAddress;

  }

  public int getRestServerListeningPort() {
    return this.restServerListeningPort;

  }

  public int getRestWaitConnectionTimeout() {
    return this.restWaitConnectionTimeout;

  }

  public int getRestClientRequestTimeout() {
    return this.restClientRequestTimeout;

  }

  public boolean isPrettyPrinting() {
    return this.restIsPrettyPrinting;

  }

  public boolean isSerializeNulls() {
    return this.restIsSerializeNulls;

  }

  public int getTrafficInterval() {
    return this.trafficInterval;

  }

  public int getTrafficDataRetentionPeriod() {
    return this.trafficDataRetentionPeriod;

  }

  public String getTrafficTmToolPath() {
    return this.trafficTmToolPath;

  }

  public String getTrafficTmInputFilePath() {
    return this.trafficTmInputFilePath;

  }

  public String getTrafficTmOutputFilePath() {
    return this.trafficTmOutputFilePath;

  }

  public int getL2SlicesMaxNum() {
    return this.l2SlicesMaxNum;

  }

  public int getL3SlicesMaxNum() {
    return this.l3SlicesMaxNum;

  }

  public int getL2SlicesMagnificationNum() {
    return this.l2SlicesMagnificationNum;

  }

  public int getL3SlicesMagnificationNum() {
    return this.l3SlicesMagnificationNum;

  }

  public int getExecutingOperationCheckCycle() {
    return this.executingOperationCheckCycle;

  }

  public int getLockRetryNum() {
    return this.lockRetryNum;

  }

  public int getLockTimeout() {
    return this.lockTimeout;

  }

  public int getAsyncOperationDataRetentionPeriod() {
    return this.asyncOperationDataRetentionPeriod;

  }

  public int getMaxAsyncRunnerThreadNum() {
    return this.maxAsyncRunnerThreadNum;

  }

  public String getHibernateConfFilePath() {
    return HIBERNATE_CONF_FILE_PATH;

  }

  private String checkHost(String host, IpAddressType ipAddressType) {

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

  }

  private Object loadConfig(String confFile, String xsdPath, Class<?> objectFactory)
      throws SAXException, JAXBException {

    File conf = new File(confFile);

    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    Schema schema = sf.newSchema(getClass().getClassLoader().getResource(xsdPath));
    JAXBContext jc = JAXBContext.newInstance(objectFactory);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    unmarshaller.setSchema(schema);

    return JAXBIntrospector.getValue(unmarshaller.unmarshal(conf));

  }

  private boolean checkSwClusterParameter() {

    boolean result = true;

    if (this.systemSwClusterMap.size() != 1 || this.dataSwClusterMap.size() != 1) {
      logger.warn("There are two or more SW Clusters. ");
      return false;

    }

    Set<Integer> systemSwClusterIdSet = this.systemSwClusterMap.keySet();

    Set<Integer> dataSwClusterIdSet = this.dataSwClusterMap.keySet();

    if (!systemSwClusterIdSet.equals(dataSwClusterIdSet)) {
      logger.warn("SW Cluster ID(Config) is wrong.");
      return false;

    }

    String checkAddress = new String();

    Iterator<Integer> systemIt = systemSwClusterIdSet.iterator();
    while (systemIt.hasNext()) {
      Integer swClusterId = systemIt.next();

      logger.info("Check SW Cluster(ID =" + swClusterId + ") Info start.");

      msf.fc.common.config.type.system.SwCluster systemSwCluster = this.systemSwClusterMap.get(swClusterId)
          .getSwCluster();

      logger.debug("Check EC Control Address.");
      checkAddress = systemSwCluster.getEcControlAddress();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4V6FQDN);

      if (checkAddress == null) {
        logger.error("EC Control Address NG :" + checkAddress);
        result = false;
      } else {
        systemSwCluster.setEcControlAddress(checkAddress);
        logger.debug("EC Control Address OK.");
      }

      msf.fc.common.config.type.data.SwCluster dataSwCluster = this.dataSwClusterMap.get(swClusterId).getSwCluster();

      logger.debug("Check RP Loopback Address.");
      checkAddress = dataSwCluster.getRpLoopbackAddress();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("RP Loopback Address NG :" + checkAddress);
        result = false;
      } else {
        dataSwCluster.setRpLoopbackAddress(checkAddress);
        logger.debug("RP Loopback Address OK.");
      }

      logger.debug("Check Interface start Address.");
      checkAddress = dataSwCluster.getInterfaceStartAddress();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Interface start Address NG :" + checkAddress);
        result = false;
      } else {
        logger.debug("Interface start Address OK.");
        dataSwCluster.setInterfaceStartAddress(checkAddress);
      }

      logger.debug("Check Loopback start Address.");
      checkAddress = dataSwCluster.getLoopbackStartAddress();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Loopback start Address NG :" + checkAddress);
        result = false;
      } else {
        logger.debug("Loopback start Address OK.");
        dataSwCluster.setLoopbackStartAddress(checkAddress);
      }

      logger.debug("Check Management start Address.");
      checkAddress = dataSwCluster.getManagementStartAddress();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.error("Management start Address NG :" + checkAddress);
        result = false;
      } else {
        logger.debug("Check Management start Address OK.");
        dataSwCluster.setManagementStartAddress(checkAddress);
      }

      int managementPrefix = dataSwCluster.getManagementAddressPrefix();
      int leafNum = dataSwCluster.getMaxLeafNum();
      int spineNum = dataSwCluster.getMaxSpineNum();

      logger.debug("Check Node(Leaf and Spine) number.");

      long nodeNum = (long) Math.pow(2, (32 - managementPrefix));

      if (nodeNum - (leafNum + spineNum + 3) < 0) {
        logger.error(
            "There are too many Nodes(Leaf and Spine). Please check Node(Leaf and Spine) number or Management Prefix.");
        result = false;

      } else {
        logger.debug("Check Node(Leaf and Spine) number OK.");

      }
      logger.info("Check SW Cluster(ID =" + swClusterId + ") Info end.");

    }
    return result;

  }

  private boolean checkConfigSwClusterId(SessionWrapper session, SwClusterDao swClusterDao) {

    boolean result = true;

    try {
      List<SwCluster> swClusterList = swClusterDao.readList(session);

      if (swClusterList.size() > 1) {
        logger.warn("There are two or more SW Clusters in the FabricController DB.");
        result = false;

      } else if (swClusterList.size() == 1) {
        for (SwCluster target : swClusterList) {
          Integer dbSwClusterId = Integer.parseInt(target.getSwClusterId());

          if (!this.systemSwClusterMap.containsKey(dbSwClusterId)) {
            logger.error("SW Cluster ID(Config) is wrong.");
            result = false;

          }
        }
      }
    } catch (MsfException error1) {
      logger.warn("DB processing failed.", error1);
      result = false;

    }
    return result;

  }

  private boolean setSwClusterDbRecord(SessionWrapper session,
      msf.fc.common.config.type.system.SwCluster systemSwCluster,
      msf.fc.common.config.type.data.SwCluster dataSwCluster, SwClusterDao swClusterDao, SwCluster dbSwCluster) {

    boolean result = true;

    try {
      if (dbSwCluster == null) {
        SwCluster newCluster = new SwCluster();

        newCluster.setSwClusterId(String.valueOf(dataSwCluster.getSwClusterId()));

        newCluster = this.setSwClusterInfo(newCluster, systemSwCluster, dataSwCluster);

        swClusterDao.create(session, newCluster);

      } else {
        dbSwCluster = this.setSwClusterInfo(dbSwCluster, systemSwCluster, dataSwCluster);

        swClusterDao.update(session, dbSwCluster);

      }
    } catch (MsfException error1) {
      result = false;
      logger.warn("DB processing failed.", error1);

    }
    return result;
  }

  private SwCluster setSwClusterInfo(SwCluster swClusterInfo,
      msf.fc.common.config.type.system.SwCluster systemSwCluster,
      msf.fc.common.config.type.data.SwCluster dataSwCluster) {

    swClusterInfo.setEcControlAddress(systemSwCluster.getEcControlAddress());
    swClusterInfo.setEcControlPort(systemSwCluster.getEcControlPort());
    swClusterInfo.setMaxLeafNum(dataSwCluster.getMaxLeafNum());
    swClusterInfo.setMaxSpineNum(dataSwCluster.getMaxSpineNum());
    swClusterInfo.setAsNum(dataSwCluster.getAsNum());
    swClusterInfo.setRpLoopbackAddress(dataSwCluster.getRpLoopbackAddress());
    swClusterInfo.setInterfaceStartAddress(dataSwCluster.getInterfaceStartAddress());
    swClusterInfo.setLoopbackStartAddress(dataSwCluster.getLoopbackStartAddress());
    swClusterInfo.setManagementStartAddress(dataSwCluster.getManagementStartAddress());
    swClusterInfo.setManagementAddressPrefix(dataSwCluster.getManagementAddressPrefix());

    return swClusterInfo;

  }

  private boolean setRrDbRecords(SessionWrapper session, msf.fc.common.config.type.data.SwCluster dataSwCluster) {
    Set<Integer> dataSwClusterIdSet = this.dataSwClusterMap.keySet();

    Iterator<Integer> dataIt = dataSwClusterIdSet.iterator();

    boolean result = true;

    try {

      while (dataIt.hasNext()) {

        List<msf.fc.common.config.type.data.Rr> tmpConfigRrList = this.dataSwClusterMap.get(dataIt.next()).getRrs()
            .getRr();

        if (!this.checkRrRouterId(tmpConfigRrList)) {
          logger.error("RR Router ID format is wrong.");
          return false;
        }

        List<Rr> configRrList = new ArrayList<>();
        for (msf.fc.common.config.type.data.Rr dataRr : tmpConfigRrList) {
          configRrList.add(this.convertRrInfo(dataRr, String.valueOf(dataSwCluster.getSwClusterId())));
        }

        RrDao rrDao = new RrDao();
        List<Rr> dbRrList = rrDao.readList(session, String.valueOf(dataSwCluster.getSwClusterId()));

        if (dbRrList.size() == 0) {
          for (Rr configRr : configRrList) {
            rrDao.create(session, configRr);
          }

        } else {

          for (Rr dbRr : dbRrList) {

            int index = configRrList.indexOf(dbRr);

            if (0 <= index) {
              Rr updateRrInfo = configRrList.get(index);
              dbRr.setNodes(updateRrInfo.getNodes());
              dbRr.setRrRouterId(updateRrInfo.getRrRouterId());
              dbRr.setSwCluster(updateRrInfo.getSwCluster());
              rrDao.update(session, dbRr);
              logger.info("Update RR info. {0}.", dbRr);

              configRrList.remove(dbRr);

            } else {
              rrDao.delete(session, dbRr.getId());
              logger.info("Delete RR info. {0}.", dbRr);
            }
          }

          for (Rr configRr : configRrList) {
            rrDao.create(session, configRr);
            logger.info("Create RR info. {0}.", configRr);
          }
        }
      }
    } catch (

    MsfException error1) {
      result = false;
      logger.warn("DB processing failed.", error1);

    }
    return result;

  }

  private boolean checkRrRouterId(List<msf.fc.common.config.type.data.Rr> dataRrList) {
    boolean result = true;

    logger.debug("Check RR Router ID.");

    String checkAddress = new String();

    for (msf.fc.common.config.type.data.Rr configRr : dataRrList) {

      checkAddress = configRr.getRrRouterId();
      checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

      if (checkAddress == null) {
        logger.warn("RR Router ID NG :" + checkAddress);
        result = false;
      } else {
        configRr.setRrRouterId(checkAddress);
      }
    }
    return result;

  }

  private Rr convertRrInfo(msf.fc.common.config.type.data.Rr configRr, String swClusterId) {
    Rr rr = new Rr();
    RrPK rrPk = new RrPK();

    rrPk.setRrNodeId(configRr.getRrNodeId());
    rrPk.setSwClusterId(swClusterId);
    rr.setId(rrPk);
    rr.setRrRouterId(configRr.getRrRouterId());

    return rr;

  }

  private boolean setL2vpnMulicastAddressBaseDbRecord(SessionWrapper session) {
    boolean result = true;

    try {
      SliceManagerBaseInfoDao sliceManagerBaseInfoDao = new SliceManagerBaseInfoDao();
      SliceManagerBaseInfo slice = sliceManagerBaseInfoDao.read(session, BASE_ID);

      if (!this.checkL2VpnMulticastAddressBase()) {
        return false;

      }

      if (slice == null) {
        SliceManagerBaseInfo newSlice = new SliceManagerBaseInfo();
        newSlice.setBaseId(BASE_ID);
        slice = this.setL2vpnMulicastAddressBaseInfo(newSlice);
        sliceManagerBaseInfoDao.create(session, newSlice);

      } else {
        slice = this.setL2vpnMulicastAddressBaseInfo(slice);
        sliceManagerBaseInfoDao.update(session, slice);

      }

    } catch (MsfException error1) {
      logger.warn("DB processing failed.", error1);
      result = false;

    }
    return result;

  }

  private boolean checkL2VpnMulticastAddressBase() {
    boolean result = true;

    logger.debug("Check IPv4 Multicast Address.");

    String multicastAddressBase = this.dataConf.getSlice().getIpv4MulticastAddressBase();

    String checkAddress = multicastAddressBase;
    checkAddress = this.checkHost(checkAddress, IpAddressType.IPV4);

    if (checkAddress == null) {
      logger.error("L2VPN MulticastAddressBase NG:" + multicastAddressBase);
      result = false;

    } else {
      this.dataConf.getSlice().setIpv4MulticastAddressBase(checkAddress);
    }
    return result;

  }

  private SliceManagerBaseInfo setL2vpnMulicastAddressBaseInfo(SliceManagerBaseInfo sliceInfo) {
    sliceInfo.setL2vpnMulticastAddressBase(this.dataConf.getSlice().getIpv4MulticastAddressBase());

    return sliceInfo;

  }
}
