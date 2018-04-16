
package msf.fc.rest.ec.node.interfaces.physical.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PhysicalIfUpdateAction;

public class PhysicalIfUpdateEcRequestBody {

  @SerializedName("action")
  private String action;

  @SerializedName("speed")
  private String speed;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public PhysicalIfUpdateAction getActionEnum() {
    return PhysicalIfUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(PhysicalIfUpdateAction action) {
    this.action = action.getMessage();
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
