package msf.fc.rest;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes.Type;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.ErrorResponseData;
import msf.fc.core.scenario.RestResponseBase;

public class MsfErrorHandler extends ErrorHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MsfErrorHandler.class);

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    logger.methodStart();



    response.setHeader(HttpHeader.CACHE_CONTROL.asString(), "must-revalidate,no-cache,no-store");
    this.generateAcceptableResponse(baseRequest, request, response, response.getStatus(),
        baseRequest.getResponse().getReason());

    logger.methodEnd();
  }

  @Override
  protected void generateAcceptableResponse(Request baseRequest, HttpServletRequest request,
      HttpServletResponse response, int code, String message, String mimeType) throws IOException {

    baseRequest.setHandled(true);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    Writer writer = response.getWriter();
    if (writer != null) {
      response.setContentType(Type.APPLICATION_JSON.asString());
      this.handleErrorPage(request, writer, code, message);

    } else {
      logger.warn("Error message cannot print to response body.");
    }

  }

  @Override
  protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message)
      throws IOException {

    logger.methodStart(new String[] { "code", "message" }, new Object[] { code, message });

    RestResponseBase rest = new ErrorResponseData(ErrorCode.UNDEFINED_ERROR, SystemInterfaceType.EXTERNAL);
    message = rest.getResponseBody();

    Throwable th = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (th != null) {
      logger.error("Jetty Server was catched Exception.", th);
    }

    writer.write(message);
    logger.debug("jsonMessage = {0}", message);
    logger.methodEnd();
  }

}
