
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL3CpDao;
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
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpQosCreateEntity;

/**
 * Implementation class for the reception process of operation result
 * notification in L3CP addition/deletion(/modification).
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateDeleteNotifyRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateDeleteNotifyRunner.class);
  private List<String> createdCpIdList = new ArrayList<>();

  public MfcL3CpCreateDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      Matcher cpCreateDeleteMatcher = MfcFcRequestUri.CP_CREATE_DELETE.getUriPattern()
          .matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (cpCreateDeleteMatcher.matches()) {
        sliceId = cpCreateDeleteMatcher.group(2);
      }
      sessionWrapper.openSession();

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, sliceId);

      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
        List<L3CpCreateDeleteRequestBody> lowerBodyList = (List<L3CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L3CpCreateDeleteRequestBody>>() {
            });
        for (L3CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
          String lowerCpId = getIdFromPath(lowerBody.getPath());
          switch (lowerBody.getOpEnum()) {
            case ADD:

              checkCpDuplicate(sessionWrapper, l3CpDao, sliceId, lowerCpId);
              MfcL3Cp newL3Cp = makeNewL3Cp(l3SliceAfterLock, lowerCpId, lowerBody.getValue().getClusterId());

              createdCpIdList.add(lowerCpId);
              l3CpDao.create(sessionWrapper, newL3Cp);
              break;
            case REMOVE:

              MfcL3Cp removeL3Cp = getL3CpAndCheck(sessionWrapper, sliceId, lowerCpId);
              l3CpDao.delete(sessionWrapper, removeL3Cp.getId());
              break;
            default:
              String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
              throw new IllegalArgumentException(message);
          }
        }
      }

      sessionWrapper.commit(MfcFcRequestUri.CP_CREATE_DELETE);

      if (createdCpIdList.size() != 0) {
        return createResponseForMultiL3CpCreate(createdCpIdList);
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
        List<L3CpCreateDeleteRequestBody> lowerBodyList = (List<L3CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L3CpCreateDeleteRequestBody>>() {
            });
        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          for (L3CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
            String lowerCpId = getIdFromPath(lowerBody.getPath());
            switch (lowerBody.getOpEnum()) {
              case ADD:

                L3CpCreateDeleteRequestBody deleteBody = makeL3CpCreateDeleteRequestBodyForDelete(lowerCpId);
                putL3CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), deleteBody);
                break;
              case REMOVE:

                rollbackData = (CpDeleteRollbackData) OperationManager.getInstance()
                    .getOperationData(asyncRequestForNotify.getOperationId());
                L3CpEntity entity = rollbackData.getTargetL3CpEntity(sliceId, lowerCpId);

                L3CpQosCreateEntity qosEntity = new L3CpQosCreateEntity();
                qosEntity.setEgressQueueMenu(entity.getQos().getEgressQueueMenu());
                qosEntity.setIngressShapingRate(entity.getQos().getIngressShapingRate());
                qosEntity.setEgressShapingRate(entity.getQos().getEgressShapingRate());
                L3CpCreateDeleteRequestBody createBody = makeL3CpCreateDeleteRequestBody(PatchOperation.ADD,
                    entity.getCpId(), entity.getClusterId(), entity.getEdgePointId(), entity.getVlanId(),
                    entity.getMtu(), entity.getIpv4Address(), entity.getIpv6Address(), entity.getIpv4Prefix(),
                    entity.getIpv6Prefix(), entity.getBgp(), entity.getStaticRouteList(), entity.getVrrp(),
                    entity.getTrafficThreshold(), qosEntity);
                putL3CpCreateDeleteRequestBodyForMap(Integer.valueOf(entity.getClusterId()), createBody);
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
