package msf.mfcfc.node.interfaces.edgepoints.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.core.scenario.RestRequestBase;


public class EdgePointRequest extends RestRequestBase {

  
  public EdgePointRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String edgePointId, String format, String userType) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.edgePointId = edgePointId;
    this.format = format;
    this.userType = userType;
  }

  
  private String clusterId;

  
  private String edgePointId;

  
  private String format;

  
  private String userType;

  
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

  
  public String getFormat() {
    return format;
  }

  
  public void setFormat(String format) {
    this.format = format;
  }

  
  public RestUserTypeOption getUserTypeEnum() {
    return RestUserTypeOption.getEnumFromMessage(userType);
  }

  
  public void setUserTypeEnum(RestUserTypeOption userType) {
    this.userType = userType.getMessage();
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
