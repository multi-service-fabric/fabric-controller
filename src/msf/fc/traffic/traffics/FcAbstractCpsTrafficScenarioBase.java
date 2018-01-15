
package msf.fc.traffic.traffics;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectAllTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.rest.ec.traffic.data.entity.TrafficInfoTrafficValueEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.AbstractCpsTrafficScenarioBase;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficValueCpEntity;

/**
 * Abstract class to implement common process of CP traffic information
 * acquisition processing in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractCpsTrafficScenarioBase<T extends RestRequestBase>
    extends AbstractCpsTrafficScenarioBase<T> {

  protected CpTrafficRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractCpsTrafficScenarioBase.class);

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

  protected TrafficInfoCollectAllTrafficEcResponseBody sendTrafficReadList() throws MsfException {

    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.TRAFFIC_READ_LIST.getHttpMethod(),
          EcRequestUri.TRAFFIC_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      TrafficInfoCollectAllTrafficEcResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          TrafficInfoCollectAllTrafficEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      if (!responseBody.getIsSuccess()) {
        String logMsg = MessageFormat.format("target resource not found. parameters={0}, isSuccess={1}", "NodeTraffic",
            responseBody.getIsSuccess());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, logMsg);
      }
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected CpTrafficEntity getCpTrafficEntity(String sliceId, String cpId,
      TrafficInfoTrafficValueEcEntity trafficInfo) {

    try {
      logger.methodStart(new String[] { "sliceId", "cpId", "trafficInfo" },
          new Object[] { sliceId, cpId, trafficInfo });

      CpTrafficEntity cpTraffic = new CpTrafficEntity();
      cpTraffic.setSliceId(sliceId);
      cpTraffic.setCpId(cpId);

      CpTrafficValueCpEntity cpTrafficValueCpEntity = new CpTrafficValueCpEntity();
      cpTrafficValueCpEntity.setReceiveRate(trafficInfo.getReceiveRate());
      cpTrafficValueCpEntity.setSendRate(trafficInfo.getSendRate());
      cpTraffic.setTrafficValue(cpTrafficValueCpEntity);
      return cpTraffic;
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
