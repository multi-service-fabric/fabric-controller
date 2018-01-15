
package msf.mfcfc.slice.cps.l3cp.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.AddressType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpBgpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosCreateEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpVrrpEntity;


public class L3CpCreateRequestBody implements RestRequestValidator {

  
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpCreateRequestBody.class);

  
  @SerializedName("cluster_id")
  private String clusterId;

  
  @SerializedName("edge_point_id")
  private String edgePointId;

  
  @SerializedName("vlan_id")
  private Integer vlanId;

  
  @SerializedName("mtu")
  private Integer mtu;

  
  @SerializedName("cp_id")
  private String cpId;

  
  @SerializedName("qos")
  private L3CpQosCreateEntity qos;

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("ipv6_address")
  private String ipv6Address;

  
  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;

  
  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;

  
  @SerializedName("bgp")
  private L3CpBgpEntity bgp;

  
  @SerializedName("static_routes")
  private List<L3CpStaticRouteEntity> staticRouteList;

  
  @SerializedName("vrrp")
  private L3CpVrrpEntity vrrp;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  public String getClusterId() {
    return clusterId;
  }

  
  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  
  public String getEdgePointId() {
    return edgePointId;
  }

  
  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  
  public Integer getVlanId() {
    return vlanId;
  }

  
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  
  public Integer getMtu() {
    return mtu;
  }

  
  public void setMtu(Integer mtu) {
    this.mtu = mtu;
  }

  
  public String getCpId() {
    return cpId;
  }

  
  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  
  public L3CpQosCreateEntity getQos() {
    return qos;
  }

  
  public void setQos(L3CpQosCreateEntity qos) {
    this.qos = qos;
  }

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  
  public String getIpv6Address() {
    return ipv6Address;
  }

  
  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  
  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  
  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  
  public Integer getIpv6Prefix() {
    return ipv6Prefix;
  }

  
  public void setIpv6Prefix(Integer ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  
  public L3CpBgpEntity getBgp() {
    return bgp;
  }

  
  public void setBgp(L3CpBgpEntity bgp) {
    this.bgp = bgp;
  }

  
  public List<L3CpStaticRouteEntity> getStaticRouteList() {
    return staticRouteList;
  }

  
  public void setStaticRouteList(List<L3CpStaticRouteEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  
  public L3CpVrrpEntity getVrrp() {
    return vrrp;
  }

  
  public void setVrrp(L3CpVrrpEntity vrrp) {
    this.vrrp = vrrp;
  }

  
  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  
  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNullAndLength(clusterId);

      ParameterCheckUtil.checkNotNullAndLength(edgePointId);

      ParameterCheckUtil.checkNotNull(vlanId);

      ParameterCheckUtil.checkNumberRange(vlanId, 0, 4096);

      ParameterCheckUtil.checkNotNull(mtu);

      if (cpId != null) {
        ParameterCheckUtil.checkIdSpecifiedByUri(cpId);
      }


      if (ipv4Address != null) {
        ipv4Address = ParameterCheckUtil.checkIpv4Address(ipv4Address);
      }

      if (ipv6Address != null) {
        ipv6Address = ParameterCheckUtil.checkIpv6Address(ipv6Address);
      }

      if (ipv4Address == null && ipv6Address == null) {
        String logMsg = "not set both IPv4 and IPv6.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      if (ipv4Address != null) {
        ParameterCheckUtil.checkNotNull(ipv4Prefix);

        ParameterCheckUtil.checkNumberRange(ipv4Prefix, 0, 31);
      } else {
        ipv4Prefix = null;
      }

      if (ipv6Address != null) {
        ParameterCheckUtil.checkNotNull(ipv6Prefix);

        ParameterCheckUtil.checkNumberRange(ipv6Prefix, 0, 64);
      } else {
        ipv6Prefix = null;
      }

      if (bgp != null) {
        validateBgp();
      }

      if (staticRouteList != null && staticRouteList.size() > 0) {
        validateStaticRouteList();
      }

      if (vrrp != null) {
        validateVrrp();
      }


    } finally {
      logger.methodEnd();
    }
  }

  private void validateBgp() throws MsfException {


    ParameterCheckUtil.checkNotNull(bgp.getRoleEnum());

    ParameterCheckUtil.checkNotNull(bgp.getNeighborAs());

    if (ipv4Address != null) {
      bgp.setNeighborIpv4Address(ParameterCheckUtil.checkIpv4Address(bgp.getNeighborIpv4Address()));
    } else {
      bgp.setNeighborIpv4Address(null);
    }

    if (ipv6Address != null) {
      bgp.setNeighborIpv6Address(ParameterCheckUtil.checkIpv6Address(bgp.getNeighborIpv6Address()));
    } else {
      bgp.setNeighborIpv6Address(null);
    }
  }

  private void validateStaticRouteList() throws MsfException {

    for (L3CpStaticRouteEntity staticRoute : staticRouteList) {
      validateStaticRoute(staticRoute);
    }
  }

  private void validateStaticRoute(L3CpStaticRouteEntity staticRoute) throws MsfException {


    ParameterCheckUtil.checkNotNull(staticRoute.getAddrTypeEnum());

    if (AddressType.IPV4.equals(staticRoute.getAddrTypeEnum())) {
      staticRoute.setAddress(ParameterCheckUtil.checkIpv4Address(staticRoute.getAddress()));

      ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
      ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 32);

      staticRoute.setNextHop(ParameterCheckUtil.checkIpv4Address(staticRoute.getNextHop()));
    } else {
      staticRoute.setAddress(ParameterCheckUtil.checkIpv6Address(staticRoute.getAddress()));

      ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
      ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 128);

      staticRoute.setNextHop(ParameterCheckUtil.checkIpv6Address(staticRoute.getNextHop()));
    }
  }

  private void validateVrrp() throws MsfException {

    ParameterCheckUtil.checkNotNull(vrrp.getGroupId());

    ParameterCheckUtil.checkNotNull(vrrp.getRoleEnum());

    if (ipv4Address != null) {
      vrrp.setVirtualIpv4Address(ParameterCheckUtil.checkIpv4Address(vrrp.getVirtualIpv4Address()));
    } else {
      vrrp.setVirtualIpv4Address(null);
    }
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
