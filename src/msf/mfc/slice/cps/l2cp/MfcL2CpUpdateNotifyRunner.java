
package msf.mfc.slice.cps.l2cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;

/**
 * Implementation class for the reception process of operation result
 * notification in L2CP modification.
 *
 */
public class MfcL2CpUpdateNotifyRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpUpdateNotifyRunner.class);

  /**
   * Constructor.
   *
   * @param asyncRequestForNotify
   *          Table instance of asynchronous request information
   */
  public MfcL2CpUpdateNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    return new RestResponseBase(HttpStatus.OK_200, (String) null);
  }

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

}
