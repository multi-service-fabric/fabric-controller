
package msf.fc.services.nodeosupgrade.scenario.detour;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.fc.services.nodeosupgrade.scenario.upgrade.FcNodeOsUpgradeDetourThread;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;

/**
 * Class to implement the asynchronous processing of the node detour.
 *
 * @author NTT
 *
 */
public class FcNodeDetourUpdateRunner extends FcAbstractNodeDetourRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeDetourUpdateRunner.class);

  private NodeDetourRequest request;
  private NodeDetourUpdateRequestBody requestBody;

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
  public FcNodeDetourUpdateRunner(NodeDetourRequest request, NodeDetourUpdateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      FcNodeDetourUpdateProcess fcNodeDetourUpdateProcess = new FcNodeDetourUpdateProcess(request.getClusterId(),
          request.getFabricType(), request.getNodeId(), requestBody);
      if (!fcNodeDetourUpdateProcess.nodeDetourUpdateProcess()) {

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
            MessageFormat.format("The target resource is already detour node. fabricType = {0}, nodeId = {1}.",
                request.getFabricType(), request.getNodeId()));
      }

      FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

      if (!requestBody.getUpdateOption().getDetoured()) {

        boolean operatorCheckWaiting = FcNodeOsUpgradeManager.getInstance().getNodeOsUpgradeScheduler()
            .isOperatorCheckWaiting();
        if (operatorCheckWaiting) {

          FcNodeOsUpgradeDetourThread thread = new FcNodeOsUpgradeDetourThread();
          thread.start();
        }
      }

      RestResponseBase responseBase = responseNodeDetourUpdateData();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseNodeDetourUpdateData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
