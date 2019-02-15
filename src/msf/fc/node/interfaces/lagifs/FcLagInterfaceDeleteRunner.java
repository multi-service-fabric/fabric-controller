
package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the Lag interface deletion.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceDeleteRunner extends FcAbstractLagInterfaceRunnerBase {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceDeleteRunner.class);

  private ErrorCode ecResponseStatus = null;

  public FcLagInterfaceDeleteRunner(LagIfRequest request) {
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
        FcLagIfDao fcLagIfDao = new FcLagIfDao();

        FcLagIf fcLagIf = getLagInterface(sessionWrapper, fcLagIfDao, request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()), Integer.parseInt(request.getLagIfId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();
        fcNodes.add(fcLagIf.getNode());
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        checkStatus(sessionWrapper, fcLagIf);

        fcLagIfDao.delete(sessionWrapper, fcLagIf.getLagIfId());

        sendLagInterfaceDelete(fcLagIf);

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

  private FcLagIf getLagInterface(SessionWrapper sessionWrapper, FcLagIfDao fcLagIfDao, Integer nodeType,
      Integer nodeId, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart();
      FcLagIf fcLagIf = fcLagIfDao.read(sessionWrapper, nodeType, nodeId, lagIfId);
      if (fcLagIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcLagIf");
      }
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkStatus(SessionWrapper sessionWrapper, FcLagIf fcLagIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "fcLagIf" }, new Object[] { sessionWrapper, fcLagIf });
      if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
            "lagifs exist related to the internal link interface.");
      }
      if (CollectionUtils.isNotEmpty(fcLagIf.getEdgePoints())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "lagifs exist related to the edge point.");
      }
      if (CollectionUtils.isNotEmpty(fcLagIf.getClusterLinkIfs())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
            "lagifs exist related to the cluster link interface.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLagInterfaceDelete(FcLagIf fcLagIf) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_DELETE.getHttpMethod(),
          EcRequestUri.LAG_IF_DELETE.getUri(String.valueOf(fcLagIf.getNode().getEcNodeId()), request.getLagIfId()),
          null, ecControlIpAddress, ecControlPort);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        ErrorInternalResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        ecResponseStatus = EcControlStatusUtil.checkEcEmControlErrorCode(responseBody.getErrorCode());
        if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR) {

          String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
              restResponseBase.getHttpStatusCode(), responseBody.getErrorCode());
          logger.error(errorMsg);
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
        }
      }
      return restResponseBase;
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
}
