package msf.fc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.slice.cps.l3cp.data.L3CpReadDetailListResponseBody;
import msf.fc.slice.cps.l3cp.data.L3CpReadListResponseBody;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.entity.L3CpEntity;

public class L3CpReadListScenario extends AbstractL3CpScenarioBase<L3CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpReadListScenario.class);

  public L3CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      List<L3Cp> l3CpList = getL3CpListFromDb(sessionWrapper, request.getSliceId());
      if (l3CpList.size() == 0) {
        L3SliceDao l3SliceDao = new L3SliceDao();
        L3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());
        checkL3SlicePresence(l3Slice, request.getSliceId(), false);
      }

      return createResponse(l3CpList, request.getFormatEnum());
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

  private List<L3Cp> getL3CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      L3CpDao l3CpDao = new L3CpDao();
      return l3CpDao.readList(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<L3Cp> l3CpList, RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3CpList", "formatOption" }, new Object[] { l3CpList, formatOption });
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createL3CpDetailReadResponseBody(l3CpList));
        return restResponse;
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL3CpReadResponseBody(l3CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadDetailListResponseBody createL3CpDetailReadResponseBody(List<L3Cp> l3CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<L3CpEntity> l3CpEntityList = new ArrayList<>();

      for (L3Cp l3Cp : l3CpList) {
        L3CpEntity l3CpEntity = createL3CpEntity(l3Cp);
        l3CpEntityList.add(l3CpEntity);
      }
      L3CpReadDetailListResponseBody responseBody = new L3CpReadDetailListResponseBody();
      responseBody.setL3CpList(l3CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L3CpReadListResponseBody createL3CpReadResponseBody(List<L3Cp> l3CpList) {
    try {
      logger.methodStart(new String[] { "l3CpList" }, new Object[] { l3CpList });
      List<String> l3CpIdList = new ArrayList<>();
      for (L3Cp l3Cp : l3CpList) {
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
