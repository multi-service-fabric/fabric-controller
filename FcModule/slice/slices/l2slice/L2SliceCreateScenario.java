package msf.fc.slice.slices.l2slice;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SliceType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.slices.l2slice.data.L2SliceCreateRequestBody;
import msf.fc.slice.slices.l2slice.data.L2SliceCreateResponseBody;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;

public class L2SliceCreateScenario extends AbstractL2SliceScenarioBase<L2SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceCreateScenario.class);
  private static final Object lockObject = new Object();
  private L2SliceCreateRequestBody requestBody;

  public L2SliceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.performance("start wait to l2slice creation process.");
    synchronized (lockObject) {
      logger.performance("end wait to l2slice creation process.");
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        logger.methodStart();
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();
        L2SliceDao l2SliceDao = new L2SliceDao();
        L2Slice newL2Slice = makeNewL2Slice(sessionWrapper, l2SliceDao);
        l2SliceDao.create(sessionWrapper, newL2Slice);
        sessionWrapper.commit();
        return createResponse(newL2Slice);
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

  @Override
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L2SliceCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L2SliceCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createVrfIdSet(List<L2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<Integer> vrfIdSet = new HashSet<>();
      for (L2Slice l2Slice : l2SliceList) {
        vrfIdSet.add(l2Slice.getVrfId());
      }
      return vrfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createSliceIdSet(List<L2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<String> sliceIdSet = new HashSet<>();
      for (L2Slice l2Slice : l2SliceList) {
        sliceIdSet.add(l2Slice.getSliceId());
      }
      return sliceIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSliceDuplicate(SessionWrapper sessionWrapper, L2SliceDao l2SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2SliceDao" }, new Object[] { sessionWrapper, l2SliceDao });
      String targetSliceId = requestBody.getSliceId();
      L2Slice l2Slice = l2SliceDao.read(sessionWrapper, targetSliceId);
      if (l2Slice != null) {
        String logMsg = MessageFormat.format("l2slice duplicate.sliceId = {0}.", targetSliceId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Slice l2Slice) {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });

      L2SliceCreateResponseBody responseBody = new L2SliceCreateResponseBody();
      responseBody.setSliceId(l2Slice.getSliceId());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private L2Slice makeNewL2Slice(SessionWrapper sessionWrapper, L2SliceDao l2SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2SliceDao" }, new Object[] { sessionWrapper, l2SliceDao });

      List<L2Slice> l2SliceList = l2SliceDao.readList(sessionWrapper);
      checkSliceMaxNum(l2SliceList.size(), SliceType.L2_SLICE);
      Set<Integer> vrfIdSet = createVrfIdSet(l2SliceList);
      int vrfId = getNextVrfId(sessionWrapper, vrfIdSet, SliceType.L2_SLICE);
      L2Slice newL2Slice = new L2Slice();
      if (requestBody.getSliceId() == null) {
        Set<String> sliceIdSet = createSliceIdSet(l2SliceList);
        int sliceId = getNextSliceId(sessionWrapper, sliceIdSet, SliceType.L2_SLICE);
        newL2Slice.setSliceId(String.valueOf(sliceId));
      } else {
        checkSliceDuplicate(sessionWrapper, l2SliceDao);
        newL2Slice.setSliceId(requestBody.getSliceId());
      }
      newL2Slice.setVrfId(vrfId);
      newL2Slice.setStatusEnum(ActiveStatus.INACTIVE);
      newL2Slice.setReservationStatusEnum(ReserveStatus.NONE);
      return newL2Slice;
    } finally {
      logger.methodEnd();
    }
  }
}
