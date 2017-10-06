package msf.fc.common.exception;

import msf.fc.common.constant.ErrorCode;

public class MsfException extends Exception {

  private ErrorCode errorCode;

  public MsfException(ErrorCode errorCode, String message) {
    super(message);

    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return this.errorCode;
  }
}
