package msf.event;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * CORS(Cross-Origin Resource Sharing)用リクエストフィルタクラス.
 *
 * @author NTT
 *
 */
public class CorsRequestFilter implements ContainerRequestFilter {

  /**
   * CORS(Cross-Origin Resource Sharing)においてサーバ(FC)にアクセスを許容するアクセス元.
   */
  private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

  /**
   * JAX-RS リクエストフィルタ実装.
   * HTTPメソッドがOPTIONSの場合はシナリオ処理せずにHTTP 200 OKを返す.
   */
  @Override
  public void filter(ContainerRequestContext request) throws IOException {

    if (request.getRequest().getMethod().equals(HTTP_METHOD_OPTIONS)) {
      request.abortWith(Response.status(Response.Status.OK).build());
    }
  }

}