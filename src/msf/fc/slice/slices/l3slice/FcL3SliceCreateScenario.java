
package msf.fc.slice.slices.l3slice;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcL3Slice;
import msf.fc.db.dao.slices.FcL3SliceDao;
import msf.fc.slice.FcSliceManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Implementation class for the L3 slice addition.
 *
 * @author NTT
 *
 */
public class FcL3SliceCreateScenario extends FcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceCreateScenario.class);

  private L3SliceCreateRequestBody requestBody;

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
  public FcL3SliceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.performance("start wait for l3slice creation process.");

    synchronized (FcSliceManager.getInstance().getL3SliceCreateLockObject()) {
      logger.performance("end wait for l3slice creation process.");
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        logger.methodStart();

        checkRrConfiguration();

        checkRemarkMenuList(requestBody.getRemarkMenu());

        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();
          FcL3SliceDao l3SliceDao = new FcL3SliceDao();

          FcL3Slice newL3Slice = makeNewL3Slice(sessionWrapper, l3SliceDao);

          l3SliceDao.create(sessionWrapper, newL3Slice);

          sessionWrapper.commit();

          return createResponse(newL3Slice);
        } catch (MsfException exp) {
          logger.error(exp.getMessage(), exp);
          sessionWrapper.rollback();
          throw exp;
        } finally {
          sessionWrapper.closeSession();
        }
      } finally {
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

  private FcL3Slice makeNewL3Slice(SessionWrapper sessionWrapper, FcL3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });

      List<FcL3Slice> l3SliceList = l3SliceDao.readList(sessionWrapper);

      checkSliceMaxNum(l3SliceList.size(), SliceType.L3_SLICE);

      FcL3Slice newL3Slice = new FcL3Slice();

      if (requestBody.getVrfId() == null) {

        Set<Integer> vrfIdSet = createVrfIdSet(l3SliceList);
        int vrfId = getNextVrfId(sessionWrapper, vrfIdSet, SliceType.L3_SLICE);
        newL3Slice.setVrfId(vrfId);

      } else {
        checkVrfDuplicate(sessionWrapper, l3SliceDao);
        newL3Slice.setVrfId(requestBody.getVrfId());
      }

      if (requestBody.getSliceId() == null) {

        Set<String> sliceIdSet = createSliceIdSet(l3SliceList);

        int sliceId = getNextSliceId(sessionWrapper, sliceIdSet, SliceType.L3_SLICE);
        newL3Slice.setSliceId(String.valueOf(sliceId));
      } else {

        checkSliceDuplicate(sessionWrapper, l3SliceDao);
        newL3Slice.setSliceId(requestBody.getSliceId());
      }
      newL3Slice.setPlane(requestBody.getPlane());
      newL3Slice.setRemarkMenu(requestBody.getRemarkMenu());
      return newL3Slice;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createVrfIdSet(List<FcL3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<Integer> vrfIdSet = new TreeSet<>();
      for (FcL3Slice l3Slice : l3SliceList) {
        vrfIdSet.add(l3Slice.getVrfId());
      }
      return vrfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createSliceIdSet(List<FcL3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<String> sliceIdSet = new TreeSet<>();
      for (FcL3Slice l3Slice : l3SliceList) {
        sliceIdSet.add(l3Slice.getSliceId());
      }
      return sliceIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkVrfDuplicate(SessionWrapper sessionWrapper, FcL3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });
      int targetVrfId = requestBody.getVrfId();
      FcL3Slice l3Slice = l3SliceDao.readByVrfId(sessionWrapper, targetVrfId);
      if (l3Slice != null) {
        String logMsg = MessageFormat.format("l3slice(vrf) duplicate.vrfId = {0}.", targetVrfId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void checkSliceDuplicate(SessionWrapper sessionWrapper, FcL3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });
      String targetSliceId = requestBody.getSliceId();
      FcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, targetSliceId);
      if (l3Slice != null) {
        String logMsg = MessageFormat.format("l3slice duplicate.sliceId = {0}.", targetSliceId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcL3Slice l3Slice) {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });

      L3SliceCreateResponseBody responseBody = new L3SliceCreateResponseBody();
      responseBody.setSliceId(l3Slice.getSliceId());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
