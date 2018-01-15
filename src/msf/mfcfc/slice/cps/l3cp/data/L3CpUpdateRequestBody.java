
package msf.mfcfc.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
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

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

    } finally {
      logger.methodEnd();
    }

  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
