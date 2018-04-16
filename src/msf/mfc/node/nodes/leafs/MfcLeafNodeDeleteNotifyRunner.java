
package msf.mfc.node.nodes.leafs;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;

/**
 * Implementation class for the reception process of operation result
 * notification in the Leaf node deletion process.
 *
 * @author NTT
 *
 */
public class MfcLeafNodeDeleteNotifyRunner extends MfcAbstractLeafNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcLeafNodeDeleteNotifyRunner.class);

  public MfcLeafNodeDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
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
