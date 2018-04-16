
package msf.mfcfc.slice.cps.l3cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L3CpStaticRouteValueEntity {

  @SerializedName("static_route")
  private L3CpStaticRouteEntity staticRoute;

  public L3CpStaticRouteEntity getStaticRoute() {
    return staticRoute;
  }

  public void setStaticRoute(L3CpStaticRouteEntity staticRoute) {
    this.staticRoute = staticRoute;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
