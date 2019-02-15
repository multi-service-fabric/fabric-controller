
package msf.fc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpUpdateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpUpdateResponseBody;

/**
 * Implementation class for the L3CP modification process.
 *
 * @author NTT
 *
 */
public class FcL3CpUpdateScenario extends FcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpUpdateScenario.class);

  private L3CpUpdateRequestBody requestBody;

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
  public FcL3CpUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL3CpUpdateRunner runner = new FcL3CpUpdateRunner(request, requestBody);
      execAsyncRunner(runner);
      L3CpUpdateResponseBody responseBody = new L3CpUpdateResponseBody();
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

      L3CpUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L3CpUpdateRequestBody.class);
      requestBody.validate();

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

}
