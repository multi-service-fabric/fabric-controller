
package msf.mfc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * L3CP addition implementation class.
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateScenario extends MfcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateScenario.class);

  private L3CpCreateRequestBody requestBody;

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
  public MfcL3CpCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      MfcL3CpCreateRunner runner = new MfcL3CpCreateRunner(request, requestBody);
      execAsyncRunner(runner);
      L3CpCreateResponseBody responseBody = new L3CpCreateResponseBody();
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

      L3CpCreateRequestBody requestBody = checkParameterL3CpCreate(request);

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

}
