
package msf.mfcfc.slice.slices.l3slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PlaneBelongsTo;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;

public class L3SliceCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceCreateRequestBody.class);

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("remark_menu")
  private String remarkMenu;

  @SerializedName("vrf_id")
  private Integer vrfId;

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

      ParameterCheckUtil.checkNotNull(plane);

      ParameterCheckUtil.checkNotNull(getPlaneEnum());
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
