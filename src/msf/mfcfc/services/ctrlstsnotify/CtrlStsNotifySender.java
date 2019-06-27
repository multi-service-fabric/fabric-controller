
package msf.mfcfc.services.ctrlstsnotify;

import java.util.concurrent.TimeUnit;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.ctrlstsnotify.common.constant.MfcFcRequestUri;

/**
 * Class to provide the notification function of the controller status
 * notification function block.
 *
 * @author NTT
 *
 */
public class CtrlStsNotifySender {

  /**
   * Constructor.
   *
   */
  public CtrlStsNotifySender() {

  }

  /**
   * Execute the notification process of the controller status function block.
   *
   * @param ipAddress
   *          Destination IP address
   * @param port
   *          Destination port number
   * @param noticeRetryNum
   *          Notification Retry Count [times]
   * @param noticeTimeout
   *          Notification Retry Interval [ms]
   * @param request
   *          Notification REST request of the controller status function block.
   * @param requestUri
   *          REST Request URI
   */
  public static void sendNotify(String ipAddress, int port, int noticeRetryNum, int noticeTimeout,
      RestRequestBase request, MfcFcRequestUri requestUri) {
    for (int retryNum = 0; retryNum < noticeRetryNum + 1; retryNum++) {
      if (sendTarget(ipAddress, port, noticeTimeout, request, requestUri) != null) {
        break;
      }
    }
  }

  protected static RestResponseBase sendTarget(String ipAddress, int port, int noticeTimeout, RestRequestBase request,
      MfcFcRequestUri requestUri) {
    RestResponseBase restResponseBase = new RestResponseBase();
    try {
      restResponseBase = RestClient.sendRequest(requestUri.getHttpMethod(), requestUri.getUri(), request, ipAddress,
          port);
    } catch (MsfException msf) {
      try {
        restResponseBase = null;
        TimeUnit.MILLISECONDS.sleep(noticeTimeout);
      } catch (InterruptedException ie) {

      }
    }
    return restResponseBase;
  }

}
