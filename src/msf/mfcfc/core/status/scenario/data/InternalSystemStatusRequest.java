package msf.mfcfc.core.status.scenario.data;

import msf.mfcfc.core.scenario.RestRequestBase;


public class InternalSystemStatusRequest extends RestRequestBase {

  
  public InternalSystemStatusRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }


}
