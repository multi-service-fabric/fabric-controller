package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.BlockadeStatus;
import msf.fc.common.constant.ServiceStatus;

@Entity
@Table(name = "system_status")
@NamedQuery(name = "SystemStatus.findAll", query = "SELECT s FROM SystemStatus s")
public class SystemStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "system_id")
  private Integer systemId;

  @Column(name = "blockade_status")
  private Integer blockadeStatus;

  @Column(name = "service_status")
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}