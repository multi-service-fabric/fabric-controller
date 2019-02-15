
package msf.mfcfc.core.scenario;

/**
 * REST response information with the corresponding request information.
 *
 * @author NTT
 *
 */
public class RestResponseData {

  private RestRequestData request;

  private RestResponseBase response;

  /**
   * Constructor.
   *
   * @param request
   *          Request information
   * @param response
   *          Response data
   */
  public RestResponseData(RestRequestData request, RestResponseBase response) {
    super();
    this.request = request;
    this.response = response;
  }

  public RestRequestData getRequest() {
    return request;
  }

  public void setRequest(RestRequestData request) {
    this.request = request;
  }

  public RestResponseBase getResponse() {
    return response;
  }

  public void setResponse(RestResponseBase response) {
    this.response = response;
  }

}
