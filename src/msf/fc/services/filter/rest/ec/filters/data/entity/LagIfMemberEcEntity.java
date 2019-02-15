
package msf.fc.services.filter.rest.ec.filters.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LagIfMemberEcEntity {

  @SerializedName("physical_ifs")
  private List<PhysicalIfIdEcEntity> physicalIfs;

  @SerializedName("breakout_ifs")
  private List<BreakoutIfIdEcEntity> breakoutIfs;

  public List<PhysicalIfIdEcEntity> getPhysicalIfs() {
    return physicalIfs;
  }

  public void setPhysicalIfs(List<PhysicalIfIdEcEntity> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  public List<BreakoutIfIdEcEntity> getBreakoutIfs() {
    return breakoutIfs;
  }

  public void setBreakoutIfs(List<BreakoutIfIdEcEntity> breakoutIfs) {
    this.breakoutIfs = breakoutIfs;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
