package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class DeleteL2CpsOptionEcEntity {
  @SerializedName("cps")
  private List<DeleteL2CpEcEntity> cpList;
  @SerializedName("slice_id")
  private String sliceId;

  public List<DeleteL2CpEcEntity> getCpList() {
    return cpList;
  }

  public void setCpList(List<DeleteL2CpEcEntity> cpList) {
    this.cpList = cpList;
  }

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
