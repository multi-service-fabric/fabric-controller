package msf.fc.slice.cps.l3cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AddressType;
import msf.fc.common.constant.CpUpdateAction;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;
import msf.fc.slice.cps.l3cp.data.entity.StaticRouteEntity;
import msf.fc.slice.cps.l3cp.data.entity.UpdateOptionEntity;

public class L3CpUpdateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpUpdateRequestBody.class);
  @SerializedName("action")
  private String action;

  @SerializedName("update_option")
  private UpdateOptionEntity updateOption;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public UpdateOptionEntity getUpdateOption() {
    return updateOption;
  }

  public void setUpdateOption(UpdateOptionEntity updateOption) {
    this.updateOption = updateOption;
  }

  public CpUpdateAction getActionEnum() {
    return CpUpdateAction.getEnumFromMessage(action);
  }

  public void setActionEnum(CpUpdateAction action) {
    this.action = action.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      CpUpdateAction cpUpdateAction = getActionEnum();
      if (cpUpdateAction == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "action", action);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      if (CpUpdateAction.FORCE_DELETE.equals(cpUpdateAction)) {
        String logMsg = MessageFormat.format("param is not target.param = {0}, value = {1}", "action", action);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      if (CpUpdateAction.UPDATE.equals(getActionEnum())) {
        ParameterCheckUtil.checkNotNull(updateOption);
      }
      if (updateOption != null) {
        if (updateOption.getOperationTypeEnum() == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "operationType",
              updateOption.getOperationType());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
        if (updateOption.getStaticRouteList() != null && updateOption.getStaticRouteList().size() > 0) {
          for (StaticRouteEntity staticRoute : updateOption.getStaticRouteList()) {
            if (staticRoute.getAddrTypeEnum() == null) {
              String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}",
                  "staticRoute.addrType", staticRoute.getAddrType());
              logger.error(logMsg);
              throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
            }
            if (AddressType.IPV4.equals(staticRoute.getAddrTypeEnum())) {
              staticRoute.setAddr(ParameterCheckUtil.checkIpv4Address(staticRoute.getAddr()));
              ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
              ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 32);
              staticRoute.setNextHop(ParameterCheckUtil.checkIpv4Address(staticRoute.getNextHop()));

            } else {
              staticRoute.setAddr(ParameterCheckUtil.checkIpv6Address(staticRoute.getAddr()));
              ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
              ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 128);
              staticRoute.setNextHop(ParameterCheckUtil.checkIpv6Address(staticRoute.getNextHop()));
            }
          }
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
