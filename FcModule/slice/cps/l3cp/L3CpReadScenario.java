package msf.fc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.slice.cps.l3cp.data.L3CpReadResponseBody;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.entity.L3CpEntity;

public class L3CpReadScenario extends AbstractL3CpScenarioBase<L3CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpReadScenario.class);

  public L3CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L3Cp l3Cp = getL3CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());
      checkL3CpPresence(l3Cp);
      return createResponse(l3Cp);
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

  private L3Cp getL3CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      L3CpDao l3CpDao = new L3CpDao();
      return l3CpDao.read(sessionWrapper, l3CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });

      L3CpEntity l3CpEntity = createL3CpEntity(l3Cp);
      L3CpReadResponseBody responseBody = new L3CpReadResponseBody();
      responseBody.setL3Cp(l3CpEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
