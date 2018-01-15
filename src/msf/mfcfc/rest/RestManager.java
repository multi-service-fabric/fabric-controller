
package msf.mfcfc.rest;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.rest.common.CorsRequestFilter;
import msf.mfcfc.rest.common.CorsResponseFilter;
import msf.mfcfc.rest.common.RestClient;

/**
 * Management class for REST function block.
 *
 * @author NTT
 *
 */

public class RestManager implements FunctionBlockBase {

  private static final RestManager instance = new RestManager();

  private static final MsfLogger logger = MsfLogger.getInstance(RestManager.class);

  public static final String REST_RESOURCE_PACKAGE_FC = "msf.fc.rest";

  public static final String REST_RESOURCE_PACKAGE_MFC = "msf.mfc.rest";

  private String restResourcePackage = REST_RESOURCE_PACKAGE_FC;

  private Server jettyServer;

  /**
   * Get the instance of RestManager.
   *
   * @return RestManager instance
   */
  public static RestManager getInstance() {
    try {
      logger.methodStart();
      return instance;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean start() {
    try {
      logger.methodStart();
      jettyServer = new Server();

      jettyServer.setHandler(createHandler());
      jettyServer.addBean(new MsfErrorHandler());

      int port = ConfigManager.getInstance().getRestServerListeningPort();
      jettyServer.addConnector(createConnector(jettyServer, port));

      jettyServer.start();

      RestClient.startHttpClient();

      return true;
    } catch (Exception exp) {
      logger.error("Failed to start rest manager.", exp);

      return false;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();

      boolean jettyServerStatus = checkJettyServerStatus();
      boolean httpClientStatus = RestClient.checkHttpClientStatus();

      return jettyServerStatus && httpClientStatus;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();
      OperationManager.getInstance().hasNoExecutingOperations(true);

      RestClient.stopHttpClient();

      jettyServer.stop();

      return true;
    } catch (Exception exp) {
      logger.error("Failed to stop rest manager.", exp);
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private Handler createHandler() {
    try {
      logger.methodStart();

      ResourceConfig resourceConfig = new ResourceConfig();
      resourceConfig.packages(restResourcePackage);
      resourceConfig.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
      resourceConfig.property(ServerProperties.PROVIDER_PACKAGES, new ClientErrorExceptionMapper());

      resourceConfig.register(CorsRequestFilter.class);
      resourceConfig.register(CorsResponseFilter.class);

      ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));

      ServletContextHandler servletContextHandler = new ServletContextHandler();

      servletContextHandler.addServlet(servletHolder, "/*");
      servletContextHandler.setErrorHandler(new MsfErrorHandler());

      return servletContextHandler;

    } finally {
      logger.methodEnd();
    }
  }

  private Connector createConnector(Server jettyServer, int port) {
    try {
      logger.methodStart(new String[] { "jettyServer", "port" }, new Object[] { jettyServer, port });
      HttpConfiguration httpConfiguration = new HttpConfiguration();
      httpConfiguration.setSecureScheme("http");

      ServerConnector serverConnector = new ServerConnector(jettyServer, new HttpConnectionFactory(httpConfiguration));

      serverConnector.setPort(port);

      return serverConnector;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkJettyServerStatus() {
    try {
      logger.methodStart();
      if (jettyServer == null) {
        logger.warn("JettyServer instance is null");
        return false;
      }

      String jettyState = jettyServer.getState();
      if (jettyState == null) {
        logger.warn("Jetty state is null");
        return false;
      } else {
        switch (jettyState) {
          case AbstractLifeCycle.STARTED:
            return true;
          default:
            logger.warn("Jetty state =" + jettyState);
            return false;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set the package path of REST handler.
   *
   * @param restResourcePackage
   *          REST handler package path
   */
  public void setRestResourcePackage(String restResourcePackage) {
    this.restResourcePackage = restResourcePackage;
  }

}
