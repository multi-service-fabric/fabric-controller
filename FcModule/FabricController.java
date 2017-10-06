package msf.fc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.AsyncProcessStatus;
import msf.fc.common.constant.BlockadeStatus;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.CoreManager;
import msf.fc.core.status.SystemStatusManager;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.AsyncRequestsDao;
import msf.fc.db.dao.common.SystemStatusDao;
import msf.fc.failure.FailureManager;
import msf.fc.node.NodeManager;
import msf.fc.rest.RestManager;
import msf.fc.slice.SliceManager;
import msf.fc.traffic.TrafficManager;

public class FabricController {

  private static final int ABNOMAL_EXIT_CODE = 1;

  private static Object lockObj = new Object();

  private ConfigManager conf;

  private NodeManager node;

  private FailureManager failure;

  private CoreManager core;

  public FabricController() {
  }

  public static void main(String[] args) {
    printBuildTimestamp();

    logger.info("FabricController start.");

    FabricController fc = new FabricController();

    if (args.length == 1 && args[0].length() != 0) {
      logger.debug("FC Config Path : " + args[0]);
      fc.confPath = args[0];

    } else if (args.length == 0 || args.length == 1) {
      logger.debug("FC Config Path : (defalt)");
      fc.confPath = null;

    } else {
      logger.error("The number of arguments is set two or more. FabricController start failure.");
      System.exit(ABNOMAL_EXIT_CODE);
    }

    PrintWriter writer = null;
    boolean isInitializationFailed = false;
    try {
      logger.performance("FabricController initialization start.");

      if (fc.initializeFunctionBlocks()) {

        logger.debug(FC_OUTPUT + " create start.");
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String vmName = bean.getName();
        long pid = Long.valueOf(vmName.split("@")[0]);

        File file = new File(FC_OUTPUT);
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.println(pid);

        logger.performance("FabricController initialization end.");

      } else {
        isInitializationFailed = true;
      }

    } catch (IOException error1) {
      isInitializationFailed = true;

    } catch (Exception error1) {
      logger.error("Unexpected Exception occurred.", error1);
      isInitializationFailed = true;

    } finally {
      if (writer != null) {
        writer.close();
      }
    }

    if (isInitializationFailed) {
      try {
        logger.debug("FabricController abnormal termination start.");
        fc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("FabricController finalization failed.", error1);
      } finally {
        logger.error("FabricController terminated abnormally.");
        logger.info("FabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    fc.isSystemStartUp = true;

    try {
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            logger.info("shutdown hook start.");

            synchronized (lockObj) {
              logger.performance("FabricController finalization start.");
              fc.finalizeFunctionBlocks();
              logger.performance("FabricController finalization end.");
            }

          } catch (Exception error1) {
            logger.error("FabricController finalization failed.", error1);

          } finally {
            logger.info("FabricController end.");

          }
        }
      });

    } catch (Throwable th) {
      logger.error("Unexpected Exception occurred.", th);

      try {
        logger.debug("FabricController abnormal termination start.");
        fc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("FabricController finalization failed.", error1);
      } finally {
        logger.error("FabricController terminated abnormally.");
        logger.info("FabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    try {

      logger.debug("FabricController loop processing start.");

      while (true) {

        synchronized (lockObj) {
          logger.performance("Delete AsyncOperationIds start.");
          fc.deleteAsyncOperationIds();
          logger.performance("Delete AsyncOperationIds end.");
        }

        logger.debug("sleep 1 day.");
        try {

          long sleepTime = TimeUnit.DAYS.toMillis(1);
          TimeUnit.MILLISECONDS.sleep(sleepTime);

        } catch (InterruptedException ex) {
          logger.warn("Sleep interrupted in main loop.");
        }
      }

    } catch (Throwable th) {
      logger.error("Unexpected Exception occurred.", th);

      System.exit(ABNOMAL_EXIT_CODE);

    }

  }

  private boolean initializeFunctionBlocks() {

    try {
      logger.methodStart();

      logger.info("ConfigManager start.");
      this.conf = ConfigManager.getInstance();

      if (this.confPath != null) {
        logger.debug("Set Config file path.");
        this.conf.setConfigPath(this.confPath);
      }

      if (!this.conf.start()) {
        logger.error("ConfigManager start processing failed.");
        return false;
      }

      logger.info("DbManager start.");
      this.db = DbManager.getInstance();
      if (!this.db.start()) {
        logger.error("DbManager start processing failed.");
        return false;
      }

      if (!this.updateDbRecords()) {
        logger.error("DbManager: Change Service Status processing failed.");
        return false;
      }

      if (!this.conf.insertDbRecords()) {
        logger.error("ConfigManager: DB Insert Config parameter processing failed.");
        return false;
      }

      logger.info("CoreManager start.");
      this.core = CoreManager.getInstance();
      if (!this.core.start()) {
        logger.error("CoreManager start processing failed.");
        return false;
      }

      logger.info("NodeManager start.");
      this.node = NodeManager.getInstance();
      if (!this.node.start()) {
        logger.error("NodeManager start processing failed.");
        return false;
      }

      logger.info("SliceManager start.");
      this.slice = SliceManager.getInstance();
      if (!this.slice.start()) {
        logger.error("SliceManager start processing failed.");
        return false;
      }

      logger.info("FailureManager start.");
      this.failure = FailureManager.getInstance();
      if (!this.failure.start()) {
        logger.error("FailureManager start processing failed.");
        return false;
      }

      this.traffic = TrafficManager.getInstance();
      logger.info("TrafficManager start.");
      if (!this.traffic.start()) {
        logger.error("TrafficManager start processing failed.");
        return false;
      }

      this.rest = RestManager.getInstance();
      logger.info("RestManager start.");
      if (!this.rest.start()) {
        logger.error("RestManager start processing failed.");
        return false;
      }

      try {
        SystemStatus changeSystemStatus = new SystemStatus();
        changeSystemStatus.setServiceStatusEnum(ServiceStatus.STARTED);

        SystemStatusManager.getInstance().changeSystemStatus(changeSystemStatus);

      } catch (MsfException ex) {
        return false;
      }

      logger.info("All Managers started successfully.");
      return true;

    } finally {
      logger.methodEnd();
    }
  }

  private void finalizeFunctionBlocks() {

    try {

      logger.methodStart();

      boolean isSwitchingMode = false;


      if (this.node != null) {

        SystemStatusManager sysStatMng = SystemStatusManager.getInstance();

        SystemStatus sysStatus = sysStatMng.getSystemStatus();

        logger.debug("SystemStatus = {0}.", sysStatus);

        if (sysStatus != null) {
          if (isSystemStartUp && sysStatus.getServiceStatusEnum() == ServiceStatus.SWITCHING) {
            logger.info("FablicController shut down in SWITCHING-MODE.");
            isSwitchingMode = true;

          } else {
            logger.info("ServiceStatus changes to FINALIZING from {0}.", sysStatus.getServiceStatusEnum());
            sysStatus.setServiceStatusEnum(ServiceStatus.FINALIZING);
            try {
              sysStatMng.changeSystemStatus(sysStatus);
            } catch (MsfException ex) {
            }
          }
        }
      }

      if (this.rest != null) {
        logger.info("RestManager stop.");
        if (!this.rest.stop()) {
          logger.warn("RestManager stop processing failed.");
        }
      }

      if (this.traffic != null) {
        logger.info("TrafficManager stop.");
        if (!this.traffic.stop()) {
          logger.warn("TrafficManager stop processing failed.");
        }
      }

      if (this.failure != null) {
        logger.info("FailureManager stop.");
        if (!this.failure.stop()) {
          logger.warn("FailureManager stop processing failed.");
        }
      }

      if (this.slice != null) {
        logger.info("SliceManager stop.");
        if (!this.slice.stop()) {
          logger.warn("SliceManager stop processing failed.");
        }
      }
      if (this.node != null) {
        logger.info("NodeManager stop.");
        if (!this.node.stop()) {
          logger.warn("NodeManager stop processing failed.");
        }
      }

      if (this.core != null) {
        logger.info("CoreManager stop.");
        if (!this.core.stop()) {
          logger.warn("CoreManager stop processing failed.");
        }
      }

      if (this.db != null) {

        if (!isSwitchingMode && this.node != null) {
          logger.info("DbManager : Change ServiceStatus to STOPPED.");
          if (!this.changeServiceStatusDirectDb(ServiceStatus.STOPPED)) {
            logger.warn("DbManager : Change ServiceStatus failed.");
          }
        }

        logger.info("DbManager stop.");
        if (!this.db.stop()) {
          logger.warn("DbManager stop processing failed.");
        }
      }

      if (this.conf != null) {
        logger.info("ConfigManager stop.");
        if (!this.conf.stop()) {
          logger.warn("ConfigManager stop processing failed.");
        }
      }

      logger.info("All Managers stopped successfully.");

    } finally {
      logger.methodEnd();

    }
  }

  private void deleteAsyncOperationIds() {
    SessionWrapper session = new SessionWrapper();

    try {
      session.openSession();
      session.beginTransaction();
      this.deleteAsyncOperationIds(session);
      session.commit();

    } catch (MsfException error1) {
      try {
        session.rollback();
      } catch (MsfException error2) {
        logger.warn("DB roll back processing failed.", error2);
      }
      logger.warn("Delete AsyncOperationIds failed.");

    } finally {

      session.closeSession();

    }
  }

  private void deleteAsyncOperationIds(SessionWrapper session) throws MsfException {

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1 * conf.getAsyncOperationDataRetentionPeriod());
    Timestamp targetTime = new Timestamp(calendar.getTimeInMillis());

    AsyncRequestsDao asyncRequestsDao = new AsyncRequestsDao();
    asyncRequestsDao.delete(session, targetTime);

    logger.info("Delete AsyncOperationIds succeed.");

  }

