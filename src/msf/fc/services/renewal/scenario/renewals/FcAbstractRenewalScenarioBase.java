
package msf.fc.services.renewal.scenario.renewals;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.core.status.FcSystemStatusManager;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.services.renewal.scenario.renewals.AbstractRenewalScenarioBase;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalRequest;
import msf.mfcfc.services.renewal.scenario.renewals.data.entity.RenewalStatusEntity;

/**
 * Abstract class to implement the common process of FC in the controller file
 * renewal function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherited the RestRequestBase class
 */
public abstract class FcAbstractRenewalScenarioBase<T extends RestRequestBase> extends AbstractRenewalScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractRenewalScenarioBase.class);

  protected RenewalRequest request;

  protected List<RenewalStatusEntity> makeRenewalStatusEntityList() {
    try {
      logger.methodStart();

      RenewalStatusEntity renewalStatusEntity = new RenewalStatusEntity();

      renewalStatusEntity.setClusterId(
          String.valueOf(FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getSwClusterId()));

      renewalStatusEntity
          .setRenewalStatusEnum(FcSystemStatusManager.getInstance().getSystemStatus().getRenewalStatusEnum());

      List<RenewalStatusEntity> controllerRenewalStatusList = new ArrayList<>();
      controllerRenewalStatusList.add(renewalStatusEntity);

      return controllerRenewalStatusList;
    } finally {
      logger.methodEnd();
    }
  }
}
