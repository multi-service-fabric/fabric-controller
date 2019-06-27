
package msf.mfcfc.node.nodes.leafs.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.nodes.leafs.data.entity.ChangeEquipmentTypeOptionEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeRecoverNodeOptionEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeTypeOptionEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class LeafNodeUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("leaf_type_option")
  private LeafNodeTypeOptionEntity leafTypeOption;

  @SerializedName("recover_node_option")
  private LeafNodeRecoverNodeOptionEntity recoverNodeOption;

  @SerializedName("change_equipment_type_option")
  private ChangeEquipmentTypeOptionEntity changeEquipmentTypeOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public LeafNodeTypeOptionEntity getLeafTypeOption() {
    return leafTypeOption;
  }

  public void setLeafTypeOption(LeafNodeTypeOptionEntity leafTypeOption) {
    this.leafTypeOption = leafTypeOption;
  }

  public LeafNodeRecoverNodeOptionEntity getRecoverNodeOption() {
    return recoverNodeOption;
  }

  public void setRecoverNodeOption(LeafNodeRecoverNodeOptionEntity recoverNodeOption) {
    this.recoverNodeOption = recoverNodeOption;
  }

  public ChangeEquipmentTypeOptionEntity getChangeEquipmentTypeOption() {
    return changeEquipmentTypeOption;
  }

  public void setChangeEquipmentTypeOption(ChangeEquipmentTypeOptionEntity changeEquipmentTypeOption) {
    this.changeEquipmentTypeOption = changeEquipmentTypeOption;
  }

  public LeafNodeUpdateAction getActionEnum() {
    return LeafNodeUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(LeafNodeUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      switch (getActionEnum()) {
        case CHG_LEAF_TYPE:

          ParameterCheckUtil.checkNotNull(leafTypeOption);
          validateLeafTypeOption();
          break;
        case RECOVER_NODE:

          ParameterCheckUtil.checkNotNull(recoverNodeOption);
          validateRecoverNodeOption();
          break;
        case CHG_EQUIPMENT_TYPE:

          ParameterCheckUtil.checkNotNull(changeEquipmentTypeOption);
          validateChangeEquipmentTypeOption();
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("action={0}", getActionEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateLeafTypeOption() throws MsfException {

    if (!LeafType.BORDER_LEAF.equals(leafTypeOption.getLeafTypeEnum())
        && !LeafType.IP_VPN_LEAF.equals(leafTypeOption.getLeafTypeEnum())) {
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
          MessageFormat.format("leafType = {0}", leafTypeOption.getLeafType()));
    }
  }

  private void validateRecoverNodeOption() throws MsfException {

    ParameterCheckUtil.checkNumericId(recoverNodeOption.getEquipmentTypeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    recoverNodeOption.setMacAddress(ParameterCheckUtil.checkMacAddress(recoverNodeOption.getMacAddress()));

    ParameterCheckUtil.checkNotNullAndLength(recoverNodeOption.getUsername());

    ParameterCheckUtil.checkNotNullAndLength(recoverNodeOption.getPassword());

  }

  private void validateChangeEquipmentTypeOption() throws MsfException {

    ParameterCheckUtil.checkNumericId(changeEquipmentTypeOption.getEquipmentTypeId(),
        ErrorCode.RELATED_RESOURCE_NOT_FOUND);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
