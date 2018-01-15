package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SystemStatusBlockadeEntity {

  
  @SerializedName("blockade_status")
  private String blockadeStatus;

  
  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  
  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
