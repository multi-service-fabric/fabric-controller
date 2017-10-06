package msf.fc.rest.common;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

public class CorsRequestFilter implements ContainerRequestFilter {

  private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

  @Override
  public void filter(ContainerRequestContext request) throws IOException {

    if (request.getRequest().getMethod().equals(HTTP_METHOD_OPTIONS)) {
      request.abortWith(Response.status(Response.Status.OK).build());
    }
  }

}
