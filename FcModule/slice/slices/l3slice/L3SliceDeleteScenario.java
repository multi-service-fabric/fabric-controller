package msf.fc.slice.slices.l3slice;

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
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;

public class L3SliceDeleteScenario extends AbstractL3SliceScenarioBase<L3SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceDeleteScenario.class);

  public L3SliceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      checkL3SlicePresence(l3Slice, request.getSliceId(), true);

      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");
      DbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");
      L3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3SliceAfterLock, request.getSliceId(), true);
      checkL3SliceStatus(l3SliceAfterLock);
      l3SliceDao.delete(sessionWrapper, l3Slice.getSliceId());
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
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });




      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      if (!ActiveStatus.INACTIVE.equals(l3Slice.getStatusEnum())) {
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

      if (l3Slice.getL3Cps() != null && l3Slice.getL3Cps().size() != 0) {
        String logMsg = MessageFormat.format("remains cp related to slice.remain cp num = {0}",
            l3Slice.getL3Cps().size());
        logger.error(logMsg);
        if (logger.isDebugEnabled()) {
          int ite = 0;
          for (L3Cp l3Cp : l3Slice.getL3Cps()) {
            logger.trace("related cpId[{0}] = {1}", ite++, l3Cp.getId().getCpId());
          }
        }
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }
}
