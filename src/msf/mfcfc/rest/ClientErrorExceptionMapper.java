
package msf.mfcfc.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import msf.mfcfc.common.log.MsfLogger;

/**
 * Definition class for custom error to return when there is an incomplete
 * client request on the Jetty/Jersey server. <br>
 * Example 1) Incorrect URI "404 Not Found" Example 2) Disallowed method type
 * "405 Method Not Allowed"
 *
 * @author NTT
 *
 */
@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

  private static final MsfLogger logger = MsfLogger.getInstance(ClientErrorExceptionMapper.class);

  @Context
  private HttpServletRequest request;

  @Override
  public Response toResponse(ClientErrorException ex) {

    logger.warn("{0} : METHOD={1}, URI={2}", ex.getMessage(), request.getMethod(), request.getRequestURI());
    logger.debug("detail = {0}", ex.getResponse());

    return Response.status(ex.getResponse().getStatus()).build();

  }

}
