
package msf.fc.slice;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEsiId;
import msf.fc.common.data.FcEsiIdPK;
import msf.fc.common.data.FcSliceId;
import msf.fc.common.data.FcVrfId;
import msf.fc.db.dao.slices.FcEsiDao;
import msf.fc.db.dao.slices.FcSliceIdDao;
import msf.fc.db.dao.slices.FcVrfIdDao;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.SliceManager;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of slice management function block.
 *
 * @author NTT
 *
 */
public final class FcSliceManager extends SliceManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSliceManager.class);

  private FcSliceManager() {

  }

  /**
   * Get the instance of FcSliceManager. This method does not guarantee the
   * uniqueness of the returned instance if it is called by multi-threads
   * simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcSliceManager instance
   */
  public static FcSliceManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcSliceManager();
      }
      return (FcSliceManager) instance;
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

      FcVrfIdDao vrfIdDao = new FcVrfIdDao();
      FcVrfId l2VrfId = vrfIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2VrfId == null) {
        FcVrfId newL2VrfId = new FcVrfId();
        newL2VrfId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL2VrfId);
      }

      FcVrfId l3VrfId = vrfIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3VrfId == null) {
        FcVrfId newL3VrfId = new FcVrfId();
        newL3VrfId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL3VrfId);
      }

      FcSliceIdDao sliceIdDao = new FcSliceIdDao();
      FcSliceId l2SliceId = sliceIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2SliceId == null) {
        FcSliceId newL2SliceId = new FcSliceId();
        newL2SliceId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL2SliceId);
      }

      FcSliceId l3SliceId = sliceIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3SliceId == null) {
        FcSliceId newL3SliceId = new FcSliceId();
        newL3SliceId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL3SliceId);
      }

      int myClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      FcEsiDao esiDao = new FcEsiDao();
      FcEsiIdPK esiPk = new FcEsiIdPK();
      esiPk.setSwClusterId1(myClusterId);
      esiPk.setSwClusterId2(myClusterId);
      FcEsiId esi = esiDao.read(sessionWrapper, esiPk);
      if (esi == null) {
        FcEsiId newEsi = new FcEsiId();
        FcEsiIdPK newEsiPk = new FcEsiIdPK();
        newEsiPk.setSwClusterId1(myClusterId);
        newEsiPk.setSwClusterId2(myClusterId);
        newEsi.setId(newEsiPk);
        newEsi.setNextId(1);
        esiDao.create(sessionWrapper, newEsi);
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
}
