
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Class to implement asynchronous processing in L3CP generation.
 *
 * @author NTT
 *
 */
public class FcL3CpCreateRunner extends FcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpCreateRunner.class);

  private L3CpCreateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario side
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request body for L3CP generation
   */
  public FcL3CpCreateRunner(L3CpRequest request, L3CpCreateRequestBody requestBody) {
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

      FcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      List<FcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      FcNode node = getNodeAndCheck(sessionWrapper, edgePointId);
      List<FcNode> nodeList = new ArrayList<>();
      nodeList.add(node);

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice and node resources lock.");

      FcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, nodeList, new ArrayList<>(),
          sessionWrapper);
      logger.performance("end get l3slice and node resources lock.");

      FcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      FcNode nodeAfterLock = getNodeAndCheck(sessionWrapper, edgePointId);

      String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), requestBody.getCpId(), null);

      processCreateL3Cp(sessionWrapper, l3SliceAfterLock, nodeAfterLock, cpId, edgePointId, requestBody.getVlanId(),
          requestBody.getMtu(), requestBody.getIpv4Address(), requestBody.getIpv6Address(), requestBody.getIpv4Prefix(),
          requestBody.getIpv6Prefix(), requestBody.getBgp(), requestBody.getStaticRouteList(), requestBody.getVrrp(),
          requestBody.getTrafficThreshold());

      String requestJson = makeCreateL3VlanIfData(createVlanIfEntityList, l3SliceAfterLock.getVrfId(),
          l3SliceAfterLock.getPlane());

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200);
      return createResponseForL3CpCreate(cpId);
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
