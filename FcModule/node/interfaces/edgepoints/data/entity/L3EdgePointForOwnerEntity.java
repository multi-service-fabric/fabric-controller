package msf.fc.node.interfaces.edgepoints.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.L3ProtocolType;

public class L3EdgePointForOwnerEntity {

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("base_if")
  private BaseIfEntity baseIf;

  @SerializedName("support_protocols")
  private List<String> supportProtocolList;

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public BaseIfEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(BaseIfEntity baseIf) {
    this.baseIf = baseIf;
  }

  public List<String> getSupportProtocolList() {
    return supportProtocolList;
  }

  public void setSupportProtocolList(List<String> supportProtocolList) {
    this.supportProtocolList = supportProtocolList;
  }

  public List<L3ProtocolType> getSupportProtocolEnumList() {
    List<L3ProtocolType> enumList = new ArrayList<L3ProtocolType>();
    for (String protocol : supportProtocolList) {
      L3ProtocolType protocolEnum = L3ProtocolType.getEnumFromMessage(protocol);
      if (protocolEnum != null) {
        enumList.add(protocolEnum);
      }
    }
    return enumList;
  }

  public void setSupportProtocolEnumList(L3ProtocolType... enums) {
    List<String> newSupportProtocolList = new ArrayList<String>();
    for (L3ProtocolType protocolEnum : enums) {
      newSupportProtocolList.add(protocolEnum.getMessage());
    }
    this.supportProtocolList = newSupportProtocolList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
