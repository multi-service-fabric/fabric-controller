package msf.fc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.slice.cps.l2cp.data.L2CpReadDetailListResponseBody;
import msf.fc.slice.cps.l2cp.data.L2CpReadListResponseBody;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;
import msf.fc.slice.cps.l2cp.data.entity.L2CpEntity;

public class L2CpReadListScenario extends AbstractL2CpScenarioBase<L2CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpReadListScenario.class);

  public L2CpReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      List<L2Cp> l2CpList = getL2CpListFromDb(sessionWrapper, request.getSliceId());
      if (l2CpList.size() == 0) {
        L2SliceDao l2SliceDao = new L2SliceDao();
        L2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());
        checkL2SlicePresence(l2Slice, request.getSliceId(), false);
      }

      return createResponse(l2CpList, request.getFormatEnum());
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

  private List<L2Cp> getL2CpListFromDb(SessionWrapper sessionWrapper, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceId" }, new Object[] { sessionWrapper, sliceId });
      L2CpDao l2CpDao = new L2CpDao();
      return l2CpDao.readList(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<L2Cp> l2CpList, RestFormatOption formatOption) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2CpList", "formatOption" }, new Object[] { l2CpList, formatOption });
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createL2CpDetailReadResponseBody(l2CpList));
        return restResponse;
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, createL2CpReadResponseBody(l2CpList));
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadDetailListResponseBody createL2CpDetailReadResponseBody(List<L2Cp> l2CpList) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<L2CpEntity> l2CpEntityList = new ArrayList<>();

      for (L2Cp l2Cp : l2CpList) {
        L2CpEntity l2CpEntity = createL2CpEntity(l2Cp);
        l2CpEntityList.add(l2CpEntity);
      }
      L2CpReadDetailListResponseBody responseBody = new L2CpReadDetailListResponseBody();
      responseBody.setL2CpList(l2CpEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L2CpReadListResponseBody createL2CpReadResponseBody(List<L2Cp> l2CpList) {
    try {
      logger.methodStart(new String[] { "l2CpList" }, new Object[] { l2CpList });
      List<String> l2CpIdList = new ArrayList<>();
      for (L2Cp l2Cp : l2CpList) {
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
