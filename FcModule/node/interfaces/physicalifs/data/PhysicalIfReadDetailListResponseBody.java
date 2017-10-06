package msf.fc.node.interfaces.physicalifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class PhysicalIfReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("physical_ifs")
  private List<PhysicalIfEntity> physicalIfList;

  public List<PhysicalIfEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
