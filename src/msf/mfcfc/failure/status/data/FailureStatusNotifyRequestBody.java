package msf.mfcfc.failure.status.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
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


    ParameterCheckUtil.checkNotNullAndLength(tempNode.getClusterId());

    ParameterCheckUtil.checkNotNullAndLength(tempNode.getFabricType());

    ParameterCheckUtil.checkNotNullAndLength(tempNode.getNodeId());

    ParameterCheckUtil.checkNotNull(tempNode.getFailureStatusEnum());
  }

  private void validateIf(FailureStatusIfFailureEntity tempIf) throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(tempIf.getClusterId());

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getFabricType());

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getNodeId());


    ParameterCheckUtil.checkNotNull(tempIf.getIfTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempIf.getIfId());


    ParameterCheckUtil.checkNotNull(tempIf.getFailureStatusEnum());
  }

  private void validateCluster(FailureStatusClusterFailureEntity tempCluster) throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(tempCluster.getClusterId());

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

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
