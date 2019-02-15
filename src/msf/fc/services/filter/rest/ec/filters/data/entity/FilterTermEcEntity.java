
package msf.fc.services.filter.rest.ec.filters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FilterTermEcEntity {

  @SerializedName("term_name")
  private String termName;

  @SerializedName("action")
  private String action;

  @SerializedName("direction")
  private String direction;

  @SerializedName("source_mac_address")
  private String sourceMacAddress;

  @SerializedName("dest_mac_address")
  private String destMacAddress;

  @SerializedName("source_ip_address")
  private String sourceIpAddress;

  @SerializedName("dest_ip_address")
  private String destIpAddress;

  @SerializedName("protocol")
  private String protocol;

  @SerializedName("source_port")
  private String sourcePort;

  @SerializedName("dest_port")
  private String destPort;

  public String getTermName() {
    return termName;
  }

  public void setTermName(String termName) {
    this.termName = termName;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getSourceMacAddress() {
    return sourceMacAddress;
  }

  public void setSourceMacAddress(String sourceMacAddress) {
    this.sourceMacAddress = sourceMacAddress;
  }

  public String getDestMacAddress() {
    return destMacAddress;
  }

  public void setDestMacAddress(String destMacAddress) {
    this.destMacAddress = destMacAddress;
  }

  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  public String getDestIpAddress() {
    return destIpAddress;
  }

  public void setDestIpAddress(String destIpAddress) {
    this.destIpAddress = destIpAddress;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getSourcePort() {
    return sourcePort;
  }

  public void setSourcePort(String sourcePort) {
    this.sourcePort = sourcePort;
  }

  public String getDestPort() {
    return destPort;
  }

  public void setDestPort(String destPort) {
    this.destPort = destPort;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
