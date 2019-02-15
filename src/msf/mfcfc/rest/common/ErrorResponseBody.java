
package msf.mfcfc.rest.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for a response body used when request errors are received.
 *
 * @author NTT
 */
public class ErrorResponseBody extends AbstractResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
