package msf.mfcfc.slice.cps.l2cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class L2CpDeleteResponseBody extends AbstractResponseBody {

  
  @SerializedName("operation_id")
  private String operationId;

  
  public String getOperationId() {
    return operationId;
  }

  
  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
