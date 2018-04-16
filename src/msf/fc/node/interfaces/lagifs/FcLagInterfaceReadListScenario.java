
package msf.fc.node.interfaces.lagifs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcLagIf;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfReadListEcResponseBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfReadListResponseBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for Lag interface information list acquisition.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceReadListScenario extends FcAbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceReadListScenario.class);

  /**
   * Constructor
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
  public FcLagInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));

      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

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
        FcLagIfDao fcLagIfDao = new FcLagIfDao();

        List<FcLagIf> fcLagIfs = fcLagIfDao.readList(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));

        if (fcLagIfs.isEmpty()) {

          checkNode(sessionWrapper, request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseLagInterfaceReadListData(fcLagIfs, request.getFormat());

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

  protected RestResponseBase responseLagInterfaceReadListData(List<FcLagIf> fcLagIfs, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        LagIfReadDetailListResponseBody body = new LagIfReadDetailListResponseBody();
        if (!fcLagIfs.isEmpty()) {
          LagIfReadListEcResponseBody sendLagInterfaceReadList = sendLagInterfaceReadList(fcLagIfs);
          body.setLagIfList(getLagIfEntities(fcLagIfs, sendLagInterfaceReadList.getLagIf()));
        } else {

          body.setLagIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        LagIfReadListResponseBody body = new LagIfReadListResponseBody();
        body.setLagIfIdList(getLagIfIdList(fcLagIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfReadListEcResponseBody sendLagInterfaceReadList(List<FcLagIf> fcLagIfs) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.LAG_IF_READ_LIST.getUri(String.valueOf(fcLagIfs.get(0).getNode().getEcNodeId())), null,
          ecControlIpAddress, ecControlPort);

      LagIfReadListEcResponseBody lagIfReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          LagIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          lagIfReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return lagIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
