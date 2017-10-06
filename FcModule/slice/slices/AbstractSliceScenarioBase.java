package msf.fc.slice.slices;

import java.text.MessageFormat;
import java.util.Set;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.SliceType;
import msf.fc.common.constant.SliceUpdateAction;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.data.SliceId;
import msf.fc.common.data.VrfId;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.SliceIdDao;
import msf.fc.db.dao.slices.VrfIdDao;
import msf.fc.slice.AbstractSliceCpScenarioBase;

public abstract class AbstractSliceScenarioBase<T extends RestRequestBase> extends AbstractSliceCpScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSliceScenarioBase.class);

  protected void setRestIfTypeByAction(SliceUpdateAction action) throws MsfException {
    try {
      logger.methodStart(new String[] { "action" }, new Object[] { action });
      switch (action) {
        case DEACTIVATE_CPS:
          setRestIfType(SynchronousType.ASYNC);
          break;
        case RESERVE_CANCEL:
          setRestIfType(SynchronousType.SYNC);
          break;
        default:
          String logMsg = MessageFormat.format("undefined cp update action. action ={0}", action.name());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkSliceMaxNum(int sliceNum, SliceType sliceType) throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceNum", "sliceType" }, new Object[] { sliceNum, sliceType });
      int sliceMaxNum;
      if (SliceType.L2_SLICE.equals(sliceType)) {
        sliceMaxNum = ConfigManager.getInstance().getL2SlicesMaxNum();
      } else {
        sliceMaxNum = ConfigManager.getInstance().getL3SlicesMaxNum();
      }
      if (sliceNum >= sliceMaxNum) {
        String logMsg = MessageFormat.format("exceeds the max of {0}. num = {1} (max = {2})", sliceType.name(),
            sliceNum, sliceMaxNum);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextVrfId(SessionWrapper sessionWrapper, Set<Integer> vrfIdSet, SliceType sliceType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vrfIdSet", "sliceType" },
          new Object[] { sessionWrapper, vrfIdSet, sliceType });
      VrfIdDao vrfIdDao = new VrfIdDao();
      VrfId vrfId = vrfIdDao.read(sessionWrapper, sliceType.getCode());
      int firstNextId = vrfId.getNextId();
      int targetNextId = firstNextId;
      logger.performance("start get available vrf id.");
      do {
        if (vrfIdSet.contains(targetNextId)) {
          targetNextId++;
          if (!checkVrfIdRange(targetNextId, sliceType)) {
            targetNextId = 1;
          }
        } else {
          updateVrfId(sessionWrapper, vrfIdDao, vrfId, targetNextId + 1, sliceType);
          logger.performance("end get available vrf id.");
          return targetNextId;
        }
      } while (targetNextId != firstNextId);
      logger.performance("end get available vrf id.");
      String logMsg = MessageFormat.format("threre is no available vrf id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextSliceId(SessionWrapper sessionWrapper, Set<String> sliceIdSet, SliceType sliceType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceIdSet", "sliceType" },
          new Object[] { sessionWrapper, sliceIdSet, sliceType });
      SliceIdDao sliceIdDao = new SliceIdDao();
      SliceId sliceId = sliceIdDao.read(sessionWrapper, sliceType.getCode());

      int firstNextId = sliceId.getNextId();
      int targetNextId = firstNextId;
      logger.performance("start get available slice id.");
      do {
        if (sliceIdSet.contains(String.valueOf(targetNextId))) {
          targetNextId++;
          if (!checkSliceIdRange(targetNextId)) {
            targetNextId = 1;
          }
        } else {
          updateSliceId(sessionWrapper, sliceIdDao, sliceId, targetNextId + 1);
          logger.performance("end get available slice id.");
          return targetNextId;
        }
      } while (targetNextId != firstNextId);
      logger.performance("end check available slice id.");
      String logMsg = MessageFormat.format("threre is no available slice id. firstCheckId = {0}", firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkVrfIdRange(int checkTargetId, SliceType sliceType) {
    try {
      logger.methodStart(new String[] { "checkTargetId", "sliceType" }, new Object[] { checkTargetId, sliceType });
      int sliceMaxNum;
      int sliceMagnificationNum;
      if (SliceType.L2_SLICE.equals(sliceType)) {
        sliceMaxNum = ConfigManager.getInstance().getL2SlicesMaxNum();
        sliceMagnificationNum = ConfigManager.getInstance().getL2SlicesMagnificationNum();
      } else {
        sliceMaxNum = ConfigManager.getInstance().getL3SlicesMaxNum();
        sliceMagnificationNum = ConfigManager.getInstance().getL3SlicesMagnificationNum();
      }
      if (checkTargetId > sliceMaxNum * sliceMagnificationNum) {
        logger.debug("return false");
        return false;
      } else {
        logger.debug("return true");
        return true;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateVrfId(SessionWrapper sessionWrapper, VrfIdDao vrfIdDao, VrfId vrfId, int nextId,
      SliceType sliceType) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "vrfIdDao", "vrfId", "nextId", "sliceType" },
          new Object[] { sessionWrapper, vrfIdDao, vrfId, nextId, sliceType });

      if (!checkVrfIdRange(nextId, sliceType)) {
        nextId = 1;
      }
      vrfId.setNextId(nextId);
      vrfIdDao.update(sessionWrapper, vrfId);
    } finally {
      logger.methodEnd();
    }
  }

  private void updateSliceId(SessionWrapper sessionWrapper, SliceIdDao sliceIdDao, SliceId sliceId, int nextId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceIdDao", "sliceId", "nextId" },
          new Object[] { sessionWrapper, sliceIdDao, sliceId, nextId });

      if (!checkSliceIdRange(nextId)) {
        nextId = 1;
      }
      sliceId.setNextId(nextId);
      sliceIdDao.update(sessionWrapper, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkSliceIdRange(int checkTargetId) {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      if (checkTargetId >= 0 && checkTargetId < Integer.MAX_VALUE) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }
}
