
package msf.mfc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3CpPK;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Implementation class for L3CP information acquisition.
 *
 * @author NTT
 *
 */
public class MfcL3CpReadScenario extends MfcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpReadScenario.class);

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
  public MfcL3CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      MfcL3Cp l3Cp = getL3CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());

      checkL3CpPresence(l3Cp);

      return createResponse(sessionWrapper, l3Cp);
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

  private MfcL3Cp getL3CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      MfcL3CpPK l3CpPk = new MfcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      return l3CpDao.read(sessionWrapper, l3CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponse(SessionWrapper sessionWrapper, MfcL3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });

      RestResponseBase restResponseBase = getCpDataFromFc(SliceType.L3_SLICE, l3Cp.getId().getSliceId(),
          l3Cp.getId().getCpId(), l3Cp.getSwCluster().getSwClusterId());
      L3CpReadResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          L3CpReadResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
