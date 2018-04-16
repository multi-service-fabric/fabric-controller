
package msf.fc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

import msf.fc.common.config.FcConfigManager;
import msf.fc.core.status.FcSystemStatusManager;
import msf.fc.db.FcDbManager;
import msf.fc.failure.FcFailureManager;
import msf.fc.node.FcNodeManager;
import msf.fc.slice.FcSliceManager;
import msf.fc.traffic.FcTrafficManager;
import msf.mfcfc.AbstractMain;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.rest.RestManager;

/**
 * The startup/shutdown function block. Class to start and shut down the
 * FabricController.
 *
 * @author NTT
 *
 */
public class FabricController extends AbstractMain {

  private static final String FC_OUTPUT = "fc.output";

  private static final MsfLogger logger = MsfLogger.getInstance(FabricController.class);

  /**
   * Constructor of FabricController.
   */
  public FabricController() {

  }

  /**
   * Startup procedure of main process of FabricController.
   *
   * @param args
   *          The first element of the array path to config file directory
   *          (optional)
   */
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

      logger.error("Couldn't create " + FC_OUTPUT + ".\nFabricController start failure.", error1);
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
      this.conf = FcConfigManager.getInstance();

      if (this.confPath != null) {
        logger.debug("Set Config file path.");
        this.conf.setConfigPath(this.confPath);
      }

      if (!this.conf.start()) {
        logger.error("ConfigManager start processing failed.");
        return false;
      }

      logger.info("DbManager start.");
      this.db = FcDbManager.getInstance();
      if (!this.db.start()) {
        logger.error("DbManager start processing failed.");
        return false;
      }

      if (!this.updateDbRecords()) {
        logger.error("DbManager: Change Service Status processing failed.");
        return false;
      }

      logger.info("CoreManager start.");

      FcSystemStatusManager.getInstance();
      this.core = CoreManager.getInstance();
      OperationManager.getInstance().setClusterIdForOperationId(
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      if (!this.core.start()) {
        logger.error("CoreManager start processing failed.");
        return false;
      }

      logger.info("NodeManager start.");
      this.node = FcNodeManager.getInstance();
      if (!this.node.start()) {
        logger.error("NodeManager start processing failed.");
        return false;
      }

      logger.info("SliceManager start.");
      this.slice = FcSliceManager.getInstance();
      if (!this.slice.start()) {
        logger.error("SliceManager start processing failed.");
        return false;
      }

      logger.info("FailureManager start.");
      this.failure = FcFailureManager.getInstance();
      if (!this.failure.start()) {
        logger.error("FailureManager start processing failed.");
        return false;
      }

      logger.info("TrafficManager start.");
      this.traffic = FcTrafficManager.getInstance();
      if (!this.traffic.start()) {
        logger.error("TrafficManager start processing failed.");
        return false;
      }

      this.rest = RestManager.getInstance();

      rest.setRestResourcePackage(RestManager.REST_RESOURCE_PACKAGE_FC);

      rest.setRestRequestTimeouts(FcConfigManager.getInstance().getSystemConfStatus().getRecvRestRequestUnitTime(),
          FcConfigManager.getInstance().getSystemConfStatus().getSendRestRequestUnitTime());

      rest.setErrorCodes(ErrorCode.EC_CONTROL_ERROR, ErrorCode.EC_CONNECTION_ERROR);
      logger.info("RestManager start.");
      if (!this.rest.start()) {
        logger.error("RestManager start processing failed.");
        return false;
      }

      try {
        SystemStatus changeSystemStatus = new SystemStatus();
        changeSystemStatus.setServiceStatusEnum(ServiceStatus.STARTED);

        FcSystemStatusManager.getInstance().changeSystemStatus(changeSystemStatus);

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
    return false;
  }

}
