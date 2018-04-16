
package msf.mfcfc.common.constant;

public enum DataConsistency {

  CONSISTENT(ErrorResponseDataConsistency.ROLLED_BACK, ErrorResponseDataConsistency.UPDATED),

  INCONSISTENT(ErrorResponseDataConsistency.NOT_CONSISTENT, ErrorResponseDataConsistency.NOT_CONSISTENT),

  UNKNOWN(ErrorResponseDataConsistency.UNKNOWN, ErrorResponseDataConsistency.UNKNOWN);

  private ErrorResponseDataConsistency errorResponseDataConsistency;

  private ErrorResponseDataConsistency errorResponseDataConsistencyForRollback;

  private DataConsistency(ErrorResponseDataConsistency errorResponseDataConsistency,
      ErrorResponseDataConsistency errorResponseDataConsistencyForRollback) {
    this.errorResponseDataConsistency = errorResponseDataConsistency;
    this.errorResponseDataConsistencyForRollback = errorResponseDataConsistencyForRollback;

  }

  public ErrorResponseDataConsistency getErrorResponseDataConsistency() {
    return errorResponseDataConsistency;
  }

  public ErrorResponseDataConsistency getErrorResponseDataConsistencyForRollback() {
    return errorResponseDataConsistencyForRollback;
  }

  public static DataConsistency getDataConsistencyFromErrorResponseDataConsistency(
      ErrorResponseDataConsistency errorResponseDataConsistency) {
    if (errorResponseDataConsistency == null) {
      return null;
    }

    for (DataConsistency enumValue : values()) {
      if (enumValue.getErrorResponseDataConsistency().equals(errorResponseDataConsistency)) {
        return enumValue;
      }
    }

    return null;
  }

  public static DataConsistency getDataConsistencyFromErrorResponseDataConsistencyForRollback(
      ErrorResponseDataConsistency errorResponseDataConsistency) {
    if (errorResponseDataConsistency == null) {
      return null;
    }

    for (DataConsistency enumValue : values()) {
      if (enumValue.getErrorResponseDataConsistencyForRollback().equals(errorResponseDataConsistency)) {
        return enumValue;
      }
    }

    return null;
  }

}
