
package msf.mfcfc.rest.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.TimeoutMap;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;

/**
 * Implementation class for REST handler common process.
 *
 * @author NTT
 *
 */
public abstract class AbstractRestHandler {

  @Context
  protected HttpServletRequest httpServletRequest;

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractRestHandler.class);

  public static Map<String, Long> recvMap = new TimeoutMap<>(10000);

  public static synchronized void setRecvTimeout(int millis) {
    recvMap = new TimeoutMap<>(millis);
  }

  public static synchronized int getRecvCount() {
    return recvMap.size();
  }

  public static synchronized void putRecvCount() {
    long time = System.currentTimeMillis();
    recvMap.put(String.valueOf(time), time);
  }

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
    logger.info("Http request received. query = {0}", httpServletRequest.getQueryString());
    logger.info("Http request received. remote address = {0}", httpServletRequest.getRemoteAddr());
    putRecvCount();
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

  protected String getQueryString() {
    return httpServletRequest.getQueryString();
  }

  protected void setCommonData(RestRequestBase restRequestBase) {
    restRequestBase.setRequestUri(getReuqestUri());
    restRequestBase.setRequestMethod(getHttpMethod());
    restRequestBase.setRequestQueryString(getQueryString());
    restRequestBase.setSourceIpAddress(httpServletRequest.getRemoteAddr());

  }

}
