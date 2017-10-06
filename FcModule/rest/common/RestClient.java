package msf.fc.rest.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.SwClusterDao;

public class RestClient {
  private static final MsfLogger logger = MsfLogger.getInstance(RestClient.class);
  private static final String CONTENT_TYPE = "application/json";
  private static HttpClient httpClient = null;
  private static List<String> ecAuthorityList = new ArrayList<>();

  private static int requestTimeout = 0;

  public static RestResponseBase sendRequest(HttpMethod httpMethod, String targetUriPath, RestRequestBase restRequest)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "httpMethod", "targetUriPath", "restRequest" },
          new Object[] { httpMethod, targetUriPath, restRequest });

      if (httpMethod == null || targetUriPath == null) {
        String logMsg = MessageFormat.format("required parameter is null. httpMethod = {0}  targetUriPath = {1}",
            httpMethod, targetUriPath);
        logger.error(logMsg);
        throw new IllegalArgumentException(logMsg);
      }
      if (ecAuthorityList.isEmpty() || httpClient == null) {
        String logMsg = logger.error("unable to send request. ecAuthorityList size = {0}", ecAuthorityList.size());
        throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, logMsg);
      }
      switch (httpMethod) {
        case DELETE:
          String jsonBody = null;
          if (restRequest != null) {
            jsonBody = restRequest.getRequestBody();
          }
          Request request = createRequest(httpMethod, targetUriPath, jsonBody);
          ContentResponse contentResponse = request.send();
          return createResponse(contentResponse);
        default:
          String logMsg = MessageFormat.format("unsupport httpMethod. httpMethod = {0}", httpMethod);
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      String logMsg = MessageFormat.format("timeout. httpMethod = {0}, targetUriPath = {1}", httpMethod.name(),
          targetUriPath);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.EC_CONTROL_TIMEOUT, logMsg);
      String logMsg = MessageFormat.format("connection error. httpMethod = {0}, targetUriPath = {1}", httpMethod.name(),
          targetUriPath);
      logger.error(logMsg, exp);
      throw new MsfException(ErrorCode.EC_CONNECTION_ERROR, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  public static synchronized void startHttpClient() throws MsfException {
    try {
      logger.methodStart();
      if (httpClient != null && !httpClient.isStopped()) {
        String logMsg = "HttpClient is already started.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      setupEcAuthorityList();

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

  private static void setupEcAuthorityList() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      SwClusterDao swClusterDao = new SwClusterDao();
      List<SwCluster> swClusterList = swClusterDao.readList(sessionWrapper);

      ecAuthorityList.clear();
      for (SwCluster swCluster : swClusterList) {
        String ecControlAddress = swCluster.getEcControlAddress();
        int ecControlPort = swCluster.getEcControlPort();

        String ecAuthority = ecControlAddress + ":" + ecControlPort;
        ecAuthorityList.add(ecAuthority);
      }

    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private static Request createRequest(HttpMethod httpMethod, String targetUriPath, String jsonBody) {
    try {
      logger.methodStart(new String[] { "httpMethod", "targetUriPath", "jsonBody" },
          new Object[] { httpMethod, targetUriPath, jsonBody });
      String targetUri = getTargetUri(targetUriPath);
      Request request = httpClient.newRequest(targetUri).timeout(requestTimeout, TimeUnit.SECONDS).method(httpMethod);
      switch (httpMethod) {
        case PUT:
          if (jsonBody != null) {
            request.content(new StringContentProvider(jsonBody), CONTENT_TYPE);
          }
          break;
        default:
          break;
      }
      logger.debug("targetUril = {0}", targetUri);
      logger.debug("httpMethod = {0}", httpMethod.asString());
      logger.debug("jsonBody = {0}", jsonBody);
      return request;
    } finally {
      logger.methodEnd();
    }
  }

  private static String getTargetUri(String targetUriPath) {
    try {
      logger.methodStart(new String[] { "targetUriPath" }, new Object[] { targetUriPath });
      String ecAuthority = ecAuthorityList.get(0);
      return HTTP_SCHEME + ecAuthority + targetUriPath;
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
}
