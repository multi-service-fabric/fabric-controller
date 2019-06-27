
package msf.mfcfc.node.nodes.spines.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.SpineNodeUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.nodes.leafs.data.entity.ChangeEquipmentTypeOptionEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class SpineNodeUpdateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeUpdateRequestBody.class);

  @SerializedName("action")
  private String action;

  @SerializedName("change_equipment_type_option")
  private ChangeEquipmentTypeOptionEntity changeEquipmentTypeOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public ChangeEquipmentTypeOptionEntity getChangeEquipmentTypeOption() {
    return changeEquipmentTypeOption;
  }

  public void setChangeEquipmentTypeOption(ChangeEquipmentTypeOptionEntity changeEquipmentTypeOption) {
    this.changeEquipmentTypeOption = changeEquipmentTypeOption;
  }

  public SpineNodeUpdateAction getActionEnum() {
    return SpineNodeUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(SpineNodeUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {

    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getActionEnum());

      switch (getActionEnum()) {
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

  private void validateChangeEquipmentTypeOption() throws MsfException {

    ParameterCheckUtil.checkNumericId(changeEquipmentTypeOption.getEquipmentTypeId(),
        ErrorCode.RELATED_RESOURCE_NOT_FOUND);

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
