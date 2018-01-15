package msf.mfcfc.node.nodes.leafs.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
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
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("action={0}", getActionEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateLeafTypeOption() throws MsfException {


    ParameterCheckUtil.checkNotNull(leafTypeOption.getLeafTypeEnum());
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
