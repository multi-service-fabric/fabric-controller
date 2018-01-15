package msf.mfcfc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;


public class EdgePointCreateRequestBody implements RestRequestValidator {
  
  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointCreateRequestBody.class);

  
  @SerializedName("leaf_node_id")
  private String leafNodeId;

  
  @SerializedName("lag_if_id")
  private String lagIfId;

  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
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

  
  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  
  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  
  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  
  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();


      ParameterCheckUtil.checkNumericId(leafNodeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);


      if (physicalIfId != null && lagIfId == null && breakoutIfId == null) {

        ParameterCheckUtil.checkIdSpecifiedByUri(physicalIfId);

      } else if (physicalIfId == null && lagIfId != null && breakoutIfId == null) {

        ParameterCheckUtil.checkNumericId(lagIfId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      } else if (physicalIfId == null && lagIfId == null && breakoutIfId != null) {

        ParameterCheckUtil.checkIdSpecifiedByUri(breakoutIfId);

      } else {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "error parameter is physical_if_id " + physicalIfId
            + " or lag_if_id " + lagIfId + " or breakout_if_id " + breakoutIfId);
      }


    } finally {
      logger.methodEnd();
    }
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
