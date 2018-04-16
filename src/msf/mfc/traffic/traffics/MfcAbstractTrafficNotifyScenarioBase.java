
package msf.mfc.traffic.traffics;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.NoticeDestInfoTraffic;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.AbstractTrafficNotifyScenarioBase;
import msf.mfcfc.traffic.traffics.data.IfTrafficNotifyRequestBody;
import msf.mfcfc.traffic.traffics.data.TrafficNotifyRequest;

/**
 * Abstract class to implement the common process of traffic information
 * notification process in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractTrafficNotifyScenarioBase<T extends RestRequestBase>
    extends AbstractTrafficNotifyScenarioBase<T> {

  protected TrafficNotifyRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractTrafficNotifyScenarioBase.class);

  protected RestResponseBase sendTrafficNotify(IfTrafficNotifyRequestBody body, NoticeDestInfoTraffic traffic,
      int retry, int timeOut) throws MsfException {

    try {
      logger.methodStart(new String[] { "body", "traffic", "retry", "timeOut" },
          new Object[] { body, traffic, retry, timeOut });

      String requestUri = MfcFcRequestUri.TRAFFIC_NOTIFY.getUri();

      String bodyStr = JsonUtil.toJson(body);

      return sendTrafficRequest(MfcFcRequestUri.TRAFFIC_NOTIFY.getHttpMethod(), requestUri, bodyStr,
          traffic.getNoticeAddress(), traffic.getNoticePort(), retry, timeOut);

    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase sendTrafficRequest(HttpMethod method, String uri, String body, String noticeIpAddress,
      int noticePort, int retry, int timeOut) throws MsfException {
    try {
      logger.methodStart(new String[] { "method", "uri", "body", "noticeIpAddress", "noticePort", "retry", "timeOut" },
          new Object[] { method, uri, body, noticeIpAddress, noticePort, retry, timeOut });

      RestResponseBase restResponseBase = null;

      String ipAdressStr = MfcConfigManager.getInstance().getRestServerListeningAddress();

      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(body);
      requestBase.setRequestUri(uri);
      requestBase.setRequestMethod(method.getMessage());
      requestBase.setSourceIpAddress(ipAdressStr);

      for (int i = 0; i < retry + 1; i++) {

        try {

          restResponseBase = RestClient.sendRequest(method, uri, requestBase, noticeIpAddress, noticePort);
        } catch (MsfException msfExc) {

          try {
            if (timeOut > 0) {
              Thread.sleep(timeOut);
            }
            continue;

          } catch (InterruptedException exc) {

          }

        }

        return restResponseBase;

      }
      logger.warn(retry + " times retry failure");
      return createRestResponseBase();

    } finally {
      logger.methodEnd();
    }

  }

  protected RestResponseBase createRestResponseBase() {

    RestResponseBase result = new RestResponseBase();

    result.setHttpStatusCode(HttpStatus.OK_200);

    return result;
  }

}
