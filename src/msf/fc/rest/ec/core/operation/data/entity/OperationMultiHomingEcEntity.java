
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationMultiHomingEcEntity {

  @SerializedName("anycast_address")
  private String anycastAddress;

  @SerializedName("if_address")
  private String ifAddress;

  @SerializedName("if_prefix")
  private Integer ifPrefix;

  @SerializedName("backup_address")
  private String backupAddress;

  @SerializedName("peer_address")
  private String peerAddress;

  public String getAnycastAddress() {
    return anycastAddress;
  }

  public void setAnycastAddress(String anycastAddress) {
    this.anycastAddress = anycastAddress;
  }

  public String getIfAddress() {
    return ifAddress;
  }

  public void setIfAddress(String ifAddress) {
    this.ifAddress = ifAddress;
  }

  public Integer getIfPrefix() {
    return ifPrefix;
  }

  public void setIfPrefix(Integer ifPrefix) {
    this.ifPrefix = ifPrefix;
  }

  public String getBackupAddress() {
    return backupAddress;
  }

  public void setBackupAddress(String backupAddress) {
    this.backupAddress = backupAddress;
  }

  public String getPeerAddress() {
    return peerAddress;
  }

  public void setPeerAddress(String peerAddress) {
    this.peerAddress = peerAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
