
package msf.mfcfc.core.status.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SystemStatusTrafficEntity {

  @SerializedName("interfaces")
  private List<SystemStatusInterfaceEntity> interfaceList;

  public List<SystemStatusInterfaceEntity> getInterfaceList() {
    return interfaceList;
  }

  public void setInterfaceList(List<SystemStatusInterfaceEntity> interfaceList) {
    this.interfaceList = interfaceList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
