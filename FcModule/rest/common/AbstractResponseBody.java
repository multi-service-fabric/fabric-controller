package msf.fc.rest.common;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractResponseBody {

  @SerializedName("error_code")
  protected String errorCode;

  @SerializedName("error_message")
  protected String errorMessage;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
