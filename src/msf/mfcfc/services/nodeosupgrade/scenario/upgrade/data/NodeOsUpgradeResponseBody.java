
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class NodeOsUpgradeResponseBody extends AbstractResponseBody {

  @SerializedName("operation_id")
  private String operationId;

  public String getOpetationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
