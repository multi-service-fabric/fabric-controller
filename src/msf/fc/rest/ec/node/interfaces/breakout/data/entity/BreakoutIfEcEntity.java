package msf.fc.rest.ec.node.interfaces.breakout.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class BreakoutIfEcEntity {

  
  @SerializedName("base_physical_if_id")
  private String basePhysicalIfId;

  
  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  
  @SerializedName("if_name")
  private String ifName;

  
  @SerializedName("if_state")
  private String ifState;

  
  @SerializedName("link_speed")
  private String linkSpeed;

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("ipv4_prefix")
  private String ipv4Prefix;

  
  @SerializedName("qos")
  private BreakoutIfQosEcEntity qos;

  
  public String getBasePhysicalIfId() {
    return basePhysicalIfId;
  }

  
  public void setBasePhysicalIfId(String basePhysicalIfId) {
    this.basePhysicalIfId = basePhysicalIfId;
  }

  
  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  
  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  
  public String getIfName() {
    return ifName;
  }

  
  public void setIfName(String ifName) {
    this.ifName = ifName;
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

  
  public BreakoutIfQosEcEntity getQos() {
    return qos;
  }

  
  public void setQos(BreakoutIfQosEcEntity qos) {
    this.qos = qos;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
