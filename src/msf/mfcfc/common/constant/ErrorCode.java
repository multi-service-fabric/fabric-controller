
package msf.mfcfc.common.constant;

import org.eclipse.jetty.http.HttpStatus;

public enum ErrorCode {

  SYSTEM_STATUS_ERROR("000001", HttpStatus.INTERNAL_SERVER_ERROR_500, "System status error.", DataConsistency.CONSISTENT),

  OPERATION_CANCELED("000002", HttpStatus.INTERNAL_SERVER_ERROR_500, "Operation canceled.", DataConsistency.CONSISTENT),

  PARAMETER_FORMAT_ERROR("010001", HttpStatus.BAD_REQUEST_400, "Parameter format error.", DataConsistency.CONSISTENT),

  PARAMETER_VALUE_ERROR("010002", HttpStatus.BAD_REQUEST_400, "Parameter value error.", DataConsistency.CONSISTENT),

  PARAMETER_VALUE_OUT_OF_RANGE("010003", HttpStatus.BAD_REQUEST_400, "Parameter value out of range.", DataConsistency.CONSISTENT),

  DATABASE_CONNECTION_ERROR("020001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database connection error.", DataConsistency.CONSISTENT),

  DATABASE_OPERATION_ERROR("020002", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database operation error.", DataConsistency.CONSISTENT),

  EXCLUSIVE_CONTROL_ERROR("020003", HttpStatus.CONFLICT_409, "Exclusive control error.", DataConsistency.CONSISTENT),

  DATABASE_TRANSACTION_ERROR("020004", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database transaction error.", DataConsistency.CONSISTENT),

  TARGET_RESOURCE_NOT_FOUND("030001", HttpStatus.NOT_FOUND_404, "Target resource is not found.", DataConsistency.CONSISTENT),

  TARGET_RESOURCE_ALREADY_EXIST("030002", HttpStatus.CONFLICT_409, "Target resource already exist.", DataConsistency.CONSISTENT),

  RELATED_RESOURCE_NOT_FOUND("030003", HttpStatus.INTERNAL_SERVER_ERROR_500, "Related resource not found.", DataConsistency.CONSISTENT),

  REGIST_INFORMATION_ERROR("030004", HttpStatus.INTERNAL_SERVER_ERROR_500, "Register information error.", DataConsistency.CONSISTENT),

  UPDATE_INFORMATION_ERROR("030005", HttpStatus.INTERNAL_SERVER_ERROR_500, "Update information error.", DataConsistency.CONSISTENT),

  DELETE_INFORMATION_ERROR("030006", HttpStatus.INTERNAL_SERVER_ERROR_500, "Delete information error.", DataConsistency.CONSISTENT),

  TRANSITION_STATUS_ERROR("030007", HttpStatus.INTERNAL_SERVER_ERROR_500, "Transition status error.", DataConsistency.CONSISTENT),

  FILE_READ_ERROR("040001", HttpStatus.INTERNAL_SERVER_ERROR_500, "File read error.", DataConsistency.CONSISTENT),

  FILE_WRITE_ERROR("040002", HttpStatus.INTERNAL_SERVER_ERROR_500, "File write error.", DataConsistency.CONSISTENT),

  EXECUTE_FILE_ERROR("040003", HttpStatus.INTERNAL_SERVER_ERROR_500, "Execute file error.", DataConsistency.CONSISTENT),

  FILE_DELETE_ERROR("040004", HttpStatus.INTERNAL_SERVER_ERROR_500, "File delete error.", DataConsistency.CONSISTENT),

  EC_CONNECTION_ERROR("050001", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC connection error.", DataConsistency.CONSISTENT),

  EC_CONTROL_ERROR("050002", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control error.", DataConsistency.CONSISTENT),

  EC_CONTROL_TIMEOUT("050004", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control timeout.", DataConsistency.UNKNOWN),

  FC_CONNECTION_ERROR("060001", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC connection error.", DataConsistency.CONSISTENT),

  FC_CONTROL_ERROR("060002", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC control error.", DataConsistency.CONSISTENT),

  FC_CONTROL_TIMEOUT("060003", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC control timeout.", DataConsistency.UNKNOWN),

  CLUSTER_CONTROL_ERROR("300001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Cluster control error.", null),

  FC_EC_CONTROL_ERROR_EM_CONTROL_COMPLETED("900001", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC, EC control error. (EM control completed.)", DataConsistency.INCONSISTENT),

  FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED("900002", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC control error. (EC, EM control completed.)", DataConsistency.INCONSISTENT),

  EC_CONTROL_ERROR_EM_CONTROL_COMPLETED("900003", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control error. (FC, EM control completed.)", DataConsistency.INCONSISTENT),

  FC_CONTROL_ERROR_EC_CONTROL_COMPLETED("900004", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC control error. (EC control completed.)", DataConsistency.INCONSISTENT),

  MFC_CONTROL_ERROR_FC_EC_EM_CONTROL_COMPLETED("900005", HttpStatus.INTERNAL_SERVER_ERROR_500, "MFC control error. (FC, EC, EM control completed.)", DataConsistency.INCONSISTENT),

  MFC_CONTROL_ERROR_FC_EC_CONTROL_COMPLETED("900006", HttpStatus.INTERNAL_SERVER_ERROR_500, "MFC control error. (FC, EC control completed.)", DataConsistency.INCONSISTENT),

  MFC_CONTROL_ERROR_FC_CONTROL_COMPLETED("900007", HttpStatus.INTERNAL_SERVER_ERROR_500, "MFC control error. (FC control completed.)", DataConsistency.INCONSISTENT),

  UNDEFINED_ERROR("990001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Undefined error.", DataConsistency.UNKNOWN),

  SYSTEM_CHECK_STATUS_ERROR("990002", HttpStatus.INTERNAL_SERVER_ERROR_500, "System check status error.", DataConsistency.CONSISTENT),;

  private String code;

  private int statusCode;

  private String message;

  private DataConsistency dataConsistency;

  private ErrorCode(String code, int statusCode, String message, DataConsistency dataConsistency) {
    this.code = code;
    this.statusCode = statusCode;
    this.message = message;
    this.dataConsistency = dataConsistency;
  }

  public String getMessage() {
    return this.message;
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public String getCode() {
    return this.code;
  }

  public DataConsistency getDataConsistency() {
    return dataConsistency;
  }

  public static ErrorCode getEnumFromErrorCode(String errorCode) {
    for (ErrorCode enumValue : values()) {
      if (enumValue.getCode().equals(errorCode)) {
        return enumValue;
      }
    }

    return null;
  }

}
