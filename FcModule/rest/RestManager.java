package msf.fc.rest;

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

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.config.ConfigManager;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.OperationManager;
import msf.fc.rest.common.CorsRequestFilter;
import msf.fc.rest.common.CorsResponseFilter;
import msf.fc.rest.common.RestClient;

public final class RestManager implements FunctionBlockBase {

  private static final RestManager instance = new RestManager();
  private static final MsfLogger logger = MsfLogger.getInstance(RestManager.class);

  private static final String REST_RESOURCE_PACKAGE = "msf.fc.rest";

  private Server jettyServer;

  private RestManager() {

  }

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

      String ipAddress = ConfigManager.getInstance().getRestServerListeningAddress();
      int port = ConfigManager.getInstance().getRestServerListeningPort();
      jettyServer.addConnector(createConnector(jettyServer, ipAddress, port));

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
      resourceConfig.packages(REST_RESOURCE_PACKAGE);
      resourceConfig.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
      resourceConfig.property(ServerProperties.PROVIDER_PACKAGES, new ClientErrorExceptionMapper());

      resourceConfig.register(CorsRequestFilter.class);
      resourceConfig.register(CorsResponseFilter.class);

      ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));

      ServletContextHandler servletContextHandler = new ServletContextHandler();

  private Connector createConnector(Server jettyServer, String ipAddress, int port) {
    try {
      logger.methodStart(new String[] { "jettyServer", "ipAddress", "port" },
          new Object[] { jettyServer, ipAddress, port });
      HttpConfiguration httpConfiguration = new HttpConfiguration();
      httpConfiguration.setSecureScheme("http");

      ServerConnector serverConnector = new ServerConnector(jettyServer, new HttpConnectionFactory(httpConfiguration));
      serverConnector.setHost(ipAddress);
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

}
