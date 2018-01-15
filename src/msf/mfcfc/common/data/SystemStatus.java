package msf.mfcfc.common.data;

import java.io.Serializable;

import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ServiceStatus;


public class SystemStatus implements Serializable {

  private Integer systemId;

  private Integer blockadeStatus;

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
}
