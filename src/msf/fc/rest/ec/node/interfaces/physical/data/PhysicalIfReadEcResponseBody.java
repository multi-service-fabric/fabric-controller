package msf.fc.rest.ec.node.interfaces.physical.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class PhysicalIfReadEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("physical_if")
  private PhysicalIfEcEntity physicalIf;

  
  public PhysicalIfEcEntity getPhysicalIf() {
    return physicalIf;
  }

  
  public void setPhysicalIf(PhysicalIfEcEntity physicalIf) {
    this.physicalIf = physicalIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
