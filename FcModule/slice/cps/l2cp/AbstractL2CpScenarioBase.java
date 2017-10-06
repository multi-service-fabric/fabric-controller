package msf.fc.slice.cps.l2cp;

import msf.fc.common.data.L2Cp;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.slice.cps.AbstractCpScenarioBase;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;
import msf.fc.slice.cps.l2cp.data.entity.L2CpEntity;

public abstract class AbstractL2CpScenarioBase<T extends RestRequestBase> extends AbstractCpScenarioBase<T> {
  private static final MsfLogger logger = MsfLogger.getInstance(AbstractL2CpScenarioBase.class);
  protected L2CpRequest request;

  protected L2CpEntity createL2CpEntity(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      L2CpEntity l2CpEntity = new L2CpEntity();
      l2CpEntity.setClusterId(l2Cp.getEdgePoint().getId().getSwClusterId());
      l2CpEntity.setCpId(l2Cp.getId().getCpId());
      l2CpEntity.setEdgePointId(String.valueOf(l2Cp.getEdgePoint().getId().getEdgePointId()));
      l2CpEntity.setOperationStatusEnum(l2Cp.getOperationStatusEnum());
      l2CpEntity.setPortModeEnum(l2Cp.getPortModeEnum());
      l2CpEntity.setReservationStatusEnum(l2Cp.getReservationStatusEnum());
      l2CpEntity.setSliceId(l2Cp.getId().getSliceId());
      l2CpEntity.setStatusEnum(l2Cp.getStatusEnum());
      return l2CpEntity;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkL2CpPresence(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l2Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

}