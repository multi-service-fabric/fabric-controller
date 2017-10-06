package msf.fc.slice.cps.l3cp.data;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AddressType;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.rest.common.RestRequestValidator;
import msf.fc.slice.cps.l3cp.data.entity.BgpEntity;
import msf.fc.slice.cps.l3cp.data.entity.OspfEntity;
import msf.fc.slice.cps.l3cp.data.entity.StaticRouteEntity;
import msf.fc.slice.cps.l3cp.data.entity.VrrpEntity;

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
  @SerializedName("ipv4_addr")
  private String ipv4Addr;
  @SerializedName("ipv6_addr")
  private String ipv6Addr;
  @SerializedName("ipv4_prefix")
  private Integer ipv4Prefix;
  @SerializedName("ipv6_prefix")
  private Integer ipv6Prefix;
  @SerializedName("bgp")
  private BgpEntity bgp;
  @SerializedName("ospf")
  private OspfEntity ospf;
  @SerializedName("static_routes")
  private List<StaticRouteEntity> staticRouteList;
  @SerializedName("vrrp")
  private VrrpEntity vrrp;

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

  public String getIpv4Addr() {
    return ipv4Addr;
  }

  public void setIpv4Addr(String ipv4Addr) {
    this.ipv4Addr = ipv4Addr;
  }

  public String getIpv6Addr() {
    return ipv6Addr;
  }

  public void setIpv6Addr(String ipv6Addr) {
    this.ipv6Addr = ipv6Addr;
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

  public BgpEntity getBgp() {
    return bgp;
  }

  public void setBgp(BgpEntity bgp) {
    this.bgp = bgp;
  }

  public OspfEntity getOspf() {
    return ospf;
  }

  public void setOspf(OspfEntity ospf) {
    this.ospf = ospf;
  }

  public List<StaticRouteEntity> getStaticRouteList() {
    return staticRouteList;
  }

  public void setStaticRouteList(List<StaticRouteEntity> staticRouteList) {
    this.staticRouteList = staticRouteList;
  }

  public VrrpEntity getVrrp() {
    return vrrp;
  }

  public void setVrrp(VrrpEntity vrrp) {
    this.vrrp = vrrp;
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
      if (ipv4Addr != null) {
        ipv4Addr = ParameterCheckUtil.checkIpv4Address(ipv4Addr);
      }
      if (ipv6Addr != null) {
        ipv6Addr = ParameterCheckUtil.checkIpv6Address(ipv6Addr);
      }
      if (ipv4Addr == null && ipv6Addr == null) {
        String logMsg = "not set both IPv4 and IPv6.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }

      if (ipv4Addr != null) {
        ParameterCheckUtil.checkNotNull(ipv4Prefix);
        ParameterCheckUtil.checkNumberRange(ipv4Prefix, 0, 31);
      } else {
        ipv4Prefix = null;
      }
      if (ipv6Addr != null) {
        ParameterCheckUtil.checkNotNull(ipv6Prefix);
        ParameterCheckUtil.checkNumberRange(ipv6Prefix, 0, 64);
      } else {
        ipv6Prefix = null;
      }
      if (bgp != null) {
        if (bgp.getRoleEnum() == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "bgp.role",
              bgp.getRole());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
        ParameterCheckUtil.checkNotNull(bgp.getNeighborAs());
        if (ipv4Addr != null) {
          bgp.setNeighborIpv4Addr(ParameterCheckUtil.checkIpv4Address(bgp.getNeighborIpv4Addr()));
        } else {
          bgp.setNeighborIpv4Addr(null);
        }
        if (ipv6Addr != null) {
          bgp.setNeighborIpv6Addr(ParameterCheckUtil.checkIpv6Address(bgp.getNeighborIpv6Addr()));
        } else {
          bgp.setNeighborIpv6Addr(null);
        }
      }
      if (ospf != null) {
        ParameterCheckUtil.checkNotNull(ospf.getMetric());
      }
      if (staticRouteList != null && staticRouteList.size() > 0) {
        for (StaticRouteEntity staticRoute : staticRouteList) {
          if (staticRoute.getAddrTypeEnum() == null) {
            String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "staticRoute.addrType",
                staticRoute.getAddrType());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          }
          if (AddressType.IPV4.equals(staticRoute.getAddrTypeEnum())) {
            staticRoute.setAddr(ParameterCheckUtil.checkIpv4Address(staticRoute.getAddr()));
            ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
            ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 32);
            staticRoute.setNextHop(ParameterCheckUtil.checkIpv4Address(staticRoute.getNextHop()));

          } else {
            staticRoute.setAddr(ParameterCheckUtil.checkIpv6Address(staticRoute.getAddr()));
            ParameterCheckUtil.checkNotNull(staticRoute.getPrefix());
            ParameterCheckUtil.checkNumberRange(staticRoute.getPrefix(), 0, 128);
            staticRoute.setNextHop(ParameterCheckUtil.checkIpv6Address(staticRoute.getNextHop()));
          }
        }
      }
      if (vrrp != null) {
        ParameterCheckUtil.checkNotNull(vrrp.getGroupId());
        if (vrrp.getRoleEnum() == null) {
          String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "vrrp.role",
              vrrp.getRole());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
        }
        if (ipv4Addr != null) {
          vrrp.setVirtualIpv4Addr(ParameterCheckUtil.checkIpv4Address(vrrp.getVirtualIpv4Addr()));
        } else {
          vrrp.setVirtualIpv4Addr(null);
        }
        if (ipv6Addr != null) {
          vrrp.setVirtualIpv6Addr(ParameterCheckUtil.checkIpv6Address(vrrp.getVirtualIpv6Addr()));
        } else {
          vrrp.setVirtualIpv6Addr(null);
        }
      }

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
