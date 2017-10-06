package msf.fc.failure.logicalif.data;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusReadRequestData;
import msf.fc.rest.common.RestRequestValidator;

public class LogicalIfStatusReadRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(LogicalIfStatusReadRequestBody.class);

  @SerializedName("get_logical_if_status_option")
  private LogicalIfStatusReadRequestData readOptionData;

  @Override
  public void validate() throws MsfException {
    logger.methodStart();

    try {
      ParameterCheckUtil.checkNotNull(readOptionData);

      ParameterCheckUtil.checkNotNullAndLength(readOptionData.getClusterId());
    } finally {
      logger.methodEnd();
    }
  }

  public LogicalIfStatusReadRequestData getReadOptionData() {
    return readOptionData;
  }

  public void setReadOptionData(LogicalIfStatusReadRequestData readOptionData) {
    this.readOptionData = readOptionData;
  }

}
