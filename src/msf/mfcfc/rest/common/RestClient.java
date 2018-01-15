
package msf.mfcfc.rest.common;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.TimeoutMap;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;

/**
 * REST client class.
 *
 * @author NTT
 */
public class RestClient {

  private static final MsfLogger logger = MsfLogger.getInstance(RestClient.class);

  private static final String HTTP_SCHEME = "http://";

  private static final String CONTENT_TYPE = "application/json";

  private static final String CONTENT_TYPE_PATCH = "application/json";

  private static HttpClient httpClient = null;

  private static int requestTimeout = 0;

  private static ErrorCode controllTimeoutError = ErrorCode.EC_CONTROL_TIMEOUT;

  private static ErrorCode connectionError = ErrorCode.EC_CONNECTION_ERROR;

  public static Map<String, Long> sendMap = new TimeoutMap<>(0);

  public static synchronized void setSendTimeout(int millis) {
    sendMap = new TimeoutMap<>(millis);
  }

  public static synchronized int getSendCount() {
    return sendMap.size();
  }

  public static synchronized void putRecvCount() {
    long time = System.currentTimeMillis();
    sendMap.put(String.valueOf(time), time);
  }

  /**
   * HTTP client startup procedeure (synchronized to prevent problems caused by
   * lazy initialization).
   *
   * @throws MsfException
   *           Exception
   */
  public static synchronized void startHttpClient() throws MsfException {
    try {
      logger.methodStart();

      if (httpClient != null && !httpClient.isStopped()) {
        String logMsg = "HttpClient is already started.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }

      requestTimeout = ConfigManager.getInstance().getRestClientRequestTimeout();
      httpClient = new HttpClient();

      int waitConnectionTimeout = ConfigManager.getInstance().getRestWaitConnectionTimeout() * 1000;
      httpClient.setConnectTimeout(waitConnectionTimeout);

      httpClient.start();

    } catch (MsfException exp) {
      throw exp;
    } catch (Exception exp) {
      String logMsg = "failed to start HttpClient.";
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * HTTP client shutdown procedure.
   *
   * @throws MsfException
   *           Exception
   */
  public static synchronized void stopHttpClient() throws MsfException {
    try {
      logger.methodStart();

      if (httpClient == null || httpClient.isStopped()) {
        httpClient = null;
        return;
      }

      httpClient.stop();
    } catch (Exception exp) {
      String logMsg = "failed to stop HttpClient.";
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the status of HTTP client.
   *
   * @return true; if HTTP client has been activated /false; otherwise
   */
  public static synchronized boolean checkHttpClientStatus() {
    try {
      logger.methodStart();
      if (httpClient == null) {
        logger.warn("HttpClient instance is null");
        return false;
      }

      String httpClientState = httpClient.getState();
      if (httpClientState == null) {
        logger.warn("HttpClient state is null");
        return false;
      } else {
        switch (httpClientState) {

          case AbstractLifeCycle.STARTED:
            return true;

          default:
            logger.warn("HttpClient state = {0}", httpClientState);
            return false;
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Method to send a REST message to the specified address.
   *
   * @param httpMethod
   *          HTTP method
   * @param targetUriPath
   *          URI path for HTTP request target (including URI parameter,
   *          optional parameter, but excluding address, port)
   * @param restRequest
   *          Parameter of HTTP request
   * @param ipAddress
   *          Destination IP address
   * @param port
   *          Destination port number
   * @return HTTP request response data
   * @throws MsfException
   *           Exception
   */
  public static RestResponseBase sendRequest(HttpMethod httpMethod, String targetUriPath, RestRequestBase restRequest,
      String ipAddress, int port) throws MsfException {
    try {
      logger.methodStart(new String[] { "httpMethod", "targetUriPath", "restRequest" },
          new Object[] { httpMethod, targetUriPath, restRequest });

      if (httpMethod == null || targetUriPath == null) {

        String logMsg = MessageFormat.format("required parameter is null. httpMethod = {0}  targetUriPath = {1}",
            httpMethod, targetUriPath);
        logger.error(logMsg);
        throw new IllegalArgumentException(logMsg);
      }

      if (httpClient == null) {
        String logMsg = "unable to send request.HttpClient instance is null.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, logMsg);
      }
      switch (httpMethod) {
        case POST:
        case GET:
        case PUT:
        case DELETE:
        case PATCH:
          String jsonBody = null;
          if (restRequest != null) {
            jsonBody = restRequest.getRequestBody();
          }

          String targetUri = getTargetUri(targetUriPath, ipAddress, port);

          Request request = createRequest(httpMethod, targetUri, jsonBody);

          ContentResponse contentResponse = request.send();
          putRecvCount();
          return createResponse(contentResponse);
        default:

          String logMsg = MessageFormat.format("unsupport httpMethod. httpMethod = {0}", httpMethod.name());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
    } catch (TimeoutException exp) {
      String logMsg = MessageFormat.format("timeout. httpMethod = {0}, targetUriPath = {1}", httpMethod.name(),
          targetUriPath);
      logger.error(logMsg, exp);
      throw new MsfException(controllTimeoutError, logMsg);
    } catch (InterruptedException | ExecutionException exp) {
      String logMsg = MessageFormat.format("connection error. httpMethod = {0}, targetUriPath = {1}", httpMethod.name(),
          targetUriPath);
      logger.error(logMsg, exp);
      throw new MsfException(connectionError, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private static Request createRequest(HttpMethod httpMethod, String targetUri, String jsonBody) {
    try {
      logger.methodStart(new String[] { "httpMethod", "targetUri", "jsonBody" },
          new Object[] { httpMethod, targetUri });
      Request request = httpClient.newRequest(targetUri).timeout(requestTimeout, TimeUnit.SECONDS)
          .method(httpMethod.name());

      switch (httpMethod) {
        case POST:
        case PUT:
          if (jsonBody != null) {
            request.content(new StringContentProvider(jsonBody), CONTENT_TYPE);
          }
          break;
        case PATCH:
          if (jsonBody != null) {
            request.content(new StringContentProvider(jsonBody), CONTENT_TYPE_PATCH);
          }
          break;
        default:
          break;
      }
      logger.debug("targetUri = {0}", targetUri);
      logger.debug("httpMethod = {0}", httpMethod.name());
      logger.debug("jsonBody = {0}", jsonBody);
      return request;
    } finally {
      logger.methodEnd();
    }
  }

  private static String getTargetUri(String targetUriPath, String ipAddress, int port) {
    try {
      logger.methodStart(new String[] { "targetUriPath", "ipAddress", "port" },
          new Object[] { targetUriPath, ipAddress, port });

      return HTTP_SCHEME + ipAddress + ":" + port + targetUriPath;
    } finally {
      logger.methodEnd();
    }
  }

  private static RestResponseBase createResponse(ContentResponse contentResponse) {
    try {
      logger.methodStart(new String[] { "contentResponse" }, new Object[] { contentResponse });
      RestResponseBase restResponseBase = new RestResponseBase(contentResponse.getStatus(),
          contentResponse.getContentAsString());
      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set the error code of timeout/control error that occurs at the process of
   * REST transmission.
   *
   * @param controllTimeoutError
   *          Timeout error
   * @param connectionError
   *          Control error
   */
  public static void setErrorCodes(ErrorCode controllTimeoutError, ErrorCode connectionError) {
    RestClient.controllTimeoutError = controllTimeoutError;
    RestClient.connectionError = connectionError;
  }
}
