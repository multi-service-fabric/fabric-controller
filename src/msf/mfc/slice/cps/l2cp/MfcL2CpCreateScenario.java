
package msf.mfc.slice.cps.l2cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * L2CP addition implementation class.
 *
 * @author NTT
 *
 */
public class MfcL2CpCreateScenario extends MfcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpCreateScenario.class);

  private L2CpCreateRequestBody requestBody;

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
  public MfcL2CpCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      MfcL2CpCreateRunner runner = new MfcL2CpCreateRunner(request, requestBody);
      execAsyncRunner(runner);
      L2CpCreateResponseBody responseBody = new L2CpCreateResponseBody();
      responseBody.setOperationId(getOperationId());
      return new RestResponseBase(HttpStatus.ACCEPTED_202, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      L2CpCreateRequestBody requestBody = checkParameterL2CpCreate(request);

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

}
