
package msf.mfc.slice.slices.l2slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceReadResponseBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;
import msf.mfcfc.slice.slices.l2slice.data.entity.L2SliceEntity;

/**
 * Implementation class for L2 slice information acquisition.
 *
 * @author NTT
 *
 */
public class MfcL2SliceReadScenario extends MfcAbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2SliceReadScenario.class);

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
  public MfcL2SliceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      MfcL2Slice l2Slice = getL2SliceFromDb(sessionWrapper, request.getSliceId());

      checkL2SlicePresence(l2Slice, request.getSliceId(), true);

      return createResponse(l2Slice);
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

  private MfcL2Slice getL2SliceFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
      return l2SliceDao.read(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(MfcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      L2SliceEntity l2SliceEntity = createL2SliceEntity(l2Slice);
      L2SliceReadResponseBody responseBody = new L2SliceReadResponseBody();
      responseBody.setL2Slice(l2SliceEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
