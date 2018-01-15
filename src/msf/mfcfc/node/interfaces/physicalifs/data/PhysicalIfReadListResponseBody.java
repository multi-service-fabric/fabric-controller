package msf.mfcfc.node.interfaces.physicalifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class PhysicalIfReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  
  public List<String> getPhysicalIfIdList() {
    return physicalIfIdList;
  }

  
  public void setPhysicalIfIdList(List<String> physicalIfIdList) {
    this.physicalIfIdList = physicalIfIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
