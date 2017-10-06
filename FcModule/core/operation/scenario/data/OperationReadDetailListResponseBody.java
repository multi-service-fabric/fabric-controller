package msf.fc.core.operation.scenario.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.fc.core.operation.scenario.data.entity.AsyncRequestEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class OperationReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("operations")
  private List<AsyncRequestEntity> asyncRequestList;

  public List<AsyncRequestEntity> getAsyncRequestList() {
    return asyncRequestList;
  }

  public void setAsyncRequestList(List<AsyncRequestEntity> asyncRequestList) {
    this.asyncRequestList = asyncRequestList;
  }

  @Override
  public String toString() {
    return "OperationReadDetailListResponseBody [asyncRequestList=" + asyncRequestList + ", errorCode=" + errorCode
        + ", errorMessage=" + errorMessage + "]";
  }

}
