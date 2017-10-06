package msf.fc.traffic.tm;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;
import msf.fc.traffic.data.TrafficCommonData;

public class TrafficNodetrafficInfo {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficNodetrafficInfo.class);

  public TrafficNodetrafficInfo() {
  }

  public void requestNodeTraffic() throws MsfException, InterruptedException {
    logger.methodStart();

    TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();


    try {
      RestResponseBase trafficInfo;
      final long start = System.currentTimeMillis();
      trafficInfo = RestClient.sendRequest(EcRequestUri.TRAFFIC_READ.getHttpMethod(),
          EcRequestUri.TRAFFIC_READ.getUri(), null);

      if (trafficInfo.getHttpStatusCode() != HttpStatus.OK_200) {
        String err = "Other than 200 OK was returned in the TRAFFIC_READ. HTTP Status Code = "
            + trafficInfo.getHttpStatusCode();
        logger.warn(err);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, err);
      }

      final long end = System.currentTimeMillis();
      logger.performance("Traffic Information Request for EC.(REST Request)(sec)：" + (end - start) / 1000);

      parseTrafficInfo(trafficInfo);

    } catch (MsfException exp) {
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }

  public void parseTrafficInfo(RestResponseBase trafficInfo) throws MsfException {
    try {
      logger.methodStart();
      TrafficInfoCollectTrafficEcResponseBody trafficResponseBody;
      String responseBody;

      responseBody = trafficInfo.getResponseBody();
      trafficResponseBody = JsonUtil.fromJson(responseBody, TrafficInfoCollectTrafficEcResponseBody.class);

      TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();
      trafficCommonData.setTrafficResponse(trafficResponseBody);

    } finally {
      logger.methodEnd();
    }
  }

}