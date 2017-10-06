package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.LagIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.node.interfaces.lagifs.data.LagIfReadDetailListResponseBody;
import msf.fc.node.interfaces.lagifs.data.LagIfReadListResponseBody;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfReadListEcResponseBody;

public class LagInterfaceReadListScenario extends AbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(LagInterfaceReadListScenario.class);

  public LagInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

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
        List<LagIf> lagIfs = lagIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        if (lagIfs.isEmpty()) {
          checkNode(sessionWrapper, request.getClusterId(), request.getFabricTypeEnum().getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseLagInterfaceReadListData(lagIfs, request.getFormat());

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

  private RestResponseBase responseLagInterfaceReadListData(List<LagIf> lagIfs, String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        LagIfReadDetailListResponseBody body = new LagIfReadDetailListResponseBody();
        if (!lagIfs.isEmpty()) {
          LagIfReadListEcResponseBody sendLagInterfaceReadList = sendLagInterfaceReadList();
          body.setLagIfList(getLagIfEntities(lagIfs, sendLagInterfaceReadList.getLagIf()));
        } else {
          body.setLagIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        LagIfReadListResponseBody body = new LagIfReadListResponseBody();
        body.setLagIfIdList(getLagIfIdList(lagIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfReadListEcResponseBody sendLagInterfaceReadList() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.LAG_IF_READ_LIST.getUri(request.getFabricType(), request.getNodeId()), null);

      LagIfReadListEcResponseBody lagIfReadListEcResponseBody = new LagIfReadListEcResponseBody();

      lagIfReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          LagIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), lagIfReadListEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return lagIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
