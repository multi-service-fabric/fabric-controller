
package msf.fc.services.priorityroutes.scenario.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityCreateDeleteRequestBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityCreateDeleteResponseBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityRequest;

/**
 * Implementation class of the nodes addition/deletion in the priority node
 * group.
 *
 * @author NTT
 *
 */
public class FcNodeGroupPriorityCreateDeleteScenario
    extends FcAbstractNodeGroupPriorityScenarioBase<NodeGroupPriorityRequest> {

  private NodeGroupPriorityRequest request;
  private List<NodeGroupPriorityCreateDeleteRequestBody> requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeGroupPriorityCreateDeleteScenario.class);

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
  public FcNodeGroupPriorityCreateDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(NodeGroupPriorityRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      List<NodeGroupPriorityCreateDeleteRequestBody> requestBody = (List<NodeGroupPriorityCreateDeleteRequestBody>) JsonUtil
          .fromJson(request.getRequestBody(), new TypeToken<ArrayList<NodeGroupPriorityCreateDeleteRequestBody>>() {
          });

      ParameterCheckUtil.checkNotNullAndLength(requestBody);
      List<PatchOperation> opList = new ArrayList<>();
      for (NodeGroupPriorityCreateDeleteRequestBody body : requestBody) {

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

      boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
          .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());
      if (!hasChangeNodeOperationStatus) {

        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
            "Another node related operation is currently in progress.");
      }

      RestResponseBase responseBase = null;

      FcNodeGroupPriorityCreateDeleteRunner createDeleteRunner = new FcNodeGroupPriorityCreateDeleteRunner(request,
          requestBody);
      execAsyncRunner(createDeleteRunner);

      responseBase = responseNodeGroupPriorityCreateDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseNodeGroupPriorityCreateDeleteData() {
    try {
      logger.methodStart();
      NodeGroupPriorityCreateDeleteResponseBody body = new NodeGroupPriorityCreateDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
