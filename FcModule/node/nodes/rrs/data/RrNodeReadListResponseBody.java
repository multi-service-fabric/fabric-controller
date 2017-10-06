package msf.fc.node.nodes.rrs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class RrNodeReadListResponseBody extends AbstractResponseBody {

  @SerializedName("rr_node_ids")
  private List<String> rrNodeIdList;

  public List<String> getRrNodeIdList() {
    return rrNodeIdList;
  }

  public void setRrNodeIdList(List<String> rrNodeIdList) {
    this.rrNodeIdList = rrNodeIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
