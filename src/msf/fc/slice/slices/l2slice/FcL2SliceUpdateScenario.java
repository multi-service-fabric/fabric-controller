
package msf.fc.slice.slices.l2slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateResponseBody;

/**
 * Implementation class for the L2 slice modification process.
 *
 * @author NTT
 *
 */
public class FcL2SliceUpdateScenario extends FcAbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2SliceUpdateScenario.class);

  private L2SliceUpdateRequestBody requestBody;

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
  public FcL2SliceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      FcL2SliceUpdateRunner runner = new FcL2SliceUpdateRunner(request, requestBody);
      execAsyncRunner(runner);
      L2SliceUpdateResponseBody responseBody = new L2SliceUpdateResponseBody();
      responseBody.setOperationId(getOperationId());
      return new RestResponseBase(HttpStatus.ACCEPTED_202, responseBody);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

      L2SliceUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L2SliceUpdateRequestBody.class);
      requestBody.validate();
      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

}
