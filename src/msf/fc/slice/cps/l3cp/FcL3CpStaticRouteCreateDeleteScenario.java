
package msf.fc.slice.cps.l3cp;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteResponseBody;

/**
 * Implementation class for L3CP static route addition/deletion.
 *
 * @author NTT
 *
 */
public class FcL3CpStaticRouteCreateDeleteScenario extends FcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpStaticRouteCreateDeleteScenario.class);

  private List<L3CpStaticRouteCreateDeleteRequestBody> requestBody;

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
  public FcL3CpStaticRouteCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL3CpStaticRouteCreateDeleteRunner runner = new FcL3CpStaticRouteCreateDeleteRunner(request, requestBody);
      execAsyncRunner(runner);
      L3CpStaticRouteCreateDeleteResponseBody responseBody = new L3CpStaticRouteCreateDeleteResponseBody();
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

      List<L3CpStaticRouteCreateDeleteRequestBody> requestBody = checkParameterL3CpStaticRouteCreateDelete(request);

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

}
