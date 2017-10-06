package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OspfEcEntity {
  @SerializedName("metric")
  private Integer metric;

  public Integer getMetric() {
    return metric;
  }

  public void setMetric(Integer metric) {
    this.metric = metric;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
