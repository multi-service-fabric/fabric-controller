package msf.fc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.slice.slices.AbstractSliceScenarioBase;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;
import msf.fc.slice.slices.l2slice.data.entity.L2SliceEntity;

public abstract class AbstractL2SliceScenarioBase<T extends RestRequestBase> extends AbstractSliceScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractL2SliceScenarioBase.class);
  protected L2SliceRequest request;

  protected L2SliceEntity createL2SliceEntity(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      List<String> cpIdList = new ArrayList<>();
      for (L2Cp l2Cp : l2Slice.getL2Cps()) {
        cpIdList.add(l2Cp.getId().getCpId());
      }
      L2SliceEntity l2SliceEntity = new L2SliceEntity();
      l2SliceEntity.setL2CpIds(cpIdList);
      l2SliceEntity.setReservationStatusEnum(l2Slice.getReservationStatusEnum());
      l2SliceEntity.setSliceId(l2Slice.getSliceId());
      l2SliceEntity.setStatusEnum(l2Slice.getStatusEnum());
      return l2SliceEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
