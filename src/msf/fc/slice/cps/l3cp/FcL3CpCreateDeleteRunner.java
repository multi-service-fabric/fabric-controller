
package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpValueEntity;

/**
 * Class to implement asynchronous processing in L3CP generation.
 *
 * @author NTT
 *
 */
public class FcL3CpCreateDeleteRunner extends FcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpCreateDeleteRunner.class);

  private List<L3CpCreateDeleteRequestBody> requestBody;

  private Set<FcNode> nodeSetForExclusive = new HashSet<>();
  private List<String> createdCpIdList = new ArrayList<>();

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario side
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request body for L3CP generation
   */
  public FcL3CpCreateDeleteRunner(L3CpRequest request, List<L3CpCreateDeleteRequestBody> requestBody) {
    this.request = request;
    this.requestBody = requestBody;
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

      for (L3CpCreateDeleteRequestBody body : requestBody) {
        switch (body.getOpEnum()) {
          case ADD:
            FcNode nodeAdd = getNodeAndCheck(sessionWrapper, Integer.valueOf(body.getValue().getEdgePointId()));
            nodeSetForExclusive.add(nodeAdd);
            break;
          case REMOVE:
            FcL3Cp l3Cp = getL3CpAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()));
            FcNode nodeRemove = getNodeAndCheck(sessionWrapper, l3Cp.getEdgePoint().getEdgePointId());
            nodeSetForExclusive.add(nodeRemove);
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }
      }

      sessionWrapper.beginTransaction();

      logger.performance("start get l3slice and node resources lock.");
      FcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, new ArrayList<>(nodeSetForExclusive),
          new ArrayList<>(), sessionWrapper);
      logger.performance("end get l3slice and node resources lock.");

      FcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, request.getSliceId());

      List<String> requestCpIdList = getNewL3CpCreateIdListFromPatchRequest(requestBody);

      Iterator<L3CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      while (iterator.hasNext()) {
        L3CpCreateDeleteRequestBody body = iterator.next();

        iterator.remove();
        switch (body.getOpEnum()) {
          case ADD:
            FcNode nodeAfterLockAdd = getNodeAndCheck(sessionWrapper,
                Integer.valueOf(body.getValue().getEdgePointId()));

            String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()),
                requestCpIdList);

            createdCpIdList.add(cpId);

            L3CpValueEntity value = body.getValue();
            processCreateL3Cp(sessionWrapper, l3SliceAfterLock, nodeAfterLockAdd, cpId,
                Integer.valueOf(body.getValue().getEdgePointId()), value.getVlanId(), value.getMtu(),
                value.getIpv4Address(), value.getIpv6Address(), value.getIpv4Prefix(), value.getIpv6Prefix(),
                value.getBgp(), value.getStaticRouteList(), value.getVrrp(), value.getTrafficThreshold());
            break;
          case REMOVE:
            FcL3Cp l3CpAfterLockRemove = getL3CpAndCheck(sessionWrapper, request.getSliceId(),
                getIdFromPath(body.getPath()));
            FcNode nodeAfterLockRemove = getNodeAndCheck(sessionWrapper,
                l3CpAfterLockRemove.getEdgePoint().getEdgePointId());

            processDeleteL3Cp(sessionWrapper, l3CpAfterLockRemove, nodeAfterLockRemove);
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }
      }

      String requestJson = null;

      RestResponseBase response = null;

      if (createVlanIfEntityList.size() != 0) {
        requestJson = makeCreateL3VlanIfData(createVlanIfEntityList, l3SliceAfterLock.getVrfId(),
            l3SliceAfterLock.getPlane());
        response = createResponseForMultiL3CpCreate(createdCpIdList);
      } else {
        requestJson = makeDeleteL3VlanIfData(deleteVlanIfEntityList, String.valueOf(l3Slice.getVrfId()));
        response = new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
      }

      RestResponseBase restResponse = sendRequestToEc(requestJson, EcRequestUri.OPERATION_REQUEST, (String[]) null);

      commitTransaction(sessionWrapper, restResponse, HttpStatus.OK_200);
      return response;

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
