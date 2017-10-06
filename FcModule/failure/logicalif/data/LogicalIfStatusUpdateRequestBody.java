package msf.fc.failure.logicalif.data;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.NodeType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusCpBaseData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusInternalLinkIfData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusNodeData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusSliceData;
import msf.fc.failure.logicalif.data.entity.LogicalIfStatusVpnBaseData;
import msf.fc.rest.common.RestRequestValidator;

public class LogicalIfStatusUpdateRequestBody implements RestRequestValidator {
  private static final MsfLogger logger = MsfLogger.getInstance(LogicalIfStatusUpdateRequestBody.class);

  @SerializedName("update_logical_if_status_option")
  private LogicalIfStatusData updateOptionData;

  @Override
  public void validate() throws MsfException {
    logger.methodStart();

    try {
      ParameterCheckUtil.checkNotNull(updateOptionData);

      ParameterCheckUtil.checkNotNullAndLength(updateOptionData.getClusterId());

      List<LogicalIfStatusNodeData> nodes = updateOptionData.getNodes();
      if (nodes != null) {
        for (LogicalIfStatusNodeData node : nodes) {
          ParameterCheckUtil.checkNotNull(NodeType.getEnumFromSingularMessage(node.getNodeType()));
          ParameterCheckUtil.checkNumericId(node.getNodeId(), ErrorCode.PARAMETER_VALUE_ERROR);
          List<LogicalIfStatusInternalLinkIfData> internalLinkIfs = node.getInternalLinkIfs();
          ParameterCheckUtil.checkNotNull(internalLinkIfs);
          for (LogicalIfStatusInternalLinkIfData internalLinkIf : node.getInternalLinkIfs()) {
            ParameterCheckUtil.checkNumericId(internalLinkIf.getInternalLinkIfId(), ErrorCode.PARAMETER_VALUE_ERROR);
            checkOperationStatus(internalLinkIf.getStatus());
          }
        }
      }

      LogicalIfStatusSliceData slices = updateOptionData.getSlices();
      if (slices != null) {
        List<LogicalIfStatusVpnBaseData> l2Vpns = slices.getL2Vpn();
        List<LogicalIfStatusVpnBaseData> l3Vpns = slices.getL3Vpn();
        if (l2Vpns == null && l3Vpns == null) {
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "l2vpn and l3vpn are null.");
        }
        boolean existCpFlag = false;

        if (l2Vpns != null && !l2Vpns.isEmpty()) {
          for (LogicalIfStatusVpnBaseData l2Vpn : l2Vpns) {
            ParameterCheckUtil.checkIdSpecifiedByUri(l2Vpn.getSliceId());
            List<LogicalIfStatusCpBaseData> l2cps = l2Vpn.getCps();
            ParameterCheckUtil.checkNotNull(l2cps);
            if (!l2cps.isEmpty()) {
              existCpFlag = true;
              for (LogicalIfStatusCpBaseData l2Cp : l2cps) {
                ParameterCheckUtil.checkIdSpecifiedByUri(l2Cp.getCpId());
                checkOperationStatus(l2Cp.getStatus());
              }
            }
          }
        }

        if (l3Vpns != null && !l3Vpns.isEmpty()) {
          for (LogicalIfStatusVpnBaseData l3Vpn : l3Vpns) {
            ParameterCheckUtil.checkIdSpecifiedByUri(l3Vpn.getSliceId());
            List<LogicalIfStatusCpBaseData> l3cps = l3Vpn.getCps();
            ParameterCheckUtil.checkNotNull(l3cps);
            if (!l3cps.isEmpty()) {
              existCpFlag = true;
              for (LogicalIfStatusCpBaseData l3Cp : l3cps) {
                ParameterCheckUtil.checkIdSpecifiedByUri(l3Cp.getCpId());
                checkOperationStatus(l3Cp.getStatus());
              }
            }
          }
        }

        if (!existCpFlag) {
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "cp not exist.");
        }
      }
    } finally {
      logger.methodEnd();
    }

  }

  public LogicalIfStatusData getUpdateOptionData() {
    return updateOptionData;
  }

  public void setUpdateOptionData(LogicalIfStatusData updateOptionData) {
    this.updateOptionData = updateOptionData;
  }

  private void checkOperationStatus(String status) throws MsfException {
    List<String> permitStatus = new ArrayList<>();
    permitStatus.add(InterfaceOperationStatus.UP.getMessage());
    permitStatus.add(InterfaceOperationStatus.DOWN.getMessage());

    if (!permitStatus.contains(status)) {
      String errMsg = MessageFormat.format("status={0}: Invalid Value.", status);
      logger.error(errMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, errMsg);
    }
  }

}
