
package msf.fc.traffic.traffics;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
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
import msf.mfcfc.traffic.traffics.data.IfTrafficReadListResponseBody;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficEntity;

/**
 * Implementation class for IF traffic information list acquisition.
 *
 * @author NTT
 *
 */
public class FcIfTrafficReadListScenario extends FcAbstractIfTrafficScenarioBase<IfTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcIfTrafficReadListScenario.class);

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
  public FcIfTrafficReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(IfTrafficRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNull(request.getClusterId());
      ParameterCheckUtil.checkNotNull(request.getFabricType());
      if (NodeType.LEAF != request.getFabricTypeEnum() && NodeType.SPINE != request.getFabricTypeEnum()) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "fabric_type",
            request.getFabricType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNotNull(request.getNodeId());

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

        List<IfTrafficEntity> ifTraffics = getIfTraffics(NodeType.getEnumFromPluralMessage(request.getFabricType()),
            request.getNodeId(), responseBody);
        RestResponseBase responseBase = responseTrafficInfoData(ifTraffics);
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

  private List<IfTrafficEntity> getIfTraffics(NodeType nodeType, String nodeId,
      TrafficInfoCollectTrafficEcResponseBody responseBody) {

    try {
      logger.methodStart(new String[] { "nodeType", "nodeId", "trafficInfoCollectTrafficEcResponseBody" },
          new Object[] { nodeType, nodeId, responseBody });

      List<IfTrafficEntity> ifTrafficList = new ArrayList<IfTrafficEntity>();

      if (responseBody.getSwitchTraffic().getTrafficValueList() == null) {

        return ifTrafficList;
      }

      for (TrafficInfoTrafficValueEcEntity trafficInfo : responseBody.getSwitchTraffic().getTrafficValueList()) {
        IfTrafficEntity ifTraffic = getIfTrafficEntity(nodeType.getPluralMessage(), nodeId, trafficInfo);
        ifTrafficList.add(ifTraffic);
      }
      return ifTrafficList;

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseTrafficInfoData(List<IfTrafficEntity> ifTraffics) {

    IfTrafficReadListResponseBody body = new IfTrafficReadListResponseBody();
    if (!ifTraffics.isEmpty()) {
      body.setIfTrafficList(ifTraffics);
    }
    return createRestResponse(body, HttpStatus.OK_200);
  }

}
