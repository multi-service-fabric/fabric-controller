
package msf.mfc.common.data;

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
@NamedQuery(name = "MfcSystemStatus.findAll", query = "SELECT m FROM MfcSystemStatus m")
public class MfcSystemStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "system_id")
  private Integer systemId;

  @Column(name = "blockade_status")
  private Integer blockadeStatus;

  @Column(name = "service_status")
  private Integer serviceStatus;

  public MfcSystemStatus() {
  }

  public MfcSystemStatus(SystemStatus systemStatus) {
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
  }

  public SystemStatus getCommonEntity() {
    SystemStatus systemStatus = new SystemStatus();
    systemStatus.setBlockadeStatus(getBlockadeStatus());
    systemStatus.setServiceStatus(getServiceStatus());
    systemStatus.setSystemId(getSystemId());
    return systemStatus;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
