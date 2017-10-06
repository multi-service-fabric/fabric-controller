package msf.fc.rest.ec.core.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.EcCommonOperationAction;
import msf.fc.rest.ec.core.operation.data.entity.CreateL2CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.CreateL3CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL2CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL3CpsOptionEcEntity;

public class OperationRequestBody {


  private String action;

  @SerializedName("create_l2cps_option")
  private CreateL2CpsOptionEcEntity createL2cpsOption;

  @SerializedName("delete_l2cps_option")
  private DeleteL2CpsOptionEcEntity deleteL2cpsOption;

  @SerializedName("create_l3cps_option")
  private CreateL3CpsOptionEcEntity createL3cpsOption;

  @SerializedName("delete_l3cps_option")
  private DeleteL3CpsOptionEcEntity deleteL3cpsOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public CreateL2CpsOptionEcEntity getCreateL2cpsOption() {
    return createL2cpsOption;
  }

  public void setCreateL2cpsOption(CreateL2CpsOptionEcEntity createL2cpsOption) {
    this.createL2cpsOption = createL2cpsOption;
  }

  public DeleteL2CpsOptionEcEntity getDeleteL2cpsOption() {
    return deleteL2cpsOption;
  }

  public void setDeleteL2cpsOption(DeleteL2CpsOptionEcEntity deleteL2cpsOption) {
    this.deleteL2cpsOption = deleteL2cpsOption;
  }

  public CreateL3CpsOptionEcEntity getCreateL3cpsOption() {
    return createL3cpsOption;
  }

  public void setCreateL3cpsOption(CreateL3CpsOptionEcEntity createL3cpsOption) {
    this.createL3cpsOption = createL3cpsOption;
  }

  public DeleteL3CpsOptionEcEntity getDeleteL3cpsOption() {
    return deleteL3cpsOption;
  }

  public void setDeleteL3cpsOption(DeleteL3CpsOptionEcEntity deleteL3cpsOption) {
    this.deleteL3cpsOption = deleteL3cpsOption;
  }

  public EcCommonOperationAction getActionEnum() {
    return EcCommonOperationAction.getEnumFromMessage(action);
  }

  public void setActionEnum(EcCommonOperationAction action) {
    this.action = action.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
