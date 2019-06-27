
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

import msf.mfcfc.core.scenario.RestRequestBase;

public class ControllerLogNotificationRequest extends RestRequestBase {

  public ControllerLogNotificationRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }

}
