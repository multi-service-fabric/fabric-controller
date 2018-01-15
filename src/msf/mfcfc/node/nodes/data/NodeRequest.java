package msf.mfcfc.node.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.core.scenario.RestRequestBase;


public class NodeRequest extends RestRequestBase {

  
  public NodeRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String format, String userType) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.format = format;
    this.userType = userType;
  }

  
  private String clusterId;

  
  private String format;

  
  private String userType;

  
  public String getClusterId() {
    return clusterId;
  }

  
  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  
  public String getFormat() {
    return format;
  }

  
  public void setFormat(String format) {
    this.format = format;
  }

  
  public String getUserType() {
    return userType;
  }

  
  public void setUserType(String userType) {
    this.userType = userType;
  }

  
  public RestFormatOption getFormatEnum() {
    return RestFormatOption.getEnumFromMessage(format);
  }

  
  public void setFormatEnum(RestFormatOption format) {
    this.format = format.getMessage();
  }

  
  public RestUserTypeOption getUserTypeEnum() {
    return RestUserTypeOption.getEnumFromMessage(userType);
  }

  
  public void setUserTypeEnum(RestUserTypeOption userType) {
    this.userType = userType.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
