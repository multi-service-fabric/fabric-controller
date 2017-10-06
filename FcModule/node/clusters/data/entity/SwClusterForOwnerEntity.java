package msf.fc.node.clusters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SwClusterForOwnerEntity {

  @SerializedName("sw_cluster_id")
  private String swClusterId;

  @SerializedName("max_leaf_num")
  private Integer maxLeafNum;

  @SerializedName("max_spine_num")
  private Integer maxSpineNum;

  @SerializedName("ec_control_address")
  private String ecControlAddress;
  @SerializedName("ec_control_port")
  private Integer ecControlPort;
  @SerializedName("as_num")
  private Integer asNum;

  @SerializedName("edge_points")
  private EdgePointEntity edgePoint;
  @SerializedName("uni_support_protocols")
  private UniSupportProtocolEntity uniSupportProtocol;

  @SerializedName("address_definitions")
  private AddressDefinitionEntity addressDefinition;

  public String getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getMaxLeafNum() {
    return maxLeafNum;
  }

  public void setMaxLeafNum(Integer maxLeafNum) {
    this.maxLeafNum = maxLeafNum;
  }

  public Integer getMaxSpineNum() {
    return maxSpineNum;
  }

  public void setMaxSpineNum(Integer maxSpineNum) {
    this.maxSpineNum = maxSpineNum;
  }

  public String getEcControlAddress() {
    return ecControlAddress;
  }

  public void setEcControlAddress(String ecControlAddress) {
    this.ecControlAddress = ecControlAddress;
  }

  public Integer getEcControlPort() {
    return ecControlPort;
  }

  public void setEcControlPort(Integer ecControlPort) {
    this.ecControlPort = ecControlPort;
  }

  public Integer getAsNum() {
    return asNum;
  }

  public void setAsNum(Integer asNum) {
    this.asNum = asNum;
  }

  public EdgePointEntity getEdgePoint() {
    return edgePoint;
  }

  public void setEdgePoint(EdgePointEntity edgePoint) {
    this.edgePoint = edgePoint;
  }

  public UniSupportProtocolEntity getUniSupportProtocol() {
    return uniSupportProtocol;
  }

  public void setUniSupportProtocol(UniSupportProtocolEntity uniSupportProtocol) {
    this.uniSupportProtocol = uniSupportProtocol;
  }

  public AddressDefinitionEntity getAddressDefinition() {
    return addressDefinition;
  }

  public void setAddressDefinition(AddressDefinitionEntity addressDefinition) {
    this.addressDefinition = addressDefinition;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
