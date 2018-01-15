package msf.fc.rest.ec.status.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ControllerStatusTrafficEcEntity {

  
  @SerializedName("interfaces")
  private List<ControllerStatusInterfaceEcEntity> interfaceList;

  
  public List<ControllerStatusInterfaceEcEntity> getInterfaceList() {
    return interfaceList;
  }

  
  public void setInterfaceList(List<ControllerStatusInterfaceEcEntity> interfaceList) {
    this.interfaceList = interfaceList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
