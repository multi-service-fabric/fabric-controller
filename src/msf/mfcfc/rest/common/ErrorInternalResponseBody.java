
package msf.mfcfc.rest.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for a response body used when system internal request errors received.
 *
 * @author NTT
 */
public class ErrorInternalResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
