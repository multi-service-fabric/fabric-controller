
package msf.mfc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.core.status.MfcSystemStatusManager;
import msf.mfc.db.MfcDbManager;
import msf.mfc.failure.MfcFailureManager;
import msf.mfc.node.MfcNodeManager;
import msf.mfc.slice.MfcSliceManager;
import msf.mfc.traffic.MfcTrafficManager;
import msf.mfcfc.AbstractMain;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.rest.RestManager;

/**
 * The startup/shutdown function block. Class to start and shut down the
 * MultiFabricController.
 *
 * @author NTT
 *
 */
public class MultiFabricController extends AbstractMain {

  private static final String MFC_OUTPUT = "mfc.output";

  private static final MsfLogger logger = MsfLogger.getInstance(MultiFabricController.class);

  /**
   * Constructor of MultiFabricController.
   */
  public MultiFabricController() {

  }

  /**
   * Startup procedure of main process of MultiFabricController.
   *
   * @param args
   *          The first element of the array path to config file directory
   *          (optional)
   */
  public static void main(String[] args) {
    printBuildTimestamp();

    logger.info("MultiFabricController start.");

    MultiFabricController mfc = new MultiFabricController();

    if (args.length == 1 && args[0].length() != 0) {

      logger.debug("MFC Config Path : " + args[0]);
      mfc.confPath = args[0];

    } else if (args.length == 0 || args.length == 1) {

      logger.debug("MFC Config Path : (defalt)");
      mfc.confPath = null;

    } else {

      logger.error("The number of arguments is set two or more. MultiFabricController start failure.");
      System.exit(ABNOMAL_EXIT_CODE);
    }

    PrintWriter writer = null;
    boolean isInitializationFailed = false;
    try {
      logger.performance("MultiFabricController initialization start.");

      if (mfc.initializeFunctionBlocks()) {

        logger.debug(MFC_OUTPUT + " create start.");
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String vmName = bean.getName();
        long pid = Long.valueOf(vmName.split("@")[0]);

        File file = new File(MFC_OUTPUT);
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.println(pid);

        logger.performance("MultiFabricController initialization end.");

      } else {

        isInitializationFailed = true;
      }

    } catch (IOException error1) {

      logger.error("Couldn't create " + MFC_OUTPUT + ".\nMultiFabricController start failure.", error1);
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
        logger.debug("MultiFabricController abnormal termination start.");

        mfc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("MultiFabricController finalization failed.", error1);
      } finally {
        logger.error("MultiFabricController terminated abnormally.");
        logger.info("MultiFabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    mfc.isSystemStartUp = true;

    try {

      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            logger.info("shutdown hook start.");

            synchronized (lockObj) {
              logger.performance("MultiFabricController finalization start.");

              mfc.finalizeFunctionBlocks();
              logger.performance("MultiFabricController finalization end.");
            }

          } catch (Exception error1) {
            logger.error("MultiFabricController finalization failed.", error1);

          } finally {
            logger.info("MultiFabricController end.");

          }
        }
      });

    } catch (Throwable th) {

      logger.error("Unexpected Exception occurred.", th);

      try {
        logger.debug("MultiFabricController abnormal termination start.");

        mfc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("MultiFabricController finalization failed.", error1);
      } finally {
        logger.error("MultiFabricController terminated abnormally.");
        logger.info("MultiFabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    try {

      logger.debug("MultiFabricController loop processing start.");

      while (true) {

        synchronized (lockObj) {
          logger.performance("Delete AsyncOperationIds start.");
          mfc.deleteAsyncOperationIds();
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
      this.conf = MfcConfigManager.getInstance();

      if (this.confPath != null) {
        logger.debug("Set Config file path.");
        this.conf.setConfigPath(this.confPath);
      }

      if (!this.conf.start()) {
        logger.error("ConfigManager start processing failed.");
        return false;
      }

      logger.info("DbManager start.");
      this.db = MfcDbManager.getInstance();
      if (!this.db.start()) {
        logger.error("DbManager start processing failed.");
        return false;
      }

      if (!this.updateDbRecords()) {
        logger.error("DbManager: Change Service Status processing failed.");
        return false;
      }

      logger.info("CoreManager start.");

      MfcSystemStatusManager.getInstance();
      this.core = CoreManager.getInstance();
      if (!this.core.start()) {
        logger.error("CoreManager start processing failed.");
        return false;
      }

      logger.info("NodeManager start.");
      this.node = MfcNodeManager.getInstance();
      if (!this.node.start()) {
        logger.error("NodeManager start processing failed.");
        return false;
      }

      logger.info("SliceManager start.");
      this.slice = MfcSliceManager.getInstance();
      if (!this.slice.start()) {
        logger.error("SliceManager start processing failed.");
        return false;
      }

      logger.info("FailureManager start.");
      this.failure = MfcFailureManager.getInstance();
      if (!this.failure.start()) {
        logger.error("FailureManager start processing failed.");
        return false;
      }

      logger.info("TrafficManager start.");
      this.traffic = MfcTrafficManager.getInstance();
      if (!this.traffic.start()) {
        logger.error("TrafficManager start processing failed.");
        return false;
      }

      this.rest = RestManager.getInstance();

      rest.setRestResourcePackage(RestManager.REST_RESOURCE_PACKAGE_MFC);

      rest.setRestRequestTimeouts(MfcConfigManager.getInstance().getSystemConfStatus().getRecvRestRequestUnitTime(),
          MfcConfigManager.getInstance().getSystemConfStatus().getSendRestRequestUnitTime());

      rest.setErrorCodes(ErrorCode.FC_CONTROL_ERROR, ErrorCode.FC_CONNECTION_ERROR);
      logger.info("RestManager start.");
      if (!this.rest.start()) {
        logger.error("RestManager start processing failed.");
        return false;
      }

      try {
        SystemStatus changeSystemStatus = new SystemStatus();
        changeSystemStatus.setServiceStatusEnum(ServiceStatus.STARTED);

        MfcSystemStatusManager.getInstance().changeSystemStatus(changeSystemStatus);

      } catch (MsfException ex) {
        logger.error("Couldn't change Service Status.", ex);
        return false;
      }

      logger.info("All Managers started successfully.");
      return true;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean isStartAsMultiCluster() {
    return true;
  }
}
