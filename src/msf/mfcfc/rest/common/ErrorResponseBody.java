
package msf.mfcfc.rest.common;

import org.apache.commons.lang.builder.ToStringBuilder;


public class ErrorResponseBody extends AbstractResponseBody {

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
