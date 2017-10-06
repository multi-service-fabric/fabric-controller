package msf.fc.slice.slices.l3slice;

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
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Rr;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.RrDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.slices.l3slice.data.L3SliceCreateRequestBody;
import msf.fc.slice.slices.l3slice.data.L3SliceCreateResponseBody;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;

public class L3SliceCreateScenario extends AbstractL3SliceScenarioBase<L3SliceRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceCreateScenario.class);
  private static final Object lockObject = new Object();
  private L3SliceCreateRequestBody requestBody;

  public L3SliceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.performance("start wait to l3slice creation process.");
    synchronized (lockObject) {
      logger.performance("start wait to l3slice creation process.");
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        logger.methodStart();
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();
        checkRrExistence(sessionWrapper);
        L3SliceDao l3SliceDao = new L3SliceDao();
        L3Slice newL3Slice = makeNewL3Slice(sessionWrapper, l3SliceDao);
        l3SliceDao.create(sessionWrapper, newL3Slice);
        sessionWrapper.commit();
        return createResponse(newL3Slice);
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
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L3SliceCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L3SliceCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createVrfIdSet(List<L3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<Integer> vrfIdSet = new HashSet<>();
      for (L3Slice l3Slice : l3SliceList) {
        vrfIdSet.add(l3Slice.getVrfId());
      }
      return vrfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createSliceIdSet(List<L3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<String> sliceIdSet = new HashSet<>();
      for (L3Slice l3Slice : l3SliceList) {
        sliceIdSet.add(l3Slice.getSliceId());
      }
      return sliceIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSliceDuplicate(SessionWrapper sessionWrapper, L3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });
      String targetSliceId = requestBody.getSliceId();
      L3Slice l3Slice = l3SliceDao.read(sessionWrapper, targetSliceId);
      if (l3Slice != null) {
        String logMsg = MessageFormat.format("l2slice duplicate.sliceId = {0}.", targetSliceId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L3Slice l3Slice) {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });

      L3SliceCreateResponseBody responseBody = new L3SliceCreateResponseBody();
      responseBody.setSliceId(l3Slice.getSliceId());
      RestResponseBase responseBase = new RestResponseBase(HttpStatus.CREATED_201, responseBody);
      return responseBase;

    } finally {
      logger.methodEnd();
    }
  }

  private L3Slice makeNewL3Slice(SessionWrapper sessionWrapper, L3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });

      List<L3Slice> l3SliceList = l3SliceDao.readList(sessionWrapper);
      checkSliceMaxNum(l3SliceList.size(), SliceType.L3_SLICE);
      Set<Integer> vrfIdSet = createVrfIdSet(l3SliceList);
      int vrfId = getNextVrfId(sessionWrapper, vrfIdSet, SliceType.L3_SLICE);
      L3Slice newL3Slice = new L3Slice();
      if (requestBody.getSliceId() == null) {
        Set<String> sliceIdSet = createSliceIdSet(l3SliceList);
        int sliceId = getNextSliceId(sessionWrapper, sliceIdSet, SliceType.L3_SLICE);
        newL3Slice.setSliceId(String.valueOf(sliceId));
      } else {
        checkSliceDuplicate(sessionWrapper, l3SliceDao);
        newL3Slice.setSliceId(requestBody.getSliceId());
      }
      newL3Slice.setVrfId(vrfId);
      newL3Slice.setPlaneEnum(requestBody.getPlaneEnum());
      newL3Slice.setStatusEnum(ActiveStatus.INACTIVE);
      newL3Slice.setReservationStatusEnum(ReserveStatus.NONE);
      return newL3Slice;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkRrExistence(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });
      SwClusterDao swClusterDao = new SwClusterDao();
      RrDao rrDao = new RrDao();
      List<SwCluster> swClusterList = swClusterDao.readList(sessionWrapper);
      for (SwCluster swCluster : swClusterList) {
        List<Rr> rrList = rrDao.readList(sessionWrapper, swCluster.getSwClusterId());
        if (rrList.size() == 0) {
          String logMsg = logger.error("RR is not registered. swClusterId = {0}", swCluster.getSwClusterId());
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
        }
      }

    } finally {
      logger.methodEnd();
    }
  }
}
