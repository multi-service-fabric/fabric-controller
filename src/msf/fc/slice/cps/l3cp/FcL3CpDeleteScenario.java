
package msf.fc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l3cp.data.L3CpDeleteResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * L3CP deletion implementation class.
 *
 * @author NTT
 *
 */
public class FcL3CpDeleteScenario extends FcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpDeleteScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcL3CpDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL3CpDeleteRunner runner = new FcL3CpDeleteRunner(request);
      execAsyncRunner(runner);
      L3CpDeleteResponseBody responseBody = new L3CpDeleteResponseBody();
      responseBody.setOperationId(getOperationId());
      return new RestResponseBase(HttpStatus.ACCEPTED_202, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }
}
