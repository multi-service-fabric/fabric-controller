package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "cp_ids")
@NamedQuery(name = "CpId.findAll", query = "SELECT c FROM CpId c")
public class CpId implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CpIdPK id;

  @Column(name = "next_id")
  private Integer nextId;

  public CpId() {
  }

  public CpIdPK getId() {
    return this.id;
  }

  public void setId(CpIdPK id) {
    this.id = id;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}