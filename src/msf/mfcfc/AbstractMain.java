
package msf.mfcfc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.RenewalStatusType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.reservation.ReservationBlockBase;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
import msf.mfcfc.db.dao.common.SystemStatusDao;
import msf.mfcfc.failure.FailureManager;
import msf.mfcfc.node.NodeManager;
import msf.mfcfc.rest.RestManager;
import msf.mfcfc.slice.SliceManager;
import msf.mfcfc.traffic.TrafficManager;

/**
 * Common class of startup/shutdown function block.
 *
 * @author NTT
 *
 */
public abstract class AbstractMain {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractMain.class);

  protected static final int ABNOMAL_EXIT_CODE = 1;

  protected static Object lockObj = new Object();

  protected String confPath;

  protected ConfigManager conf;

  protected DbManager db;

  protected NodeManager node;

  protected SliceManager slice;

  protected CoreManager core;

  protected RestManager rest;

  protected FailureManager failure;

  protected TrafficManager traffic;

  protected boolean isSystemStartUp = false;

  protected String functionalConfigPath = null;

  protected AbstractMain() {
  }

  protected void finalizeFunctionBlocks() {

    try {

      logger.methodStart();

      stopReservationScheduler();

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

              logger.warn("ServiceStatus couldn't change.", ex);
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

      stopExtensionFunctions();

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

  protected void deleteAsyncOperationIds() {
    SessionWrapper session = new SessionWrapper();
    try {
      logger.methodStart();
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
      logger.methodEnd();
    }
  }

  private void deleteAsyncOperationIds(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart();

      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE, -1 * ConfigManager.getInstance().getAsyncOperationDataRetentionPeriod());
      Timestamp targetTime = new Timestamp(calendar.getTimeInMillis());

      AsyncRequestsDao asyncRequestsDao = DbManager.getInstance().createAsyncRequestsDao();
      asyncRequestsDao.delete(session, targetTime);

      logger.info("Delete AsyncOperationIds succeed.");
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean updateDbRecords() {
    try {
      logger.methodStart();

      boolean result = false;

      SessionWrapper session = new SessionWrapper();
      try {

        session.openSession();
        session.beginTransaction();

        SystemStatusDao systemStatusDao = DbManager.getInstance().createSystemStatusDao();
        SystemStatus sysStatus = systemStatusDao.read(session, SystemStatusManager.FIXED_SYSTEM_ID);

        logger.debug("Initialize Service Status : " + sysStatus);

        if (sysStatus == null) {

          SystemStatus newSysStatus = new SystemStatus();
          newSysStatus.setSystemId(SystemStatusManager.FIXED_SYSTEM_ID);
          newSysStatus.setServiceStatusEnum(ServiceStatus.INITIALIZING);
          newSysStatus.setBlockadeStatusEnum(BlockadeStatus.NONE);
          newSysStatus.setRenewalStatusEnum(RenewalStatusType.NONE);

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

        AsyncRequestsDao asyncRequestsDao = DbManager.getInstance().createAsyncRequestsDao();
        asyncRequestsDao.updateList(session, AsyncProcessStatus.WAITING.getCode(),
            AsyncProcessStatus.CANCELED.getCode(), null);
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

      return result;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean changeServiceStatusDirectDb(ServiceStatus updateServiceStatus) {
    try {
      logger.methodStart(new String[] { "ServiceStatus" }, new Object[] { updateServiceStatus });

      boolean result = true;

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();
        session.beginTransaction();

        SystemStatusDao systemStatusDao = DbManager.getInstance().createSystemStatusDao();
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

        logger.warn("Couldn't change Service Status.", error1);
        logger.warn("Change Service Status failed.");

      } finally {
        session.closeSession();
      }

      return result;
    } finally {
      logger.methodEnd();
    }
  }

  protected static void printBuildTimestamp() {
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

  protected abstract boolean isStartAsMultiCluster();

  protected void setFunctionalConfigPath(String path) {
    functionalConfigPath = ConfigManager.getInstance().getConfigPath() + path;
  }

  protected List<String> loadFunctionalConfig() {
    try {
      logger.methodStart();

      List<String> resultList = new ArrayList<>();
      File file = new File(functionalConfigPath);

      if (!file.exists()) {

        logger.error(MessageFormat.format("FunctionalConfig file Not Found ({0}).", functionalConfigPath));
        return null;
      }
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      try {
        String lineData;
        while ((lineData = bufferedReader.readLine()) != null) {
          if (lineData.length() != 0) {
            resultList.add(lineData);
          }
        }
      } finally {

        bufferedReader.close();
      }
      return resultList;
    } catch (IOException error1) {
      logger.error("FunctionalConfig file could not read.", error1);
      return null;
    }
  }

  protected FunctionBlockBase makeExtensionFunctionInstance(String className) {
    try {
      logger.methodStart();

      Class<?> classObj = Class.forName(className);

      Constructor<?> myConst = classObj.getDeclaredConstructor();

      myConst.setAccessible(true);

      Object myObj = myConst.newInstance();

      Method myMethod = classObj.getMethod("getInstance");

      return (FunctionBlockBase) myMethod.invoke(myObj);
    } catch (Exception ex1) {
      logger.error(MessageFormat.format("Cannot make instance. className({0}).", className), ex1);
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean initExtensionFunctions() {
    logger.methodStart();

    if (this.core == null) {
      logger.error("CoreManager not found.");
      return false;
    }

    if (functionalConfigPath == null) {
      logger.error("FunctionalConfigPath not set.");
      return false;
    }

    List<String> lineList = loadFunctionalConfig();
    if (lineList == null) {

      return false;
    }

    for (int num = 0; num < lineList.size(); num++) {
      String extensionFunctionStr = lineList.get(num);
      if (extensionFunctionStr != null) {
        FunctionBlockBase extensionFunction = makeExtensionFunctionInstance(extensionFunctionStr);
        if (extensionFunction != null) {

          this.core.addExtensionFunction(extensionFunction);
        } else {
          logger.error(MessageFormat.format("Illegal parameter. extensionFunctionStr({0}).", extensionFunctionStr));
          return false;
        }
      }
    }
    return true;
  }

  protected boolean startExtensionFunctions() {
    logger.methodStart();

    if (this.core == null) {
      logger.error("CoreManager not found.");
      return false;
    }

    List<FunctionBlockBase> extensionsList = this.core.getExtensionFunctions();
    for (int num = 0; num < extensionsList.size(); num++) {
      FunctionBlockBase extensionFunction = extensionsList.get(num);

      if (!extensionFunction.start()) {

        logger.error(MessageFormat.format("ExtensionFunction start processing failed. className({0}).",
            extensionFunction.getClass().getName()));
        return false;
      }
    }
    return true;
  }

  protected void stopExtensionFunctions() {
    logger.methodStart();

    if (this.core == null) {
      logger.error("CoreManager not found.");
      return;
    }
    List<FunctionBlockBase> extensionsList = this.core.getExtensionFunctions();
    for (int num = extensionsList.size() - 1; num >= 0; num--) {
      FunctionBlockBase extensionFunction = extensionsList.get(num);

      if (!extensionFunction.stop()) {

        logger.error(MessageFormat.format("ExtensionFunction stop processing failed. className({0}).",
            extensionFunction.getClass().getName()));
      }
    }
  }

  protected boolean startReservationScheduler() {
    try {
      logger.methodStart();

      if (this.core == null) {
        logger.error("CoreManager not found.");
        return false;
      }

      List<ReservationBlockBase> reservationsList = this.core.getReservationFunctions();
      for (int num = 0; num < reservationsList.size(); num++) {
        ReservationBlockBase reservationFunction = reservationsList.get(num);

        if (!reservationFunction.startScheduler()) {

          logger.error(MessageFormat.format("ReservationFunction start scheduler processing failed. className({0}).",
              reservationFunction.getClass().getName()));
          return false;
        }
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  protected void stopReservationScheduler() {
    try {
      logger.methodStart();

      if (this.core == null) {
        logger.error("CoreManager not found.");
        return;
      }

      List<ReservationBlockBase> reservationsList = this.core.getReservationFunctions();
      for (int num = reservationsList.size() - 1; num >= 0; num--) {
        ReservationBlockBase reservationFunction = reservationsList.get(num);

        if (!reservationFunction.stopScheduler()) {

          logger.error(MessageFormat.format("ReservationFunction stop scheduler processing failed. className({0}).",
              reservationFunction.getClass().getName()));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

}