  private boolean updateDbRecords() {
    logger.methodStart();

    boolean result = false;

    SessionWrapper session = new SessionWrapper();
    try {

      session.openSession();
      session.beginTransaction();

      SystemStatusDao systemStatusDao = new SystemStatusDao();
      SystemStatus sysStatus = systemStatusDao.read(session, SystemStatusManager.FIXED_SYSTEM_ID);

      logger.debug("Initialize Service Status : " + sysStatus);

      if (sysStatus == null) {
        SystemStatus newSysStatus = new SystemStatus();
        newSysStatus.setSystemId(SystemStatusManager.FIXED_SYSTEM_ID);
        newSysStatus.setServiceStatusEnum(ServiceStatus.INITIALIZING);
        newSysStatus.setBlockadeStatusEnum(BlockadeStatus.NONE);

        systemStatusDao.create(session, newSysStatus);
        logger.info("Create new SystemStatus : " + newSysStatus);

      } else if (sysStatus.getServiceStatusEnum() == ServiceStatus.SWITCHING) {
        logger.debug("Nothing to do. (Because SystemServiceStatus is SWITCHING)");

      } else {
        logger.info("ServiceStatus changes to INITIALIZING from {0}.", sysStatus.getServiceStatusEnum());
        sysStatus.setServiceStatusEnum(ServiceStatus.INITIALIZING);

        systemStatusDao.update(session, sysStatus);
      }

      logger.performance("Delete AsyncOperationIds start.");
      this.deleteAsyncOperationIds(session);
      logger.performance("Delete AsyncOperationIds end.");

      AsyncRequestsDao asyncRequestsDao = new AsyncRequestsDao();
      asyncRequestsDao.updateList(session, AsyncProcessStatus.WAITING.getCode(), AsyncProcessStatus.CANCELED.getCode(),
          null);
      asyncRequestsDao.updateList(session, AsyncProcessStatus.RUNNING.getCode(), AsyncProcessStatus.FAILED.getCode(),
          null);

      session.commit();
      result = true;

    } catch (MsfException error1) {
      logger.warn("DB processing failed.", error1);

      try {
        session.rollback();
      } catch (MsfException error2) {
        logger.warn("DB roll back processing failed.", error2);
      }

    } finally {
      session.closeSession();
    }

    logger.methodEnd("result = " + result);
    return result;
  }

