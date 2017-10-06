package msf.fc.slice.slices.l3slice;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.slice.slices.l3slice.data.L3SliceReadResponseBody;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;
import msf.fc.slice.slices.l3slice.data.entity.L3SliceEntity;

public class L3SliceReadScenario extends AbstractL3SliceScenarioBase<L3SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceReadScenario.class);

  public L3SliceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L3Slice l3Slice = getL3SliceFromDb(sessionWrapper, request.getSliceId());
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

  private L3Slice getL3SliceFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      L3SliceDao l3SliceDao = new L3SliceDao();
      return l3SliceDao.read(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L3Slice l3Slice) throws MsfException {
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
