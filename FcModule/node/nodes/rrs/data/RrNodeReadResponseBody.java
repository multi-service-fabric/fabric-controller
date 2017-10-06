package msf.fc.node.nodes.rrs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.rrs.data.entity.RrEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class RrNodeReadResponseBody extends AbstractResponseBody {

  @SerializedName("rr")
  private RrEntity rr;

  public RrEntity getRr() {
    return rr;
  }

  public void setRr(RrEntity rr) {
    this.rr = rr;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
