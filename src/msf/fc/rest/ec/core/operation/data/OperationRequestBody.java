
package msf.fc.rest.ec.core.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.core.operation.data.entity.OperationCreateL3VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteBreakoutOptionIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteL3VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationRegisterBreakoutOptionIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateL2VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationUpdateL3VlanIfOptionEcEntity;

public class OperationRequestBody {

  @SerializedName("action")
  private String action;

  @SerializedName("create_update_l2vlan_if_option")
  private OperationCreateUpdateL2VlanIfOptionEcEntity createUpdateL2vlanIfOption;

  @SerializedName("delete_update_l2vlan_if_option")
  private OperationDeleteUpdateL2VlanIfOptionEcEntity deleteUpdateL2vlanIfOption;

  @SerializedName("create_l3vlan_if_option")
  private OperationCreateL3VlanIfOptionEcEntity createL3vlanIfOption;

  @SerializedName("delete_l3vlan_if_option")
  private OperationDeleteL3VlanIfOptionEcEntity deleteL3vlanIfOption;

  @SerializedName("update_l2vlan_if_option")
  private OperationUpdateL2VlanIfOptionEcEntity updateL2vlanIfOption;

  @SerializedName("update_l3vlan_if_option")
  private OperationUpdateL3VlanIfOptionEcEntity updateL3vlanIfOption;

  @SerializedName("register_breakout_if_option")
  private OperationRegisterBreakoutOptionIfEcEntity registerBreakoutIfOption;

  @SerializedName("delete_breakout_if_option")
  private OperationDeleteBreakoutOptionIfEcEntity deleteBreakoutIfOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public OperationCreateUpdateL2VlanIfOptionEcEntity getCreateUpdateL2vlanIfOption() {
    return createUpdateL2vlanIfOption;
  }

  public void setCreateUpdateL2vlanIfOption(OperationCreateUpdateL2VlanIfOptionEcEntity createUpdateL2vlanIfOption) {
    this.createUpdateL2vlanIfOption = createUpdateL2vlanIfOption;
  }

  public OperationDeleteUpdateL2VlanIfOptionEcEntity getDeleteUpdateL2vlanIfOption() {
    return deleteUpdateL2vlanIfOption;
  }

  public void setDeleteUpdateL2vlanIfOption(OperationDeleteUpdateL2VlanIfOptionEcEntity deleteUpdateL2vlanIfOption) {
    this.deleteUpdateL2vlanIfOption = deleteUpdateL2vlanIfOption;
  }

  public OperationCreateL3VlanIfOptionEcEntity getCreateL3vlanIfOption() {
    return createL3vlanIfOption;
  }

  public void setCreateL3vlanIfOption(OperationCreateL3VlanIfOptionEcEntity createL3vlanIfOption) {
    this.createL3vlanIfOption = createL3vlanIfOption;
  }

  public OperationDeleteL3VlanIfOptionEcEntity getDeleteL3vlanIfOption() {
    return deleteL3vlanIfOption;
  }

  public void setDeleteL3vlanIfOption(OperationDeleteL3VlanIfOptionEcEntity deleteL3vlanIfOption) {
    this.deleteL3vlanIfOption = deleteL3vlanIfOption;
  }

  public OperationUpdateL2VlanIfOptionEcEntity getUpdateL2vlanIfOption() {
    return updateL2vlanIfOption;
  }

  public void setUpdateL2vlanIfOption(OperationUpdateL2VlanIfOptionEcEntity updateL2vlanIfOption) {
    this.updateL2vlanIfOption = updateL2vlanIfOption;
  }

  public OperationUpdateL3VlanIfOptionEcEntity getUpdateL3vlanIfOption() {
    return updateL3vlanIfOption;
  }

  public void setUpdateL3vlanIfOption(OperationUpdateL3VlanIfOptionEcEntity updateL3vlanIfOption) {
    this.updateL3vlanIfOption = updateL3vlanIfOption;
  }

  public OperationRegisterBreakoutOptionIfEcEntity getRegisterBreakoutIfOption() {
    return registerBreakoutIfOption;
  }

  public void setRegisterBreakoutIfOption(OperationRegisterBreakoutOptionIfEcEntity registerBreakoutIfOption) {
    this.registerBreakoutIfOption = registerBreakoutIfOption;
  }

  public OperationDeleteBreakoutOptionIfEcEntity getDeleteBreakoutIfOption() {
    return deleteBreakoutIfOption;
  }

  public void setDeleteBreakoutIfOption(OperationDeleteBreakoutOptionIfEcEntity deleteBreakoutIfOption) {
    this.deleteBreakoutIfOption = deleteBreakoutIfOption;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
