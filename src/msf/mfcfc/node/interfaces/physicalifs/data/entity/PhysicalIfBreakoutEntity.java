
package msf.mfcfc.node.interfaces.physicalifs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfBreakoutEntity {

  @SerializedName("speed")
  private String speed;

  @SerializedName("ifs")
  private List<PhysicalIfIfEntity> ifList;

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public List<PhysicalIfIfEntity> getIfList() {
    return ifList;
  }

  public void setIfList(List<PhysicalIfIfEntity> ifList) {
    this.ifList = ifList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
