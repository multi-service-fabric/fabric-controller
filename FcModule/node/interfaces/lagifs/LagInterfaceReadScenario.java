package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.LagIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.node.interfaces.lagifs.data.LagIfReadResponseBody;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfReadEcResponseBody;

public class LagInterfaceReadScenario extends AbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(LagInterfaceReadScenario.class);

  public LagInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFabricType(request.getFabricType());

      checkNodeId(request.getNodeId());

      checkLagIfId(request.getLagIfId());

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
        LagIfDao lagIfDao = new LagIfDao();
        LagIf lagIf = getLagInterface(sessionWrapper, lagIfDao, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()),
            Integer.parseInt(request.getLagIfId()));

        responseBase = responseLagInterfaceReadData(lagIf);

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

  private LagIf getLagInterface(SessionWrapper sessionWrapper, LagIfDao lagIfDao, String swClusterId, Integer nodeType,
      Integer nodeId, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart();
      LagIf lagIf = lagIfDao.read(sessionWrapper, swClusterId, nodeType, nodeId, lagIfId);
      if (lagIf == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = lagIf");
      }
      return lagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceReadData(LagIf lagIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf" }, new Object[] { lagIf });
      LagIfReadResponseBody body = new LagIfReadResponseBody();

      LagIfReadEcResponseBody lagIfReadEcResponseBody = sendLagInterfaceRead();
      body.setLagIf(getLagIfData(lagIf, lagIfReadEcResponseBody.getLagIf()));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfReadEcResponseBody sendLagInterfaceRead() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_READ.getHttpMethod(),
          EcRequestUri.LAG_IF_READ.getUri(request.getFabricType(), request.getNodeId(), request.getLagIfId()), null);

      LagIfReadEcResponseBody lagIfReadEcResponseBody = new LagIfReadEcResponseBody();

      lagIfReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), LagIfReadEcResponseBody.class,
          ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), lagIfReadEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return lagIfReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
