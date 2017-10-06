package msf.fc.slice.slices.l2slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.slice.slices.l2slice.data.L2SliceReadResponseBody;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;
import msf.fc.slice.slices.l2slice.data.entity.L2SliceEntity;

public class L2SliceReadScenario extends AbstractL2SliceScenarioBase<L2SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceReadScenario.class);

  public L2SliceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L2Slice l2Slice = getL2SliceFromDb(sessionWrapper, request.getSliceId());
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

  private L2Slice getL2SliceFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      L2SliceDao l2SliceDao = new L2SliceDao();
      return l2SliceDao.read(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Slice l2Slice) throws MsfException {
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
