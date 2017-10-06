package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "traffic_history_l2slice")
@NamedQuery(name = "TrafficHistoryL2slice.findAll", query = "SELECT t FROM TrafficHistoryL2slice t")
public class TrafficHistoryL2slice implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private TrafficHistoryL2slicePK id;

  private double value;

  public TrafficHistoryL2slice() {
  }

  public TrafficHistoryL2slicePK getId() {
    return this.id;
  }

  public void setId(TrafficHistoryL2slicePK id) {
    this.id = id;
  }

  public double getValue() {
    return this.value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}