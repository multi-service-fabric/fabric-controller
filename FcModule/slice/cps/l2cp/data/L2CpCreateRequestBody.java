package msf.fc.slice.cps.l2cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.PortMode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;

public class L2CpCreateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpCreateRequestBody.class);
  @SerializedName("cluster_id")
  private String clusterId;
  @SerializedName("edge_point_id")
  private String edgePointId;
  @SerializedName("vlan_id")
  private Integer vlanId;
  @SerializedName("cp_id")
  private String cpId;
  @SerializedName("port_mode")
  private String portMode;

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

  public Integer getVlanId() {
    return vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromMessage(portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      ParameterCheckUtil.checkNotNullAndLength(clusterId);
      ParameterCheckUtil.checkNotNullAndLength(edgePointId);
      ParameterCheckUtil.checkNotNull(vlanId);
      ParameterCheckUtil.checkNumberRange(vlanId, 1, 4096);
      if (cpId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(cpId);
      }
      if (getPortModeEnum() == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "portMode", portMode);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
