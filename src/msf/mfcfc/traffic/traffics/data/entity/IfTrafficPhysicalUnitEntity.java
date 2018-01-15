package msf.mfcfc.traffic.traffics.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class IfTrafficPhysicalUnitEntity {

  
  @SerializedName("ifs")
  private List<IfTrafficNotifyEntity> ifList;

  
  public List<IfTrafficNotifyEntity> getIfList() {
    return ifList;
  }

  
  public void setIfList(List<IfTrafficNotifyEntity> ifList) {
    this.ifList = ifList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
