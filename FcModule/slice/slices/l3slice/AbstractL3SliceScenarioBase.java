package msf.fc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.slice.slices.AbstractSliceScenarioBase;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;
import msf.fc.slice.slices.l3slice.data.entity.L3SliceEntity;

public abstract class AbstractL3SliceScenarioBase<T extends RestRequestBase> extends AbstractSliceScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractL3SliceScenarioBase.class);
  protected L3SliceRequest request;

  protected L3SliceEntity createL3SliceEntity(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      List<String> cpIdList = new ArrayList<>();
      for (L3Cp l3Cp : l3Slice.getL3Cps()) {
        cpIdList.add(l3Cp.getId().getCpId());
      }
      L3SliceEntity l3SliceEntity = new L3SliceEntity();
      l3SliceEntity.setL3CpIds(cpIdList);
      l3SliceEntity.setPlaneEnum(l3Slice.getPlaneEnum());
      l3SliceEntity.setReservationStatusEnum(l3Slice.getReservationStatusEnum());
      l3SliceEntity.setSliceId(l3Slice.getSliceId());
      l3SliceEntity.setStatusEnum(l3Slice.getStatusEnum());
      return l3SliceEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
