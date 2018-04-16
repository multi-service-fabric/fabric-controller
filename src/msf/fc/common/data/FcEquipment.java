
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "equipments")
@NamedQuery(name = "FcEquipment.findAll", query = "SELECT f FROM FcEquipment f")
public class FcEquipment implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "equipment_type_id")
  private Integer equipmentTypeId;

  @OneToMany(mappedBy = "equipment")
  private List<FcNode> nodes;

  public FcEquipment() {
  }

  public Integer getEquipmentTypeId() {
    return this.equipmentTypeId;
  }

  public void setEquipmentTypeId(Integer equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public List<FcNode> getNodes() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.nodes);
  }

  public void setNodes(List<FcNode> nodes) {
    this.nodes = nodes;
  }

  public FcNode addNode(FcNode node) throws MsfException {
    getNodes().add(node);
    node.setEquipment(this);

    return node;
  }

  public FcNode removeNode(FcNode node) throws MsfException {
    getNodes().remove(node);
    node.setEquipment(null);

    return node;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "nodes" }).toString();
  }

}
