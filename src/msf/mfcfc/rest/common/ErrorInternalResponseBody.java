
package msf.mfcfc.rest.common;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ErrorInternalResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
