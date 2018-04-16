
package msf.fc.node.interfaces.breakoutifs;

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
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfCreateDeleteRequestBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfCreateDeleteResposeBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for breakout interface registration/deletion.
 *
 * @author NTT
 *
 */
public class FcBreakoutInterfaceCreateDeleteScenario
    extends FcAbstractBreakoutInterfaceScenarioBase<BreakoutIfRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutInterfaceCreateDeleteScenario.class);

  private BreakoutIfRequest request;
  private List<BreakoutIfCreateDeleteRequestBody> requestBody;

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
  public FcBreakoutInterfaceCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(BreakoutIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))
          && !NodeType.SPINE.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

      List<BreakoutIfCreateDeleteRequestBody> requestBody = (List<BreakoutIfCreateDeleteRequestBody>) JsonUtil
          .fromJson(request.getRequestBody(), new TypeToken<ArrayList<BreakoutIfCreateDeleteRequestBody>>() {
          });

      ParameterCheckUtil.checkNotNullAndLength(requestBody);
      List<PatchOperation> opList = new ArrayList<>();
      for (BreakoutIfCreateDeleteRequestBody body : requestBody) {

        body.validate();

        switch (body.getOpEnum()) {
          case ADD:

            if (request.getFabricTypeEnum().equals(NodeType.SPINE)) {
              String logMsg = "if operation is add but fabric_type is spines.";
              logger.error(logMsg);
              throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
            }
            break;
          default:

            break;
        }
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

      FcBreakoutInterfaceCreateDeleteRunner createDeleteRunner = new FcBreakoutInterfaceCreateDeleteRunner(request,
          requestBody);
      execAsyncRunner(createDeleteRunner);

      responseBase = responseBreakoutInterfaceCreateDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseBreakoutInterfaceCreateDeleteData() {
    try {
      logger.methodStart();
      BreakoutIfCreateDeleteResposeBody body = new BreakoutIfCreateDeleteResposeBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
