package msf.event;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

/**
 * CORS(Cross-Origin Resource Sharing)用レスポンスフィルタクラス.
 *
 * @author NTT
 *
 */
public class CorsResponseFilter implements ContainerResponseFilter {

  /**
   * CORS(Cross-Origin Resource Sharing)においてサーバ(FC)にアクセスを許容するアクセス元.
   */
  private static final String CORS_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  private static final String CORS_ALLOW_ORIGIN_VALUE = "*";
  /**
   * CORS(Cross-Origin Resource Sharing)においてサーバ(FC)にアクセスを許容するHTTPメソッド.
   */
  private static final String CORS_ALLOW_METHODS = "Access-Control-Allow-Methods";
  private static final String CORS_ALLOW_METHODS_VALUE = "GET, POST, DELETE, PUT, PATCH";
  /**
   * CORS(Cross-Origin Resource Sharing)においてサーバ(FC)にアクセスを許容するHTTPヘッダ.
   */
  private static final String CORS_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  private static final String CORS_ALLOW_HEADERS_VALUE = "Content-Type";

  /**
   * JAX-RS レスポンスフィルタ実装. クロスドメイン制約対応の為のHTTPヘッダを付与する.
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {

    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add(CORS_ALLOW_ORIGIN, CORS_ALLOW_ORIGIN_VALUE);
    headers.add(CORS_ALLOW_METHODS, CORS_ALLOW_METHODS_VALUE);
    headers.add(CORS_ALLOW_HEADERS, CORS_ALLOW_HEADERS_VALUE);
  }

}
