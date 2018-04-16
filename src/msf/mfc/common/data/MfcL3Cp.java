
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "l3_cps")
@NamedQuery(name = "MfcL3Cp.findAll", query = "SELECT m FROM MfcL3Cp m")
public class MfcL3Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private MfcL3CpPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private MfcL3Slice l3Slice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id")
  private MfcSwCluster swCluster;

  public MfcL3Cp() {
  }

  public MfcL3CpPK getId() {
    return this.id;
  }

  public void setId(MfcL3CpPK id) {
    this.id = id;
  }

  public MfcL3Slice getL3Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Slice);
  }

  public void setL3Slice(MfcL3Slice l3Slice) {
    this.l3Slice = l3Slice;
  }

  public MfcSwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(MfcSwCluster swCluster) {
    this.swCluster = swCluster;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l3Slice", "swCluster" }).toString();
  }

}