  private boolean changeServiceStatusDirectDb(ServiceStatus updateServiceStatus) {

    logger.methodStart(new String[] { "ServiceStatus" }, new Object[] { updateServiceStatus });

    boolean result = true;

    SessionWrapper session = new SessionWrapper();
    try {
      session.openSession();
      session.beginTransaction();

      SystemStatusDao systemStatusDao = new SystemStatusDao();
      SystemStatus sysStatus = systemStatusDao.read(session, SystemStatusManager.FIXED_SYSTEM_ID);

      sysStatus.setServiceStatusEnum(updateServiceStatus);
      systemStatusDao.update(session, sysStatus);

      session.commit();

      logger.debug("Change Service Status succeed.");

    } catch (MsfException error1) {
      logger.warn("DB processing failed.", error1);

      try {
        result = false;

        session.rollback();

      } catch (MsfException error2) {
        logger.warn("DB roll back processing failed.", error2);
      }

      logger.warn("Change Service Status failed.");

    } finally {
      session.closeSession();
    }

    logger.methodEnd("result = " + result);

    return result;
  }

  private static void printBuildTimestamp() {
    try {
      String jcp = System.getProperty("java.class.path");
      String jfp = new File(jcp).getAbsolutePath();

      try (JarFile jarFile = new JarFile(jfp)) {
        Manifest manifest = jarFile.getManifest();
        Attributes attributes = manifest.getMainAttributes();

        String timestamp = attributes.getValue("Timestamp");
        logger.info("Jar file build timestamp = {0}.", timestamp);
      } catch (IOException ioe) {
        logger.warn("{0} open failed.", ioe, jfp);
      }
    } catch (Exception exception) {
      logger.warn("Print build timestamp failed.", exception);
    }
  }

}
