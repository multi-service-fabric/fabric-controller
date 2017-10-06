package msf.fc.node.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.RestFormatOption;
import msf.fc.core.scenario.RestRequestBase;

public class NodeRequest extends RestRequestBase {

  String clusterId;
  String format;

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
