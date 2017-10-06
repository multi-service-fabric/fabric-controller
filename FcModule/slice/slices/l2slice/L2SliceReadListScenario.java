package msf.fc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.slice.slices.l2slice.data.L2SliceReadDetailListResponseBody;
import msf.fc.slice.slices.l2slice.data.L2SliceReadListResponseBody;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;
import msf.fc.slice.slices.l2slice.data.entity.L2SliceEntity;

public class L2SliceReadListScenario extends AbstractL2SliceScenarioBase<L2SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceReadListScenario.class);

  public L2SliceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      List<L2Slice> l2SliceList = getL2SliceListFromDb(sessionWrapper);

      return createResponse(l2SliceList, request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      checkFormatOption(request.getFormat());


      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<L2Slice> getL2SliceListFromDb(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });
      L2SliceDao l2SliceDao = new L2SliceDao();
      return l2SliceDao.readList(sessionWrapper);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<L2Slice> l2SliceList, RestFormatOption formatOption)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2SliceList", "formatOption" }, new Object[] { l2SliceList, formatOption });
        L2SliceReadDetailListResponseBody responseBody = createL2SliceDetailReadResponseBody(l2SliceList);
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
        return restResponse;
        L2SliceReadListResponseBody responseBody = createL2SliceReadResponseBody(l2SliceList);
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private L2SliceReadDetailListResponseBody createL2SliceDetailReadResponseBody(List<L2Slice> l2SliceList)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      List<L2SliceEntity> l2SliceEntityList = new ArrayList<>();

      for (L2Slice l2Slice : l2SliceList) {
        L2SliceEntity l2SliceEntity = createL2SliceEntity(l2Slice);
        l2SliceEntityList.add(l2SliceEntity);
      }
      L2SliceReadDetailListResponseBody responseBody = new L2SliceReadDetailListResponseBody();
      responseBody.setL2SliceList(l2SliceEntityList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private L2SliceReadListResponseBody createL2SliceReadResponseBody(List<L2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      List<String> l2SliceIdList = new ArrayList<>();
      for (L2Slice l2Slice : l2SliceList) {
        l2SliceIdList.add(l2Slice.getSliceId());
      }
      L2SliceReadListResponseBody responseBody = new L2SliceReadListResponseBody();
      responseBody.setL2SliceIdList(l2SliceIdList);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
