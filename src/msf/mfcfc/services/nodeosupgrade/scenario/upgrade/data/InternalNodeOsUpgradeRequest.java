
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.core.scenario.RestRequestBase;

public class InternalNodeOsUpgradeRequest extends RestRequestBase {

  public InternalNodeOsUpgradeRequest(String requestBody, String notificationAddress, String notificationPort) {
    super(requestBody, notificationAddress, notificationPort);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
