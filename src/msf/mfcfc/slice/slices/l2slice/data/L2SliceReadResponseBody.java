
package msf.mfcfc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.slice.slices.l2slice.data.entity.L2SliceEntity;


public class L2SliceReadResponseBody extends AbstractResponseBody {

  
  @SerializedName("l2_slice")
  private L2SliceEntity l2Slice;

  
  public L2SliceEntity getL2Slice() {
    return l2Slice;
  }

  
  public void setL2Slice(L2SliceEntity l2Slice) {
    this.l2Slice = l2Slice;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
