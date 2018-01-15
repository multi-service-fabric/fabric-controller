package msf.mfcfc.node.nodes.rrs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class RrNodeReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("rr_node_ids")
  private List<String> rrNodeIdList;

  
  public List<String> getRrNodeIds() {
    return rrNodeIdList;
  }

  
  public void setRrNodeIds(List<String> rrNodeIds) {
    this.rrNodeIdList = rrNodeIds;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
