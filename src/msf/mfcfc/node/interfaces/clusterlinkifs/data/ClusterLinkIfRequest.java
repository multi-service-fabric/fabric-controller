
package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class ClusterLinkIfRequest extends RestRequestBase {

  public ClusterLinkIfRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String clusterLinkIfId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.clusterLinkIfId = clusterLinkIfId;
    this.format = format;
  }

  private String clusterId;

  private String clusterLinkIfId;

  private String format;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getClusterLinkIfId() {
    return clusterLinkIfId;
  }

  public void setClusterLinkIfId(String clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
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
