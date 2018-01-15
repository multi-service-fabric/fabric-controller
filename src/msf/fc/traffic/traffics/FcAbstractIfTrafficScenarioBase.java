
package msf.fc.traffic.traffics;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.AbstractIfTrafficScenarioBase;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficValueNodeEntity;

/**
 * Abstract class to implement common process of IF traffic information
 * acquisition processing in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractIfTrafficScenarioBase<T extends RestRequestBase>
    extends AbstractIfTrafficScenarioBase<T> {

  protected IfTrafficRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractIfTrafficScenarioBase.class);

  protected FcNode getFcNode(SessionWrapper session) throws MsfException {

    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      FcNodeDao fcNodeDao = new FcNodeDao();

      FcNode fcNode = fcNodeDao.read(session, NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
          Integer.valueOf(request.getNodeId()));

      if (fcNode == null) {

        String logMsg = MessageFormat.format("target resource not found. parameters={0}, fabricType={1}, nodeId={2}",
            "node", request.getFabricType(), request.getNodeId());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }

      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  protected TrafficInfoCollectTrafficEcResponseBody sendTrafficRead(String ecNodeId) throws MsfException {

    try {
      logger.methodStart(new String[] { "ecNodeId" }, new Object[] { ecNodeId });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.TRAFFIC_READ.getHttpMethod(),
          EcRequestUri.TRAFFIC_READ.getUri(ecNodeId), null, ecControlIpAddress, ecControlPort);

      TrafficInfoCollectTrafficEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          TrafficInfoCollectTrafficEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      if (!responseBody.getIsSuccess()) {
        String logMsg = MessageFormat.format("target resource not found. parameters={0}, ecNodeId={1}, isSuccess={2}",
            "NodeTraffic", ecNodeId, responseBody.getIsSuccess());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }

      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected IfTrafficEntity getIfTrafficEntity(String fabricType, String nodeId,
      TrafficInfoTrafficValueEcEntity trafficInfo) {

    try {
      logger.methodStart(new String[] { "fabricType", "nodeId", "trafficInfo" },
          new Object[] { fabricType, nodeId, trafficInfo });
      IfTrafficEntity ifTraffic = new IfTrafficEntity();
      ifTraffic.setFabricType(fabricType);
      ifTraffic.setNodeId(nodeId);

      IfTrafficValueNodeEntity ifTrafficValueNode = new IfTrafficValueNodeEntity();
      ifTrafficValueNode.setIfType(trafficInfo.getIfType());
      ifTrafficValueNode.setIfId(trafficInfo.getIfId());
      ifTrafficValueNode.setReceiveRate(trafficInfo.getReceiveRate());
      ifTrafficValueNode.setSendRate(trafficInfo.getSendRate());
      ifTraffic.setTrafficValue(ifTrafficValueNode);
      return ifTraffic;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {

    try {
      logger.methodStart(new String[] { "body", "statusCode" },
          new Object[] { ToStringBuilder.reflectionToString(body), statusCode });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

}