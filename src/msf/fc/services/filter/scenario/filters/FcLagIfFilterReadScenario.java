
package msf.fc.services.filter.scenario.filters;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfFilterInfo;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.filters.FcLagIfFilterInfoDao;
import msf.fc.services.filter.rest.ec.filters.data.LagIfFilterReadDetailEcResponseBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.filter.common.constant.EcRequestUri;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterReadResponseBody;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterRequest;
import msf.mfcfc.services.filter.scenario.filters.data.entity.LagIfFilterDetailEntity;

/**
 * Implementation class for the LagIF filter information acquisition.
 *
 * @author NTT
 *
 */
public class FcLagIfFilterReadScenario extends FcAbstractFilterScenarioBase<LagIfFilterRequest> {

  private LagIfFilterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterReadScenario.class);

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
  public FcLagIfFilterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

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

      this.request = request;

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
        FcLagIfFilterInfoDao fcLagIfFilterInfoDao = new FcLagIfFilterInfoDao();

        List<FcLagIfFilterInfo> lagIfFilters = fcLagIfFilterInfoDao.readList(sessionWrapper,
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()),
            Integer.parseInt(request.getLagIfId()));

        responseBase = responseLagIfFilterReadData(lagIfFilters, sessionWrapper);

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

  private RestResponseBase responseLagIfFilterReadData(List<FcLagIfFilterInfo> lagIfFilters,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      LagIfFilterReadResponseBody body = new LagIfFilterReadResponseBody();
      if (!lagIfFilters.isEmpty()) {

        FcLagIf lagIf = lagIfFilters.get(0).getLagIf();
        LagIfFilterReadDetailEcResponseBody ecResponseBody = sendLagInterfaceFilterRead(lagIf.getNode(),
            lagIf.getLagIfId());

        LagIfFilterDetailEntity lagIfFilter = new LagIfFilterDetailEntity();
        lagIfFilter.setClusterId(request.getClusterId());
        lagIfFilter.setFabricType(lagIf.getNode().getNodeTypeEnum().getPluralMessage());
        lagIfFilter.setNodeId(String.valueOf(lagIf.getNode().getNodeId()));
        lagIfFilter.setlagIfId(String.valueOf(lagIf.getLagIfId()));
        lagIfFilter.setTerms(getLagIfFilterTermEntities(lagIfFilters, ecResponseBody.getLagIfFilter()));
        body.setLagIfFilter(lagIfFilter);
      } else {

        FcLagIf fcLagIf = getLagInterface(sessionWrapper, new FcLagIfDao(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()), Integer.parseInt(request.getLagIfId()));

        LagIfFilterDetailEntity lagIfFilterDetailEntity = new LagIfFilterDetailEntity();
        lagIfFilterDetailEntity.setClusterId(request.getClusterId());
        lagIfFilterDetailEntity.setFabricType(fcLagIf.getNode().getNodeTypeEnum().getPluralMessage());
        lagIfFilterDetailEntity.setNodeId(String.valueOf(fcLagIf.getNode().getNodeId()));
        lagIfFilterDetailEntity.setlagIfId(String.valueOf(fcLagIf.getLagIfId()));

        lagIfFilterDetailEntity.setTerms(null);
        body.setLagIfFilter(lagIfFilterDetailEntity);
      }

      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfFilterReadDetailEcResponseBody sendLagInterfaceFilterRead(FcNode node, Integer lagIfId)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_FILTER_READ.getHttpMethod(),
          EcRequestUri.LAG_IF_FILTER_READ.getUri(String.valueOf(node.getEcNodeId()), String.valueOf(lagIfId)), null,
          ecControlIpAddress, ecControlPort);

      LagIfFilterReadDetailEcResponseBody lagIfFilterReadDetailEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), LagIfFilterReadDetailEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          lagIfFilterReadDetailEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return lagIfFilterReadDetailEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private FcLagIf getLagInterface(SessionWrapper sessionWrapper, FcLagIfDao fcLagIfDao, Integer nodeType,
      Integer nodeId, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart();
      FcLagIf fcLagIf = fcLagIfDao.read(sessionWrapper, nodeType, nodeId, lagIfId);
      if (fcLagIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, MessageFormat.format(
            "target resource is not found. nodeType = {0}, nodeId = {1}, lagIfId = {2}.", nodeType, nodeId, lagIfId));
      }
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }
}
