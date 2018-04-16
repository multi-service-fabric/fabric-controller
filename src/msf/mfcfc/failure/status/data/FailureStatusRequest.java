
package msf.mfcfc.failure.status.data;

import msf.mfcfc.core.scenario.RestRequestBase;

public class FailureStatusRequest extends RestRequestBase {

  public FailureStatusRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }

}
