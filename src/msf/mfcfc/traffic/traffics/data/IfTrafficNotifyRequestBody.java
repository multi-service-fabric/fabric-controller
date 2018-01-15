package msf.mfcfc.traffic.traffics.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficClusterEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficClusterUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficNotifyEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficPhysicalUnitEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficSliceEntity;
import msf.mfcfc.traffic.traffics.data.entity.IfTrafficSliceUnitEntity;


public class IfTrafficNotifyRequestBody implements RestRequestValidator {

  
  private static final MsfLogger logger = MsfLogger.getInstance(IfTrafficNotifyRequestBody.class);

  
  @SerializedName("physical_unit")
  private IfTrafficPhysicalUnitEntity physicalUnit;

  
  @SerializedName("cluster_unit")
  private IfTrafficClusterUnitEntity clusterUnit;

  
  @SerializedName("slice_unit")
  private IfTrafficSliceUnitEntity sliceUnit;

  
  public IfTrafficPhysicalUnitEntity getPhysicalUnit() {
    return physicalUnit;
  }

  
  public void setPhysicalUnit(IfTrafficPhysicalUnitEntity physicalUnit) {
    this.physicalUnit = physicalUnit;
  }

  
  public IfTrafficClusterUnitEntity getClusterUnit() {
    return clusterUnit;
  }

  
  public void setClusterUnit(IfTrafficClusterUnitEntity clusterUnit) {
    this.clusterUnit = clusterUnit;
  }

  
  public IfTrafficSliceUnitEntity getSliceUnit() {
    return sliceUnit;
  }

  
  public void setSliceUnit(IfTrafficSliceUnitEntity sliceUnit) {
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

  private void validateSliceUnit() throws MsfException {


    ParameterCheckUtil.checkNotNull(sliceUnit.getSliceList());
    validateSliceList();
  }

  private void validatePhysicalUnit() throws MsfException {


    ParameterCheckUtil.checkNotNull(physicalUnit.getIfList());
    validateIfList();
  }

  private void validateClusterUnit() throws MsfException {


    ParameterCheckUtil.checkNotNull(clusterUnit.getClusterList());
    validateClusterList();
  }

  private void validateSliceList() throws MsfException {

    for (IfTrafficSliceEntity tempSlice : sliceUnit.getSliceList()) {
      validateSlice(tempSlice);
    }
  }

  private void validateClusterList() throws MsfException {

    for (IfTrafficClusterEntity tempCluster : clusterUnit.getClusterList()) {
      validateCluster(tempCluster);
    }
  }

  private void validateIfList() throws MsfException {

    for (IfTrafficNotifyEntity tempTraffic : physicalUnit.getIfList()) {
      validateIf(tempTraffic);
    }
  }

  private void validateSlice(IfTrafficSliceEntity tempSlice) throws MsfException {


    ParameterCheckUtil.checkNotNull(tempSlice.getSliceTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempSlice.getSliceId());

    ParameterCheckUtil.checkNotNullAndLength(tempSlice.getCpIdList());
  }

  private void validateCluster(IfTrafficClusterEntity tempCluster) throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(tempCluster.getClusterId());

    ParameterCheckUtil.checkNotNull(tempCluster.getClusterTypeEnum());

    switch (tempCluster.getClusterTypeEnum()) {
      case CLUSTER_LINK_IF:
      case EDGE_POINT:

        ParameterCheckUtil.checkNotNullAndLength(tempCluster.getIfId());
        break;
      case INTERNAL:
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("type={0}", tempCluster.getClusterTypeEnum()));
    }
  }

  private void validateIf(IfTrafficNotifyEntity tempTraffic) throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(tempTraffic.getClusterId());

    ParameterCheckUtil.checkNotNullAndLength(tempTraffic.getFabricType());

    ParameterCheckUtil.checkNotNullAndLength(tempTraffic.getNodeId());

    ParameterCheckUtil.checkNotNull(tempTraffic.getIfTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(tempTraffic.getIfId());
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
