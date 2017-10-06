package msf.fc.rest.ec.core.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class DeleteL3CpsOptionEcEntity {
  @SerializedName("cps")
  private List<DeleteL3CpEcEntity> cpList;
  @SerializedName("slice_id")
  private String sliceId;

  public List<DeleteL3CpEcEntity> getCpList() {
    return cpList;
  }

  public void setCpList(List<DeleteL3CpEcEntity> cpList) {
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
