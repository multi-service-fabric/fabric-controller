package msf.fc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class L2SliceCreateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceCreateRequestBody.class);
  @SerializedName("slice_id")
  private String sliceId;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      if (sliceId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(sliceId);
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
