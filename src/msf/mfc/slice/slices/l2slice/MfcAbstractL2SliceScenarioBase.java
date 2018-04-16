
package msf.mfc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.slice.slices.MfcAbstractSliceScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;
import msf.mfcfc.slice.slices.l2slice.data.entity.L2SliceEntity;

/**
 * Abstract class to implement the common process of L2 slice-related processing
 * in slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractL2SliceScenarioBase<T extends RestRequestBase>
    extends MfcAbstractSliceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL2SliceScenarioBase.class);

  protected L2SliceRequest request;

  protected void checkL2SlicePresence(MfcL2Slice l2Slice, String sliceId, boolean isTarget) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "sliceId", "isTarget" },
          new Object[] { l2Slice, sliceId, isTarget });
      if (isTarget) {
        ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      } else {
        ParameterCheckUtil.checkNotNullRelatedResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected L2SliceEntity createL2SliceEntity(MfcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      List<String> cpIdList = new ArrayList<>();

      for (MfcL2Cp l2Cp : l2Slice.getL2Cps()) {
        cpIdList.add(l2Cp.getId().getCpId());
      }
      L2SliceEntity l2SliceEntity = new L2SliceEntity();
      l2SliceEntity.setL2CpIdList(cpIdList);
      l2SliceEntity.setSliceId(l2Slice.getSliceId());
      l2SliceEntity.setRemarkMenu(l2Slice.getRemarkMenu());
      return l2SliceEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
