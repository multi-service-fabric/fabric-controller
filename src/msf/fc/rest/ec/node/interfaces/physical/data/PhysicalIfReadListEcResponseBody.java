
package msf.fc.rest.ec.node.interfaces.physical.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class PhysicalIfReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("physical_ifs")
  private List<PhysicalIfEcEntity> physicalIfList;

  public List<PhysicalIfEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
