
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Class to implement the asynchronous processing in L3CP deletion.
 *
 * @author NTT
 *
 */
public class FcL3CpDeleteRunner extends FcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpDeleteRunner.class);

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   */
  public FcL3CpDeleteRunner(L3CpRequest request) {
    this.request = request;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      FcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      FcL3Cp l3Cp = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode node = getNodeAndCheck(sessionWrapper, l3Cp.getEdgePoint().getEdgePointId());
      List<FcNode> nodeList = new ArrayList<>();
      nodeList.add(node);

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice and node resources lock.");

      FcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l3slice and node resources lock.");

      FcL3Cp l3CpAfterLock = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, l3CpAfterLock.getEdgePoint().getEdgePointId());

      processDeleteL3Cp(sessionWrapper, l3CpAfterLock, nodeAfterLock);

      String requestJson = makeDeleteL3VlanIfData(deleteVlanIfEntityList, String.valueOf(l3Slice.getVrfId()));

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
