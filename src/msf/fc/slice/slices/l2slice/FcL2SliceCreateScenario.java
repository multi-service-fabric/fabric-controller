
package msf.fc.slice.slices.l2slice;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import msf.fc.common.data.FcL2Slice;
import msf.fc.db.dao.slices.FcL2SliceDao;
import msf.fc.slice.FcSliceManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.IrbType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;

/**
 * Implementation class for L2 the slice addition.
 *
 * @author NTT
 *
 */
public class FcL2SliceCreateScenario extends FcAbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2SliceCreateScenario.class);

  private L2SliceCreateRequestBody requestBody;

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
  public FcL2SliceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.performance("start wait for l2slice creation process.");

    synchronized (FcSliceManager.getInstance().getL2SliceCreateLockObject()) {
      logger.performance("end wait for l2slice creation process.");
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        logger.methodStart();

        checkRrConfiguration();

        checkRemarkMenuList(requestBody.getRemarkMenu());

        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();
          FcL2SliceDao l2SliceDao = new FcL2SliceDao();

          FcL2Slice newL2Slice = makeNewL2Slice(sessionWrapper, l2SliceDao);

          l2SliceDao.create(sessionWrapper, newL2Slice);

          sessionWrapper.commit();

          return createL2SliceCreateResponse(newL2Slice.getSliceId());
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

  private FcL2Slice makeNewL2Slice(SessionWrapper sessionWrapper, FcL2SliceDao l2SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2SliceDao" }, new Object[] { sessionWrapper, l2SliceDao });

      List<FcL2Slice> l2SliceList = l2SliceDao.readList(sessionWrapper);

      checkSliceMaxNum(l2SliceList.size(), SliceType.L2_SLICE);

      FcL2Slice newL2Slice = new FcL2Slice();

      IrbType irbType = IrbType.getEnumFromMessage(requestBody.getIrbType());
      if (irbType.equals(IrbType.ASYMMETRIC) || irbType.equals(IrbType.SYMMETRIC)) {

        newL2Slice.setIrbType(irbType.getCode());

        if (requestBody.getVrfId() == null) {

          Set<Integer> vrfIdSet = createVrfIdSet(l2SliceList);
          int vrfId = getNextVrfId(sessionWrapper, vrfIdSet, SliceType.L2_SLICE);
          newL2Slice.setVrfId(vrfId);

        } else {
          checkVrfDuplicate(sessionWrapper, l2SliceDao);
          newL2Slice.setVrfId(requestBody.getVrfId());
        }

        if (irbType.equals(IrbType.SYMMETRIC)) {

          int l3Vni = getL3Vni(newL2Slice.getVrfId());
          newL2Slice.setL3vni(l3Vni);
          List<FcL2Slice> irbL2SliceList = l2SliceDao.readListByIrbType(sessionWrapper, IrbType.SYMMETRIC.getCode());
          Set<Integer> l3VniVlanIdSet = createL3VniVlanIdSet(irbL2SliceList);
          int l3VniVlanId = getVlanIdForL3Vni(sessionWrapper, l3VniVlanIdSet);
          newL2Slice.setL3vniVlanId(l3VniVlanId);
        }
      } else {

        Set<Integer> vniSet = createVniSet(l2SliceList);
        int vni = getNextVni(sessionWrapper, vniSet);
        newL2Slice.setVni(vni);
      }

      if (requestBody.getSliceId() == null) {

        Set<String> sliceIdSet = createSliceIdSet(l2SliceList);

        int sliceId = getNextSliceId(sessionWrapper, sliceIdSet, SliceType.L2_SLICE);
        newL2Slice.setSliceId(String.valueOf(sliceId));
      } else {

        checkSliceDuplicate(sessionWrapper, l2SliceDao);
        newL2Slice.setSliceId(requestBody.getSliceId());
      }
      newL2Slice.setRemarkMenu(requestBody.getRemarkMenu());

      newL2Slice.setQInQEnable(requestBody.getQInQEnable() != null ? requestBody.getQInQEnable() : false);
      return newL2Slice;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createVrfIdSet(List<FcL2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<Integer> vrfIdSet = new TreeSet<>();
      for (FcL2Slice l2Slice : l2SliceList) {
        if (l2Slice.getVrfId() != null) {
          vrfIdSet.add(l2Slice.getVrfId());
        }
      }
      return vrfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<String> createSliceIdSet(List<FcL2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<String> sliceIdSet = new TreeSet<>();
      for (FcL2Slice l2Slice : l2SliceList) {
        sliceIdSet.add(l2Slice.getSliceId());
      }
      return sliceIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkVrfDuplicate(SessionWrapper sessionWrapper, FcL2SliceDao l2SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2SliceDao" }, new Object[] { sessionWrapper, l2SliceDao });
      int targetVrfId = requestBody.getVrfId();
      FcL2Slice l2Slice = l2SliceDao.readByVrfId(sessionWrapper, targetVrfId);
      if (l2Slice != null) {
        String logMsg = MessageFormat.format("l2slice(vrf) duplicate.vrfId = {0}.", targetVrfId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void checkSliceDuplicate(SessionWrapper sessionWrapper, FcL2SliceDao l2SliceDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "l2SliceDao" }, new Object[] { sessionWrapper, l2SliceDao });
      String targetSliceId = requestBody.getSliceId();
      FcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, targetSliceId);
      if (l2Slice != null) {
        String logMsg = MessageFormat.format("l2slice duplicate.sliceId = {0}.", targetSliceId);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createL3VniVlanIdSet(List<FcL2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<Integer> vlanIdSet = new TreeSet<>();
      for (FcL2Slice l2Slice : l2SliceList) {
        if (l2Slice.getL3vniVlanId() != null) {
          vlanIdSet.add(l2Slice.getL3vniVlanId());
        }
      }
      return vlanIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createVniSet(List<FcL2Slice> l2SliceList) {
    try {
      logger.methodStart(new String[] { "l2SliceList" }, new Object[] { l2SliceList });
      Set<Integer> vniSet = new TreeSet<>();
      for (FcL2Slice l2Slice : l2SliceList) {
        if (l2Slice.getVni() != null) {
          vniSet.add(l2Slice.getVni());
        }
      }
      return vniSet;
    } finally {
      logger.methodEnd();
    }
  }

}
