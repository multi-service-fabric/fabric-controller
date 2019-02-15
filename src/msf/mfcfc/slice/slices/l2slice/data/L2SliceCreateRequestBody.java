
package msf.mfcfc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.IrbType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

public class L2SliceCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceCreateRequestBody.class);

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("remark_menu")
  private String remarkMenu;

  @SerializedName("vrf_id")
  private Integer vrfId;

  @SerializedName("irb_type")
  private String irbType;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getRemarkMenu() {
    return remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  public Integer getVrfId() {
    return vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public String getIrbType() {
    return irbType;
  }

  public void setIrbType(String irbType) {
    this.irbType = irbType;
  }

  public IrbType getIrbTypeEnum() {
    return IrbType.getEnumFromMessage(irbType);
  }

  public void setIrbTypeEnum(IrbType irbType) {
    this.irbType = irbType.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      if (sliceId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(sliceId);
      }

      if (irbType != null) {
        ParameterCheckUtil.checkNotNull(getIrbTypeEnum());
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
