package msf.mfcfc.node.interfaces.lagifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class LagIfReadResponseBody extends AbstractResponseBody {

  
  @SerializedName("lag_if")
  private LagIfEntity lagIf;

  
  public LagIfEntity getLagIf() {
    return lagIf;
  }

  
  public void setLagIf(LagIfEntity lagIf) {
    this.lagIf = lagIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
