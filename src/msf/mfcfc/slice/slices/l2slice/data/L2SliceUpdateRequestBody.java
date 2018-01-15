
package msf.mfcfc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;


public class L2SliceUpdateRequestBody implements RestRequestValidator {

  
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceCreateRequestBody.class);

  
  @SerializedName("action")
  private String action;

  
  @SerializedName("remark_menu")
  private String remarkMenu;

  
  public String getAction() {
    return action;
  }

  
  public void setAction(String action) {
    this.action = action;
  }

  
  public String getRemarkMenu() {
    return remarkMenu;
  }

  
  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
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
