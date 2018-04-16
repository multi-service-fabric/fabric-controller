
package msf.mfc.slice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwClusterData;
import msf.mfc.common.data.MfcEsiId;
import msf.mfc.common.data.MfcEsiIdPK;
import msf.mfc.common.data.MfcSliceId;
import msf.mfc.common.data.MfcVrfId;
import msf.mfc.db.dao.slices.MfcEsiDao;
import msf.mfc.db.dao.slices.MfcSliceIdDao;
import msf.mfc.db.dao.slices.MfcVrfIdDao;
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
public final class MfcSliceManager extends SliceManager {

  protected static final MsfLogger logger = MsfLogger.getInstance(MfcSliceManager.class);

  private MfcSliceManager() {

  }

  /**
   * Get the instance of MfcSliceManager.
   *
   * @return MfcSliceManager instance
   */
  public static MfcSliceManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcSliceManager();
      }
      return (MfcSliceManager) instance;
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

      MfcVrfIdDao vrfIdDao = new MfcVrfIdDao();
      MfcVrfId l2VrfId = vrfIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2VrfId == null) {
        MfcVrfId newL2VrfId = new MfcVrfId();
        newL2VrfId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL2VrfId);
      }

      MfcVrfId l3VrfId = vrfIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3VrfId == null) {
        MfcVrfId newL3VrfId = new MfcVrfId();
        newL3VrfId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3VrfId.setNextId(1);
        vrfIdDao.create(sessionWrapper, newL3VrfId);
      }

      MfcSliceIdDao sliceIdDao = new MfcSliceIdDao();
      MfcSliceId l2SliceId = sliceIdDao.read(sessionWrapper, SliceType.L2_SLICE.getCode());
      if (l2SliceId == null) {
        MfcSliceId newL2SliceId = new MfcSliceId();
        newL2SliceId.setLayerTypeEnum(SliceType.L2_SLICE);
        newL2SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL2SliceId);
      }

      MfcSliceId l3SliceId = sliceIdDao.read(sessionWrapper, SliceType.L3_SLICE.getCode());
      if (l3SliceId == null) {
        MfcSliceId newL3SliceId = new MfcSliceId();
        newL3SliceId.setLayerTypeEnum(SliceType.L3_SLICE);
        newL3SliceId.setNextId(1);
        sliceIdDao.create(sessionWrapper, newL3SliceId);
      }

      MfcEsiDao esiDao = new MfcEsiDao();

      Map<Integer, List<Integer>> clusterIdPairMap = getClusterIdPairMap();
      for (Integer lowerClusterId : clusterIdPairMap.keySet()) {
        for (Integer higherClusterId : clusterIdPairMap.get(lowerClusterId)) {
          MfcEsiIdPK esiPk = new MfcEsiIdPK();
          esiPk.setSwClusterId1(lowerClusterId);
          esiPk.setSwClusterId2(higherClusterId);
          MfcEsiId esi = esiDao.read(sessionWrapper, esiPk);
          if (esi == null) {
            MfcEsiId newEsi = new MfcEsiId();
            MfcEsiIdPK newEsiPk = new MfcEsiIdPK();
            newEsiPk.setSwClusterId1(lowerClusterId);
            newEsiPk.setSwClusterId2(higherClusterId);
            newEsi.setId(newEsiPk);
            newEsi.setNextId(1);
            esiDao.create(sessionWrapper, newEsi);
          }
        }
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

  private Map<Integer, List<Integer>> getClusterIdPairMap() {
    try {
      logger.methodStart();

      Map<Integer, List<Integer>> clusterIdMap = new HashMap<>();

      List<SwClusterData> swClusterDataList = MfcConfigManager.getInstance().getSystemConfSwClusterDataList();

      for (SwClusterData data1 : swClusterDataList) {

        List<Integer> oldClusterIdList = new ArrayList<>();
        for (SwClusterData data2 : swClusterDataList) {

          if (data1.getSwCluster().getSwClusterId() <= data2.getSwCluster().getSwClusterId()) {
            oldClusterIdList.add(data2.getSwCluster().getSwClusterId());
          }
        }
        clusterIdMap.put(data1.getSwCluster().getSwClusterId(), oldClusterIdList);
      }
      return clusterIdMap;
    } finally {
      logger.methodEnd();
    }
  }
}
