
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcL3Cp;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfStaticRouteEcEntity;
import msf.fc.slice.cps.FcAbstractCpScenarioBase;
import msf.mfcfc.common.constant.L3ProtocolType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpBgpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpVrrpEntity;

/**
 * Abstract class to implement the common process of L3CP-related processing in
 * slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractL3CpScenarioBase<T extends RestRequestBase> extends FcAbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL3CpScenarioBase.class);

  protected L3CpRequest request;

  protected void checkL3CpPresence(FcL3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l3Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

  protected L3CpEntity createL3CpEntity(SessionWrapper sessionWrapper, FcL3Cp l3Cp, VlanIfEcEntity vlanIfEcEntity)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp", "vlanIfResponseBody" }, new Object[] { l3Cp, vlanIfEcEntity });
      L3CpEntity l3CpEntity = new L3CpEntity();
      l3CpEntity.setClusterId(
          String.valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId()));
      l3CpEntity.setCpId(l3Cp.getId().getCpId());
      l3CpEntity.setEdgePointId(String.valueOf(l3Cp.getEdgePoint().getEdgePointId()));
      l3CpEntity.setIpv4Address(vlanIfEcEntity.getIpv4Address());
      l3CpEntity.setIpv4Prefix(
          vlanIfEcEntity.getIpv4Prefix() != null ? Integer.valueOf(vlanIfEcEntity.getIpv4Prefix()) : null);
      l3CpEntity.setIpv6Address(vlanIfEcEntity.getIpv6Address());
      l3CpEntity.setIpv6Prefix(
          vlanIfEcEntity.getIpv6Prefix() != null ? Integer.valueOf(vlanIfEcEntity.getIpv6Prefix()) : null);
      l3CpEntity.setMtu(vlanIfEcEntity.getMtu());
      l3CpEntity.setSliceId(l3Cp.getId().getSliceId());
      l3CpEntity.setVlanId(vlanIfEcEntity.getVlanId() == null ? 0 : Integer.valueOf(vlanIfEcEntity.getVlanId()));

      List<String> supportProtocolList = new ArrayList<>();

      if (vlanIfEcEntity.getBgp() != null) {
        L3CpBgpEntity bgp = new L3CpBgpEntity();
        bgp.setNeighborAs(vlanIfEcEntity.getBgp().getNeighborAs());
        bgp.setNeighborIpv4Address(vlanIfEcEntity.getBgp().getNeighborIpv4Addr());
        bgp.setNeighborIpv6Address(vlanIfEcEntity.getBgp().getNeighborIpv6Addr());
        bgp.setRole(vlanIfEcEntity.getBgp().getRole());
        l3CpEntity.setBgp(bgp);
        supportProtocolList.add(L3ProtocolType.BGP.getMessage());
      }

      if (vlanIfEcEntity.getVrrp() != null) {
        L3CpVrrpEntity vrrp = new L3CpVrrpEntity();
        vrrp.setGroupId(vlanIfEcEntity.getVrrp().getGroupId());
        vrrp.setRole(vlanIfEcEntity.getVrrp().getRole());
        vrrp.setVirtualIpv4Address(vlanIfEcEntity.getVrrp().getVirtualIpv4Addr());
        l3CpEntity.setVrrp(vrrp);
        supportProtocolList.add(L3ProtocolType.VRRP.getMessage());
      }

      if (vlanIfEcEntity.getStaticRouteList() != null) {
        List<L3CpStaticRouteEntity> staticRouteList = new ArrayList<>();
        for (VlanIfStaticRouteEcEntity staticRouteEc : vlanIfEcEntity.getStaticRouteList()) {
          L3CpStaticRouteEntity staticRoute = new L3CpStaticRouteEntity();
          staticRoute.setAddress(staticRouteEc.getAddress());
          staticRoute.setAddrType(staticRouteEc.getAddressType());
          staticRoute.setNextHop(staticRouteEc.getNextHop());
          staticRoute.setPrefix(staticRouteEc.getPrefix());
          staticRouteList.add(staticRoute);
        }
        l3CpEntity.setStaticRouteList(staticRouteList);
        supportProtocolList.add(L3ProtocolType.STATIC.getMessage());
      }
      l3CpEntity.setSupportProtocolList(supportProtocolList);
      l3CpEntity.setTrafficThreshold(l3Cp.getTrafficThreshold());

      L3CpQosEntity qos = new L3CpQosEntity();

      qos.setEgressQueueCapabilityList(vlanIfEcEntity.getQos().getCapability().getEgressMenuList());
      qos.setRemarkCapabilityList(vlanIfEcEntity.getQos().getCapability().getRemarkMenuList());
      qos.setRemark(vlanIfEcEntity.getQos().getCapability().getRemark());
      qos.setShaping(vlanIfEcEntity.getQos().getCapability().getShaping());

      if (vlanIfEcEntity.getQos().getSetValue() != null) {
        qos.setEgressQueueMenu(vlanIfEcEntity.getQos().getSetValue().getEgressMenu());
        qos.setRemarkMenu(vlanIfEcEntity.getQos().getSetValue().getRemarkMenu());
        qos.setIngressShapingRate(vlanIfEcEntity.getQos().getSetValue().getInflowShapingRate());
        qos.setEgressShapingRate(vlanIfEcEntity.getQos().getSetValue().getOutflowShapingRate());
      }
      l3CpEntity.setQos(qos);

      return l3CpEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
