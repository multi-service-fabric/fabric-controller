
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in L2CP addition.
 *
 * @author NTT
 *
 */
public class MfcL2CpCreateDeleteRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpCreateDeleteRunner.class);

  private List<L2CpCreateDeleteRequestBody> requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2CP control
   * @param requestBody
   *          Request body for L2CP addition/deletion(/modification)
   */
  public MfcL2CpCreateDeleteRunner(L2CpRequest request, List<L2CpCreateDeleteRequestBody> requestBody) {
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

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      sessionWrapper.beginTransaction();

      logger.performance("start get l2slice resources lock.");
      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, request.getSliceId());

      List<String> requestCpIdList = getNewL2CpCreateIdListFromPatchRequest(requestBody);

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        iterator.remove();
        switch (body.getOpEnum()) {
          case ADD:

            String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), getIdFromPath(body.getPath()),
                requestCpIdList);

            processCreateL2Cp(sessionWrapper, l2SliceAfterLock, cpId, Integer.valueOf(body.getValue().getEdgePointId()),
                body.getValue().getPortMode(), body.getValue().getVlanId(), body.getValue().getPairCpId(),
                Integer.valueOf(body.getValue().getClusterId()), body.getValue().getQos());
            break;
          case REMOVE:
            MfcL2Cp l2CpAfterLockRemove = getL2CpAndCheck(sessionWrapper, request.getSliceId(),
                getIdFromPath(body.getPath()));

            processDeleteL2Cp(sessionWrapper, l2CpAfterLockRemove);
            break;
          case REPLACE:
            String logMsg = "replace operation is not supported in MFC.";
            logger.debug(logMsg);
            throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", body.getOp());
            throw new IllegalArgumentException(message);
        }

        iterator = requestBody.iterator();
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

  @Override
  protected void processCreateL2CpForPairCpNotFound(SessionWrapper sessionWrapper, MfcL2Cp newL2Cp, String pairCpId,
      String portMode, int vlanId, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "newL2Cp", "pairCpId", "portMode", "vlanId", "edgePointId" },
          new Object[] { sessionWrapper, newL2Cp, pairCpId, portMode, vlanId, edgePointId });
      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      boolean isFoundPairCp = false;
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        if (pairCpId.equals(getIdFromPath(body.getPath())) && body.getValue() != null
            && newL2Cp.getId().getCpId().equals(body.getValue().getPairCpId())) {

          iterator.remove();

          isFoundPairCp = true;
          int pairCpClusterId = Integer.valueOf(body.getValue().getClusterId());
          int newCpClusterId = newL2Cp.getSwCluster().getSwClusterId();

          checkPairCpVlanIdAndPortMode(request.getSliceId(), portMode, vlanId, pairCpClusterId, null,
              body.getValue().getPortMode(), body.getValue().getVlanId());

          MfcL2Cp pairL2Cp = makeNewL2Cp(sessionWrapper, request.getSliceId(), pairCpId, pairCpClusterId);

          List<MfcL2Cp> l2CpList = l2CpDao.readList(sessionWrapper);
          Set<String> esiIdSet = createEsiIdSet(l2CpList);

          String esi;
          if (pairCpClusterId > newCpClusterId) {
            esi = getNextEsi(sessionWrapper, esiIdSet, newCpClusterId, pairCpClusterId);
          } else {
            esi = getNextEsi(sessionWrapper, esiIdSet, pairCpClusterId, newCpClusterId);
          }
          newL2Cp.setEsi(esi);
          pairL2Cp.setEsi(esi);

          L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBody(PatchOperation.ADD, pairCpId,
              pairCpClusterId, Integer.valueOf(body.getValue().getEdgePointId()), esi, portMode, vlanId,
              body.getValue().getQos());
          putL2CpCreateDeleteRequestBodyForMap(pairCpClusterId, requestBody);
          break;
        }
      }

      if (!isFoundPairCp) {
        super.processCreateL2CpForPairCpNotFound(sessionWrapper, newL2Cp, pairCpId, portMode, vlanId, edgePointId);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void processDeleteL2CpForPairCpFound(SessionWrapper sessionWrapper, MfcL2Cp pairL2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "pairL2Cp" }, new Object[] { sessionWrapper, pairL2Cp });

      Iterator<L2CpCreateDeleteRequestBody> iterator = requestBody.iterator();
      boolean isFoundPairCp = false;
      while (iterator.hasNext()) {
        L2CpCreateDeleteRequestBody body = iterator.next();

        if (pairL2Cp.getId().getCpId().equals(getIdFromPath(body.getPath()))) {

          iterator.remove();

          isFoundPairCp = true;

          RestResponseBase response = getCpDataFromFc(SliceType.L2_SLICE, pairL2Cp.getId().getSliceId(),
              pairL2Cp.getId().getCpId(), pairL2Cp.getSwCluster().getSwClusterId());

          rollbackData.addL2CpEntity(JsonUtil
              .fromJson(response.getResponseBody(), L2CpReadResponseBody.class, ErrorCode.UNDEFINED_ERROR).getL2Cp());

          L2CpCreateDeleteRequestBody requestBody = makeL2CpCreateDeleteRequestBodyForDelete(
              pairL2Cp.getId().getCpId());
          putL2CpCreateDeleteRequestBodyForMap(pairL2Cp.getSwCluster().getSwClusterId(), requestBody);
        }
      }

      if (!isFoundPairCp) {
        super.processDeleteL2CpForPairCpFound(sessionWrapper, pairL2Cp);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
