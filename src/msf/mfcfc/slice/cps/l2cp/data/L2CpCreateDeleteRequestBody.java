
package msf.mfcfc.slice.cps.l2cp.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpValueEntity;


public class L2CpCreateDeleteRequestBody implements RestRequestValidator {

  
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpCreateDeleteAsyncResponseBody.class);

  
  @SerializedName("op")
  private String op;

  
  @SerializedName("path")
  private String path;

  
  @SerializedName("value")
  private L2CpValueEntity value;

  
  public String getOp() {
    return op;
  }

  
  public void setOp(String op) {
    this.op = op;
  }

  
  public String getPath() {
    return path;
  }

  
  public void setPath(String path) {
    this.path = path;
  }

  
  public L2CpValueEntity getValue() {
    return value;
  }

  
  public void setValue(L2CpValueEntity value) {
    this.value = value;
  }

  
  public PatchOperation getOpEnum() {
    return PatchOperation.getEnumFromMessage(op);
  }

  
  public void setOpEnum(PatchOperation op) {
    this.op = op.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getOpEnum());

      ParameterCheckUtil.checkNotNullAndLength(path);


      switch (getOpEnum()) {
        case ADD:
          validateAddOp();
          break;
        case REMOVE:
          validateRemoveOp();
          break;
        case REPLACE:
          validateReplace();
          break;
        case TEST:
        case COPY:
        case MOVE:
          String logMsg = "op is not set add or remove or replace.";
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        default:

          throw new IllegalArgumentException(MessageFormat.format("op={0}", getOpEnum()));
      }
    } finally {
      logger.methodEnd();
    }

  }

  private void validateReplace() throws MsfException {


    ParameterCheckUtil.checkPatchPath(path, true);


    ParameterCheckUtil.checkNotNull(value);

    validateReplaceValue();
  }

  private void validateReplaceValue() throws MsfException {


    if (value.getClusterId() != null) {
      String logMsg = "value.getClusterId() is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    if (value.getEdgePointId() != null) {
      String logMsg = "value.getEdgePointId is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    if (value.getVlanId() != null) {
      String logMsg = "value.getVlanId is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }



    ParameterCheckUtil.checkNotNullAndLength(value.getEsi());

    ParameterCheckUtil.checkNotNullAndLength(value.getLacpSystemId());

    if (value.getPortModeEnum() != null) {
      String logMsg = "value.getPortModeEnum is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }
  }

  private void validateRemoveOp() throws MsfException {


    ParameterCheckUtil.checkPatchPath(path, true);

    if (value != null) {
      String logMsg = "value is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

  }

  private void validateAddOp() throws MsfException {


    ParameterCheckUtil.checkPatchPath(path, false);

    ParameterCheckUtil.checkNotNull(value);

    validateAddValue();
  }

  private void validateAddValue() throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(value.getClusterId());

    ParameterCheckUtil.checkNotNullAndLength(value.getEdgePointId());

    ParameterCheckUtil.checkNotNull(value.getVlanId());
    ParameterCheckUtil.checkNumberRange(value.getVlanId(), 1, 4096);





    ParameterCheckUtil.checkNotNull(value.getPortModeEnum());

  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
