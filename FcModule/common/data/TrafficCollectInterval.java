package msf.fc.common.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "traffic_collect_interval")
@NamedQuery(name = "TrafficCollectInterval.findAll", query = "SELECT t FROM TrafficCollectInterval t")
public class TrafficCollectInterval implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_time")
  private Date startTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end_time")
  private Date endTime;

  private Integer interval;

  public TrafficCollectInterval() {
  }

  public Date getStartTime() {
    return this.startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return this.endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Integer getInterval() {
    return this.interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}