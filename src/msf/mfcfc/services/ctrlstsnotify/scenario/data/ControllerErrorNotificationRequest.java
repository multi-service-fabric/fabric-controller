
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import msf.mfcfc.core.scenario.RestRequestBase;

public class ControllerErrorNotificationRequest extends RestRequestBase {

  public ControllerErrorNotificationRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }

}
