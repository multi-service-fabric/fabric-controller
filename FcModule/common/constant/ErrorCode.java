package msf.fc.common.constant;

import org.eclipse.jetty.http.HttpStatus;

public enum ErrorCode {

  SYSTEM_STATUS_ERROR("000001", HttpStatus.INTERNAL_SERVER_ERROR_500, "System status error."),
  OPERATION_CANCELED("000002", HttpStatus.INTERNAL_SERVER_ERROR_500, "Operation canceled."),
  PARAMETER_FORMAT_ERROR("010001", HttpStatus.BAD_REQUEST_400, "Parameter format error."),
  PARAMETER_VALUE_ERROR("010002", HttpStatus.BAD_REQUEST_400, "Parameter value error."),
  PARAMETER_VALUE_OUT_OF_RANGE("010003", HttpStatus.BAD_REQUEST_400, "Parameter value out of range."),
  DATABASE_CONNECTION_ERROR("020001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database connection error."),
  DATABASE_OPERATION_ERROR("020002", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database operation error."),
  EXCLUSIVE_CONTROL_ERROR("020003", HttpStatus.CONFLICT_409, "Exclusive control error."),
  DATABASE_TRANSACTION_ERROR("020004", HttpStatus.INTERNAL_SERVER_ERROR_500, "Database transaction error."),
  TARGET_RESOURCE_NOT_FOUND("030001", HttpStatus.NOT_FOUND_404, "Target resource not found."),
  TARGET_RESOURCE_ALREADY_EXIST("030002", HttpStatus.CONFLICT_409, "Target resource already exist."),
  RELATED_RESOURCE_NOT_FOUND("030003", HttpStatus.INTERNAL_SERVER_ERROR_500, "Related resource not found."),
  REGIST_INFORMATION_ERROR("030004", HttpStatus.INTERNAL_SERVER_ERROR_500, "Regist information error."),
  UPDATE_INFORMATION_ERROR("030005", HttpStatus.INTERNAL_SERVER_ERROR_500, "Update information error."),
  DELETE_INFORMATION_ERROR("030006", HttpStatus.INTERNAL_SERVER_ERROR_500, "Delete information error."),
  TRANSITION_STATUS_ERROR("030007", HttpStatus.INTERNAL_SERVER_ERROR_500, "Transition status error."),
  FILE_READ_ERROR("040001", HttpStatus.INTERNAL_SERVER_ERROR_500, "File read error."),
  FILE_WRITE_ERROR("040002", HttpStatus.INTERNAL_SERVER_ERROR_500, "File write error."),
  EXECUTE_FILE_ERROR("040003", HttpStatus.INTERNAL_SERVER_ERROR_500, "Execute file error."),
  FILE_DELETE_ERROR("040004", HttpStatus.INTERNAL_SERVER_ERROR_500, "File delete error."),
  EC_CONNECTION_ERROR("050001", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC connection error."),
  EC_CONTROL_ERROR("050002", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control error."),
  EC_CONTROL_TIMEOUT("050003", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control timeout."),
  EC_CONTROL_ERROR_EM_CONTROL_COMPLETED("050004", HttpStatus.INTERNAL_SERVER_ERROR_500, "EC control error. (FC, EM control completed.)"),
  DELETE_RP_SPINE_ERROR("140001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Delete RP spine error."),
  FC_EC_CONTROL_ERROR_EM_CONTROL_COMPLETED("900001", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC, EC control error. (EM control completed.)"),
  FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED("900002", HttpStatus.INTERNAL_SERVER_ERROR_500, "FC control error. (EC, EM control completed.)"),
  UNDEFINED_ERROR("990001", HttpStatus.INTERNAL_SERVER_ERROR_500, "Undefined error."),
  SYSTEM_CHECK_STATUS_ERROR("990002", HttpStatus.INTERNAL_SERVER_ERROR_500, "System check status error."),;

  private String code;

  private int statusCode;

  private String message;

  private ErrorCode(String code, int statusCode, String message) {
    this.code = code;
    this.statusCode = statusCode;
    this.message = message;
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

}
