package msf.fc.traffic.history.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.traffic.history.data.entity.SliceTrafficEntity;

public class TrafficInfoReadResponseBody extends AbstractResponseBody {

  @SerializedName("slice_traffic")
  private List<SliceTrafficEntity> sliceTrafficList;

  public List<SliceTrafficEntity> getSliceTrafficList() {
    return sliceTrafficList;
  }

  public void setSliceTrafficList(List<SliceTrafficEntity> sliceTrafficList) {
    this.sliceTrafficList = sliceTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
