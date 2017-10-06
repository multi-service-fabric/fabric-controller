package msf.fc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SliceType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.constant.VpnType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpBgpOption;
import msf.fc.common.data.L3CpBgpOptionPK;
import msf.fc.common.data.L3CpOspfOption;
import msf.fc.common.data.L3CpOspfOptionPK;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpStaticRouteOptionPK;
import msf.fc.common.data.L3CpVrrpOption;
import msf.fc.common.data.L3CpVrrpOptionPK;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.cps.l3cp.data.L3CpCreateRequestBody;
import msf.fc.slice.cps.l3cp.data.L3CpCreateResponseBody;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.entity.BgpEntity;
import msf.fc.slice.cps.l3cp.data.entity.OspfEntity;
import msf.fc.slice.cps.l3cp.data.entity.StaticRouteEntity;
import msf.fc.slice.cps.l3cp.data.entity.VrrpEntity;

public class L3CpCreateScenario extends AbstractL3CpScenarioBase<L3CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpCreateScenario.class);
  private L3CpCreateRequestBody requestBody;

  public L3CpCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      L3SliceDao l3SliceDao = new L3SliceDao();
      L3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3Slice, request.getSliceId(), false);
      NodeDao nodeDao = new NodeDao();
      String swClusterId = requestBody.getClusterId();
      int edgePointId = Integer.valueOf(requestBody.getEdgePointId());
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(node, swClusterId, edgePointId);
      checkNodeVpnType(node, VpnType.L3VPN);
      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      List<Node> nodeList = new ArrayList<>();
      nodeList.add(node);
      logger.performance("start get l3slice and node resources lock.");
      DbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l3slice and node resources lock.");
      L3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3SliceAfterLock, request.getSliceId(), false);
      checkL3SliceStatus(l3SliceAfterLock);
      Node nodeAfterLock = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(nodeAfterLock, swClusterId, edgePointId);
      L3CpDao l3CpDao = new L3CpDao();
      L3Cp newL3Cp = makeNewL3Cp(sessionWrapper, l3CpDao, l3SliceAfterLock, nodeAfterLock);
      l3CpDao.create(sessionWrapper, newL3Cp);
      sessionWrapper.commit();

      return createResponse(newL3Cp);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L3CpCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L3CpCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      if (!ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum())) {
        String logMsg = MessageFormat.format("active status invalid. status = {0}", l3Slice.getStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum())) {
        String logMsg = MessageFormat.format("reservation status invalid. status = {0}",
            l3Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createCpIdSet(List<L3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      Set<String> cpIdSet = new HashSet<>();
      for (L3Cp l3Cp : l3CpList) {
        cpIdSet.add(l3Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkCpDuplicate(SessionWrapper sessionWrapper, L3CpDao l3CpDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao" }, new Object[] { sessionWrapper, l3CpDao });
      String targetSliceId = request.getSliceId();
      String targetCpId = requestBody.getCpId();
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setSliceId(targetSliceId);
      l3CpPk.setCpId(targetCpId);
      L3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);
      if (l3Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", targetSliceId, targetCpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L3Cp l3Cp) {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });

      L3CpCreateResponseBody responseBody = new L3CpCreateResponseBody();
      responseBody.setCpId(l3Cp.getId().getCpId());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private L3Cp makeNewL3Cp(SessionWrapper sessionWrapper, L3CpDao l3CpDao, L3Slice l3Slice, Node node)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao", "l3Slice", "node" },
          new Object[] { sessionWrapper, l3CpDao, l3Slice, node });
      L3Cp newL3Cp = new L3Cp();
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setSliceId(request.getSliceId());
      if (requestBody.getCpId() == null) {
        List<L3Cp> l3CpList = l3CpDao.readList(sessionWrapper, request.getSliceId());
        Set<String> cpIdSet = createCpIdSet(l3CpList);
        int cpId = getNextCpId(sessionWrapper, cpIdSet, request.getSliceId(), SliceType.L3_SLICE);
        l3CpPk.setCpId(String.valueOf(cpId));
        newL3Cp.setId(l3CpPk);
      } else {
        checkCpDuplicate(sessionWrapper, l3CpDao);
        l3CpPk.setCpId(requestBody.getCpId());
        newL3Cp.setId(l3CpPk);
      }
      checkVlanIdDuplicate(sessionWrapper, l3CpDao, node);
      newL3Cp.setEdgePoint(getEdgePointFromDb(sessionWrapper, requestBody.getClusterId(),
          Integer.valueOf(requestBody.getEdgePointId())));
      newL3Cp.setIpv4Address(requestBody.getIpv4Addr());
      newL3Cp.setIpv4Prefix(requestBody.getIpv4Prefix());
      newL3Cp.setIpv6Address(requestBody.getIpv6Addr());
      newL3Cp.setIpv6Prefix(requestBody.getIpv6Prefix());
      newL3Cp.setL3Slice(l3Slice);
      newL3Cp.setMtu(requestBody.getMtu());
      newL3Cp.setOperationStatusEnum(InterfaceOperationStatus.UNKNOWN);
      newL3Cp.setReservationStatusEnum(ReserveStatus.NONE);
      newL3Cp.setStatusEnum(ActiveStatus.INACTIVE);
      newL3Cp.setVlanId(requestBody.getVlanId());
      setOptionDataToL3Cp(newL3Cp);

      return newL3Cp;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkVlanIdDuplicate(SessionWrapper sessionWrapper, L3CpDao l3CpDao, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao", "node" },
          new Object[] { sessionWrapper, l3CpDao, node });
      List<L3Cp> l3CpList = l3CpDao.readList(sessionWrapper, node.getNodeInfoId());
      int checkTargetVlanId = requestBody.getVlanId();
      if (checkTargetVlanId == 0) {
        logger.debug("VLAN ID is 0");
        return;
      } else {
        for (L3Cp l3Cp : l3CpList) {
          if (checkTargetVlanId == l3Cp.getVlanId().intValue()) {
            String logMsg = MessageFormat.format("duplicate vlanId. vlanId = {0},nodeId = {1}", checkTargetVlanId,
                node.getNodeId());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setOptionDataToL3Cp(L3Cp l3Cp) {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      if (requestBody.getBgp() != null) {
        L3CpBgpOption bgpOption = new L3CpBgpOption();
        L3CpBgpOptionPK bgpOptionPk = new L3CpBgpOptionPK();
        bgpOptionPk.setCpId(l3Cp.getId().getCpId());
        bgpOptionPk.setSliceId(l3Cp.getId().getSliceId());
        BgpEntity bgp = requestBody.getBgp();
        bgpOption.setId(bgpOptionPk);
        bgpOption.setL3Cp(l3Cp);
        bgpOption.setNeighborAs(bgp.getNeighborAs());
        bgpOption.setNeighborIpv4Address(bgp.getNeighborIpv4Addr());
        bgpOption.setNeighborIpv6Address(bgp.getNeighborIpv6Addr());
        bgpOption.setRoleEnum(bgp.getRoleEnum());
        l3Cp.setL3CpBgpOption(bgpOption);
      }
      if (requestBody.getOspf() != null) {
        L3CpOspfOption ospfOption = new L3CpOspfOption();
        L3CpOspfOptionPK ospfOptionPk = new L3CpOspfOptionPK();
        ospfOptionPk.setCpId(l3Cp.getId().getCpId());
        ospfOptionPk.setSliceId(l3Cp.getId().getSliceId());
        OspfEntity ospf = requestBody.getOspf();
        ospfOption.setId(ospfOptionPk);
        ospfOption.setL3Cp(l3Cp);
        ospfOption.setMetric(ospf.getMetric());
        l3Cp.setL3CpOspfOption(ospfOption);
      }
      if (requestBody.getVrrp() != null) {
        L3CpVrrpOption vrrpOption = new L3CpVrrpOption();
        L3CpVrrpOptionPK vrrpOptionPk = new L3CpVrrpOptionPK();
        vrrpOptionPk.setCpId(l3Cp.getId().getCpId());
        vrrpOptionPk.setSliceId(l3Cp.getId().getSliceId());
        vrrpOption.setId(vrrpOptionPk);
        VrrpEntity vrrp = requestBody.getVrrp();
        vrrpOption.setGroupId(vrrp.getGroupId());
        vrrpOption.setL3Cp(l3Cp);
        vrrpOption.setRoleEnum(vrrp.getRoleEnum());
        vrrpOption.setVirtualIpv4Address(vrrp.getVirtualIpv4Addr());
        vrrpOption.setVirtualIpv6Address(vrrp.getVirtualIpv6Addr());
        l3Cp.setL3CpVrrpOption(vrrpOption);
      }
      if (requestBody.getStaticRouteList() != null && requestBody.getStaticRouteList().size() > 0) {
        List<StaticRouteEntity> staticRouteList = requestBody.getStaticRouteList();
        List<L3CpStaticRouteOption> staticRouteOptionList = new ArrayList<>();
        for (StaticRouteEntity staticRoute : staticRouteList) {
          L3CpStaticRouteOptionPK staticRouteOptionPk = new L3CpStaticRouteOptionPK();
          staticRouteOptionPk.setAddressTypeEnum(staticRoute.getAddrTypeEnum());
          staticRouteOptionPk.setCpId(l3Cp.getId().getCpId());
          staticRouteOptionPk.setDestinationAddress(staticRoute.getAddr());
          staticRouteOptionPk.setNexthopAddress(staticRoute.getNextHop());
          staticRouteOptionPk.setPrefix(staticRoute.getPrefix());
          staticRouteOptionPk.setSliceId(l3Cp.getId().getSliceId());
          L3CpStaticRouteOption staticRouteOption = new L3CpStaticRouteOption();
          staticRouteOption.setId(staticRouteOptionPk);
          staticRouteOption.setL3Cp(l3Cp);
          staticRouteOptionList.add(staticRouteOption);
        }
        l3Cp.setL3CpStaticRouteOptions(staticRouteOptionList);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
