package msf.fc.slice.slices.l2slice;

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
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;

public class L2SliceDeleteScenario extends AbstractL2SliceScenarioBase<L2SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceDeleteScenario.class);

  public L2SliceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      checkL2SlicePresence(l2Slice, request.getSliceId(), true);

      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      logger.performance("start get l2slice resources lock.");
      DbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");
      L2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2SliceAfterLock, request.getSliceId(), true);
      checkL2SliceStatus(l2SliceAfterLock);
      l2SliceDao.delete(sessionWrapper, l2Slice.getSliceId());
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
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });




      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      if (!ActiveStatus.INACTIVE.equals(l2Slice.getStatusEnum())) {
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

      if (l2Slice.getL2Cps() != null && l2Slice.getL2Cps().size() != 0) {
        String logMsg = MessageFormat.format("remains cp related to slice.remain cp num = {0}",
            l2Slice.getL2Cps().size());
        logger.error(logMsg);
        if (logger.isDebugEnabled()) {
          int ite = 0;
          for (L2Cp l2Cp : l2Slice.getL2Cps()) {
            logger.trace("related cpId[{0}] = {1}", ite++, l2Cp.getId().getCpId());
          }
        }
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }
}
