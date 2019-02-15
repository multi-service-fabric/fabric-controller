
package msf.fc.services.filter.scenario.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterCreateDeleteRequestBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterCreateDeleteResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterRequest;

/**
 * Implementation class for the L2CP filter information registration/deletion.
 *
 * @author NTT
 *
 */
public class FcL2CpFilterCreateDeleteScenario extends FcAbstractFilterScenarioBase<L2CpFilterRequest> {

  private L2CpFilterRequest request;
  private List<L2CpFilterCreateDeleteRequestBody> requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpFilterCreateDeleteScenario.class);

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
  public FcL2CpFilterCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(L2CpFilterRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(request.getSliceId());
      ParameterCheckUtil.checkNotNull(request.getCpId());
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      List<L2CpFilterCreateDeleteRequestBody> requestBody = (List<L2CpFilterCreateDeleteRequestBody>) JsonUtil
          .fromJson(request.getRequestBody(), new TypeToken<ArrayList<L2CpFilterCreateDeleteRequestBody>>() {
          });

      ParameterCheckUtil.checkNotNullAndLength(requestBody);
      List<PatchOperation> opList = new ArrayList<>();
      for (L2CpFilterCreateDeleteRequestBody body : requestBody) {

        body.validate();

        opList.add(body.getOpEnum());
      }
      ParameterCheckUtil.checkPatchOperationMix(opList);
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;

      FcL2CpFilterCreateDeleteRunner createDeleteRunner = new FcL2CpFilterCreateDeleteRunner(request, requestBody);
      execAsyncRunner(createDeleteRunner);

      responseBase = responseL2CpFilterCreateDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseL2CpFilterCreateDeleteData() {
    try {
      logger.methodStart();
      L2CpFilterCreateDeleteResponseBody body = new L2CpFilterCreateDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
