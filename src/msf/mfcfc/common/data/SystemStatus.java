
package msf.mfcfc.common.data;

import java.io.Serializable;

import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.RenewalStatusType;
import msf.mfcfc.common.constant.ServiceStatus;

public class SystemStatus implements Serializable {

  private Integer systemId;

  private Integer blockadeStatus;

  private Integer renewalStatus;

  private Integer serviceStatus;

  public SystemStatus() {
  }

  public Integer getSystemId() {
    return this.systemId;
  }

  public void setSystemId(Integer systemId) {
    this.systemId = systemId;
  }

  public Integer getBlockadeStatus() {
    return this.blockadeStatus;
  }

  public void setBlockadeStatus(Integer blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  public BlockadeStatus getBlockadeStatusEnum() {
    return BlockadeStatus.getEnumFromCode(this.blockadeStatus);
  }

  public void setBlockadeStatusEnum(BlockadeStatus blockadeStatus) {
    this.blockadeStatus = blockadeStatus.getCode();
  }

  public Integer getRenewalStatus() {
    return this.renewalStatus;
  }

  public void setRenewalStatus(Integer renewalStatus) {
    this.renewalStatus = renewalStatus;
  }

  public Integer getServiceStatus() {
    return this.serviceStatus;
  }

  public void setServiceStatus(Integer serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public ServiceStatus getServiceStatusEnum() {
    return ServiceStatus.getEnumFromCode(this.serviceStatus);
  }

  public void setServiceStatusEnum(ServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus.getCode();
  }

  public RenewalStatusType getRenewalStatusEnum() {
    return RenewalStatusType.getEnumFromCode(this.renewalStatus);
  }

  public void setRenewalStatusEnum(RenewalStatusType renewalStatus) {
    this.renewalStatus = renewalStatus.getCode();
  }
}
