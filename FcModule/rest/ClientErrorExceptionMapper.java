package msf.fc.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import msf.fc.common.log.MsfLogger;

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
