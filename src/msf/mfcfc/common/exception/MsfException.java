
package msf.mfcfc.common.exception;

import msf.mfcfc.common.constant.ErrorCode;

/**
 * Exception class of the MSF controller.
 *
 * @author NTT
 *
 */
public class MsfException extends Exception {

  private ErrorCode errorCode;

  /**
   * Constructor.
   *
   * @param errorCode
   *          Error code.
   * @param message
   *          Detailed message about exception.
   */
  public MsfException(ErrorCode errorCode, String message) {
    super(message);

    this.errorCode = errorCode;
  }

  /**
   * Get the error code.
   *
   * @return error code.
   */
  public ErrorCode getErrorCode() {
    return this.errorCode;
  }
}
