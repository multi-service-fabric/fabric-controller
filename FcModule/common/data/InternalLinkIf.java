package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "internal_link_ifs")
@NamedQuery(name = "InternalLinkIf.findAll", query = "SELECT i FROM InternalLinkIf i")
public class InternalLinkIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "internal_link_if_id")
  private Integer internalLinkIfId;

  @Column(name = "operation_status")
  private Integer operationStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_info_id")
  private LagIf lagIf;

  public InternalLinkIf() {
  }

  public Integer getInternalLinkIfId() {
    return this.internalLinkIfId;
  }

  public void setInternalLinkIfId(Integer internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public Integer getOperationStatus() {
    return this.operationStatus;
  }

  public void setOperationStatus(Integer operationStatus) {
    this.operationStatus = operationStatus;
  }

  public InterfaceOperationStatus getOperationStatusEnum() {
    return InterfaceOperationStatus.getEnumFromCode(this.operationStatus);
  }

  public void setOperationStatusEnum(InterfaceOperationStatus operationStatus) {
    this.operationStatus = operationStatus.getCode();
  }

  public LagIf getLagIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIf);
  }

  public void setLagIf(LagIf lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "lagIf" })
        .toString();
  }

}