package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l3_cp_ospf_options")
@NamedQuery(name = "L3CpOspfOption.findAll", query = "SELECT l FROM L3CpOspfOption l")
public class L3CpOspfOption implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L3CpOspfOptionPK id;

  private Integer metric;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "cp_id", referencedColumnName = "cp_id", insertable = false, updatable = false),
      @JoinColumn(name = "slice_id", referencedColumnName = "slice_id", insertable = false, updatable = false) })
  private L3Cp l3Cp;

  public L3CpOspfOption() {
  }

  public L3CpOspfOptionPK getId() {
    return this.id;
  }

  public void setId(L3CpOspfOptionPK id) {
    this.id = id;
  }

  public Integer getMetric() {
    return this.metric;
  }

  public void setMetric(Integer metric) {
    this.metric = metric;
  }

  public L3Cp getL3Cp() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cp);
  }

  public void setL3Cp(L3Cp l3Cp) {
    this.l3Cp = l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "l3Cp" })
        .toString();
  }

}