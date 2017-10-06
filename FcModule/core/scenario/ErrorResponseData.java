package msf.fc.core.scenario;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.rest.common.JsonUtil;

public class ErrorResponseData extends RestResponseBase {

  public ErrorResponseData(ErrorCode errCode, SystemInterfaceType systemIfType) {

    ErrorResponseBody responseBody = new ErrorResponseBody();

    responseBody.setErrorCode(errCode.getCode());
    switch (systemIfType) {
      case INTERNAL:
        break;
      case EXTERNAL:
        responseBody.setErrorMessage(errCode.getMessage());
        break;
      default:
        break;
    }

    String responseBodyJson = null;
    responseBodyJson = JsonUtil.toJson(responseBody);

    this.httpStatusCode = errCode.getStatusCode();
    this.responseBody = responseBodyJson;

  }

  class ErrorResponseBody extends AbstractResponseBody {

  }
}
