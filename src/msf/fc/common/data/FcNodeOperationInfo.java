
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeOperationStatus;

@Entity
@Table(name = "node_operation_info")
@NamedQuery(name = "FcNodeOperationInfo.findAll", query = "SELECT f FROM FcNodeOperationInfo f")
public class FcNodeOperationInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "node_operation_status")
  private Integer nodeOperationStatus;

  public FcNodeOperationInfo() {
  }

  public Integer getNodeOperationStatus() {
    return this.nodeOperationStatus;
  }

  public void setNodeOperationStatus(Integer nodeOperationStatus) {
    this.nodeOperationStatus = nodeOperationStatus;
  }

  public NodeOperationStatus getNodeOperationStatusEnum() {
    return NodeOperationStatus.getEnumFromCode(this.nodeOperationStatus);
  }

  public void setNodeOperationStatusEnum(NodeOperationStatus nodeOperationStatus) {
    this.nodeOperationStatus = nodeOperationStatus.getCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
