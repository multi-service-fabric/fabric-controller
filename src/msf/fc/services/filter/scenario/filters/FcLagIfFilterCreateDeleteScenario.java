
package msf.fc.services.filter.scenario.filters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterCreateDeleteRequestBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterCreateDeleteResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterRequest;

/**
 * Implementation class for the LagIF filter information registration/deletion.
 *
 * @author NTT
 *
 */
public class FcLagIfFilterCreateDeleteScenario extends FcAbstractFilterScenarioBase<LagIfFilterRequest> {

  private LagIfFilterRequest request;
  private List<LagIfFilterCreateDeleteRequestBody> requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterCreateDeleteScenario.class);

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
  public FcLagIfFilterCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(LagIfFilterRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNumericId(request.getLagIfId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      List<LagIfFilterCreateDeleteRequestBody> requestBody = (List<LagIfFilterCreateDeleteRequestBody>) JsonUtil
          .fromJson(request.getRequestBody(), new TypeToken<ArrayList<LagIfFilterCreateDeleteRequestBody>>() {
          });

      ParameterCheckUtil.checkNotNullAndLength(requestBody);
      List<PatchOperation> opList = new ArrayList<>();
      for (LagIfFilterCreateDeleteRequestBody body : requestBody) {

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

      FcLagIfFilterCreateDeleteRunner createDeleteRunner = new FcLagIfFilterCreateDeleteRunner(request, requestBody);
      execAsyncRunner(createDeleteRunner);

      responseBase = responseLagIfFilterCreateDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagIfFilterCreateDeleteData() {
    try {
      logger.methodStart();
      LagIfFilterCreateDeleteResponseBody body = new LagIfFilterCreateDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
