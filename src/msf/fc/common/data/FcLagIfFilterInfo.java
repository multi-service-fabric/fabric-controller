
package msf.fc.common.data;

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
@Table(name = "lag_if_filter_info")
@NamedQuery(name = "FcLagIfFilterInfo.findAll", query = "SELECT f FROM FcLagIfFilterInfo f")
public class FcLagIfFilterInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcLagIfFilterInfoPK id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_info_id", insertable = false, updatable = false)
  private FcLagIf lagIf;

  public FcLagIfFilterInfo() {
  }

  public FcLagIfFilterInfoPK getId() {
    return this.id;
  }

  public void setId(FcLagIfFilterInfoPK id) {
    this.id = id;
  }

  public FcLagIf getLagIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIf);
  }

  public void setLagIf(FcLagIf lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "lagIf" }).toString();
  }

}
