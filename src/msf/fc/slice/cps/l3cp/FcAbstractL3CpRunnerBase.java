
package msf.fc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3CpPK;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.db.dao.slices.FcL3SliceDao;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.OperationBaseIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationBgpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationCreateL3VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationDeleteL3VlanIfOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationQosEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationStaticRouteEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationTrackingIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationVlanIfCreateEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationVlanIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OperationVrrpEcEntity;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.vlan.data.entity.VlanIfEcEntity;
import msf.fc.slice.cps.FcAbstractCpRunnerBase;
import msf.mfcfc.common.constant.EcCommonOperationAction;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.IpAddressUtil;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpBgpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosCreateEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpStaticRouteEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpVrrpEntity;

/**
 * Abstract class to implement the common process of the L3CP-related
 * asynchronous runner processing in the slice management function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractL3CpRunnerBase extends FcAbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL3CpRunnerBase.class);

  protected L3CpRequest request;

  protected List<OperationVlanIfCreateEcEntity> createVlanIfEntityList = new ArrayList<>();

  protected List<OperationVlanIfEcEntity> deleteVlanIfEntityList = new ArrayList<>();

  protected OperationVlanIfCreateEcEntity makeOperationCreateVlanIfEcEntity(SessionWrapper sessionWrapper, int vrfId,
      FcL3Cp l3Cp, int vlanId, int mtu, String ipv4Address, String ipv6Address, Integer ipv4Prefix, Integer ipv6Prefix,
      L3CpBgpEntity bgpEntity, List<L3CpStaticRouteEntity> staticRouteEntityList, L3CpVrrpEntity vrrpEntity,
      List<OperationTrackingIfEcEntity> trackingIfList, L3CpQosCreateEntity qosEntity) throws MsfException {
    try {
      logger.methodStart();
      FcNodeDao nodeDao = new FcNodeDao();
      FcNode requestTargetNode = nodeDao.read(sessionWrapper, l3Cp.getEdgePoint().getEdgePointId());
      OperationBaseIfEcEntity baseIfEntity = new OperationBaseIfEcEntity();
      baseIfEntity.setIfId(getIfId(l3Cp.getEdgePoint()));
      baseIfEntity.setIfType(getIfType(l3Cp.getEdgePoint()).getMessage());
      baseIfEntity.setNodeId(String.valueOf(requestTargetNode.getEcNodeId()));
      OperationVlanIfCreateEcEntity entity = new OperationVlanIfCreateEcEntity();
      entity.setBaseIf(baseIfEntity);
      entity.setIpv4Address(ipv4Address);
      entity.setIpv4Prefix(ipv4Prefix);
      entity.setIpv6Address(ipv6Address);
      entity.setIpv6Prefix(ipv6Prefix);
      entity.setMtu(mtu);
      entity.setQos(makeOperationQosEcEntity(l3Cp, qosEntity));
      entity.setVlanId(vlanId);
      entity.setVlanIfId(String.valueOf(l3Cp.getVlanIf().getId().getVlanIfId()));
      entity.setRouteDistinguisher(makeRouteDistinguisher(vrfId,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId(),
          requestTargetNode.getEcNodeId()));

      if (bgpEntity != null) {
        OperationBgpEcEntity bgpEcEntity = new OperationBgpEcEntity();
        bgpEcEntity.setNeighborAs(bgpEntity.getNeighborAs());
        bgpEcEntity.setNeighborIpv4Addr(bgpEntity.getNeighborIpv4Address());
        bgpEcEntity.setNeighborIpv6Addr(bgpEntity.getNeighborIpv6Address());
        bgpEcEntity.setRole(bgpEntity.getRole());
        entity.setBgp(bgpEcEntity);
      }

      if (vrrpEntity != null) {
        OperationVrrpEcEntity vrrpEcEntity = new OperationVrrpEcEntity();
        vrrpEcEntity.setGroupId(vrrpEntity.getGroupId());
        vrrpEcEntity.setRole(vrrpEntity.getRole());
        vrrpEcEntity.setVirtualIpv4Addr(vrrpEntity.getVirtualIpv4Address());
        vrrpEcEntity.setVirtualIpv6Addr(null);
        vrrpEcEntity.setTrackingIfList(trackingIfList);
        entity.setVrrp(vrrpEcEntity);
      }

      if (staticRouteEntityList != null) {
        List<OperationStaticRouteEcEntity> staticRouteEcEntityList = new ArrayList<>();
        for (L3CpStaticRouteEntity staticRouteEntity : staticRouteEntityList) {
          OperationStaticRouteEcEntity staticRouteEcEntity = new OperationStaticRouteEcEntity();
          staticRouteEcEntity.setAddress(staticRouteEntity.getAddress());
          staticRouteEcEntity.setAddressType(staticRouteEntity.getAddrType());
          staticRouteEcEntity.setNextHop(staticRouteEntity.getNextHop());
          staticRouteEcEntity.setPrefix(staticRouteEntity.getPrefix());
          staticRouteEcEntityList.add(staticRouteEcEntity);
        }
        entity.setStaticRouteList(staticRouteEcEntityList);
      }
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationVlanIfEcEntity makeOperationDeleteVlanIfEcEntity(Integer ecNodeId, Integer vlanIfId) {
    try {
      logger.methodStart(new String[] { "ecNodeId", "vlanIfId" }, new Object[] { ecNodeId, vlanIfId });
      OperationVlanIfEcEntity entity = new OperationVlanIfEcEntity();
      entity.setNodeId(String.valueOf(ecNodeId));
      entity.setVlanIfId(String.valueOf(vlanIfId));
      return entity;
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeCreateL3VlanIfData(List<OperationVlanIfCreateEcEntity> createVlanIfEntityList, Integer vrfId,
      int plane) {
    try {
      logger.methodStart(new String[] { "createVlanIfEntityList", "vrfId", "plane" },
          new Object[] { createVlanIfEntityList, vrfId, plane });
      OperationCreateL3VlanIfOptionEcEntity entity = new OperationCreateL3VlanIfOptionEcEntity();
      entity.setVlanIfList(createVlanIfEntityList);
      entity.setPlane(plane);
      entity.setVrfId(vrfId);
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.CREATE_L3VLAN_IF.getMessage());
      body.setCreateL3vlanIfOption(entity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeDeleteL3VlanIfData(List<OperationVlanIfEcEntity> deleteVlanIfEntityList, String vrfId) {
    try {
      logger.methodStart(new String[] { "deleteVlanIfEntityList", "vrfId" },
          new Object[] { deleteVlanIfEntityList, vrfId });
      OperationDeleteL3VlanIfOptionEcEntity entity = new OperationDeleteL3VlanIfOptionEcEntity();
      entity.setVlanIfList(deleteVlanIfEntityList);
      entity.setVrfId(vrfId);
      OperationRequestBody body = new OperationRequestBody();
      body.setAction(EcCommonOperationAction.DELETE_L3VLAN_IF.getMessage());
      body.setDeleteL3vlanIfOption(entity);
      return JsonUtil.toJson(body);
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL3Cp getL3CpAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart();
      FcL3CpDao l3CpDao = new FcL3CpDao();
      FcL3CpPK l3CpPk = new FcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      FcL3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);

      checkCpPresence(l3Cp, sliceId, cpId);
      return l3Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcL3Slice getL3SliceAndCheck(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart();
      FcL3SliceDao l3SliceDao = new FcL3SliceDao();
      FcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, sliceId);

      checkSlicePresence(l3Slice, sliceId);
      return l3Slice;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processCreateL3Cp(SessionWrapper sessionWrapper, FcL3Slice l3Slice, FcNode node, String cpId,
      int edgePointId, int vlanId, int mtu, String ipv4Address, String ipv6Address, Integer ipv4Prefix,
      Integer ipv6Prefix, L3CpBgpEntity bgpEntity, List<L3CpStaticRouteEntity> staticRouteEntityList,
      L3CpVrrpEntity vrrpEntity, Double trafficThreshold, L3CpQosCreateEntity qosEntity) throws MsfException {
    try {
      logger.methodStart();

      checkVlanIdSameCheck(sessionWrapper, l3Slice, node, edgePointId, vlanId);

      checkL3NwConstraints(staticRouteEntityList);

      FcL3CpDao l3CpDao = new FcL3CpDao();
      FcL3Cp newL3Cp = makeNewL3Cp(sessionWrapper, l3Slice, node, cpId, edgePointId, trafficThreshold);

      List<OperationTrackingIfEcEntity> trackingIfList = null;

      if (vrrpEntity != null) {
        trackingIfList = getTrackingIfList(node);
      }

      OperationVlanIfCreateEcEntity createVlanIfEntity = makeOperationCreateVlanIfEcEntity(sessionWrapper,
          l3Slice.getVrfId(), newL3Cp, vlanId, mtu, ipv4Address, ipv6Address, ipv4Prefix, ipv6Prefix, bgpEntity,
          staticRouteEntityList, vrrpEntity, trackingIfList, qosEntity);
      createVlanIfEntityList.add(createVlanIfEntity);

      l3CpDao.create(sessionWrapper, newL3Cp);

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3NwConstraints(List<L3CpStaticRouteEntity> staticRouteEntityList) throws MsfException {
    try {
      logger.methodStart();

      if (staticRouteEntityList != null) {
        for (L3CpStaticRouteEntity staticRouteEntity : staticRouteEntityList) {
          checkL3NwConstraints(staticRouteEntity);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL3NwConstraints(L3CpStaticRouteEntity staticRouteEntity) throws MsfException {
    try {
      logger.methodStart();
      if (!IpAddressUtil.isNetworkAddress(staticRouteEntity.getAddress(), staticRouteEntity.getPrefix())) {

        String logMsg = logger.error("static route address violates network constraints. address = {0}, prefix = {1}",
            staticRouteEntity.getAddress(), staticRouteEntity.getPrefix());
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkVlanIdSameCheck(SessionWrapper sessionWrapper, FcL3Slice l3Slice, FcNode node, int edgePointId,
      int vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice", "node", "edgePointId", "vlanId" },
          new Object[] { l3Slice, node, edgePointId, vlanId });
      FcL3CpDao l3CpDao = new FcL3CpDao();

      List<FcL3Cp> l3CpList = l3CpDao.readListByEdgePoint(sessionWrapper, l3Slice.getSliceId(), edgePointId);

      if (!l3CpList.isEmpty()) {

        VlanIfReadListEcResponseBody responseBody = getVlanIfList(sessionWrapper, node.getNodeInfoId());
        for (VlanIfEcEntity entity : responseBody.getVlanIfList()) {

          for (FcL3Cp l3Cp : l3CpList) {

            if (l3Cp.getVlanIf().getId().getVlanIfId().toString().equals(entity.getVlanIfId())) {

              int vlanIdFromEc = 0;

              if (entity.getVlanId() != null) {
                vlanIdFromEc = Integer.valueOf(entity.getVlanId());
              }

              if (vlanId == vlanIdFromEc) {
                String logMsg = MessageFormat.format(
                    "l3 nw constraint violation.l3cps vlan id are same. request vlan id = {0}, vlan id from EC = {1}",
                    vlanId, entity.getVlanId());
                logger.error(logMsg);
                throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
              }
              break;
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<OperationTrackingIfEcEntity> getTrackingIfList(FcNode node) throws MsfException {
    try {
      logger.methodStart();
      List<OperationTrackingIfEcEntity> trackingIfList = new ArrayList<>();

      LeafType leafType = LeafType.getEnumFromCode(node.getLeafNode().getLeafType());

      for (FcPhysicalIf physicalIf : node.getPhysicalIfs()) {

        if (CollectionUtils.isNotEmpty(physicalIf.getInternalLinkIfs())) {
          trackingIfList.add(getTrackingIf(InterfaceType.PHYSICAL_IF, physicalIf.getPhysicalIfId()));

          if (leafType.equals(LeafType.BORDER_LEAF)) {
            if (physicalIf.getClusterLinkIfs() != null && physicalIf.getClusterLinkIfs().size() != 0) {
              trackingIfList.add(getTrackingIf(InterfaceType.PHYSICAL_IF, physicalIf.getPhysicalIfId()));
            }
          }
        }
      }
      for (FcBreakoutIf breakoutIf : node.getBreakoutIfs()) {

        if (CollectionUtils.isNotEmpty(breakoutIf.getInternalLinkIfs())) {
          trackingIfList.add(getTrackingIf(InterfaceType.BREAKOUT_IF, breakoutIf.getBreakoutIfId()));

          if (leafType.equals(LeafType.BORDER_LEAF)) {
            if (breakoutIf.getClusterLinkIfs() != null && breakoutIf.getClusterLinkIfs().size() != 0) {
              trackingIfList.add(getTrackingIf(InterfaceType.BREAKOUT_IF, breakoutIf.getBreakoutIfId()));
            }
          }
        }
      }
      for (FcLagIf lagIf : node.getLagIfs()) {

        if (CollectionUtils.isNotEmpty(lagIf.getInternalLinkIfs())) {
          trackingIfList.add(getTrackingIf(InterfaceType.LAG_IF, String.valueOf(lagIf.getLagIfId())));

          if (leafType.equals(LeafType.BORDER_LEAF)) {
            if (lagIf.getClusterLinkIfs() != null && lagIf.getClusterLinkIfs().size() != 0) {
              trackingIfList.add(getTrackingIf(InterfaceType.LAG_IF, String.valueOf(lagIf.getLagIfId())));
            }
          }
        }
      }
      return trackingIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationTrackingIfEcEntity getTrackingIf(InterfaceType type, String ifId) {
    OperationTrackingIfEcEntity entity = new OperationTrackingIfEcEntity();
    entity.setIfType(type.getMessage());
    entity.setIfId(ifId);
    return entity;
  }

  protected String getCpIdAndCheck(SessionWrapper sessionWrapper, String sliceId, String cpId,
      List<String> requestCpIdList) throws MsfException {
    try {
      logger.methodStart();
      FcL3CpDao l3CpDao = new FcL3CpDao();

      if (cpId == null) {

        List<FcL3Cp> l3CpList = l3CpDao.readListBySliceId(sessionWrapper, sliceId);
        Set<String> cpIdSet = createCpIdSet(l3CpList);
        if (requestCpIdList != null) {
          cpIdSet.addAll(requestCpIdList);
        }
        return String.valueOf(getNextCpId(sessionWrapper, cpIdSet, sliceId, SliceType.L3_SLICE));
      } else {
        checkCpDuplicate(sessionWrapper, l3CpDao, sliceId, cpId);
        return cpId;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createCpIdSet(List<FcL3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      Set<String> cpIdSet = new TreeSet<>();
      for (FcL3Cp l3Cp : l3CpList) {
        cpIdSet.add(l3Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkCpDuplicate(SessionWrapper sessionWrapper, FcL3CpDao l3CpDao, String sliceId, String cpId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao" }, new Object[] { sessionWrapper, l3CpDao });
      FcL3CpPK l3CpPk = new FcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      FcL3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);
      if (l3Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", sliceId, cpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected FcL3Cp makeNewL3Cp(SessionWrapper sessionWrapper, FcL3Slice l3Slice, FcNode node, String cpId,
      int edgePointId, Double trafficThreshold) throws MsfException {
    try {
      logger.methodStart();
      FcL3Cp newL3Cp = new FcL3Cp();
      FcL3CpPK l3CpPk = new FcL3CpPK();
      l3CpPk.setSliceId(l3Slice.getSliceId());
      l3CpPk.setCpId(cpId);
      newL3Cp.setId(l3CpPk);
      newL3Cp.setL3Slice(l3Slice);

      FcVlanIfPK vlanIfPk = new FcVlanIfPK();
      Set<String> vlanIfIdSet = createVlanIfIdSet(sessionWrapper, node.getNodeInfoId());
      int vlanIfId = getNextVlanIfId(sessionWrapper, vlanIfIdSet, node.getNodeInfoId().intValue());
      vlanIfPk.setVlanIfId(vlanIfId);
      vlanIfPk.setNodeInfoId(node.getNodeInfoId().intValue());
      FcVlanIf vlanIf = new FcVlanIf();
      vlanIf.setId(vlanIfPk);
      newL3Cp.setVlanIf(vlanIf);

      FcEdgePoint edgePoint = getEdgePointFromDb(sessionWrapper, edgePointId);
      newL3Cp.setEdgePoint(edgePoint);

      newL3Cp.setTrafficThreshold(trafficThreshold);
      return newL3Cp;
    } finally {
      logger.methodEnd();
    }
  }

  protected void processDeleteL3Cp(SessionWrapper sessionWrapper, FcL3Cp l3Cp, FcNode node) throws MsfException {
    try {
      FcL3CpDao l3CpDao = new FcL3CpDao();
      logger.methodStart();

      OperationVlanIfEcEntity deleteVlanIfEntity = makeOperationDeleteVlanIfEcEntity(node.getEcNodeId(),
          l3Cp.getVlanIf().getId().getVlanIfId());
      deleteVlanIfEntityList.add(deleteVlanIfEntity);
      l3CpDao.delete(sessionWrapper, l3Cp.getId());
    } finally {
      logger.methodEnd();
    }
  }

  protected String makeRouteDistinguisher(int vrfId, int clusterId, int ecNodeId) {
    return vrfId + ":" + (clusterId * MULTIPLY_VALUE_FOR_RD_CALCULATION_ + ecNodeId);
  }

  private OperationQosEcEntity makeOperationQosEcEntity(FcL3Cp fcL3Cp, L3CpQosCreateEntity qosEntity)
      throws MsfException {
    OperationQosEcEntity qosEcEntity = new OperationQosEcEntity();

    if (fcL3Cp.getL3Slice().getRemarkMenu() == null && (qosEntity == null || (qosEntity.getEgressQueueMenu() == null
        && qosEntity.getEgressShapingRate() == null && qosEntity.getIngressShapingRate() == null))) {
      return null;
    }
    qosEcEntity.setRemarkMenu(fcL3Cp.getL3Slice().getRemarkMenu());
    if (qosEntity != null) {
      qosEcEntity.setEgressQueue(qosEntity.getEgressQueueMenu());
      qosEcEntity.setInflowShapingRate(qosEntity.getIngressShapingRate());
      qosEcEntity.setOutflowShapingRate(qosEntity.getEgressShapingRate());
    }
    return qosEcEntity;
  }
}
