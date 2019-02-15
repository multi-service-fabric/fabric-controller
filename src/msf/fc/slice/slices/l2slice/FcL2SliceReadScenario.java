
package msf.fc.slice.slices.l2slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Slice;
import msf.fc.db.dao.slices.FcL2SliceDao;
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
 * Implementation class for the L2 slice information acquisition.
 *
 * @author NTT
 *
 */
public class FcL2SliceReadScenario extends FcAbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2SliceReadScenario.class);

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
  public FcL2SliceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      FcL2Slice l2Slice = getL2SliceFromDb(sessionWrapper, request.getSliceId());

      checkL2SlicePresence(l2Slice, request.getSliceId());

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

  private FcL2Slice getL2SliceFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      FcL2SliceDao l2SliceDao = new FcL2SliceDao();
      return l2SliceDao.read(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcL2Slice l2Slice) throws MsfException {
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
