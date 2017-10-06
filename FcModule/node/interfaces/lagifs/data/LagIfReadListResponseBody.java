package msf.fc.node.interfaces.lagifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class LagIfReadListResponseBody extends AbstractResponseBody {

  @SerializedName("lag_if_ids")
  private List<String> lagIfIdList;

  public List<String> getLagIfIdList() {
    return lagIfIdList;
  }

  public void setLagIfIdList(List<String> lagIfIdList) {
    this.lagIfIdList = lagIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
