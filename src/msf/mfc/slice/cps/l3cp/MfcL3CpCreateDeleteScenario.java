
package msf.mfc.slice.cps.l3cp;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Implementation class for the L3CP addition/deletion process.
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateDeleteScenario extends MfcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateDeleteScenario.class);

  private List<L3CpCreateDeleteRequestBody> requestBody;

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
  public MfcL3CpCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      MfcL3CpCreateDeleteRunner runner = new MfcL3CpCreateDeleteRunner(request, requestBody);
      execAsyncRunner(runner);
      L3CpCreateDeleteResponseBody responseBody = new L3CpCreateDeleteResponseBody();
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

      List<L3CpCreateDeleteRequestBody> requestBody = checkParameterL3CpCreateDelete(request);

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

}
