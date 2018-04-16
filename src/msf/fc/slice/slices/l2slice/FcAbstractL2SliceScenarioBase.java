
package msf.fc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2Slice;
import msf.fc.slice.slices.FcAbstractSliceScenarioBase;
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
public abstract class FcAbstractL2SliceScenarioBase<T extends RestRequestBase> extends FcAbstractSliceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL2SliceScenarioBase.class);

  protected L2SliceRequest request;

  protected void checkL2SlicePresence(FcL2Slice l2Slice, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice", "sliceId" }, new Object[] { l2Slice, sliceId });
      ParameterCheckUtil.checkNotNullTargetResource(l2Slice, new String[] { "sliceId" }, new Object[] { sliceId });
    } finally {
      logger.methodEnd();
    }
  }

  protected L2SliceEntity createL2SliceEntity(FcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      List<String> cpIdList = new ArrayList<>();

      for (FcL2Cp l2Cp : l2Slice.getL2Cps()) {
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
