
package msf.fc.slice.slices.l3slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Slice;
import msf.fc.db.dao.slices.FcL3SliceDao;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceReadResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;
import msf.mfcfc.slice.slices.l3slice.data.entity.L3SliceEntity;

/**
 * Implementation class for L3 slice information acquisition.
 *
 * @author NTT
 *
 */
public class FcL3SliceReadScenario extends FcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceReadScenario.class);

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
  public FcL3SliceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      FcL3Slice l3Slice = getL3SliceFromDb(sessionWrapper, request.getSliceId());

      checkL3SlicePresence(l3Slice, request.getSliceId(), true);

      return createResponse(l3Slice);
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

  private FcL3Slice getL3SliceFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      FcL3SliceDao l3SliceDao = new FcL3SliceDao();
      return l3SliceDao.read(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcL3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      L3SliceEntity l3SliceEntity = createL3SliceEntity(l3Slice);
      L3SliceReadResponseBody responseBody = new L3SliceReadResponseBody();
      responseBody.setL3Slice(l3SliceEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
