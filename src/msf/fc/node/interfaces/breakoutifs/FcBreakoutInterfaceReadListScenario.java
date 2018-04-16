
package msf.fc.node.interfaces.breakoutifs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.BreakoutIfReadListEcResponseBody;
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
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfReadListResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for breakout interface information list acquisition.
 *
 * @author NTT
 *
 */
public class FcBreakoutInterfaceReadListScenario extends FcAbstractBreakoutInterfaceScenarioBase<BreakoutIfRequest> {

  private BreakoutIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutInterfaceReadListScenario.class);

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
  public FcBreakoutInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(BreakoutIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))
          && !NodeType.SPINE.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }

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
        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

        List<FcBreakoutIf> fcBreakoutIfs = fcBreakoutIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));

        if (fcBreakoutIfs.isEmpty()) {

          checkNode(sessionWrapper, NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseBreakoutInterfaceReadListData(fcBreakoutIfs, request.getFormat());

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

  protected RestResponseBase responseBreakoutInterfaceReadListData(List<FcBreakoutIf> fcBreakoutIfs, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        BreakoutIfReadDetailListResponseBody body = new BreakoutIfReadDetailListResponseBody();
        if (!fcBreakoutIfs.isEmpty()) {
          BreakoutIfReadListEcResponseBody breakoutLagInterfaceReadList = sendBreakoutIfReadList(
              fcBreakoutIfs.get(0).getNode());
          body.setBreakoutIfList(
              getBreakoutIfEntities(fcBreakoutIfs, breakoutLagInterfaceReadList.getBreakoutIfList()));
        } else {

          body.setBreakoutIfList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        BreakoutIfReadListResponseBody body = new BreakoutIfReadListResponseBody();
        body.setBreakoutIfIdList(getBreakoutIfIdList(fcBreakoutIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private BreakoutIfReadListEcResponseBody sendBreakoutIfReadList(FcNode node) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.BREAKOUT_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.BREAKOUT_IF_READ_LIST.getUri(String.valueOf(node.getEcNodeId())), null, ecControlIpAddress,
          ecControlPort);

      BreakoutIfReadListEcResponseBody breakoutIfReadListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), BreakoutIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          breakoutIfReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return breakoutIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
