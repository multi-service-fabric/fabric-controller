
package msf.mfcfc.node.interfaces.lagifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.LagIfUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

public class LagIfUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LagIfUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

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

  public LagIfUpdateAction getActionEnum() {
    return LagIfUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(LagIfUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      if (breakoutIfIdList == null) {

        ParameterCheckUtil.checkNotNull(physicalIfIdList);
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
