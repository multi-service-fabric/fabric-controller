package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.AddressType;

@Embeddable
public class L3CpStaticRouteOptionPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "slice_id", insertable = false, updatable = false)
  private String sliceId;

  @Column(name = "cp_id", insertable = false, updatable = false)
  private String cpId;

  @Column(name = "address_type")
  private Integer addressType;

  @Column(name = "destination_address")
  private String destinationAddress;

  private Integer prefix;

  @Column(name = "nexthop_address")
  private String nexthopAddress;

  public L3CpStaticRouteOptionPK() {
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getCpId() {
    return this.cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public Integer getAddressType() {
    return this.addressType;
  }

  public void setAddressType(Integer addressType) {
    this.addressType = addressType;
  }

  public AddressType getAddressTypeEnum() {
    return AddressType.getEnumFromCode(this.addressType);
  }

  public void setAddressTypeEnum(AddressType addressType) {
    this.addressType = addressType.getCode();
  }

  public String getDestinationAddress() {
    return this.destinationAddress;
  }

  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  public Integer getPrefix() {
    return this.prefix;
  }

  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  public String getNexthopAddress() {
    return this.nexthopAddress;
  }

  public void setNexthopAddress(String nexthopAddress) {
    this.nexthopAddress = nexthopAddress;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof L3CpStaticRouteOptionPK)) {
      return false;
    }
    L3CpStaticRouteOptionPK castOther = (L3CpStaticRouteOptionPK) other;
    return this.sliceId.equals(castOther.sliceId) && this.cpId.equals(castOther.cpId)
        && this.addressType.equals(castOther.addressType)
        && this.destinationAddress.equals(castOther.destinationAddress) && this.prefix.equals(castOther.prefix)
        && this.nexthopAddress.equals(castOther.nexthopAddress);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.sliceId.hashCode();
    hash = hash * prime + this.cpId.hashCode();
    hash = hash * prime + this.addressType.hashCode();
    hash = hash * prime + this.destinationAddress.hashCode();
    hash = hash * prime + this.prefix.hashCode();
    hash = hash * prime + this.nexthopAddress.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}