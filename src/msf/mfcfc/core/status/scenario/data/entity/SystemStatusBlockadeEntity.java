
package msf.mfcfc.core.status.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.BlockadeStatus;

public class SystemStatusBlockadeEntity {

  @SerializedName("blockade_status")
  private String blockadeStatus;

  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  public BlockadeStatus getBlockadeStatusEnum() {
    return BlockadeStatus.getEnumFromMessage(blockadeStatus);
  }

  public void setBlockadeStatusEnum(BlockadeStatus blockadeStatusType) {
    this.blockadeStatus = blockadeStatusType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
