package msf.fc.slice.cps.l3cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.slice.cps.l3cp.data.entity.L3CpEntity;

public class L3CpReadDetailListResponseBody extends AbstractResponseBody {
  @SerializedName("l3_cps")
  private List<L3CpEntity> l3CpList;

  public List<L3CpEntity> getL3CpList() {
    return l3CpList;
  }

  public void setL3CpList(List<L3CpEntity> l3CpList) {
    this.l3CpList = l3CpList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
