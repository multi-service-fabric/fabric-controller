
package msf.mfc.slice.slices.l3slice;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfc.slice.MfcSliceManager;
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
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Implementation class for L3 slice addition.
 *
 * @author NTT
 *
 */
public class MfcL3SliceCreateScenario extends MfcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3SliceCreateScenario.class);

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
  public MfcL3SliceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.performance("start wait to l3slice creation process.");

    synchronized (MfcSliceManager.getInstance().getL3SliceCreateLockObject()) {
      logger.performance("end wait to l3slice creation process.");
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        logger.methodStart();

        checkRrConfiguration();

        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();
          MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();

          MfcL3Slice newL3Slice = makeNewL3Slice(sessionWrapper, l3SliceDao);

          l3SliceDao.create(sessionWrapper, newL3Slice);

          return sendSliceCreateDeleteRequest(sessionWrapper, SliceType.L3_SLICE, true, JsonUtil.toJson(requestBody),
              newL3Slice.getSliceId());

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

  private MfcL3Slice makeNewL3Slice(SessionWrapper sessionWrapper, MfcL3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });

      List<MfcL3Slice> l3SliceList = l3SliceDao.readList(sessionWrapper);

      checkSliceMaxNum(l3SliceList.size(), SliceType.L3_SLICE);

      MfcL3Slice newL3Slice = new MfcL3Slice();

      Set<Integer> vrfIdSet = createVrfIdSet(l3SliceList);
      int vrfId = getNextVrfId(sessionWrapper, vrfIdSet, SliceType.L3_SLICE);

      requestBody.setVrfId(vrfId);
      newL3Slice.setVrfId(vrfId);

      if (requestBody.getSliceId() == null) {

        Set<String> sliceIdSet = createSliceIdSet(l3SliceList);

        int sliceId = getNextSliceId(sessionWrapper, sliceIdSet, SliceType.L3_SLICE);

        requestBody.setSliceId(String.valueOf(sliceId));
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

  private Set<Integer> createVrfIdSet(List<MfcL3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<Integer> vrfIdSet = new HashSet<>();
      for (MfcL3Slice l3Slice : l3SliceList) {
        vrfIdSet.add(l3Slice.getVrfId());
      }
      return vrfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createSliceIdSet(List<MfcL3Slice> l3SliceList) {
    try {
      logger.methodStart(new String[] { "l3SliceList" }, new Object[] { l3SliceList });
      Set<String> sliceIdSet = new HashSet<>();
      for (MfcL3Slice l3Slice : l3SliceList) {
        sliceIdSet.add(l3Slice.getSliceId());
      }
      return sliceIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSliceDuplicate(SessionWrapper sessionWrapper, MfcL3SliceDao l3SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l3SliceDao" }, new Object[] { sessionWrapper, l3SliceDao });
      String targetSliceId = requestBody.getSliceId();
      MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, targetSliceId);
      if (l3Slice != null) {
        String logMsg = MessageFormat.format("l3slice duplicate.sliceId = {0}.", targetSliceId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

}
