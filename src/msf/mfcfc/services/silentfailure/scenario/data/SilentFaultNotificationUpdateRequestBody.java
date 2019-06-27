
package msf.mfcfc.services.silentfailure.scenario.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.silentfailure.common.constant.DetectionTriggerType;
import msf.mfcfc.services.silentfailure.scenario.data.entity.FailedInternalLinkListEntity;

public class SilentFaultNotificationUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SilentFaultNotificationUpdateRequestBody.class);

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("detection_trigger")
  private String detectionTrigger;

  @SerializedName("failed_internal_links")
  private List<FailedInternalLinkListEntity> failedInternalLinkList;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getDetectionTrigger() {
    return detectionTrigger;
  }

  public void setDetectionTrigger(String detectionTrigger) {
    this.detectionTrigger = detectionTrigger;
  }

  public List<FailedInternalLinkListEntity> getFailedInternalLinkList() {
    return failedInternalLinkList;
  }

  public void setFailedInternalLinkList(List<FailedInternalLinkListEntity> failedInternalLinkList) {
    this.failedInternalLinkList = failedInternalLinkList;
  }

  public DetectionTriggerType getDetectionTriggerEnum() {
    return DetectionTriggerType.getEnumFromMessage(detectionTrigger);
  }

  public void setDetectionTriggerEnum(DetectionTriggerType detectionTrigger) {
    this.detectionTrigger = detectionTrigger.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
