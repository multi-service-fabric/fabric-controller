package msf.fc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.RestUserTypeOption;
import msf.fc.core.scenario.RestRequestBase;

public class EdgePointRequest extends RestRequestBase {

  private String clusterId;
  private String edgePointId;
  private String userType;
  private String format;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public RestUserTypeOption getUserTypeEnum() {
    return RestUserTypeOption.getEnumFromMessage(userType);
  }


  public void setUserTypeEnum(RestUserTypeOption userType) {
    this.userType = userType.getMessage();
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public RestFormatOption getFormatEnum() {
    return RestFormatOption.getEnumFromMessage(format);
  }

  public void setFormatEnum(RestFormatOption format) {
    this.format = format.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
