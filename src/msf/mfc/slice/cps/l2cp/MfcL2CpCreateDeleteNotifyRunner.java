
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.PatchOperation;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpQosCreateEntity;

/**
 * Implementation class for the reception process of operation result
 * notification in L2CP addition/deletion(/modification).
 *
 * @author NTT
 *
 */
public class MfcL2CpCreateDeleteNotifyRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpCreateDeleteNotifyRunner.class);

  private List<String> createdCpIdList = new ArrayList<>();

  public MfcL2CpCreateDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      Matcher cpCreateDeleteMatcher = MfcFcRequestUri.CP_CREATE_DELETE.getUriPattern()
          .matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (cpCreateDeleteMatcher.matches()) {
        sliceId = cpCreateDeleteMatcher.group(2);
      }
      sessionWrapper.openSession();

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, sliceId);

      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
        List<L2CpCreateDeleteRequestBody> lowerBodyList = (List<L2CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
            });
        for (L2CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
          String lowerCpId = getIdFromPath(lowerBody.getPath());
          switch (lowerBody.getOpEnum()) {
            case ADD:

              checkCpDuplicate(sessionWrapper, l2CpDao, sliceId, lowerCpId);
              MfcL2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2SliceAfterLock.getSliceId(), lowerCpId,
                  Integer.valueOf(lowerBody.getValue().getClusterId()));

              createdCpIdList.add(lowerCpId);
              newL2Cp.setEsi(lowerBody.getValue().getEsi());
              l2CpDao.create(sessionWrapper, newL2Cp);
              break;
            case REPLACE:
              MfcL2Cp updateL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);

              if (lowerBody.getValue().getEsi().equals("0")) {
                updateL2Cp.setEsi(null);
              } else {
                updateL2Cp.setEsi(lowerBody.getValue().getEsi());
              }
              l2CpDao.update(sessionWrapper, updateL2Cp);
              break;
            case REMOVE:
              MfcL2Cp removeL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);
              l2CpDao.delete(sessionWrapper, removeL2Cp.getId());
              break;
            default:
              String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
              throw new IllegalArgumentException(message);
          }
        }
      }

      sessionWrapper.commit(MfcFcRequestUri.CP_CREATE_DELETE);

      if (createdCpIdList.size() != 0) {
        return createResponseForMultiL2CpCreate(createdCpIdList);
      } else {
        return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
      }
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
  protected RestResponseBase executeRollbackImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      Matcher cpCreateDeleteMatcher = MfcFcRequestUri.CP_CREATE_DELETE.getUriPattern()
          .matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (cpCreateDeleteMatcher.matches()) {
        sliceId = cpCreateDeleteMatcher.group(2);
      }
      sessionWrapper.openSession();

      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
        List<L2CpCreateDeleteRequestBody> lowerBodyList = (List<L2CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
            });
        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          for (L2CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
            String lowerCpId = getIdFromPath(lowerBody.getPath());
            switch (lowerBody.getOpEnum()) {
              case ADD:

                L2CpCreateDeleteRequestBody deleteBody = makeL2CpCreateDeleteRequestBodyForDelete(lowerCpId);
                putL2CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), deleteBody);
                break;
              case REMOVE:

                rollbackData = (CpDeleteRollbackData) OperationManager.getInstance()
                    .getOperationData(asyncRequestForNotify.getOperationId());
                L2CpEntity entity = rollbackData.getTargetL2CpEntity(sliceId, lowerCpId);

                L2CpQosCreateEntity qosEntity = new L2CpQosCreateEntity();
                qosEntity.setEgressQueueMenu(entity.getQos().getEgressQueueMenu());
                qosEntity.setIngressShapingRate(entity.getQos().getIngressShapingRate());
                qosEntity.setEgressShapingRate(entity.getQos().getEgressShapingRate());

                L2CpCreateDeleteRequestBody createBody = makeL2CpCreateDeleteRequestBody(PatchOperation.ADD,
                    entity.getCpId(), Integer.valueOf(entity.getClusterId()), Integer.valueOf(entity.getEdgePointId()),
                    entity.getEsi(), entity.getPortMode(), entity.getVlanId(), qosEntity);
                putL2CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), createBody);
                break;
              case REPLACE:

                MfcL2Cp updateL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);

                L2CpCreateDeleteRequestBody updateBody = makeL2CpCreateDeleteRequestBodyForUpdate(lowerCpId,
                    updateL2Cp.getEsi() != null ? updateL2Cp.getEsi() : "0");
                putL2CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), updateBody);
                break;
              default:
                String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
                throw new IllegalArgumentException(message);
            }
          }
        }
      }

      return sendAsyncRequestAndCreateResponse(sliceId, makeSendRequestDataList(sliceId,
          getLowerOperationIdMap(asyncRequestForNotify.getAsyncRequestsForLowerList())), RequestType.ROLLBACK);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }
}
