package msf.fc.node.nodes.leafs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.NodeBootStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class InternalLeafNodeRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalLeafNodeRequestBody.class);

  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public NodeBootStatus getStatusEnum() {
    return NodeBootStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(NodeBootStatus status) {
    this.status = status.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {

      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getStatusEnum());

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
