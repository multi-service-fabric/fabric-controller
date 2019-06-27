
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;

/**
 * Implementation class for the processing after the detour finished in the
 * upgrade operation of the node OS.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeDetourThread extends Thread {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeDetourThread.class);

  /**
   * Execution of processing after the node detour finished in the node OS
   * upgrade function.
   */
  @Override
  public void run() {
    try {
      logger.methodStart();
      FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler = FcNodeOsUpgradeManager.getInstance()
          .getNodeOsUpgradeScheduler();

      try {

        FcAsyncRequest currentAsyncRequest = nodeOsUpgradeScheduler.getCurrentAsyncRequest();

        Object lockObject = OperationManager.getInstance().getOperationIdObject(currentAsyncRequest.getOperationId());
        if (lockObject != null) {
          synchronized (lockObject) {

            boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
                .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());

            if (!hasChangeNodeOperationStatus) {

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Another node related operation is currently in progress.");
            }

            nodeOsUpgradeScheduler.processingAfterRecoverNode();

          }
        }

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        try {
          nodeOsUpgradeScheduler.runningOperationFailureProcess(false, msfException.getErrorCode(),
              msfException.getMessage());
        } catch (MsfException msfException3) {

        }
      }

    } finally {
      logger.methodEnd();
    }

  }

}
