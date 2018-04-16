
package msf.mfcfc.slice.slices.l3slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.entity.L3SliceEntity;

public class L3SliceReadResponseBody extends AbstractResponseBody {

  @SerializedName("l3_slice")
  private L3SliceEntity l3Slice;

  public L3SliceEntity getL3Slice() {
    return l3Slice;
  }

  public void setL3Slice(L3SliceEntity l3Slice) {
    this.l3Slice = l3Slice;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
