
package msf.mfcfc.rest.common;

import msf.mfcfc.common.exception.MsfException;

/**
 * Interface for Request validation.
 *
 * @author NTT
 *
 */
public interface RestRequestValidator {

  public void validate() throws MsfException;

}
