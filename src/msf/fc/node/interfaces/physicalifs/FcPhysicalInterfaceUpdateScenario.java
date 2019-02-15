
package msf.fc.node.interfaces.physicalifs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfUpdateEcRequestBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
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
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for the physical interface modification.
 *
 * @author NTT
 *
 */
public class FcPhysicalInterfaceUpdateScenario extends FcAbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;
  private PhysicalIfUpdateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalInterfaceUpdateScenario.class);

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
  public FcPhysicalInterfaceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      RestResponseBase restResponseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        FcNodeDao fcNodeDao = new FcNodeDao();

        FcNode fcNode = getNode(sessionWrapper, fcNodeDao, request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();
        fcNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
        FcPhysicalIf fcPhysicalIf = getPhysicalInterface(sessionWrapper, fcPhysicalIfDao,
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        checkForPhysicalInterface(fcPhysicalIf);

        sendPhysicalInterfaceUpdate(fcNode);

        restResponseBase = responsePhysicalInterfaceUpdateData();

        sessionWrapper.rollback();

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType, nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource is not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkForPhysicalInterface(FcPhysicalIf fcPhysicalIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcPhysicalIf" }, new Object[] { fcPhysicalIf });

      if (CollectionUtils.isNotEmpty(fcPhysicalIf.getEdgePoints())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Already physical IF is used in edgepoint.");
      }

      if (CollectionUtils.isNotEmpty(fcPhysicalIf.getInternalLinkIfs())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Already physical IF is used in internal link IF.");
      }

      if (CollectionUtils.isNotEmpty(fcPhysicalIf.getClusterLinkIfs())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Already physical IF is used in cluster link IF.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendPhysicalInterfaceUpdate(FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();
      PhysicalIfUpdateEcRequestBody physicalIfUpdateEcRequestBody = new PhysicalIfUpdateEcRequestBody();
      physicalIfUpdateEcRequestBody.setAction(requestBody.getAction());
      physicalIfUpdateEcRequestBody.setSpeed(requestBody.getSpeed());

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(physicalIfUpdateEcRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_UPDATE.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_UPDATE.getUri(String.valueOf(fcNode.getEcNodeId()), request.getIfId()), restRequest,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody physicalIfUpdateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = physicalIfUpdateEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responsePhysicalInterfaceUpdateData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
