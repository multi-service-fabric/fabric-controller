
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.data.SystemStatus;

@Entity
@Table(name = "system_status")
@NamedQuery(name = "FcSystemStatus.findAll", query = "SELECT f FROM FcSystemStatus f")
public class FcSystemStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "system_id")
  private Integer systemId;

  @Column(name = "blockade_status")
  private Integer blockadeStatus;

  @Column(name = "renewal_status")
  private Integer renewalStatus;

  @Column(name = "service_status")
  private Integer serviceStatus;

  public FcSystemStatus() {
  }

  public FcSystemStatus(SystemStatus systemStatus) {
    setCommonEntity(systemStatus);
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

  public void setCommonEntity(SystemStatus systemStatus) {
    setBlockadeStatus(systemStatus.getBlockadeStatus());
    setServiceStatus(systemStatus.getServiceStatus());
    setSystemId(systemStatus.getSystemId());
    setRenewalStatus(systemStatus.getRenewalStatus());
  }

  public SystemStatus getCommonEntity() {
    SystemStatus systemStatus = new SystemStatus();
    systemStatus.setBlockadeStatus(getBlockadeStatus());
    systemStatus.setServiceStatus(getServiceStatus());
    systemStatus.setSystemId(getSystemId());
    systemStatus.setRenewalStatus(getRenewalStatus());
    return systemStatus;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
