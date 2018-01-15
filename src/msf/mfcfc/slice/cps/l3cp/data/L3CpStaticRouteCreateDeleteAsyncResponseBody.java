
package msf.mfcfc.slice.cps.l3cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class L3CpStaticRouteCreateDeleteAsyncResponseBody extends AbstractResponseBody {

  
  @SerializedName("static_route_ids")
  private List<String> staticRouteIdList;

  
  public List<String> getStaticRouteIdList() {
    return staticRouteIdList;
  }

  
  public void setStaticRouteIdList(List<String> staticRouteIdList) {
    this.staticRouteIdList = staticRouteIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
