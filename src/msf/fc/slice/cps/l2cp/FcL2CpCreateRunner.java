
package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in L2CP addition.
 *
 * @author NTT
 *
 */
public class FcL2CpCreateRunner extends FcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpCreateRunner.class);

  private L2CpCreateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2CP control
   * @param requestBody
   *          Request body for L2CP addition
   */
  public FcL2CpCreateRunner(L2CpRequest request, L2CpCreateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      int edgePointId = Integer.valueOf(requestBody.getEdgePointId());

      FcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      FcNode node = getNodeAndCheck(sessionWrapper, edgePointId);
      List<FcNode> nodeList = new ArrayList<>();
      nodeList.add(node);

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice and node resources lock.");

      FcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l2slice and node resources lock.");

      FcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, edgePointId);

      String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), requestBody.getCpId(), null);

      processCreateL2Cp(sessionWrapper, l2SliceAfterLock, nodeAfterLock, cpId, edgePointId, requestBody.getPortMode(),
          requestBody.getVlanId(), requestBody.getPairCpId(), null, requestBody.getQos());

      String requestJson = makeCreateUpdateL2VlanIfData(createVlanIfEntityList, updateVlanIfEntityList,
          l2Slice.getVrfId());

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200);
      return createResponseForL2CpCreate(cpId);
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
