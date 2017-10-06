package msf.fc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpPK;
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
import msf.fc.slice.cps.l3cp.data.L3CpRequest;

public class L3CpDeleteScenario extends AbstractL3CpScenarioBase<L3CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpDeleteScenario.class);

  public L3CpDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L3CpDao l3CpDao = new L3CpDao();
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setSliceId(request.getSliceId());
      l3CpPk.setCpId(request.getCpId());
      L3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);
      checkL3CpPresence(l3Cp);
      NodeDao nodeDao = new NodeDao();
      String swClusterId = l3Cp.getEdgePoint().getId().getSwClusterId();
      int edgePointId = l3Cp.getEdgePoint().getId().getEdgePointId();
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(node, swClusterId, edgePointId);

      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      List<Node> nodeList = new ArrayList<>();
      nodeList.add(node);
      logger.performance("start get l3slice and node resources lock.");
      DbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l3slice and node resources lock.");
      L3Cp l3CpAfterLock = l3CpDao.read(sessionWrapper, l3CpPk);
      checkL3CpPresence(l3CpAfterLock);
      checkL3CpStatus(l3CpAfterLock);
      l3CpDao.delete(sessionWrapper, l3CpPk);
      sessionWrapper.commit();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
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




      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3CpStatus(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      if (!ActiveStatus.INACTIVE.equals(l3Cp.getStatusEnum())) {
        String logMsg = MessageFormat.format("active status invalid. status = {0}", l3Cp.getStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum())) {
        String logMsg = MessageFormat.format("reservation status invalid. status = {0}",
            l3Cp.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
