package msf.fc.node.clusters.data;

import java.util.ArrayList;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class SwClusterReadListResponseBody extends AbstractResponseBody {

  @SerializedName("sw_cluster_ids")
  private ArrayList<String> swClusterIdList;

  public ArrayList<String> getSwClusterIdList() {
    return swClusterIdList;
  }

  public void setSwClusterIdList(ArrayList<String> swClusterIdList) {
    this.swClusterIdList = swClusterIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
