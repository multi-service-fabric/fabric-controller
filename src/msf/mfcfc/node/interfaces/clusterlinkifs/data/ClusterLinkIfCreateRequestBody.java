
package msf.mfcfc.node.interfaces.clusterlinkifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ClusterLinkIfPortStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLagLinkCreateEntity;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfPhysicalLinkCreateEntity;
import msf.mfcfc.rest.common.RestRequestValidator;


public class ClusterLinkIfCreateRequestBody implements RestRequestValidator {
  
  private static final MsfLogger logger = MsfLogger.getInstance(ClusterLinkIfCreateRequestBody.class);

  
  @SerializedName("cluster_link_if_id")
  private String clusterLinkIfId;

  
  @SerializedName("opposite_cluster_id")
  private String oppositeClusterId;

  
  @SerializedName("physical_link")
  private ClusterLinkIfPhysicalLinkCreateEntity physicalLink;

  
  @SerializedName("lag_link")
  private ClusterLinkIfPhysicalLagLinkCreateEntity lagLink;

  
  @SerializedName("igp_cost")
  private Integer igpCost;

  
  @SerializedName("port_status")
  private Boolean portStatus;

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  public String getClusterLinkIfId() {
    return clusterLinkIfId;
  }

  
  public void setClusterLinkIfId(String clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  
  public String getOppositeClusterId() {
    return oppositeClusterId;
  }

  
  public void setOppositeClusterId(String oppositeClusterId) {
    this.oppositeClusterId = oppositeClusterId;
  }

  
  public ClusterLinkIfPhysicalLinkCreateEntity getPhysicalLink() {
    return physicalLink;
  }

  
  public void setPhysicalLink(ClusterLinkIfPhysicalLinkCreateEntity physicalLink) {
    this.physicalLink = physicalLink;
  }

  
  public ClusterLinkIfPhysicalLagLinkCreateEntity getLagLink() {
    return lagLink;
  }

  
  public void setLagLink(ClusterLinkIfPhysicalLagLinkCreateEntity lagLink) {
    this.lagLink = lagLink;
  }

  
  public Integer getIgpCost() {
    return igpCost;
  }

  
  public void setIgpCost(Integer igpCost) {
    this.igpCost = igpCost;
  }

  
  public Boolean getPortStatus() {
    return portStatus;
  }

  
  public void setPortStatus(Boolean portStatus) {
    this.portStatus = portStatus;
  }

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  
  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  
  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  
  public ClusterLinkIfPortStatus getPortStatusEnum() {
    return ClusterLinkIfPortStatus.getEnumFromMessage(portStatus);
  }

  
  public void setPortStatusEnum(ClusterLinkIfPortStatus portStatus) {
    this.portStatus = portStatus.isBoolMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();


      ParameterCheckUtil.checkNotNullAndLength(oppositeClusterId);


      if (physicalLink == null ^ lagLink == null) {

        if (physicalLink != null) {
          validatePhysicalLink();
        }
        if (lagLink != null) {
          validateLagLink();
        }
      } else {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
            "error parameter is physical_link" + physicalLink + "or lag_link" + lagLink);
      }


      ParameterCheckUtil.checkNotNull(igpCost);


      if (portStatus != null) {
        ParameterCheckUtil.checkNotNull(getPortStatusEnum());
      }


      if (ipv4Address != null) {
        ipv4Address = ParameterCheckUtil.checkIpv4Address(ipv4Address);
      }


    } finally {
      logger.methodEnd();
    }
  }

  private void validatePhysicalLink() throws MsfException {


    ParameterCheckUtil.checkNumericId(physicalLink.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    if (!(physicalLink.getPhysicalIfId() == null ^ physicalLink.getBreakoutIfId() == null)) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "error parameter is physical_if_id"
          + physicalLink.getPhysicalIfId() + "or breakout_if_id" + physicalLink.getBreakoutIfId());
    }

    if (physicalLink.getBreakoutIfId() == null) {
      ParameterCheckUtil.checkNotNullAndLength(physicalLink.getPhysicalIfId());
    }

    if (physicalLink.getPhysicalIfId() == null) {
      ParameterCheckUtil.checkNotNullAndLength(physicalLink.getBreakoutIfId());
    }

    ParameterCheckUtil.checkNumericId(physicalLink.getOppositeNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    if (!(physicalLink.getOppositeIfId() == null ^ physicalLink.getOppositeBreakoutIfId() == null)) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "error parameter is opposite_if_id"
          + physicalLink.getOppositeIfId() + "or opposite_breakout_if_id" + physicalLink.getOppositeBreakoutIfId());
    }

    if (physicalLink.getOppositeBreakoutIfId() == null) {
      ParameterCheckUtil.checkNotNullAndLength(physicalLink.getOppositeIfId());
    }

    if (physicalLink.getOppositeIfId() == null) {
      ParameterCheckUtil.checkNotNullAndLength(physicalLink.getOppositeBreakoutIfId());
    }
  }

  private void validateLagLink() throws MsfException {

    ParameterCheckUtil.checkNumericId(lagLink.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNumericId(lagLink.getLagIfId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNumericId(lagLink.getOppositeNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNotNullAndLength(lagLink.getOppositeLagIfId());
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
