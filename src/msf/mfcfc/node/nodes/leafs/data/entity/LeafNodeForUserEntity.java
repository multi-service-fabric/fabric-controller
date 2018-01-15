package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LeafNodeForUserEntity {
  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  
  @SerializedName("vpn_type")
  private String vpnType;

  
  @SerializedName("plane")
  private Integer plane;

  
  @SerializedName("physical_ifs")
  private List<LeafNodePhysicalIfForUserEntity> physicalIfList;

  
  @SerializedName("breakout_ifs")
  private List<LeafNodeBreakoutIfForUserEntity> breakoutIfList;

  
  @SerializedName("lag_ifs")
  private List<LeafNodeLagIfForUserEntity> lagIfList;

  
  @SerializedName("provisioning_status")
  private String provisioningStatus;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  
  public String getVpnType() {
    return vpnType;
  }

  
  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  
  public Integer getPlane() {
    return plane;
  }

  
  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  
  public List<LeafNodePhysicalIfForUserEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  
  public void setPhysicalIfList(List<LeafNodePhysicalIfForUserEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  
  public List<LeafNodeBreakoutIfForUserEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  
  public void setBreakoutIfList(List<LeafNodeBreakoutIfForUserEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  
  public List<LeafNodeLagIfForUserEntity> getLagIfList() {
    return lagIfList;
  }

  
  public void setLagIfList(List<LeafNodeLagIfForUserEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  
  public String getProvisioningStatus() {
    return provisioningStatus;
  }

  
  public void setProvisioningStatus(String provisioningStatus) {
    this.provisioningStatus = provisioningStatus;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
