package msf.mfcfc.node.nodes.rrs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.rrs.data.entity.RrNodeRrEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class RrNodeReadOwnerResponseBody extends AbstractResponseBody {

  
  @SerializedName("rr")
  private RrNodeRrEntity rr;

  
  public RrNodeRrEntity getRr() {
    return rr;
  }

  
  public void setRr(RrNodeRrEntity rr) {
    this.rr = rr;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
