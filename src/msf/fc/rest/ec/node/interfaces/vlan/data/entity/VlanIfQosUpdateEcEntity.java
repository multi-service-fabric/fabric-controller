package msf.fc.rest.ec.node.interfaces.vlan.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class VlanIfQosUpdateEcEntity {

  
  @SerializedName("operation_type")
  private String operationType;

  
  @SerializedName("inflow_shaping_rate")
  private Float inflowShapingRate;

  
  @SerializedName("outflow_shaping_rate")
  private Float outflowShapingRate;

  
  @SerializedName("egress_queue")
  private String egressQueue;

  
  public String getOperationType() {
    return operationType;
  }

  
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  
  public Float getInflowShapingRate() {
    return inflowShapingRate;
  }

  
  public void setInflowShapingRate(Float inflowShapingRate) {
    this.inflowShapingRate = inflowShapingRate;
  }

  
  public Float getOutflowShapingRate() {
    return outflowShapingRate;
  }

  
  public void setOutflowShapingRate(Float outflowShapingRate) {
    this.outflowShapingRate = outflowShapingRate;
  }

  
  public String getEgressQueue() {
    return egressQueue;
  }

  
  public void setEgressQueue(String egressQueue) {
    this.egressQueue = egressQueue;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
