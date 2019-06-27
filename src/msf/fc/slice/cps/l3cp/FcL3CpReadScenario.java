
package msf.fc.slice.cps.l3cp;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3CpPK;
import msf.fc.db.dao.slices.FcL3CpDao;
import msf.fc.rest.ec.node.interfaces.vlan.data.VlanIfReadEcResponseBody;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;

/**
 * Implementation class for the L3CP information acquisition.
 *
 * @author NTT
 *
 */
public class FcL3CpReadScenario extends FcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpReadScenario.class);

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
  public FcL3CpReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      FcL3Cp l3Cp = getL3CpFromDb(sessionWrapper, request.getSliceId(), request.getCpId());

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

  private FcL3Cp getL3CpFromDb(SessionWrapper sessionWrapper, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId", "cpId" },
          new Object[] { sessionWrapper, sliceId, cpId });
      FcL3CpPK l3CpPk = new FcL3CpPK();
      l3CpPk.setSliceId(sliceId);
      l3CpPk.setCpId(cpId);
      FcL3CpDao l3CpDao = new FcL3CpDao();
      return l3CpDao.read(sessionWrapper, l3CpPk);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponse(SessionWrapper sessionWrapper, FcL3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });

      VlanIfReadEcResponseBody vlanIfResponseBody = getVlanIf(sessionWrapper, l3Cp.getVlanIf().getId().getNodeInfoId(),
          l3Cp.getVlanIf().getId().getVlanIfId());

      L3CpEntity l3CpEntity = createL3CpEntity(sessionWrapper, l3Cp, vlanIfResponseBody.getVlanIf());
      L3CpReadResponseBody responseBody = new L3CpReadResponseBody();
      responseBody.setL3Cp(l3CpEntity);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
