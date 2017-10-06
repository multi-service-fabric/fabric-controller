package msf.fc.rest.ec.node.nodes.spine.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class AddNodeOptionEcEntity {

  @SerializedName("equipment")
  private EmEquipmentEcEntity equipment;

  @SerializedName("node")
  private EmNodeEcEntity node;

  @SerializedName("ifs")
  private IfEcEntity ifs;

  @SerializedName("opposite_nodes")
  private List<OppositeNodeExpansionEcEntity> oppositeNodeList;

  @SerializedName("msdp")
  private MsdpEcEntity msdp;

  @SerializedName("vpn")
  private VpnEcEntity vpn;

  public EmEquipmentEcEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(EmEquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  public EmNodeEcEntity getNode() {
    return node;
  }

  public void setNode(EmNodeEcEntity node) {
    this.node = node;
  }

  public IfEcEntity getIfs() {
    return ifs;
  }

  public void setIfs(IfEcEntity ifs) {
    this.ifs = ifs;
  }

  public List<OppositeNodeExpansionEcEntity> getOppositeNodeList() {
    return oppositeNodeList;
  }

  public void setOppositeNodeList(List<OppositeNodeExpansionEcEntity> oppositeNodeList) {
    this.oppositeNodeList = oppositeNodeList;
  }

  public MsdpEcEntity getMsdp() {
    return msdp;
  }

  public void setMsdp(MsdpEcEntity msdp) {
    this.msdp = msdp;
  }

  public VpnEcEntity getVpn() {
    return vpn;
  }

  public void setVpn(VpnEcEntity vpn) {
    this.vpn = vpn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
