
package msf.mfcfc.slice.cps.l3cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.CpUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpUpdateOptionEntity;

public class L3CpUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("update_option")
  private L3CpUpdateOptionEntity updateOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public L3CpUpdateOptionEntity getUpdateOption() {
    return updateOption;
  }

  public void setUpdateOption(L3CpUpdateOptionEntity updateOption) {
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

  private void validateUpdateOption() throws MsfException {

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
