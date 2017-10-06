package msf.fc.slice.cps.l2cp;

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
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2CpPK;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.cps.l2cp.data.L2CpCreateRequestBody;
import msf.fc.slice.cps.l2cp.data.L2CpCreateResponseBody;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;

public class L2CpCreateScenario extends AbstractL2CpScenarioBase<L2CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpCreateScenario.class);
  private L2CpCreateRequestBody requestBody;

  public L2CpCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L2SliceDao l2SliceDao = new L2SliceDao();
      L2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2Slice, request.getSliceId(), false);
      NodeDao nodeDao = new NodeDao();
      String swClusterId = requestBody.getClusterId();
      int edgePointId = Integer.valueOf(requestBody.getEdgePointId());
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(node, swClusterId, edgePointId);
      checkNodeVpnType(node, VpnType.L2VPN);
      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      List<Node> nodeList = new ArrayList<>();
      nodeList.add(node);
      logger.performance("start get l2slice and node resources lock.");
      DbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l2slice and node resources lock.");
      L2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2SliceAfterLock, request.getSliceId(), false);
      checkL2SliceStatus(l2SliceAfterLock);
      Node nodeAfterLock = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(nodeAfterLock, swClusterId, edgePointId);
      L2CpDao l2CpDao = new L2CpDao();
      L2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2CpDao, l2SliceAfterLock);
      l2CpDao.create(sessionWrapper, newL2Cp);
      sessionWrapper.commit();

      return createResponse(newL2Cp);
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
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L2CpCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L2CpCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      if (!ActiveStatus.ACTIVE.equals(l2Slice.getStatusEnum())) {
        String logMsg = MessageFormat.format("active status invalid. status = {0}", l2Slice.getStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum())) {
        String logMsg = MessageFormat.format("reservation status invalid. status = {0}",
            l2Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createCpIdSet(List<L2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      Set<String> cpIdSet = new HashSet<>();
      for (L2Cp l2Cp : l2CpList) {
        cpIdSet.add(l2Cp.getId().getCpId());
      }
      return cpIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkCpDuplicate(SessionWrapper sessionWrapper, L2CpDao l2CpDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpDao" }, new Object[] { sessionWrapper, l2CpDao });
      String targetSliceId = request.getSliceId();
      String targetCpId = requestBody.getCpId();
      L2CpPK l2CpPk = new L2CpPK();
      l2CpPk.setSliceId(targetSliceId);
      l2CpPk.setCpId(targetCpId);
      L2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);
      if (l2Cp != null) {
        String logMsg = MessageFormat.format("sliceId = {0}, cpId = {1}", targetSliceId, targetCpId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Cp l2Cp) {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });

      L2CpCreateResponseBody responseBody = new L2CpCreateResponseBody();
      responseBody.setCpId(l2Cp.getId().getCpId());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private L2Cp makeNewL2Cp(SessionWrapper sessionWrapper, L2CpDao l2CpDao, L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2CpDao", "l2Slice" },
          new Object[] { sessionWrapper, l2CpDao, l2Slice });
      L2Cp newL2Cp = new L2Cp();
      L2CpPK l2CpPk = new L2CpPK();
      l2CpPk.setSliceId(request.getSliceId());
      if (requestBody.getCpId() == null) {
        List<L2Cp> l2CpList = l2CpDao.readList(sessionWrapper, request.getSliceId());
        Set<String> cpIdSet = createCpIdSet(l2CpList);
        int cpId = getNextCpId(sessionWrapper, cpIdSet, request.getSliceId(), SliceType.L2_SLICE);
        l2CpPk.setCpId(String.valueOf(cpId));
        newL2Cp.setId(l2CpPk);
      } else {
        checkCpDuplicate(sessionWrapper, l2CpDao);
        l2CpPk.setCpId(requestBody.getCpId());
        newL2Cp.setId(l2CpPk);
      }
      newL2Cp.setEdgePoint(getEdgePointFromDb(sessionWrapper, requestBody.getClusterId(),
          Integer.valueOf(requestBody.getEdgePointId())));
      newL2Cp.setL2Slice(l2Slice);
      newL2Cp.setOperationStatusEnum(InterfaceOperationStatus.UNKNOWN);
      newL2Cp.setPortModeEnum(requestBody.getPortModeEnum());
      newL2Cp.setReservationStatusEnum(ReserveStatus.NONE);
      newL2Cp.setStatusEnum(ActiveStatus.INACTIVE);
      newL2Cp.setVlanId(requestBody.getVlanId());
      return newL2Cp;
    } finally {
      logger.methodEnd();
    }
  }
}
