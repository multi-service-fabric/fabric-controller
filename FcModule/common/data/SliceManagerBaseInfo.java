package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "slice_manager_base_info")
@NamedQuery(name = "SliceManagerBaseInfo.findAll", query = "SELECT s FROM SliceManagerBaseInfo s")
public class SliceManagerBaseInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "base_id")
  private Integer baseId;

  @Column(name = "l2vpn_multicast_address_base")
  private String l2vpnMulticastAddressBase;

  public SliceManagerBaseInfo() {
  }

  public Integer getBaseId() {
    return this.baseId;
  }

  public void setBaseId(Integer baseId) {
    this.baseId = baseId;
  }

  public String getL2vpnMulticastAddressBase() {
    return this.l2vpnMulticastAddressBase;
  }

  public void setL2vpnMulticastAddressBase(String l2vpnMulticastAddressBase) {
    this.l2vpnMulticastAddressBase = l2vpnMulticastAddressBase;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}