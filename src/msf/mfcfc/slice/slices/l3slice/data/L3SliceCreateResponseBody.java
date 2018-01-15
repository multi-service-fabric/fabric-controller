
package msf.mfcfc.slice.slices.l3slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class L3SliceCreateResponseBody extends AbstractResponseBody {

  
  @SerializedName("slice_id")
  private String sliceId;

  
  public String getSliceId() {
    return sliceId;
  }

  
  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
