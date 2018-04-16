
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureStatusSliceUnitEntity implements Serializable {

  @SerializedName("slices")
  private List<FailureStatusSliceFailureEntity> sliceList;

  @SerializedName("cluster_link")
  private FailureStatusSliceClusterLinkFailureEntity clusterLink;

  public List<FailureStatusSliceFailureEntity> getSliceList() {
    return sliceList;
  }

  public void setSliceList(List<FailureStatusSliceFailureEntity> sliceList) {
    this.sliceList = sliceList;
  }

  public FailureStatusSliceClusterLinkFailureEntity getClusterLink() {
    return clusterLink;
  }

  public void setClusterLink(FailureStatusSliceClusterLinkFailureEntity clusterLink) {
    this.clusterLink = clusterLink;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
