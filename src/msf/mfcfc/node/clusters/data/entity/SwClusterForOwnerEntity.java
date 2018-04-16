
package msf.mfcfc.node.clusters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SwClusterForOwnerEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("max_leaf_num")
  private Integer maxLeafNum;

  @SerializedName("max_spine_num")
  private Integer maxSpineNum;

  @SerializedName("ec_control_address")
  private String ecControlAddress;

  @SerializedName("ec_control_port")
  private Integer ecControlPort;

  @SerializedName("as_number")
  private Integer asNumber;

  @SerializedName("edge_points")
  private SwClusterEdgePointEntity edgePoints;

  @SerializedName("uni_support_protocols")
  private SwClusterUniSupportProtocolEntity uniSupportProtocols;

  @SerializedName("address_definitions")
  private SwClusterAddressDefinitionEntity addressDefinitions;

  @SerializedName("rrs")
  private SwClusterRrEntity rrs;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
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

  public Integer getAsNumber() {
    return asNumber;
  }

  public void setAsNumber(Integer asNumber) {
    this.asNumber = asNumber;
  }

  public SwClusterEdgePointEntity getEdgePoints() {
    return edgePoints;
  }

  public void setEdgePoints(SwClusterEdgePointEntity edgePoints) {
    this.edgePoints = edgePoints;
  }

  public SwClusterUniSupportProtocolEntity getUniSupportProtocols() {
    return uniSupportProtocols;
  }

  public void setUniSupportProtocols(SwClusterUniSupportProtocolEntity uniSupportProtocols) {
    this.uniSupportProtocols = uniSupportProtocols;
  }

  public SwClusterAddressDefinitionEntity getAddressDefinitions() {
    return addressDefinitions;
  }

  public void setAddressDefinitions(SwClusterAddressDefinitionEntity addressDefinitions) {
    this.addressDefinitions = addressDefinitions;
  }

  public SwClusterRrEntity getRrs() {
    return rrs;
  }

  public void setRrs(SwClusterRrEntity rrs) {
    this.rrs = rrs;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
