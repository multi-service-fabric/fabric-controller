package msf.fc.slice.cps.l2cp;

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
import msf.fc.slice.cps.l2cp.data.L2CpRequest;

public class L2CpDeleteScenario extends AbstractL2CpScenarioBase<L2CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpDeleteScenario.class);

  public L2CpDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L2CpDao l2CpDao = new L2CpDao();
      L2CpPK l2CpPk = new L2CpPK();
      l2CpPk.setSliceId(request.getSliceId());
      l2CpPk.setCpId(request.getCpId());
      L2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);
      checkL2CpPresence(l2Cp);
      NodeDao nodeDao = new NodeDao();
      String swClusterId = l2Cp.getEdgePoint().getId().getSwClusterId();
      int edgePointId = l2Cp.getEdgePoint().getId().getEdgePointId();
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);
      checkNodePresence(node, swClusterId, edgePointId);

      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      List<Node> nodeList = new ArrayList<>();
      nodeList.add(node);
      logger.performance("start get l2slice and node resources lock.");
      DbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l2slice and node resources lock.");
      L2Cp l2CpAfterLock = l2CpDao.read(sessionWrapper, l2CpPk);
      checkL2CpPresence(l2CpAfterLock);
      checkL2CpStatus(l2CpAfterLock);
      l2CpDao.delete(sessionWrapper, l2CpPk);
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
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });




      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2CpStatus(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      if (!ActiveStatus.INACTIVE.equals(l2Cp.getStatusEnum())) {
        String logMsg = MessageFormat.format("active status invalid. status = {0}", l2Cp.getStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!ReserveStatus.NONE.equals(l2Cp.getReservationStatusEnum())) {
        String logMsg = MessageFormat.format("reservation status invalid. status = {0}",
            l2Cp.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
