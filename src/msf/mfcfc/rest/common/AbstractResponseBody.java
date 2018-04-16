
package msf.mfcfc.rest.common;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorResponseDataConsistency;

public abstract class AbstractResponseBody {

  @SerializedName("error_code")
  protected String errorCode;

  @SerializedName("error_message")
  protected String errorMessage;

  @SerializedName("data_consistency")
  protected String dataConsistency;

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

  public String getDataConsistency() {
    return dataConsistency;
  }

  public void setDataConsistency(String dataConsistency) {
    this.dataConsistency = dataConsistency;
  }

  public void setDataConsistencyEnum(ErrorResponseDataConsistency errorResponseDataConsistency) {
    this.dataConsistency = errorResponseDataConsistency.getMessage();
  }
}
