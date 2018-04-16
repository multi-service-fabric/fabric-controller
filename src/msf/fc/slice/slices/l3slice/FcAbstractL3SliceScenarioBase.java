
package msf.fc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3Slice;
import msf.fc.slice.slices.FcAbstractSliceScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;
import msf.mfcfc.slice.slices.l3slice.data.entity.L3SliceEntity;

/**
 * Abstract class to implement the common process of L3 slice-related processing
 * in slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractL3SliceScenarioBase<T extends RestRequestBase> extends FcAbstractSliceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractL3SliceScenarioBase.class);

  protected L3SliceRequest request;

  protected void checkL3SlicePresence(FcL3Slice l3Slice, String sliceId, boolean isTarget) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice", "sliceId", "isTarget" },
          new Object[] { l3Slice, sliceId, isTarget });
      if (isTarget) {
        ParameterCheckUtil.checkNotNullTargetResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      } else {
        ParameterCheckUtil.checkNotNullRelatedResource(l3Slice, new String[] { "sliceId" }, new Object[] { sliceId });
      }

    } finally {
      logger.methodEnd();
    }
  }

  protected L3SliceEntity createL3SliceEntity(FcL3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      List<String> cpIdList = new ArrayList<>();

      for (FcL3Cp l3Cp : l3Slice.getL3Cps()) {
        cpIdList.add(l3Cp.getId().getCpId());
      }
      L3SliceEntity l3SliceEntity = new L3SliceEntity();
      l3SliceEntity.setL3CpIdList(cpIdList);
      l3SliceEntity.setSliceId(l3Slice.getSliceId());
      l3SliceEntity.setPlane(l3Slice.getPlane());
      l3SliceEntity.setRemarkMenu(l3Slice.getRemarkMenu());
      return l3SliceEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
