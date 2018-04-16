
package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;

public class TrafficNotifyRequest extends RestRequestBase {

  public TrafficNotifyRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
