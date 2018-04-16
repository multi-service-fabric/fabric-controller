
package msf.mfcfc.node.interfaces.lagifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.lagifs.data.entity.LagIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class LagIfReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("lag_ifs")
  private List<LagIfEntity> lagIfList;

  public List<LagIfEntity> getLagIfList() {
    return lagIfList;
  }

  public void setLagIfList(List<LagIfEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
