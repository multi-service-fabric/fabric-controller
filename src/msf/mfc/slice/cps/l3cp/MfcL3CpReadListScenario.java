
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
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
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadDetailListResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadListResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;

/**
 * Implementation class for L3CP information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcL3CpReadListScenario extends MfcAbstractL3CpScenarioBase<L3CpRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpReadListScenario.class);

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
  public MfcL3CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      List<MfcL3Cp> l3CpList = getL3CpListFromDb(sessionWrapper, request.getSliceId());

      if (l3CpList.size() == 0) {
        MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
        MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

        ParameterCheckUtil.checkNotNullRelatedResource(l3Slice, new String[] { "sliceId" },
            new Object[] { request.getSliceId() });
      }

      return createResponse(sessionWrapper, l3CpList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFormatOption(request.getFormat());

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<MfcL3Cp> getL3CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      return l3CpDao.readListBySliceId(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(SessionWrapper sessionWrapper, List<MfcL3Cp> l3CpList,
      RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3CpList", "formatOption" }, new Object[] { l3CpList, formatOption });
      if (RestFormatOption.DETAIL_LIST.equals(formatOption)) {

        Set<Integer> clusterIdSet = new HashSet<>();
        for (MfcL3Cp l3Cp : l3CpList) {
          clusterIdSet.add(l3Cp.getSwCluster().getSwClusterId());
        }

        List<RestResponseData> restResponseDataList = getCpDataListFromFc(SliceType.L3_SLICE, clusterIdSet,
            request.getSliceId());

        if (checkResponseAllSuccess(restResponseDataList)) {
          RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
              createL3CpDetailReadResponseBody(restResponseDataList));
          return restResponse;
        } else {

          return createErrorResponse(restResponseDataList, null);
        }
      } else {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL3CpReadResponseBody(l3CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadDetailListResponseBody createL3CpDetailReadResponseBody(List<RestResponseData> restResponseDataList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseDataList" }, new Object[] { restResponseDataList });
      List<L3CpEntity> l3CpEntityList = new ArrayList<>();
      for (RestResponseData restResponseData : restResponseDataList) {
        L3CpReadDetailListResponseBody body = JsonUtil.fromJson(restResponseData.getResponse().getResponseBody(),
            L3CpReadDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        if (body.getL3CpList() != null) {
          l3CpEntityList.addAll(body.getL3CpList());
        }
      }
      L3CpReadDetailListResponseBody responseBody = new L3CpReadDetailListResponseBody();
      responseBody.setL3CpList(l3CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadListResponseBody createL3CpReadResponseBody(List<MfcL3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<String> l3CpIdList = new ArrayList<>();
      for (MfcL3Cp l3Cp : l3CpList) {
        l3CpIdList.add(l3Cp.getId().getCpId());
      }
      L3CpReadListResponseBody responseBody = new L3CpReadListResponseBody();
      responseBody.setL3CpIdList(l3CpIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
