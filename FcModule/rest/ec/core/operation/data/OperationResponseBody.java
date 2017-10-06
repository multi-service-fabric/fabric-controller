package msf.fc.rest.ec.core.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractResponseBody;

public class OperationResponseBody extends AbstractResponseBody {
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
