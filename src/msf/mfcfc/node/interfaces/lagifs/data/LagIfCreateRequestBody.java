
package msf.mfcfc.node.interfaces.lagifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

public class LagIfCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LagIfCreateRequestBody.class);

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;

  public List<String> getPhysicalIfIdList() {
    return physicalIfIdList;
  }

  public void setPhysicalIfIdList(List<String> physicalIfIdList) {
    this.physicalIfIdList = physicalIfIdList;
  }

  public List<String> getBreakoutIfIdList() {
    return breakoutIfIdList;
  }

  public void setBreakoutIfIdList(List<String> breakoutIfIdList) {
    this.breakoutIfIdList = breakoutIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      if (physicalIfIdList.isEmpty()) {

        ParameterCheckUtil.checkNotNullAndLength(breakoutIfIdList);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
