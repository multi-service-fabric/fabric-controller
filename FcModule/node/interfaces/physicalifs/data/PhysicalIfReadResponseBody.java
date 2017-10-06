package msf.fc.node.interfaces.physicalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class PhysicalIfReadResponseBody extends AbstractResponseBody {

  @SerializedName("physical_if")
  private PhysicalIfEntity physicalIf;

  public PhysicalIfEntity getPhysicalIf() {
    return physicalIf;
  }

  public void setPhysicalIf(PhysicalIfEntity physicalIf) {
    this.physicalIf = physicalIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
