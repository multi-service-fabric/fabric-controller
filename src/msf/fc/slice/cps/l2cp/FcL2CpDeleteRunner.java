
package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in the L2CP deletion.
 *
 * @author NTT
 *
 */
public class FcL2CpDeleteRunner extends FcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpDeleteRunner.class);

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for the L2CP control
   */
  public FcL2CpDeleteRunner(L2CpRequest request) {
    this.request = request;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      FcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      FcL2Cp l2Cp = getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode node = getNodeAndCheck(sessionWrapper, l2Cp.getEdgePoint().getEdgePointId());
      List<FcNode> nodeList = new ArrayList<>();
      nodeList.add(node);

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice and node resources lock.");

      FcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l2slice and node resources lock.");

      FcL2Cp l2CpAfterLock = getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, l2CpAfterLock.getEdgePoint().getEdgePointId());

      processDeleteL2Cp(sessionWrapper, l2CpAfterLock, nodeAfterLock);

      String requestJson = makeDeleteUpdateL2VlanIfData(deleteVlanIfEntityList, updateVlanIfEntityList, l2Slice);

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200);
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }
}
