package msf.fc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class EdgePointCreateRequestBody implements RestRequestValidator {

  @SerializedName("leaf_node_id")
  private String leafNodeId;

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("physical_if_id")
  private String physicalIfId;

  public String getLeafNodeId() {
    return leafNodeId;
  }

  public void setLeafNodeId(String leafNodeId) {
    this.leafNodeId = leafNodeId;
  }

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  @Override
  public void validate() throws MsfException {

    ParameterCheckUtil.checkNumericId(leafNodeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    if ((physicalIfId == null && lagIfId == null) || (physicalIfId != null && lagIfId != null)) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
          "error parameter is physical_if_id" + physicalIfId + "or lag_if_id" + lagIfId);
    } else {
      if (physicalIfId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(physicalIfId);
      }
      if (lagIfId != null) {
        ParameterCheckUtil.checkNumericId(lagIfId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      }
    }

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
