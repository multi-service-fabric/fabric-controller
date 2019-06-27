
package msf.mfcfc.services.renewal.scenario.renewals.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.renewal.scenario.renewals.data.entity.RenewalStatusEntity;

public class RenewalReadResponseBody extends AbstractResponseBody {

  @SerializedName("controller_renewal_statuses")
  private List<RenewalStatusEntity> controllerRenewalStatusList;

  public List<RenewalStatusEntity> getControllerRenewalStatusList() {
    return controllerRenewalStatusList;
  }

  public void setControllerRenewalStatusList(List<RenewalStatusEntity> controllerRenewalStatusList) {
    this.controllerRenewalStatusList = controllerRenewalStatusList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
