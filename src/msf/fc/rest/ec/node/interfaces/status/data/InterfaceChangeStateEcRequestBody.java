
package msf.fc.rest.ec.node.interfaces.status.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InterfaceChangeStateEcRequestBody {

  @SerializedName("status")
  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
