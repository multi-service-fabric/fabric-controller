package msf.fc.node.clusters.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SwClusterReadUserDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("sw_clusters")
  private List<SwClusterForUserEntity> swClusterList;

  public List<SwClusterForUserEntity> getSwClusterList() {
    return swClusterList;
  }

  public void setSwClusterList(List<SwClusterForUserEntity> swClusterList) {
    this.swClusterList = swClusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
