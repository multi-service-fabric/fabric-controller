
package msf.mfc.node.nodes.spines;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;

/**
 * Implementation class for the reception process of operation result
 * notification in the Spine node addition process.
 *
 * @author NTT
 *
 */
public class MfcSpineNodeCreateNotifyRunner extends MfcAbstractSpineNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcSpineNodeCreateNotifyRunner.class);

  public MfcSpineNodeCreateNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = createResponseDataFromAsyncRequestsForLower(
          asyncRequestForNotify.getAsyncRequestsForLowerList().get(0));

      return responseBase;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

}