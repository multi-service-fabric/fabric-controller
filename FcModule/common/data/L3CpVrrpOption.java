package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.RoleType;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l3_cp_vrrp_options")
@NamedQuery(name = "L3CpVrrpOption.findAll", query = "SELECT l FROM L3CpVrrpOption l")
public class L3CpVrrpOption implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L3CpVrrpOptionPK id;

  @Column(name = "group_id")
  private Integer groupId;

  private Integer role;

  @Column(name = "virtual_ipv4_address")
  private String virtualIpv4Address;

  @Column(name = "virtual_ipv6_address")
  private String virtualIpv6Address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "cp_id", referencedColumnName = "cp_id", insertable = false, updatable = false),
      @JoinColumn(name = "slice_id", referencedColumnName = "slice_id", insertable = false, updatable = false) })
  private L3Cp l3Cp;

  public L3CpVrrpOption() {
  }

  public L3CpVrrpOptionPK getId() {
    return this.id;
  }

  public void setId(L3CpVrrpOptionPK id) {
    this.id = id;
  }

  public Integer getGroupId() {
    return this.groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public Integer getRole() {
    return this.role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public RoleType getRoleEnum() {
    return RoleType.getEnumFromCode(this.role);
  }

  public void setRoleEnum(RoleType role) {
    this.role = role.getCode();
  }

  public String getVirtualIpv4Address() {
    return this.virtualIpv4Address;
  }

  public void setVirtualIpv4Address(String virtualIpv4Address) {
    this.virtualIpv4Address = virtualIpv4Address;
  }

  public String getVirtualIpv6Address() {
    return this.virtualIpv6Address;
  }

  public void setVirtualIpv6Address(String virtualIpv6Address) {
    this.virtualIpv6Address = virtualIpv6Address;
  }

  public L3Cp getL3Cp() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cp);
  }

  public void setL3Cp(L3Cp l3Cp) {
    this.l3Cp = l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "l3Cp" })
        .toString();
  }

}