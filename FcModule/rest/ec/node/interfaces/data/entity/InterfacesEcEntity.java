package msf.fc.rest.ec.node.interfaces.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.internal.data.entity.InternalLinkIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.ReadLagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;

public class InterfacesEcEntity {
  @SerializedName("physical_ifs")
  private List<PhysicalIfEcEntity> physicalIfList;

  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfEcEntity> internalLinkIf;

  @SerializedName("lag_ifs")
  private List<ReadLagIfEcEntity> lagIf;

  public List<PhysicalIfEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public List<InternalLinkIfEcEntity> getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(List<InternalLinkIfEcEntity> internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  public List<ReadLagIfEcEntity> getLagIf() {
    return lagIf;
  }

  public void setLagIf(List<ReadLagIfEcEntity> lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
