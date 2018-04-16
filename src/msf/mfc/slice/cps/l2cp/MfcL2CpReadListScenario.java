
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadDetailListResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadListResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;

/**
 * Implementation class for L2CP information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcL2CpReadListScenario extends MfcAbstractL2CpScenarioBase<L2CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpReadListScenario.class);

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
  public MfcL2CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      List<MfcL2Cp> l2CpList = getL2CpListFromDb(sessionWrapper, request.getSliceId());

      if (l2CpList.size() == 0) {
        MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
        MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());

        ParameterCheckUtil.checkNotNullRelatedResource(l2Slice, new String[] { "sliceId" },
            new Object[] { request.getSliceId() });
      }

      return createResponse(sessionWrapper, l2CpList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFormatOption(request.getFormat());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<MfcL2Cp> getL2CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      return l2CpDao.readListBySliceId(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(SessionWrapper sessionWrapper, List<MfcL2Cp> l2CpList,
      RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2CpList", "formatOption" }, new Object[] { l2CpList, formatOption });
      if (RestFormatOption.DETAIL_LIST.equals(formatOption)) {

        Set<Integer> clusterIdSet = new HashSet<>();
        for (MfcL2Cp l2Cp : l2CpList) {
          clusterIdSet.add(l2Cp.getSwCluster().getSwClusterId());
        }

        List<RestResponseData> restResponseDataList = getCpDataListFromFc(SliceType.L2_SLICE, clusterIdSet,
            request.getSliceId());

        if (checkResponseAllSuccess(restResponseDataList)) {
          RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
              createL2CpDetailReadResponseBody(restResponseDataList));
          return restResponse;
        } else {

          return createErrorResponse(restResponseDataList, null);
        }
      } else {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL2CpReadResponseBody(l2CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadDetailListResponseBody createL2CpDetailReadResponseBody(List<RestResponseData> restResponseDataList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseDataList" }, new Object[] { restResponseDataList });
      List<L2CpEntity> l2CpEntityList = new ArrayList<>();
      for (RestResponseData restResponseData : restResponseDataList) {
        L2CpReadDetailListResponseBody body = JsonUtil.fromJson(restResponseData.getResponse().getResponseBody(),
            L2CpReadDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        if (body.getL2CpList() != null) {
          l2CpEntityList.addAll(body.getL2CpList());
        }
      }
      L2CpReadDetailListResponseBody responseBody = new L2CpReadDetailListResponseBody();
      responseBody.setL2CpList(l2CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadListResponseBody createL2CpReadResponseBody(List<MfcL2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<String> l2CpIdList = new ArrayList<>();
      for (MfcL2Cp l2Cp : l2CpList) {
        l2CpIdList.add(l2Cp.getId().getCpId());
      }
      L2CpReadListResponseBody responseBody = new L2CpReadListResponseBody();
      responseBody.setL2CpIdList(l2CpIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
