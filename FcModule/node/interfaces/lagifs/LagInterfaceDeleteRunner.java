package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.EcControlStatusUtil;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfDeleteEcResponseBody;

public class LagInterfaceDeleteRunner extends AbstractLagInterfaceRunnerBase {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  private ErrorCode ecResponseStatus = null;

  public LagInterfaceDeleteRunner(LagIfRequest request) {
    this.request = request;
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

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<Node> nodes = new ArrayList<>();
        nodes.add(lagIf.getNode());
        DbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        EdgePointDao edgePointDao = new EdgePointDao();
        checkStatus(sessionWrapper, edgePointDao, lagIf);

        lagIfDao.delete(sessionWrapper, lagIf.getLagIfInfoId());

        sendLagInterfaceDelete();

        responseBase = responseLagInterfaceDeleteAsyncData();

        sessionWrapper.commit(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED) {
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED,
            "Lag Interface Delete Complete. EC Control Error.");
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

  private void checkStatus(SessionWrapper sessionWrapper, EdgePointDao edgePointDao, LagIf lagIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "edgePointDao", "lagIf" },
          new Object[] { sessionWrapper, edgePointDao, lagIf });
      if (lagIf.getInternalLinkIf() != null) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "lagifs exist related to the internal link.");
      }
      EdgePoint edgePoint = edgePointDao.readBylagIfInfoId(sessionWrapper, lagIf.getLagIfInfoId());
      if (edgePoint != null) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "lagifs exist related to the edge point.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceDeleteAsyncData() {
    try {
      logger.methodStart();
      if (ecResponseStatus != null) {
        return null;
      }

      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfDeleteEcResponseBody sendLagInterfaceDelete() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_DELETE.getHttpMethod(),
          EcRequestUri.LAG_IF_DELETE.getUri(request.getFabricType(), request.getNodeId(), request.getLagIfId()), null);

      LagIfDeleteEcResponseBody lagIfDeleteEcResponseBody = new LagIfDeleteEcResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        lagIfDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            LagIfDeleteEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        ecResponseStatus = EcControlStatusUtil.checkEcEmControlErrorCode(lagIfDeleteEcResponseBody.getErrorCode());
        if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR) {
          String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
              restResponseBase.getHttpStatusCode(), lagIfDeleteEcResponseBody.getErrorCode());
          logger.error(errorMsg);
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
        }
      }
      return lagIfDeleteEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
