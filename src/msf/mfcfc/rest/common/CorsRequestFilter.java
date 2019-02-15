
package msf.mfcfc.rest.common;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * Request filter class for CORS (Cross-Origin Resource Sharing).
 *
 * @author NTT
 *
 */
public class CorsRequestFilter implements ContainerRequestFilter {

  private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

  /**
   * Implementation of the JAX-RS request filter. Returns HTTP 200 OK without
   * processing scenario when HTTP method is OPTIONS.
   */
  @Override
  public void filter(ContainerRequestContext request) throws IOException {

    if (request.getRequest().getMethod().equals(HTTP_METHOD_OPTIONS)) {
      request.abortWith(Response.status(Response.Status.OK).build());
    }
  }

}
