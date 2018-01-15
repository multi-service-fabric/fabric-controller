
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lag_if_ids")
@NamedQuery(name = "FcLagIfId.findAll", query = "SELECT f FROM FcLagIfId f")
public class FcLagIfId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "next_id")
  private Integer nextId;

  public FcLagIfId() {
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