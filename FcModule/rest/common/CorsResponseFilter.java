package msf.fc.rest.common;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

public class CorsResponseFilter implements ContainerResponseFilter {

  private static final String CORS_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  private static final String CORS_ALLOW_ORIGIN_VALUE = "*";
  private static final String CORS_ALLOW_METHODS = "Access-Control-Allow-Methods";
  private static final String CORS_ALLOW_METHODS_VALUE = "GET, POST, DELETE, PUT";
  private static final String CORS_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  private static final String CORS_ALLOW_HEADERS_VALUE = "Content-Type";

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {

    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add(CORS_ALLOW_ORIGIN, CORS_ALLOW_ORIGIN_VALUE);
    headers.add(CORS_ALLOW_METHODS, CORS_ALLOW_METHODS_VALUE);
    headers.add(CORS_ALLOW_HEADERS, CORS_ALLOW_HEADERS_VALUE);
  }

}
