
package msf.fc.traffic.traffics;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.traffic.traffics.data.IfTrafficReadResponseBody;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficEntity;

/**
 * Implementation class for IF traffic information acquisition.
 *
 * @author NTT
 *
 */
public class FcIfTrafficReadScenario extends FcAbstractIfTrafficScenarioBase<IfTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcIfTrafficReadScenario.class);

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
  public FcIfTrafficReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(IfTrafficRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(request.getFabricType());
      if ((!NodeType.LEAF.getPluralMessage().equals(request.getFabricType()))
          && (!NodeType.SPINE.getPluralMessage().equals(request.getFabricType()))) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "fabric_type",
            request.getFabricType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotNull(request.getIfType());
      if (InterfaceType.PHYSICAL_IF != request.getIfTypeEnum() && InterfaceType.BREAKOUT_IF != request.getIfTypeEnum()
          && InterfaceType.LAG_IF != request.getIfTypeEnum()) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "if_type",
            request.getIfType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNotNull(request.getIfId());

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();

        FcNode fcNode = getFcNode(session);

        TrafficInfoCollectTrafficEcResponseBody responseBody = sendTrafficRead(String.valueOf(fcNode.getEcNodeId()));

        IfTrafficEntity ifTrafficEntity = getIfTraffic(NodeType.getEnumFromPluralMessage(request.getFabricType()),
            request.getNodeId(), request.getIfType(), request.getIfId(), responseBody);
        RestResponseBase responseBase = responseTrafficInfoData(ifTrafficEntity);
        return responseBase;

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        session.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private IfTrafficEntity getIfTraffic(NodeType nodeType, String nodeId, String ifType, String ifId,
      TrafficInfoCollectTrafficEcResponseBody trafficInfoCollectTrafficEcResponseBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeType", "nodeId", "trafficInfoCollectTrafficEcResponseBody" },
          new Object[] { nodeType, nodeId, trafficInfoCollectTrafficEcResponseBody });

      IfTrafficEntity ifTraffic = null;

      if (trafficInfoCollectTrafficEcResponseBody.getSwitchTraffic().getTrafficValueList() == null) {

        String logMsg = MessageFormat.format("target resource not found. parameters={0}, ecNodeId={1}",
            "TrafficValueList", trafficInfoCollectTrafficEcResponseBody.getNodeId());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }

      for (TrafficInfoTrafficValueEcEntity trafficInfo : trafficInfoCollectTrafficEcResponseBody.getSwitchTraffic()
          .getTrafficValueList()) {

        if (!ifType.equals(trafficInfo.getIfType())) {
          continue;
        }
        if (!ifId.equals(trafficInfo.getIfId())) {
          continue;
        }
        ifTraffic = getIfTrafficEntity(nodeType.getPluralMessage(), nodeId, trafficInfo);
        break;
      }

      if (ifTraffic == null) {

        String logMsg = MessageFormat.format(
            "target resource not found. parameters={0}, nodeType={1}, nodeId={2}, ifType={3}, ifId={4}", "IfTraffic",
            nodeType, nodeId, ifType, ifId);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }
      return ifTraffic;

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseTrafficInfoData(IfTrafficEntity ifTraffic) {
    IfTrafficReadResponseBody body = new IfTrafficReadResponseBody();
    body.setIfTraffic(ifTraffic);
    return createRestResponse(body, HttpStatus.OK_200);
  }

}
