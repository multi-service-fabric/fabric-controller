
package msf.mfcfc.node.nodes.rrs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.rrs.data.entity.RrNodeRrEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class RrNodeReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("rrs")
  private List<RrNodeRrEntity> rrList;

  public List<RrNodeRrEntity> getRrList() {
    return rrList;
  }

  public void setRrList(List<RrNodeRrEntity> rrList) {
    this.rrList = rrList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
