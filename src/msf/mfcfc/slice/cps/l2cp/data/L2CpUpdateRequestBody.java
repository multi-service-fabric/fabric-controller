
package msf.mfcfc.slice.cps.l2cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.CpUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpUpdateOptionEntity;

public class L2CpUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(L2CpUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("update_option")
  private L2CpUpdateOptionEntity updateOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public L2CpUpdateOptionEntity getUpdateOption() {
    return updateOption;
  }

  public void setUpdateOption(L2CpUpdateOptionEntity updateOption) {
    this.updateOption = updateOption;
  }

  public CpUpdateAction getActionEnum() {
    return CpUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(CpUpdateAction actionType) {
    this.action = actionType.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      switch (getActionEnum()) {
        case UPDATE:

          ParameterCheckUtil.checkNotNull(updateOption);
          validateUpdateOption();
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("action={0}", getActionEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateUpdateOption() {

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
