package msf.fc.slice.cps.l2cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2CpPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;
import msf.fc.slice.cps.l2cp.data.entity.L2CpEntity;

public class L2CpReadScenario extends AbstractL2CpScenarioBase<L2CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpReadScenario.class);

  public L2CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L2Cp l2Cp = getL2CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());
      checkL2CpPresence(l2Cp);
      return createResponse(l2Cp);
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

  private L2Cp getL2CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      L2CpPK l2CpPk = new L2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      L2CpDao l2CpDao = new L2CpDao();
      return l2CpDao.read(sessionWrapper, l2CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });

      L2CpEntity l2CpEntity = createL2CpEntity(l2Cp);
      L2CpReadResponseBody responseBody = new L2CpReadResponseBody();
      responseBody.setL2Cp(l2CpEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
