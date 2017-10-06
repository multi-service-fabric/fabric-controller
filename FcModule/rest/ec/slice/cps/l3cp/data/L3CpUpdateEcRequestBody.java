package msf.fc.rest.ec.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.slice.cps.l3cp.data.entity.UpdateOptionEcEntity;

public class L3CpUpdateEcRequestBody {

  @SerializedName("update_option")
  private UpdateOptionEcEntity updateOption;

  public UpdateOptionEcEntity getUpdateOption() {
    return updateOption;
  }

  public void setUpdateOption(UpdateOptionEcEntity updateOption) {
    this.updateOption = updateOption;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
