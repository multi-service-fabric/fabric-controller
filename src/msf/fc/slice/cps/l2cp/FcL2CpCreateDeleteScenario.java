
package msf.fc.slice.cps.l2cp;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Implementation class for L2CP addition/deletion(/modification).
 *
 * @author NTT
 *
 */
public class FcL2CpCreateDeleteScenario extends FcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpCreateDeleteScenario.class);

  private List<L2CpCreateDeleteRequestBody> requestBody;

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
  public FcL2CpCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL2CpCreateDeleteRunner runner = new FcL2CpCreateDeleteRunner(request, requestBody);
      execAsyncRunner(runner);
      L2CpCreateDeleteResponseBody responseBody = new L2CpCreateDeleteResponseBody();
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

      List<L2CpCreateDeleteRequestBody> requestBody = checkParameterL2CpCreateDelete(request);

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }
}
