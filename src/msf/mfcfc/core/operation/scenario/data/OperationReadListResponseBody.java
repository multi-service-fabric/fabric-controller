
package msf.mfcfc.core.operation.scenario.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

public class OperationReadListResponseBody extends AbstractResponseBody {

  @SerializedName("operation_ids")
  private List<String> operationIdList;

  public List<String> getOperationIdList() {
    return operationIdList;
  }

  public void setOperationIdList(List<String> operationIdList) {
    this.operationIdList = operationIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
