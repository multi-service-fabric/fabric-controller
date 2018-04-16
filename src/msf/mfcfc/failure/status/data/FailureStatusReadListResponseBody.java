
package msf.mfcfc.failure.status.data;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.failure.status.data.entity.FailureStatusClusterUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusPhysicalUnitEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class FailureStatusReadListResponseBody extends AbstractResponseBody implements Serializable {

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
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
