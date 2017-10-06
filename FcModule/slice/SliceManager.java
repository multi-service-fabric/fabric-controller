package msf.fc.slice;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.constant.SliceType;
import msf.fc.common.data.SliceId;
import msf.fc.common.data.VrfId;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.SliceIdDao;
import msf.fc.db.dao.slices.VrfIdDao;

public final class SliceManager implements FunctionBlockBase {
  private static final SliceManager instance = new SliceManager();
  private static final MsfLogger logger = MsfLogger.getInstance(SliceManager.class);

  private SliceManager() {

  }

  public static SliceManager getInstance() {
    try {
      logger.methodStart();
      return instance;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean start() {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();
      VrfIdDao vrfIdDao = new VrfIdDao();
      VrfId l2VrfId = vrfIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2VrfId == null) {
        VrfId newL2VrfId = new VrfId();
        newL2VrfId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL2VrfId);
      }

      VrfId l3VrfId = vrfIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3VrfId == null) {
        VrfId newL3VrfId = new VrfId();
        newL3VrfId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL3VrfId);
      }

      SliceIdDao sliceIdDao = new SliceIdDao();
      SliceId l2SliceId = sliceIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2SliceId == null) {
        SliceId newL2SliceId = new SliceId();
        newL2SliceId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL2SliceId);
      }

      SliceId l3SliceId = sliceIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3SliceId == null) {
        SliceId newL3SliceId = new SliceId();
        newL3SliceId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL3SliceId);
      }

      sessionWrapper.commit();
      return true;
    } catch (Exception ex) {
      logger.error("Failed to start Slice Manager.", ex);
      try {
        sessionWrapper.rollback();
      } catch (MsfException me) {
      }
      return false;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();
      return true;
    } finally {
      logger.methodEnd();
    }
  }

}
