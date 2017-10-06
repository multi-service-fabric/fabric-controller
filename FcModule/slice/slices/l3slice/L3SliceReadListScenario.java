package msf.fc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.slice.slices.l3slice.data.L3SliceReadDetailListResponseBody;
import msf.fc.slice.slices.l3slice.data.L3SliceReadListResponseBody;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;
import msf.fc.slice.slices.l3slice.data.entity.L3SliceEntity;

public class L3SliceReadListScenario extends AbstractL3SliceScenarioBase<L3SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceReadListScenario.class);

  public L3SliceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      List<L3Slice> l3SliceList = getL3SliceListFromDb(sessionWrapper);

      return createResponse(l3SliceList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      checkFormatOption(request.getFormat());


      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<L3Slice> getL3SliceListFromDb(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });
      L3SliceDao l3SliceDao = new L3SliceDao();
      return l3SliceDao.readList(sessionWrapper);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<L3Slice> l3SliceList, RestFormatOption formatOption)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l3SliceList", "formatOption" }, new Object[] { l3SliceList, formatOption });
        L3SliceReadDetailListResponseBody responseBody = createL3SliceDetailReadResponseBody(l3SliceList);
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
        return restResponse;
        L3SliceReadListResponseBody responseBody = createL3SliceReadResponseBody(l3SliceList);
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L3SliceReadDetailListResponseBody createL3SliceDetailReadResponseBody(List<L3Slice> l3SliceList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      List<L3SliceEntity> l3SliceEntityList = new ArrayList<>();

      for (L3Slice l3Slice : l3SliceList) {
        L3SliceEntity l3SliceEntity = createL3SliceEntity(l3Slice);
        l3SliceEntityList.add(l3SliceEntity);
      }
      L3SliceReadDetailListResponseBody responseBody = new L3SliceReadDetailListResponseBody();
      responseBody.setL3SliceList(l3SliceEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L3SliceReadListResponseBody createL3SliceReadResponseBody(List<L3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      List<String> l3SliceIdList = new ArrayList<>();
      for (L3Slice l3Slice : l3SliceList) {
        l3SliceIdList.add(l3Slice.getSliceId());
      }
      L3SliceReadListResponseBody responseBody = new L3SliceReadListResponseBody();
      responseBody.setL3SliceIdList(l3SliceIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
