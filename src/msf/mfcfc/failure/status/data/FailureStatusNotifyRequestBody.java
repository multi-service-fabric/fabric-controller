
package msf.mfcfc.failure.status.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusReachableStatusFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceClusterLinkReachableStatusEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class FailureStatusNotifyRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(FailureStatusNotifyRequestBody.class);

  @SerializedName("physical_unit")
  private FailureStatusPhysicalUnitEntity physicalUnit;

  @SerializedName("cluster_unit")
  private FailureStatusClusterUnitEntity clusterUnit;

  @SerializedName("slice_unit")
  private FailureStatusSliceUnitEntity sliceUnit;

  public FailureStatusPhysicalUnitEntity getPhysicalUnit() {
    return physicalUnit;
  }

  public void setPhysicalUnit(FailureStatusPhysicalUnitEntity physicalUnit) {
    this.physicalUnit = physicalUnit;
  }

  public FailureStatusClusterUnitEntity getClusterUnit() {
    return clusterUnit;
  }

  public void setClusterUnit(FailureStatusClusterUnitEntity clusterUnit) {
    this.clusterUnit = clusterUnit;
  }

  public FailureStatusSliceUnitEntity getSliceUnit() {
    return sliceUnit;
  }

  public void setSliceUnit(FailureStatusSliceUnitEntity sliceUnit) {
    this.sliceUnit = sliceUnit;
  }

  @Override
  public void validate() throws MsfException {
    try {

      logger.methodStart();

      if (physicalUnit != null) {
        validatePhysicalUnit();
      }
      if (clusterUnit != null) {
        validateClusterUnit();
      }
      if (sliceUnit != null) {
        validateSliceUnit();
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void validateClusterUnit() throws MsfException {

    ParameterCheckUtil.checkNotNull(clusterUnit.getClusterList());

    for (FailureStatusClusterFailureEntity tempCluster : clusterUnit.getClusterList()) {
      validateCluster(tempCluster);
    }
  }

  private void validatePhysicalUnit() throws MsfException {

    if (physicalUnit.getNodeList() != null) {
      validateNodeList();
    }
    if (physicalUnit.getIfList() != null) {
      validateIfList();
    }
  }

  private void validateNodeList() throws MsfException {
    for (FailureStatusNodeFailureEntity tempNode : physicalUnit.getNodeList()) {
      validateNode(tempNode);
    }
  }

  private void validateIfList() throws MsfException {

    for (FailureStatusIfFailureEntity tempIf : physicalUnit.getIfList()) {
      validateIf(tempIf);
    }
  }

  private void validateNode(FailureStatusNodeFailureEntity tempNode) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempNode.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

    ParameterCheckUtil.checkNotNullAndLength(tempNode.getFabricType());

    ParameterCheckUtil.checkNumericId(tempNode.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNotNull(tempNode.getFailureStatusEnum());
  }

  private void validateIf(FailureStatusIfFailureEntity tempIf) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempIf.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getFabricType());

    ParameterCheckUtil.checkNumericId(tempIf.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNotNull(tempIf.getIfTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getIfId());

    ParameterCheckUtil.checkNotNull(tempIf.getFailureStatusEnum());
  }

  private void validateCluster(FailureStatusClusterFailureEntity tempCluster) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempCluster.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

    ParameterCheckUtil.checkNotNull(tempCluster.getClusterTypeEnum());

    switch (tempCluster.getClusterTypeEnum()) {
      case CLUSTER_LINK_IF:
      case EDGE_POINT:

        ParameterCheckUtil.checkNotNullAndLength(tempCluster.getId());
        break;
      case INTERNAL:
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("type={0}", tempCluster.getClusterTypeEnum()));
    }

    ParameterCheckUtil.checkNotNull(tempCluster.getFailureStatusEnum());
  }

  private void validateSliceUnit() throws MsfException {

    ParameterCheckUtil.checkNotNull(sliceUnit.getSliceList());

    for (FailureStatusSliceFailureEntity tempSlice : sliceUnit.getSliceList()) {
      validateSlice(tempSlice);
    }
    if (sliceUnit.getClusterLink() != null) {
      validateClusterLink(sliceUnit.getClusterLink());
    }
  }

  private void validateSlice(FailureStatusSliceFailureEntity tempSlice) throws MsfException {

    ParameterCheckUtil.checkNotNull(tempSlice.getSliceTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempSlice.getSliceId());

    ParameterCheckUtil.checkNotNull(tempSlice.getCpIdSet());

    ParameterCheckUtil.checkNotNull(tempSlice.getFailureStatusEnum());

    for (FailureStatusReachableStatusFailureEntity tempSliceReachableStatus : tempSlice.getReachableStatusList()) {
      validateSliceReachableStatus(tempSliceReachableStatus);
    }
  }

  private void validateSliceReachableStatus(FailureStatusReachableStatusFailureEntity tempSliceReachableStatus)
      throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(tempSliceReachableStatus.getCpId());

    ParameterCheckUtil.checkNotNull(tempSliceReachableStatus.getOppositeTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempSliceReachableStatus.getOppositeId());

    ParameterCheckUtil.checkNotNull(tempSliceReachableStatus.getReachableStatusEnum());
  }

  private void validateClusterLink(FailureStatusSliceClusterLinkFailureEntity tempClusterLink) throws MsfException {

    ParameterCheckUtil.checkNotNull(tempClusterLink.getReachableStatusList());
    for (FailureStatusSliceClusterLinkReachableStatusEntity tempClusterLinkReachableStatus : tempClusterLink
        .getReachableStatusList()) {
      validateClusterLinkReachableStatus(tempClusterLinkReachableStatus);
    }
  }

  private void validateClusterLinkReachableStatus(
      FailureStatusSliceClusterLinkReachableStatusEntity tempClusterLinkReachableStatus) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempClusterLinkReachableStatus.getClusterLinkIfId(),
        ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNumericId(tempClusterLinkReachableStatus.getOppositeClusterLinkIfId(),
        ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNotNull(tempClusterLinkReachableStatus.getReachableStatusEnum());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
