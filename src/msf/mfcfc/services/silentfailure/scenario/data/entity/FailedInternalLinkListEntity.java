
package msf.mfcfc.services.silentfailure.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailedInternalLinkListEntity {

  @SerializedName("node_options")
  private List<NodeOptionListEntity> nodeOptionList;

  public List<NodeOptionListEntity> getNodeOptionList() {
    return nodeOptionList;
  }

  public void setNodeOptionList(List<NodeOptionListEntity> nodeOptionList) {
    this.nodeOptionList = nodeOptionList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
