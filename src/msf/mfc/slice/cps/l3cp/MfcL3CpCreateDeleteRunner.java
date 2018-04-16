
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpValueEntity;

/**
 * Implementation class for the asynchronous process in the L3CP
 * addition/deletion process.
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateDeleteRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateDeleteRunner.class);

  private List<L3CpCreateDeleteRequestBody> requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request body for L3CP addition
   */
  public MfcL3CpCreateDeleteRunner(L3CpRequest request, List<L3CpCreateDeleteRequestBody> requestBody) {
    this.request = request;
    this.requestBody = requestBody;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      sessionWrapper.beginTransaction();

      logger.performance("start get l3slice resources lock.");
      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, request.getSliceId());

      List<String> requestCpIdList = getNewL3CpCreateIdListFromPatchRequest(requestBody);

      Iterator<L3CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      while (iterator.hasNext()) {
        L3CpCreateDeleteRequestBody body = iterator.next();

        iterator.remove();
        switch (body.getOpEnum()) {
          case ADD:

            String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()),
                requestCpIdList);
            L3CpValueEntity entity = body.getValue();

            processCreateL3Cp(sessionWrapper, l3SliceAfterLock, cpId, entity.getClusterId(), entity.getEdgePointId(),
                entity.getVlanId(), entity.getMtu(), entity.getIpv4Address(), entity.getIpv6Address(),
                entity.getIpv4Prefix(), entity.getIpv6Prefix(), entity.getBgp(), entity.getStaticRouteList(),
                entity.getVrrp(), entity.getTrafficThreshold(), entity.getQos());
            break;
          case REMOVE:
            MfcL3Cp l3CpAfterLockRemove = getL3CpAndCheck(sessionWrapper, request.getSliceId(),
                getIdFromPath(body.getPath()));

            processDeleteL3Cp(l3CpAfterLockRemove, true);

            OperationManager.getInstance().setOperationData(getOperationId(), rollbackData);
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }
      }

      OperationManager.getInstance().setOperationData(getOperationId(), rollbackData);

      return sendAsyncRequestAndCreateResponse(sessionWrapper, true, request.getSliceId(),
          makeSendRequestDataList(request.getSliceId()), RequestType.REQUEST);
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
