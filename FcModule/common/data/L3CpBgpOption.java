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
@Table(name = "l3_cp_bgp_options")
@NamedQuery(name = "L3CpBgpOption.findAll", query = "SELECT l FROM L3CpBgpOption l")
public class L3CpBgpOption implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L3CpBgpOptionPK id;

  @Column(name = "neighbor_as")
  private Integer neighborAs;

  @Column(name = "neighbor_ipv4_address")
  private String neighborIpv4Address;

  @Column(name = "neighbor_ipv6_address")
  private String neighborIpv6Address;

  private Integer role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "cp_id", referencedColumnName = "cp_id", insertable = false, updatable = false),
      @JoinColumn(name = "slice_id", referencedColumnName = "slice_id", insertable = false, updatable = false) })
  private L3Cp l3Cp;

  public L3CpBgpOption() {
  }

  public L3CpBgpOptionPK getId() {
    return this.id;
  }

  public void setId(L3CpBgpOptionPK id) {
    this.id = id;
  }

  public Integer getNeighborAs() {
    return this.neighborAs;
  }

  public void setNeighborAs(Integer neighborAs) {
    this.neighborAs = neighborAs;
  }

  public String getNeighborIpv4Address() {
    return this.neighborIpv4Address;
  }

  public void setNeighborIpv4Address(String neighborIpv4Address) {
    this.neighborIpv4Address = neighborIpv4Address;
  }

  public String getNeighborIpv6Address() {
    return this.neighborIpv6Address;
  }

  public void setNeighborIpv6Address(String neighborIpv6Address) {
    this.neighborIpv6Address = neighborIpv6Address;
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