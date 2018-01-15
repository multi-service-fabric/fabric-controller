
package msf.fc.slice.cps.l2cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.db.dao.slices.FcL2CpDao;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;

/**
 * Implementation class for L2CP information acquisition.
 *
 * @author NTT
 *
 */
public class FcL2CpReadScenario extends FcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpReadScenario.class);

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
  public FcL2CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      FcL2Cp l2Cp = getL2CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());

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

  private FcL2Cp getL2CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      FcL2CpPK l2CpPk = new FcL2CpPK();
      l2CpPk.setSliceId(sliceId);
      l2CpPk.setCpId(cpId);
      FcL2CpDao l2CpDao = new FcL2CpDao();
      return l2CpDao.read(sessionWrapper, l2CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponse(SessionWrapper sessionWrapper, FcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });

      VlanIfReadEcResponseBody vlanIfResponseBody = getVlanIf(sessionWrapper,
          l2Cp.getVlanIf().getId().getNodeInfoId().longValue(), l2Cp.getVlanIf().getId().getVlanIfId());

      L2CpEntity l2CpEntity = createL2CpEntity(sessionWrapper, l2Cp, vlanIfResponseBody.getVlanIf());
      L2CpReadResponseBody responseBody = new L2CpReadResponseBody();
      responseBody.setL2Cp(l2CpEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
