
package msf.mfc.node.interfaces.physicalifs;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.mfcfc.node.interfaces.physicalifs.data.PhysicalIfUpdateRequestBody;
import msf.mfcfc.rest.common.ErrorResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for physical interface modification.
 *
 * @author NTT
 *
 */
public class MfcPhysicalInterfaceUpdateScenario extends MfcAbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;
  private PhysicalIfUpdateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcPhysicalInterfaceUpdateScenario.class);

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
  public MfcPhysicalInterfaceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(PhysicalIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkIdSpecifiedByUri(request.getIfId());

      PhysicalIfUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          PhysicalIfUpdateRequestBody.class);
      requestBody.validate();
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
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {

        sessionWrapper.openSession();

        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

        getSwCluster(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));

        responseBase = sendPhysicalInterfaceUpdate(request.getClusterId());

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendPhysicalInterfaceUpdate(String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "swClusterId" }, new Object[] { swClusterId });
      PhysicalIfUpdateRequestBody physicalIfUpdateRequestBody = new PhysicalIfUpdateRequestBody();
      physicalIfUpdateRequestBody.setAction(requestBody.getAction());
      physicalIfUpdateRequestBody.setSpeed(requestBody.getSpeed());

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(physicalIfUpdateRequestBody));

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(Integer.valueOf(swClusterId))
          .getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(
          MfcFcRequestUri.PHYSICAL_IF_UPDATE.getHttpMethod(), MfcFcRequestUri.PHYSICAL_IF_UPDATE.getUri(swClusterId,
              request.getFabricType(), request.getNodeId(), request.getIfId()),
          restRequest, fcControlAddress, fcControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), ErrorResponseBody.class,
            ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.FC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }
}
