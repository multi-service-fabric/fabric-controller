
package msf.mfcfc.node.interfaces.ifmaintenance.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.BlockadeStatusType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

public class InterfaceChangeStateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(InterfaceChangeStateRequestBody.class);

  @SerializedName("blockade_status")
  private String blockadeStatus;

  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  public BlockadeStatusType getBlockadeStatusEnum() {
    return BlockadeStatusType.getEnumFromMessage(blockadeStatus);
  }

  public void setBlockadeStatusEnum(BlockadeStatusType blockadeStatus) {
    this.blockadeStatus = blockadeStatus.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNullAndLength(blockadeStatus);
      ParameterCheckUtil.checkNotNull(getBlockadeStatusEnum());
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
