
package msf.fc.rest.ec.node.interfaces.physical.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfEcEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("if_name")
  private String ifName;

  @SerializedName("breakout_if")
  private List<PhysicalIfBreakoutIfEcEntity> breakoutIfList;

  @SerializedName("if_state")
  private String ifState;

  @SerializedName("link_speed")
  private String linkSpeed;

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("ipv4_prefix")
  private String ipv4Prefix;

  @SerializedName("qos")
  private PhysicalIfQosEcEntity qos;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public String getIfName() {
    return ifName;
  }

  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  public List<PhysicalIfBreakoutIfEcEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  public void setBreakoutIfList(List<PhysicalIfBreakoutIfEcEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  public String getIfState() {
    return ifState;
  }

  public void setIfState(String ifState) {
    this.ifState = ifState;
  }

  public String getLinkSpeed() {
    return linkSpeed;
  }

  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  public void setIpv4Prefix(String ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  public PhysicalIfQosEcEntity getQos() {
    return qos;
  }

  public void setQos(PhysicalIfQosEcEntity qos) {
    this.qos = qos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
