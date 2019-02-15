
package msf.mfcfc.slice.slices;

import java.text.MessageFormat;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.data.SliceId;
import msf.mfcfc.common.data.VrfId;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.slices.SliceIdDao;
import msf.mfcfc.db.dao.slices.VrfIdDao;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceCreateResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateResponseBody;

/**
 * Abstract class to implement the common process of the L2/L3 slice-related
 * processing in the slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractSliceScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractSliceScenarioBase.class);

  private static final int SYMMETRIC_IRB_L3VNI_BASE = 20000;

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
      VrfIdDao vrfIdDao = DbManager.getInstance().createVrfIdDao();

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
      SliceIdDao sliceIdDao = DbManager.getInstance().createSliceIdDao();

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

  protected void checkFormatOption(String format) throws MsfException {
    try {
      logger.methodStart(new String[] { "format" }, new Object[] { format });

      if (format == null) {
        return;
      }
      RestFormatOption formatEnum = RestFormatOption.getEnumFromMessage(format);
      if (formatEnum == null) {
        String logMsg = MessageFormat.format("param is undefined.param = {0}, value = {1}", "format", format);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createL2SliceCreateResponse(String sliceId) {
    try {
      logger.methodStart(new String[] { "sliceId" }, new Object[] { sliceId });

      L2SliceCreateResponseBody responseBody = new L2SliceCreateResponseBody();
      responseBody.setSliceId(sliceId);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createL3SliceCreateResponse(String sliceId) {
    try {
      logger.methodStart(new String[] { "sliceId" }, new Object[] { sliceId });

      L3SliceCreateResponseBody responseBody = new L3SliceCreateResponseBody();
      responseBody.setSliceId(sliceId);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.CREATED_201, responseBody);

      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createL3SliceDeleteResponse() {

    RestResponseBase restResponse = new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    return restResponse;
  }

  protected int getL3Vni(int vrfId) {
    try {
      logger.methodStart(new String[] { "vrfId" }, new Object[] { vrfId });
      return SYMMETRIC_IRB_L3VNI_BASE + vrfId;
    } finally {
      logger.methodEnd();
    }
  }

  protected int getVlanIdForL3Vni(SessionWrapper sessionWrapper, Set<Integer> vlanIdSet) throws MsfException {
    try {
      logger.methodStart(new String[] { "vlanIdSet" }, new Object[] { vlanIdSet });

      int startPos = ConfigManager.getInstance().getL3VniVlanIdStartPos();
      int endPos = ConfigManager.getInstance().getL3VniVlanIdEndPos();

      for (int i = startPos; i <= endPos; i++) {
        if (!vlanIdSet.contains(i)) {

          return i;
        }
      }

      String logMsg = MessageFormat.format("could not be assigned vlan id for l3vi. startPos = {0}, endPos = {1}",
          startPos, endPos);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  protected int getNextVni(SessionWrapper sessionWrapper, Set<Integer> vniSet) throws MsfException {
    try {
      logger.methodStart(new String[] { "vniSet" }, new Object[] { vniSet });
      int vniId = getNextVrfId(sessionWrapper, vniSet, SliceType.L2_SLICE);
      return vniId;
    } finally {
      logger.methodEnd();
    }
  }

}
