package msf.fc.slice.cps.l3cp.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.CpUpdateOperationType;

public class UpdateOptionEntity {

  @SerializedName("operation_type")
  private String operationType;

  @SerializedName("static_routes")
  private List<StaticRouteEntity> staticRouteList;

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public List<StaticRouteEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<StaticRouteEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public CpUpdateOperationType getOperationTypeEnum() {
    return CpUpdateOperationType.getEnumFromMessage(operationType);
  }

  public void setOperationTypeEnum(CpUpdateOperationType operationType) {
    this.operationType = operationType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
