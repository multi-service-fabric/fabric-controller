package msf.fc.slice.slices.l3slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.EcCommonOperationAction;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpBgpOption;
import msf.fc.common.data.L3CpOspfOption;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpVrrpOption;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.ec.core.operation.data.OperationRequestBody;
import msf.fc.rest.ec.core.operation.data.entity.BgpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.CreateL3CpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.CreateL3CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL3CpEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.DeleteL3CpsOptionEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.L3BaseIfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.OspfEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.StaticRouteEcEntity;
import msf.fc.rest.ec.core.operation.data.entity.VrrpEcEntity;
import msf.fc.slice.AbstractSliceCpRunnerBase;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;
import msf.fc.slice.slices.l3slice.data.L3SliceUpdateAsyncResponseBody;
import msf.fc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;

public class L3SliceUpdateRunner extends AbstractSliceCpRunnerBase {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceUpdateRunner.class);
  private L3SliceRequest request;
  private L3SliceUpdateRequestBody requestBody;

  public L3SliceUpdateRunner(L3SliceRequest request, L3SliceUpdateRequestBody requestBody) {
    try {
      logger.methodStart(new String[] { "request", "requestBody" }, new Object[] { request, requestBody });
      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      L3SliceDao l3SliceDao = new L3SliceDao();
      L3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3Slice);
      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");
      DbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");
      L3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3SliceAfterLock);
      checkL3SliceStatus(l3SliceAfterLock);
      L3CpDao l3CpDao = new L3CpDao();
      List<L3Cp> l3CpListAfterLock = l3CpDao.readList(sessionWrapper, request.getSliceId(),
          getReserveStatusByAction().getCode());
      List<String> udpateCpIdList = getUpdateTargetIdList(l3CpListAfterLock);
      if (l3CpListAfterLock.size() > 0) {
        updateL3Cps(sessionWrapper, l3CpDao, l3CpListAfterLock);
        String requestJson = makeEcSendRequestBody(sessionWrapper, l3CpListAfterLock, l3SliceAfterLock);
        RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);
        commitTransaction(sessionWrapper, restResponse, true);
      } else {
        sessionWrapper.rollback();
      }
      return createResponse(udpateCpIdList);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private ReserveStatus getReserveStatusByAction() throws MsfException {
    try {
      logger.methodStart();
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_CPS:
          return ReserveStatus.ACTIVATE_RESERVE;
        case DEACTIVATE_CPS:
          return ReserveStatus.DEACTIVATE_RESERVE;
        default:
          String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getUpdateTargetIdList(List<L3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<String> cpIdList = new ArrayList<>();
      for (L3Cp l3Cp : l3CpList) {
        cpIdList.add(l3Cp.getId().getCpId());
      }
      return cpIdList;
    } finally {
      logger.methodEnd();
    }
  }

  private void updateL3Cps(SessionWrapper sessionWrapper, L3CpDao l3CpDao, List<L3Cp> l3CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpDao", "l3CpList" },
          new Object[] { sessionWrapper, l3CpDao, l3CpList });
      for (L3Cp l3Cp : l3CpList) {
        l3Cp.setReservationStatusEnum(ReserveStatus.NONE);
        l3Cp.setOperationStatusEnum(InterfaceOperationStatus.UNKNOWN);
        switch (requestBody.getActionEnum()) {
          case ACTIVATE_CPS:
            l3Cp.setStatusEnum(ActiveStatus.ACTIVE);
            break;
          case DEACTIVATE_CPS:
            l3Cp.setStatusEnum(ActiveStatus.INACTIVE);
            break;
          default:
            String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
        l3CpDao.update(sessionWrapper, l3Cp);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private String makeEcSendRequestBody(SessionWrapper sessionWrapper, List<L3Cp> l3CpList, L3Slice l3Slice)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3CpList", "l3Slice" },
          new Object[] { sessionWrapper, l3CpList, l3Slice });
      NodeDao nodeDao = new NodeDao();
      OperationRequestBody ecRequestBody = new OperationRequestBody();
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_CPS:
          List<CreateL3CpEcEntity> createL3CpEntityList = new ArrayList<>();
          for (L3Cp l3Cp : l3CpList) {
            String swClusterId = l3Cp.getEdgePoint().getId().getSwClusterId();
            int edgePointId = l3Cp.getEdgePoint().getId().getEdgePointId();
            Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
            checkNodePresence(node, swClusterId, edgePointId);

            createL3CpEntityList.add(makeCreateL3CpEcEntity(sessionWrapper, l3Cp, node));
          }

          CreateL3CpsOptionEcEntity createL3CpsOptionEcEntity = new CreateL3CpsOptionEcEntity();
          createL3CpsOptionEcEntity.setL3CpList(createL3CpEntityList);
          createL3CpsOptionEcEntity.setPlaneEnum(l3Slice.getPlaneEnum());
          createL3CpsOptionEcEntity.setSliceId(l3Slice.getSliceId());
          createL3CpsOptionEcEntity.setVrfId(l3Slice.getVrfId());
          ecRequestBody.setActionEnum(EcCommonOperationAction.CREATE_L3CPS);
          ecRequestBody.setCreateL3cpsOption(createL3CpsOptionEcEntity);
          break;
        default:
          List<DeleteL3CpEcEntity> deleteL3CpEcEntityList = new ArrayList<>();
          for (L3Cp l3Cp : l3CpList) {
            DeleteL3CpEcEntity deleteL3CpEcEntity = new DeleteL3CpEcEntity();
            deleteL3CpEcEntity.setCpId(l3Cp.getId().getCpId());
            deleteL3CpEcEntityList.add(deleteL3CpEcEntity);
          }
          DeleteL3CpsOptionEcEntity deleteL3CpsOptionEcEntity = new DeleteL3CpsOptionEcEntity();
          deleteL3CpsOptionEcEntity.setSliceId(l3Slice.getSliceId());
          deleteL3CpsOptionEcEntity.setCpList(deleteL3CpEcEntityList);
          ecRequestBody.setActionEnum(EcCommonOperationAction.DELETE_L3CPS);
          ecRequestBody.setDeleteL3cpsOption(deleteL3CpsOptionEcEntity);
          break;
      }
      String requestJson = JsonUtil.toJson(ecRequestBody);
      return requestJson;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SlicePresence(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      ParameterCheckUtil.checkNotNullTargetResource(l3Slice, new String[] { "sliceId" },
          new Object[] { request.getSliceId() });
    } finally {
      logger.methodEnd();
    }
  }

  private CreateL3CpEcEntity makeCreateL3CpEcEntity(SessionWrapper sessionWrapper, L3Cp l3Cp, Node node)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3Cp", "node" },
          new Object[] { sessionWrapper, l3Cp, node });
      L3BaseIfEcEntity l3BaseIfEcEntity = new L3BaseIfEcEntity();
      l3BaseIfEcEntity.setNodeTypeEnum(NodeType.LEAF);
      l3BaseIfEcEntity.setNodeId(String.valueOf(node.getNodeId()));
      l3BaseIfEcEntity.setTypeEnum(getIfType(l3Cp.getEdgePoint()));
      l3BaseIfEcEntity.setIfId(getIfId(sessionWrapper, l3Cp.getEdgePoint()));
      l3BaseIfEcEntity.setRouterId(node.getRouterId());
      CreateL3CpEcEntity createL3CpEcEntity = new CreateL3CpEcEntity();
      createL3CpEcEntity.setBaseIf(l3BaseIfEcEntity);
      createL3CpEcEntity.setCpId(l3Cp.getId().getCpId());
      createL3CpEcEntity.setIpv4Addr(l3Cp.getIpv4Address());
      createL3CpEcEntity.setIpv4Prefix(l3Cp.getIpv4Prefix());
      createL3CpEcEntity.setIpv6Addr(l3Cp.getIpv6Address());
      createL3CpEcEntity.setIpv6Prefix(l3Cp.getIpv6Prefix());
      createL3CpEcEntity.setMtu(l3Cp.getMtu());
      createL3CpEcEntity.setVlanId(l3Cp.getVlanId());
      if (l3Cp.getL3CpBgpOption() != null) {
        L3CpBgpOption bgp = l3Cp.getL3CpBgpOption();
        BgpEcEntity bgpEntity = new BgpEcEntity();
        bgpEntity.setNeighborAs(bgp.getNeighborAs());
        bgpEntity.setNeighborIpv4Addr(bgp.getNeighborIpv4Address());
        bgpEntity.setNeighborIpv6Addr(bgp.getNeighborIpv6Address());
        bgpEntity.setRoleEnum(bgp.getRoleEnum());
        createL3CpEcEntity.setBgp(bgpEntity);
      }
      if (l3Cp.getL3CpOspfOption() != null) {
        L3CpOspfOption ospf = l3Cp.getL3CpOspfOption();
        OspfEcEntity ospfEntity = new OspfEcEntity();
        ospfEntity.setMetric(ospf.getMetric());
        createL3CpEcEntity.setOspf(ospfEntity);
      }
      if (l3Cp.getL3CpVrrpOption() != null) {
        L3CpVrrpOption vrrp = l3Cp.getL3CpVrrpOption();
        VrrpEcEntity vrrpEntity = new VrrpEcEntity();
        vrrpEntity.setGroupId(vrrp.getGroupId());
        vrrpEntity.setRoleEnum(vrrp.getRoleEnum());
        vrrpEntity.setVirtualIpv4Addr(vrrp.getVirtualIpv4Address());
        vrrpEntity.setVirtualIpv6Addr(vrrp.getVirtualIpv6Address());
        createL3CpEcEntity.setVrrp(vrrpEntity);
      }
      if (l3Cp.getL3CpStaticRouteOptions() != null && l3Cp.getL3CpStaticRouteOptions().size() > 0) {
        List<L3CpStaticRouteOption> staticRouteList = l3Cp.getL3CpStaticRouteOptions();
        List<StaticRouteEcEntity> staticRouteEntityList = new ArrayList<>();
        for (L3CpStaticRouteOption staticRoute : staticRouteList) {
          StaticRouteEcEntity staticRouteEntity = new StaticRouteEcEntity();
          staticRouteEntity.setAddress(staticRoute.getId().getDestinationAddress());
          staticRouteEntity.setAddressTypeEnum(staticRoute.getId().getAddressTypeEnum());
          staticRouteEntity.setNextHop(staticRoute.getId().getNexthopAddress());
          staticRouteEntity.setPrefix(staticRoute.getId().getPrefix());
          staticRouteEntityList.add(staticRouteEntity);
        }
        createL3CpEcEntity.setStaticRouteList(staticRouteEntityList);
      }
      return createL3CpEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;

      switch (requestBody.getActionEnum()) {
        case DEACTIVATE_CPS:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());
          break;
        default:
          String logMsg = MessageFormat.format("action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("slice status invalid. activeStatus = {0}, reserveStatus = {1}",
            l3Slice.getStatusEnum().name(), l3Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<String> updatedCpIdList) {
    try {
      logger.methodStart();
      L3SliceUpdateAsyncResponseBody responseBody = new L3SliceUpdateAsyncResponseBody();
      responseBody.setUpdatedCpList(updatedCpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
