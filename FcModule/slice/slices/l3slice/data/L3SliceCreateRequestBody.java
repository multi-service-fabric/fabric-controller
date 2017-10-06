package msf.fc.slice.slices.l3slice.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.PlaneBelongsTo;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class L3SliceCreateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceCreateRequestBody.class);
  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("plane")
  private Integer plane;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public PlaneBelongsTo getPlaneEnum() {
    return PlaneBelongsTo.getEnumFromMessage(plane);
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      if (sliceId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(sliceId);
      }
      if (getPlaneEnum() == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "plane", plane);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
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
