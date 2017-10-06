package msf.fc.core.operation.scenario.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class OperationReadListResponseBody extends AbstractResponseBody {

  @SerializedName("operation_ids")
  private List<String> asyncRequestIdList;

  public List<String> getAsyncRequestIdList() {
    return asyncRequestIdList;
  }

  public void setAsyncRequestIdList(List<String> asyncRequestIdList) {
    this.asyncRequestIdList = asyncRequestIdList;
  }

  @Override
  public String toString() {
    return "OperationReadListResponseBody [asyncRequestIdList=" + asyncRequestIdList + ", errorCode=" + errorCode
        + ", errorMessage=" + errorMessage + "]";
  }

}
