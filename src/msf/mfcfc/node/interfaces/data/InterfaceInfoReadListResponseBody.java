package msf.mfcfc.node.interfaces.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class InterfaceInfoReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  
  @SerializedName("breakoutl_if_ids")
  private List<String> breakoutlIfIdList;

  
  @SerializedName("internal_link_if_ids")
  private List<String> internalLinkIfIdList;

  
  @SerializedName("lag_if_ids")
  private List<String> lagIfIdList;

  
  public List<String> getPhysicalIfIdList() {
    return physicalIfIdList;
  }

  
  public void setPhysicalIfIdList(List<String> physicalIfIdList) {
    this.physicalIfIdList = physicalIfIdList;
  }

  
  public List<String> getBreakoutlIfIdList() {
    return breakoutlIfIdList;
  }

  
  public void setBreakoutlIfIdList(List<String> breakoutlIfIdList) {
    this.breakoutlIfIdList = breakoutlIfIdList;
  }

  
  public List<String> getInternalLinkIfIdList() {
    return internalLinkIfIdList;
  }

  
  public void setInternalLinkIfIdList(List<String> internalLinkIfIdList) {
    this.internalLinkIfIdList = internalLinkIfIdList;
  }

  
  public List<String> getLagIfIdList() {
    return lagIfIdList;
  }

  
  public void setLagIfIdList(List<String> lagIfIdList) {
    this.lagIfIdList = lagIfIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}