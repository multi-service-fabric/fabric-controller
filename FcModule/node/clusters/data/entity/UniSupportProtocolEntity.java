package msf.fc.node.clusters.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.L3ProtocolType;

public class UniSupportProtocolEntity {
  @SerializedName("L2")
  private Boolean l2;
  @SerializedName("L3")
  private Boolean l3;

  @SerializedName("L3_protocols")
  private List<String> l3ProtocolList;

  public Boolean isL2() {
    return l2;
  }

  public void setL2(Boolean l2) {
    this.l2 = l2;
  }

  public Boolean isL3() {
    return l3;
  }

  public void setL3(Boolean l3) {
    this.l3 = l3;
  }

  public List<String> getL3ProtocolList() {
    return l3ProtocolList;
  }

  public void setL3ProtocolList(List<String> l3ProtocolList) {
    this.l3ProtocolList = l3ProtocolList;
  }

  public List<L3ProtocolType> getL3ProtocolEnumList() {
    List<L3ProtocolType> enumList = new ArrayList<L3ProtocolType>();
    for (String protocol : l3ProtocolList) {
      L3ProtocolType protocolEnum = L3ProtocolType.getEnumFromMessage(protocol);
      if (protocolEnum != null) {
        enumList.add(protocolEnum);
      }
    }
    return enumList;
  }

  public void setL3ProtocolEnumList(L3ProtocolType... enums) {
    List<String> newSupportProtocolList = new ArrayList<String>();
    for (L3ProtocolType protocolEnum : enums) {
      newSupportProtocolList.add(protocolEnum.getMessage());
    }
    this.l3ProtocolList = newSupportProtocolList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
