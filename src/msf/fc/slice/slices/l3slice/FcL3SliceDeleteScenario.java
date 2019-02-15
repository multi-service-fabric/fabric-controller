
package msf.fc.slice.slices.l3slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.slices.FcL3SliceDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Implementation class for the L3 slice deletion.
 *
 * @author NTT
 *
 */
public class FcL3SliceDeleteScenario extends FcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceDeleteScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcL3SliceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      FcL3SliceDao l3SliceDao = new FcL3SliceDao();
      FcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

      checkL3SlicePresence(l3Slice, request.getSliceId(), true);

      sessionWrapper.beginTransaction();
      List<FcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");

      FcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      FcL3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());

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

  protected void checkL3SliceStatus(FcL3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });

      if (l3Slice.getL3Cps() != null && l3Slice.getL3Cps().size() != 0) {
        String logMsg = MessageFormat.format("remains cp related to slice.remain cp num = {0}",
            l3Slice.getL3Cps().size());
        logger.error(logMsg);
        if (logger.isDebugEnabled()) {
          int ite = 0;
          for (FcL3Cp l3Cp : l3Slice.getL3Cps()) {
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
