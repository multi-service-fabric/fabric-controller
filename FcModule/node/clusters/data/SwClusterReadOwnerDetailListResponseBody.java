package msf.fc.node.clusters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SwClusterReadOwnerDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("sw_clusters")
  private List<SwClusterForOwnerEntity> swClusterList;

  public List<SwClusterForOwnerEntity> getSwClusterList() {
    return swClusterList;
  }

  public void setSwClusterList(List<SwClusterForOwnerEntity> swClusterList) {
    this.swClusterList = swClusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
