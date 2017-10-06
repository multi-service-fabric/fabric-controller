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
@Table(name = "l3_cp_static_route_options")
@NamedQuery(name = "L3CpStaticRouteOption.findAll", query = "SELECT l FROM L3CpStaticRouteOption l")
public class L3CpStaticRouteOption implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L3CpStaticRouteOptionPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "cp_id", referencedColumnName = "cp_id", insertable = false, updatable = false),
      @JoinColumn(name = "slice_id", referencedColumnName = "slice_id", insertable = false, updatable = false) })
  private L3Cp l3Cp;

  public L3CpStaticRouteOption() {
  }

  public L3CpStaticRouteOptionPK getId() {
    return this.id;
  }

  public void setId(L3CpStaticRouteOptionPK id) {
    this.id = id;
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