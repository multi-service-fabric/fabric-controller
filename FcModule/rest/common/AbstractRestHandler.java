package msf.fc.rest.common;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;

public abstract class AbstractRestHandler {
  @Context
  protected HttpServletRequest httpServletRequest;

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractRestHandler.class);

  protected Response createResponse(RestResponseBase restResponseBase) {
    try {
      logger.methodStart(new String[] { "restResponseBase" }, new Object[] { restResponseBase });
      Response response = Response.status(restResponseBase.getHttpStatusCode())
          .entity(restResponseBase.getResponseBody()).build();
      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());
      return response;
    } finally {
      logger.methodEnd();

    }
  }

  protected void loggingRequestReceived() {
    logger.info("Http request received. uri = {0}", httpServletRequest.getRequestURI());
    logger.info("Http request received. remote address = {0}", httpServletRequest.getRemoteAddr());
  }

  protected void loggingReturnedResponse(int httpStatusCode) {
    logger.info("Http response returned. status code = {0}", httpStatusCode);
  }

  protected void loggingRequestJsonBody(String jsonBody) {
    logger.debug("Http request json parameter = {0}", jsonBody);
  }

  protected void loggingResponseJsonBody(String jsonBody) {
    logger.debug("Http request json parameter = {0}", jsonBody);
  }

  protected String getReuqestUri() {
    return httpServletRequest.getRequestURI();
  }

  protected String getHttpMethod() {
    return httpServletRequest.getMethod();
  }

}
