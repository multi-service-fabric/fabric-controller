package msf.fc.node.interfaces.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;
import msf.fc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.fc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class InterfaceInfoReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("physical_ifs")
  private List<PhysicalIfEntity> physicalIfList;

  @SerializedName("internal_link_ifs")
  private List<InternalIfEntity> internalLinkIfList;

  @SerializedName("lag_ifs")
  private List<LagIfEntity> lagIfList;

  public List<PhysicalIfEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public List<InternalIfEntity> getInternalIfList() {
    return internalLinkIfList;
  }

  public void setInternalIfList(List<InternalIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  public List<LagIfEntity> getLagIfList() {
    return lagIfList;
  }

  public void setLagIfList(List<LagIfEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
