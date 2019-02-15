
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

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "cp_filter_info")
@NamedQuery(name = "FcCpFilterInfo.findAll", query = "SELECT f FROM FcCpFilterInfo f")
public class FcCpFilterInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcCpFilterInfoPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "cp_id", referencedColumnName = "cp_id", insertable = false, updatable = false),
      @JoinColumn(name = "slice_id", referencedColumnName = "slice_id", insertable = false, updatable = false) })
  private FcL2Cp l2Cp;

  public FcCpFilterInfo() {
  }

  public FcCpFilterInfoPK getId() {
    return this.id;
  }

  public void setId(FcCpFilterInfoPK id) {
    this.id = id;
  }

  public FcL2Cp getL2Cp() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cp);
  }

  public void setL2Cp(FcL2Cp l2Cp) {
    this.l2Cp = l2Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l2Cp" }).toString();
  }

}
