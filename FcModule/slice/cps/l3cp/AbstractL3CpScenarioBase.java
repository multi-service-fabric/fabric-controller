package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.constant.L3ProtocolType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpBgpOption;
import msf.fc.common.data.L3CpOspfOption;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpVrrpOption;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.slice.cps.AbstractCpScenarioBase;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.entity.BgpEntity;
import msf.fc.slice.cps.l3cp.data.entity.L3CpEntity;
import msf.fc.slice.cps.l3cp.data.entity.OspfEntity;
import msf.fc.slice.cps.l3cp.data.entity.StaticRouteEntity;
import msf.fc.slice.cps.l3cp.data.entity.VrrpEntity;

public abstract class AbstractL3CpScenarioBase<T extends RestRequestBase> extends AbstractCpScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractL3CpScenarioBase.class);
  protected L3CpRequest request;

  protected L3CpEntity createL3CpEntity(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      L3CpEntity l3CpEntity = new L3CpEntity();
      l3CpEntity.setClusterId(l3Cp.getEdgePoint().getId().getSwClusterId());
      l3CpEntity.setCpId(l3Cp.getId().getCpId());
      l3CpEntity.setEdgePointId(String.valueOf(l3Cp.getEdgePoint().getId().getEdgePointId()));
      l3CpEntity.setIpv4Addr(l3Cp.getIpv4Address());
      l3CpEntity.setIpv4Prefix(l3Cp.getIpv4Prefix());
      l3CpEntity.setIpv6Addr(l3Cp.getIpv6Address());
      l3CpEntity.setIpv6Prefix(l3Cp.getIpv6Prefix());
      l3CpEntity.setMtu(l3Cp.getMtu());
      l3CpEntity.setOperationStatusEnum(l3Cp.getOperationStatusEnum());
      l3CpEntity.setReservationStatusEnum(l3Cp.getReservationStatusEnum());
      l3CpEntity.setSliceId(l3Cp.getId().getSliceId());
      l3CpEntity.setStatusEnum(l3Cp.getStatusEnum());
      l3CpEntity.setVlanId(l3Cp.getVlanId());
      List<String> supportProtocolList = new ArrayList<>();
      if (l3Cp.getL3CpBgpOption() != null) {
        L3CpBgpOption bgp = l3Cp.getL3CpBgpOption();
        BgpEntity bgpEntity = new BgpEntity();
        bgpEntity.setNeighborAs(bgp.getNeighborAs());
        bgpEntity.setNeighborIpv4Addr(bgp.getNeighborIpv4Address());
        bgpEntity.setNeighborIpv6Addr(bgp.getNeighborIpv6Address());
        bgpEntity.setRoleEnum(bgp.getRoleEnum());
        l3CpEntity.setBgp(bgpEntity);
        supportProtocolList.add(L3ProtocolType.BGP.getMessage());
      }
      if (l3Cp.getL3CpOspfOption() != null) {
        L3CpOspfOption ospf = l3Cp.getL3CpOspfOption();
        OspfEntity ospfEntity = new OspfEntity();
        ospfEntity.setMetric(ospf.getMetric());
        l3CpEntity.setOspf(ospfEntity);
        supportProtocolList.add(L3ProtocolType.OSPF.getMessage());
      }
      if (l3Cp.getL3CpVrrpOption() != null) {
        L3CpVrrpOption vrrp = l3Cp.getL3CpVrrpOption();
        VrrpEntity vrrpEntity = new VrrpEntity();
        vrrpEntity.setGroupId(vrrp.getGroupId());
        vrrpEntity.setRoleEnum(vrrp.getRoleEnum());
        vrrpEntity.setVirtualIpv4Addr(vrrp.getVirtualIpv4Address());
        vrrpEntity.setVirtualIpv6Addr(vrrp.getVirtualIpv6Address());
        l3CpEntity.setVrrp(vrrpEntity);
        supportProtocolList.add(L3ProtocolType.VRRP.getMessage());
      }
      if (l3Cp.getL3CpStaticRouteOptions() != null && l3Cp.getL3CpStaticRouteOptions().size() > 0) {
        List<L3CpStaticRouteOption> staticRouteList = l3Cp.getL3CpStaticRouteOptions();
        List<StaticRouteEntity> staticRouteEntityList = new ArrayList<>();
        for (L3CpStaticRouteOption staticRoute : staticRouteList) {
          StaticRouteEntity staticRouteEntity = new StaticRouteEntity();
          staticRouteEntity.setAddr(staticRoute.getId().getDestinationAddress());
          staticRouteEntity.setAddrTypeEnum(staticRoute.getId().getAddressTypeEnum());
          staticRouteEntity.setNextHop(staticRoute.getId().getNexthopAddress());
          staticRouteEntity.setPrefix(staticRoute.getId().getPrefix());
          staticRouteEntityList.add(staticRouteEntity);
        }
        l3CpEntity.setStaticRouteList(staticRouteEntityList);
        supportProtocolList.add(L3ProtocolType.STATIC.getMessage());
      }
      l3CpEntity.setSupportProtocolList(supportProtocolList);

      return l3CpEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL3CpPresence(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l3Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

}