
package msf.fc.rest.ec.core.operation.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class OperationResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
