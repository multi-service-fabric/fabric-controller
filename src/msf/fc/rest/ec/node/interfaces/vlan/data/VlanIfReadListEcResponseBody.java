package msf.fc.rest.ec.node.interfaces.vlan.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class VlanIfReadListEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("vlan_ifs")
  private List<VlanIfEcEntity> vlanIfList;

  
  public List<VlanIfEcEntity> getVlanIfList() {
    return vlanIfList;
  }

  
  public void setVlanIfList(List<VlanIfEcEntity> vlanIfList) {
    this.vlanIfList = vlanIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
