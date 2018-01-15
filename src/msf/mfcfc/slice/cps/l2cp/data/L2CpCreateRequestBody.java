
package msf.mfcfc.slice.cps.l2cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PortMode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;


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

  
  @SerializedName("pair_cp_id")
  private String pairCpId;

  
  @SerializedName("qos")
  private L2CpQosCreateEntity qos;

  
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

  
  public String getPairCpId() {
    return pairCpId;
  }

  
  public void setPairCpId(String pairCpId) {
    this.pairCpId = pairCpId;
  }

  
  public L2CpQosCreateEntity getQos() {
    return qos;
  }

  
  public void setQos(L2CpQosCreateEntity qos) {
    this.qos = qos;
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


      ParameterCheckUtil.checkNotNull(getPortModeEnum());
    } finally {
      logger.methodEnd();
    }

  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
