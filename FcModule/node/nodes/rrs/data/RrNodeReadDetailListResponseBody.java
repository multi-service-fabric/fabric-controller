package msf.fc.node.nodes.rrs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.rrs.data.entity.RrEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class RrNodeReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("rrs")
  private List<RrEntity> rrList;

  public List<RrEntity> getRrList() {
    return rrList;
  }

  public void setRrList(List<RrEntity> rrList) {
    this.rrList = rrList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
