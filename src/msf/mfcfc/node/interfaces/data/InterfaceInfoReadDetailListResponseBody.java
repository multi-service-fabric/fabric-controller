
package msf.mfcfc.node.interfaces.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfEntity;
import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;
import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.mfcfc.node.interfaces.physicalifs.data.entity.PhysicalIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class InterfaceInfoReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("physical_ifs")
  private List<PhysicalIfEntity> physicalIfList;

  @SerializedName("breakout_ifs")
  private List<BreakoutIfEntity> breakoutIfList;

  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfEntity> internalLinkIfList;

  @SerializedName("lag_ifs")
  private List<LagIfEntity> lagIfList;

  public List<PhysicalIfEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public List<BreakoutIfEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  public void setBreakoutIfList(List<BreakoutIfEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  public List<InternalLinkIfEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  public void setInternalLinkIfList(List<InternalLinkIfEntity> internalLinkIfList) {
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
