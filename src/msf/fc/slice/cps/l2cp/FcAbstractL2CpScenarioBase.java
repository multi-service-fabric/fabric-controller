
package msf.fc.slice.cps.l2cp;

import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcL2Cp;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.slice.cps.FcAbstractCpScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.EsiUtil;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpIrbEntity;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosEntity;

/**
 * Abstract class to implement the common process of the L2CP-related processing
 * in the slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractL2CpScenarioBase<T extends RestRequestBase> extends FcAbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL2CpScenarioBase.class);

  protected L2CpRequest request;

  protected void checkL2CpPresence(FcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l2Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

  protected L2CpEntity createL2CpEntity(SessionWrapper sessionWrapper, FcL2Cp l2Cp, VlanIfEcEntity vlanIfEcEntity)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp", "vlanIfResponseBody" }, new Object[] { l2Cp, vlanIfEcEntity });
      L2CpEntity l2CpEntity = new L2CpEntity();
      l2CpEntity.setClusterId(
          String.valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId()));
      l2CpEntity.setCpId(l2Cp.getId().getCpId());
      l2CpEntity.setSliceId(l2Cp.getId().getSliceId());
      l2CpEntity.setVlanId(vlanIfEcEntity.getVlanIdInt());
      l2CpEntity.setEdgePointId(String.valueOf(l2Cp.getEdgePoint().getEdgePointId()));

      int myClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      if (l2Cp.getEsi() != null && EsiUtil.getLowerSwClusterId(l2Cp.getEsi()) == myClusterId
          && EsiUtil.getHigherSwClusterId(l2Cp.getEsi()) == myClusterId) {
        FcL2CpDao l2CpDao = new FcL2CpDao();
        List<FcL2Cp> pairL2CpList = l2CpDao.readListByEsi(sessionWrapper, l2Cp.getEsi());
        for (FcL2Cp pairL2Cp : pairL2CpList) {

          if (!pairL2Cp.getId().equals(l2Cp.getId())) {
            l2CpEntity.setPairCpId(pairL2Cp.getId().getCpId());
            break;
          }
        }
      }
      l2CpEntity.setPortMode(vlanIfEcEntity.getPortMode());

      L2CpQosEntity qos = new L2CpQosEntity();

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
      l2CpEntity.setQos(qos);

      if (vlanIfEcEntity.getIrb() != null) {
        L2CpIrbEntity irb = new L2CpIrbEntity();
        irb.setIrbIpv4Address(vlanIfEcEntity.getIrb().getIpv4Address());
        irb.setVgaIpv4Address(vlanIfEcEntity.getIrb().getVirtualGatewayAddress());
        irb.setIpv4AddressPrefix(vlanIfEcEntity.getIrb().getIpv4Prefix());
        l2CpEntity.setIrb(irb);
      }
      l2CpEntity.setTrafficThreshold(l2Cp.getTrafficThreshold());

      return l2CpEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
