
package msf.mfc.slice.cps.l2cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2CpPK;
import msf.mfc.db.dao.slices.MfcL2CpDao;
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
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Implementation class for L2CP information acquisition.
 *
 * @author NTT
 *
 */
public class MfcL2CpReadScenario extends MfcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpReadScenario.class);

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
  public MfcL2CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      MfcL2Cp l2Cp = getL2CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());

      checkL2CpPresence(l2Cp);

      return createResponse(sessionWrapper, l2Cp);
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

  private MfcL2Cp getL2CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      MfcL2CpPK l2CpPk = new MfcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      return l2CpDao.read(sessionWrapper, l2CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponse(SessionWrapper sessionWrapper, MfcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });

      RestResponseBase restResponseBase = getCpDataFromFc(SliceType.L2_SLICE, l2Cp.getId().getSliceId(),
          l2Cp.getId().getCpId(), l2Cp.getSwCluster().getSwClusterId());
      L2CpReadResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          L2CpReadResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
