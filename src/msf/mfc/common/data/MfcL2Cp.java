
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
@Table(name = "l2_cps")
@NamedQuery(name = "MfcL2Cp.findAll", query = "SELECT m FROM MfcL2Cp m")
public class MfcL2Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private MfcL2CpPK id;

  private String esi;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private MfcL2Slice l2Slice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id")
  private MfcSwCluster swCluster;

  public MfcL2Cp() {
  }

  public MfcL2CpPK getId() {
    return this.id;
  }

  public void setId(MfcL2CpPK id) {
    this.id = id;
  }

  public String getEsi() {
    return this.esi;
  }

  public void setEsi(String esi) {
    this.esi = esi;
  }

  public MfcL2Slice getL2Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Slice);
  }

  public void setL2Slice(MfcL2Slice l2Slice) {
    this.l2Slice = l2Slice;
  }

  public MfcSwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(MfcSwCluster swCluster) {
    this.swCluster = swCluster;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l2Slice", "swCluster" }).toString();
  }

}
