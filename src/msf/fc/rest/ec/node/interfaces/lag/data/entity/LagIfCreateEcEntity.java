
package msf.fc.rest.ec.node.interfaces.lag.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfCreateEcEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("physical_ifs")
  private List<LagIfPhysicalIfCreateEcEntity> physicalIfList;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public List<LagIfPhysicalIfCreateEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<LagIfPhysicalIfCreateEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
