
package msf.mfcfc.services.filter.scenario.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.filter.scenario.filters.data.entity.FilterValueEntity;

public class LagIfFilterCreateDeleteRequestBody extends AbstractFilterCreateDeleteRequestBody
    implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LagIfFilterCreateDeleteRequestBody.class);

  @SerializedName("op")
  private String op;

  @SerializedName("path")
  private String path;

  @SerializedName("value")
  private FilterValueEntity value;

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

  public FilterValueEntity getValue() {
    return value;
  }

  public void setValue(FilterValueEntity value) {
    this.value = value;
  }

  public PatchOperation getOpEnum() throws MsfException {
    return super.getOpEnum(op);
  }

  public void setOpEnum(PatchOperation op) {
    this.op = op.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      super.validate(op, path, value);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
