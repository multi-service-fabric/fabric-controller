
package msf.mfcfc.core.status.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;


public class SystemStatusNotifyRequest extends RestRequestBase {

  
  public SystemStatusNotifyRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);



  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
