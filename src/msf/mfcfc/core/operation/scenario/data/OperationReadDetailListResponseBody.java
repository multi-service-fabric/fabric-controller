package msf.mfcfc.core.operation.scenario.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.core.operation.scenario.data.entity.OperationDetailEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class OperationReadDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("operations")
  private List<OperationDetailEntity> operationList;

  
  public List<OperationDetailEntity> getOperationList() {
    return operationList;
  }

  
  public void setOperationList(List<OperationDetailEntity> operationList) {
    this.operationList = operationList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
