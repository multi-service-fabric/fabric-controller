package msf.fc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpStaticRouteOptionPK;
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
import msf.fc.db.dao.slices.L3CpStaticRouteOptionDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.ec.slice.cps.l3cp.data.L3CpUpdateEcRequestBody;
import msf.fc.rest.ec.slice.cps.l3cp.data.entity.StaticRouteEcEntity;
import msf.fc.rest.ec.slice.cps.l3cp.data.entity.UpdateOptionEcEntity;
import msf.fc.slice.AbstractSliceCpRunnerBase;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.L3CpUpdateRequestBody;
import msf.fc.slice.cps.l3cp.data.entity.StaticRouteEntity;

public class L3CpUpdateRunner extends AbstractSliceCpRunnerBase {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpUpdateRunner.class);
  private L3CpRequest request;
  private L3CpUpdateRequestBody requestBody;

  public L3CpUpdateRunner(L3CpRequest request, L3CpUpdateRequestBody requestBody) {
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
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setCpId(request.getCpId());
      l3CpPk.setSliceId(request.getSliceId());
      L3Cp l3CpAfterLock = l3CpDao.read(sessionWrapper, l3CpPk);
      checkL3CpPresence(l3CpAfterLock);
      List<StaticRouteEntity> extractedStaticRouteList = extractUpdatableList(
          requestBody.getUpdateOption().getStaticRouteList(), l3CpAfterLock.getL3CpStaticRouteOptions());
      if (extractedStaticRouteList.size() > 0) {
        updateStaticRoute(sessionWrapper, extractedStaticRouteList);
        if (checkSendRequestNecessity(l3CpAfterLock)) {
          String requestJson = makeEcSendRequestBody(sessionWrapper, l3SliceAfterLock, l3CpAfterLock,
              extractedStaticRouteList);
          RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.L3CP_UPDATE, l3CpPk.getSliceId(),
              l3CpPk.getCpId());
          commitTransaction(sessionWrapper, restResponse, false);

        } else {
          sessionWrapper.commit();
        }
      }
      return createResponse();
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private void checkL3SlicePresence(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      ParameterCheckUtil.checkNotNullRelatedResource(l3Slice, new String[] { "sliceId" },
          new Object[] { request.getSliceId() });
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3CpPresence(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l3Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      boolean isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum());
      boolean isReserveStatusOk = ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());

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

  private List<StaticRouteEntity> extractUpdatableList(List<StaticRouteEntity> requestList,
      List<L3CpStaticRouteOption> dbRecordList) throws MsfException {
    try {
      logger.methodStart();
      List<StaticRouteEntity> extractedList = new ArrayList<>();
      switch (requestBody.getUpdateOption().getOperationTypeEnum()) {
          for (StaticRouteEntity srEntity : requestList) {
            boolean isMatch = false;
            for (L3CpStaticRouteOption srOption : dbRecordList) {
              if (isMatchStaticRoute(srEntity, srOption)) {
                isMatch = true;
                break;
              }
            }
            if (!isMatch) {
              extractedList.add(srEntity);
            } else {
              logger.warn("static route to add is already registered. info = {0}", srEntity.toString());
            }
          }
          break;
          for (StaticRouteEntity srEntity : requestList) {
            boolean isMatch = false;
            for (L3CpStaticRouteOption srOption : dbRecordList) {
              if (isMatchStaticRoute(srEntity, srOption)) {
                extractedList.add(srEntity);
                isMatch = true;
                break;
              }
            }
            if (!isMatch) {
              logger.warn("static route to delete is not registered. info = {0}", srEntity.toString());
            }
          }
          break;
        default:
          String logMsg = MessageFormat.format("operation is undefined.action = {0}",
              requestBody.getUpdateOption().getOperationType());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      return extractedList;

    } finally {
      logger.methodEnd();
    }
  }

  private boolean isMatchStaticRoute(StaticRouteEntity srEntity, L3CpStaticRouteOption srOption) {
    try {
      logger.methodStart(new String[] { "srEntity", "srOption" }, new Object[] { srEntity, srOption });
      boolean isMatch = true;
      L3CpStaticRouteOptionPK pk = srOption.getId();
      if (!srEntity.getAddr().equals(pk.getDestinationAddress())) {
        logger.trace("not match address.request = {0}, db = {1}", srEntity.getAddr(), pk.getDestinationAddress());
        isMatch = false;
      }
      if (!srEntity.getAddrTypeEnum().equals(pk.getAddressTypeEnum())) {
        logger.trace("not match address type.request = {0}, db = {1}", srEntity.getAddrTypeEnum(),
            pk.getAddressTypeEnum());
        isMatch = false;
      }
      if (!srEntity.getPrefix().equals(pk.getPrefix())) {
        logger.trace("not match prefix.request = {0}, db = {1}", srEntity.getPrefix(), pk.getPrefix());
        isMatch = false;
      }
      if (!srEntity.getNextHop().equals(pk.getNexthopAddress())) {
        logger.trace("not match next hop.request = {0}, db = {1}", srEntity.getNextHop(), pk.getNexthopAddress());
        isMatch = false;
      }
      return isMatch;

    } finally {
      logger.methodEnd();
    }
  }

  private void updateStaticRoute(SessionWrapper sessionWrapper, List<StaticRouteEntity> staticRouteEntityList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "staticRouteEntityList" },
          new Object[] { sessionWrapper, staticRouteEntityList });
      L3CpStaticRouteOptionDao dao = new L3CpStaticRouteOptionDao();
      for (StaticRouteEntity entity : staticRouteEntityList) {
        L3CpStaticRouteOptionPK pk = new L3CpStaticRouteOptionPK();
        pk.setSliceId(request.getSliceId());
        pk.setCpId(request.getCpId());
        pk.setAddressTypeEnum(entity.getAddrTypeEnum());
        pk.setDestinationAddress(entity.getAddr());
        pk.setNexthopAddress(entity.getNextHop());
        pk.setPrefix(entity.getPrefix());
        switch (requestBody.getUpdateOption().getOperationTypeEnum()) {
          case STATIC_ROUTE_ADD:
            L3CpStaticRouteOption staticRouteOption = new L3CpStaticRouteOption();
            staticRouteOption.setId(pk);
            dao.create(sessionWrapper, staticRouteOption);
            break;
          case STATIC_ROUTE_DELETE:
            dao.delete(sessionWrapper, pk);
            break;
          default:
            String logMsg = MessageFormat.format("operation is undefined.action = {0}",
                requestBody.getUpdateOption().getOperationType());
            logger.error(logMsg);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkSendRequestNecessity(L3Cp l3Cp) {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      if (ActiveStatus.ACTIVE.equals(l3Cp.getStatusEnum())) {
        if (ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum())
            || ReserveStatus.DEACTIVATE_RESERVE.equals(l3Cp.getReservationStatusEnum())) {
          logger.debug("need to send request.");
          return true;
        }
      }
      logger.debug("no need to send request.");
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private String makeEcSendRequestBody(SessionWrapper sessionWrapper, L3Slice l3Slice, L3Cp l3Cp,
      List<StaticRouteEntity> srEntityList) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3Slice", "l3Cp", "srEntityList" },
          new Object[] { sessionWrapper, l3Slice, l3Cp, srEntityList });
      NodeDao nodeDao = new NodeDao();
      String swClusterId = l3Cp.getEdgePoint().getId().getSwClusterId();
      int edgePointId = l3Cp.getEdgePoint().getId().getEdgePointId();
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(node, swClusterId, edgePointId);

      List<StaticRouteEcEntity> sreEntityList = new ArrayList<>();
      for (StaticRouteEntity srEntity : srEntityList) {
        StaticRouteEcEntity sreEntity = new StaticRouteEcEntity();
        sreEntity.setAddr(srEntity.getAddr());
        sreEntity.setAddrTypeEnum(srEntity.getAddrTypeEnum());
        sreEntity.setNextHop(srEntity.getNextHop());
        sreEntity.setPrefix(srEntity.getPrefix());
        sreEntityList.add(sreEntity);
      }
      UpdateOptionEcEntity uoeEntity = new UpdateOptionEcEntity();
      uoeEntity.setStaticRouteList(sreEntityList);
      uoeEntity.setIpv4Address(l3Cp.getIpv4Address());
      uoeEntity.setIpv4Prefix(l3Cp.getIpv4Prefix());
      uoeEntity.setIpv6Address(l3Cp.getIpv6Address());
      uoeEntity.setIpv6Prefix(l3Cp.getIpv6Prefix());
      uoeEntity.setOperationTypeEnum(requestBody.getUpdateOption().getOperationTypeEnum());
      uoeEntity.setRouterId(node.getRouterId());
      uoeEntity.setVrfId(l3Slice.getVrfId());
      L3CpUpdateEcRequestBody requestBody = new L3CpUpdateEcRequestBody();
      requestBody.setUpdateOption(uoeEntity);

      String requestJson = JsonUtil.toJson(requestBody);
      return requestJson;

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
