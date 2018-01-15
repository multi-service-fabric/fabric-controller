
package msf.mfcfc.rest.common;

import msf.mfcfc.common.exception.MsfException;

/**
 * Interface for Request validation.
 *
 * @author NTT
 *
 */
public interface RestRequestValidator {

  /**
   * Method for checking request validation.
   *
   * @throws MsfException
   *           Arises when a check error occurs
   */
  public void validate() throws MsfException;

}
