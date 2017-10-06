package msf.fc.slice;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.CpCreateIfType;
import msf.fc.common.constant.EcEmControlStatus;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.AbstractAsyncRunner;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.rest.common.EcControlStatusUtil;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.core.operation.data.OperationResponseBody;
import msf.fc.traffic.TrafficManager;

public abstract class AbstractSliceCpRunnerBase extends AbstractAsyncRunner {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSliceCpRunnerBase.class);

  protected CpCreateIfType getIfType(EdgePoint edgePoint) {
    try {
      logger.methodStart(new String[] { "edgePoint" }, new Object[] { edgePoint });
      if (edgePoint.getPhysicalIfInfoId() != null) {
        return CpCreateIfType.PHYSICAL_IF;
      } else {
        return CpCreateIfType.LAG_IF;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected String getIfId(SessionWrapper sessionWrapper, EdgePoint edgePoint) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "edgePoint" }, new Object[] { sessionWrapper, edgePoint });
      switch (getIfType(edgePoint)) {
        case PHYSICAL_IF:
          PhysicalIfDao physicalIfDao = new PhysicalIfDao();
          PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, edgePoint.getPhysicalIfInfoId());
          return physicalIf.getPhysicalIfId();
        default:
          LagIfDao lagIfDao = new LagIfDao();
          LagIf lagIf = lagIfDao.read(sessionWrapper, edgePoint.getLagIfInfoId());
          return String.valueOf(lagIf.getLagIfId());
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void commitTransaction(SessionWrapper sessionWrapper, RestResponseBase restResponse, boolean renewTopology)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "restResponse", "renewTopology" },
          new Object[] { sessionWrapper, restResponse, renewTopology });

      if (restResponse.getHttpStatusCode() == HttpStatus.OK_200) {
        sessionWrapper.commit(EcEmControlStatus.SUCCESS);
        if (renewTopology) {
          TrafficManager.getInstance().setRenewTopology(true);
        }
      } else {
        OperationResponseBody responseBody = JsonUtil.fromJson(restResponse.getResponseBody(),
            OperationResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        EcEmControlStatus ecEmControlStatus = EcControlStatusUtil.getStatusFromEcErrorCode(responseBody.getErrorCode());
        switch (ecEmControlStatus) {
          case EM_SUCCESS_BUT_EC_FAILED:
            sessionWrapper.commit(ecEmControlStatus);
            if (renewTopology) {
              TrafficManager.getInstance().setRenewTopology(true);
            }
            String logMsg1 = MessageFormat.format("ecErorCode = {0}, ecErrorMessage", responseBody.getErrorCode(),
                responseBody.getErrorMessage());
            logger.error(logMsg1);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, logMsg1);
          case FAILED:
            String logMsg2 = MessageFormat.format("ecErorCode = {0}, ecErrorMessage", responseBody.getErrorCode(),
                responseBody.getErrorMessage());
            logger.error(logMsg2);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, logMsg2);
          default:
            String logMsg3 = MessageFormat.format("unexpected ec status. status = {0}.", ecEmControlStatus.name());
            logger.error(logMsg3);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg3);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendRequestToEc(String requestJson, EcRequestUri ecReuqestUri, String... uriParams)
      throws MsfException {
    try {
      logger.methodStart();
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(requestJson);
      RestResponseBase restResponse = RestClient.sendRequest(ecReuqestUri.getHttpMethod(),
          ecReuqestUri.getUri(uriParams), requestBase);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkNodePresence(Node node, String swClusterId, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "swClusterId", "edgePointId" },
          new Object[] { node, swClusterId, edgePointId });
      ParameterCheckUtil.checkNotNullRelatedResource(node, new String[] { "swClusterId", "edgePointId" },
          new Object[] { swClusterId, String.valueOf(edgePointId) });
    } finally {
      logger.methodEnd();
    }
  }
}
