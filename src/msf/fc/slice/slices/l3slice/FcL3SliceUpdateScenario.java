
package msf.fc.slice.slices.l3slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateResponseBody;

/**
 * Implementation class for the L3 slice modification process.
 *
 * @author NTT
 *
 */
public class FcL3SliceUpdateScenario extends FcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceUpdateScenario.class);

  private L3SliceUpdateRequestBody requestBody;

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
  public FcL3SliceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL3SliceUpdateRunner runner = new FcL3SliceUpdateRunner(request, requestBody);
      execAsyncRunner(runner);
      L3SliceUpdateResponseBody responseBody = new L3SliceUpdateResponseBody();
      responseBody.setOperationId(getOperationId());
      return new RestResponseBase(HttpStatus.ACCEPTED_202, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      L3SliceUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L3SliceUpdateRequestBody.class);
      requestBody.validate();
      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

}
