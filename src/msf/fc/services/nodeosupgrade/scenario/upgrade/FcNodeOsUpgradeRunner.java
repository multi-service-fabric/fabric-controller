
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequestBody;

/**
 * Class to implement the asynchronous processing in the node OS upgrade.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeRunner extends FcAbstractNodeOsUpgradeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeRunner.class);

  private NodeOsUpgradeRequest request;
  private NodeOsUpgradeRequestBody requestBody;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as arguments
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcNodeOsUpgradeRunner(NodeOsUpgradeRequest request, NodeOsUpgradeRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait for node os upgrade process.");
      synchronized (FcNodeOsUpgradeManager.getInstance().getFcNodeOsUpgradeLockObject()) {
        logger.performance("end wait for node os upgrade process.");

        FcNodeOsUpgradeSchedulerTask fcNodeOsUpgradeSchedulerTask = new FcNodeOsUpgradeSchedulerTask(request,
            requestBody, getOperationId());
        FcNodeOsUpgradeManager.getInstance().getNodeOsUpgradeScheduler()
            .setNodeOsUpgradeTask(fcNodeOsUpgradeSchedulerTask, getOperationId());

        setOperationEndFlag(false);

        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
