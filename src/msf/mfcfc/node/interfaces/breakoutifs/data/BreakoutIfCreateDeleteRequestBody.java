
package msf.mfcfc.node.interfaces.breakoutifs.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.interfaces.breakoutifs.data.entity.BreakoutIfValueEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class BreakoutIfCreateDeleteRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(BreakoutIfCreateDeleteRequestBody.class);

  @SerializedName("op")
  private String op;

  @SerializedName("path")
  private String path;

  @SerializedName("value")
  private BreakoutIfValueEntity value;

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

  public BreakoutIfValueEntity getValue() {
    return value;
  }

  public void setValue(BreakoutIfValueEntity value) {
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
        case COPY:
        case MOVE:
        case REPLACE:
        case TEST:
          String logMsg = "op is not set add or remove.";
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        default:

          throw new IllegalArgumentException(MessageFormat.format("op={0}", getOpEnum()));
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void validateRemoveOp() throws MsfException {

    if (!path.matches("/.+$")) {
      String logMsg = MessageFormat.format("param is not match.param = {0}, value = {1}", "path.matches('/.+$')",
          path.matches("/.+$"));
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } else {
      ParameterCheckUtil.checkIdSpecifiedByUri(path.substring(1));
    }

    if (value != null) {
      String logMsg = "value is not null.";
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

  }

  private void validateAddOp() throws MsfException {

    if (!path.matches("/.+$")) {
      String logMsg = MessageFormat.format("param is not match.param = {0}, value = {1}", "path.matches('/.+$')",
          path.matches("/.+$"));
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    } else {
      ParameterCheckUtil.checkIdSpecifiedByUri(path.substring(1));
    }

    ParameterCheckUtil.checkNotNull(value);

    validateValue();
  }

  private void validateValue() throws MsfException {

    ParameterCheckUtil.checkNotNull(value.getBaseIf());

    ParameterCheckUtil.checkNotNull(value.getDivisionNumber());

    ParameterCheckUtil.checkNotNullAndLength(value.getBreakoutIfSpeed());

    validateBaseIf();
  }

  private void validateBaseIf() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(value.getBaseIf().getPhysicalIfId());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
