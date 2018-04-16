
package msf.mfcfc.rest.common;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractInternalResponseBody {

  @SerializedName("error_code")
  private String errorCode;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

}
