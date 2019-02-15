
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Common response body for asynchronous operation
 * 
 * @author NTT
 *
 */
public class AsyncOperationResponseBody extends AbstractResponseBody {

  @SerializedName("operation_id")
  private String operationId;

  /**
   * Get the ID for the asynchronous operation information acquisition.
   *
   * @return operationId
   */
  public String getOperationId() {
    return operationId;
  }

  /**
   * Set the ID for the asynchronous operation information acquisition.
   *
   * @param operationId
   *          ID for the asynchronous operation information acquisition.
   */
  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
