package msf.fc.failure.logicalif.data;

import com.google.gson.annotations.SerializedName;

import msf.fc.failure.logicalif.data.entity.LogicalIfStatusData;
import msf.fc.rest.common.AbstractResponseBody;

public class LogicalIfStatusReadResponseBody extends AbstractResponseBody {
  @SerializedName("get_logical_if_status")
  private LogicalIfStatusData readResponse;

  public LogicalIfStatusData getReadResponse() {
    return readResponse;
  }

  public void setReadResponse(LogicalIfStatusData readResponse) {
    this.readResponse = readResponse;
  }

}
