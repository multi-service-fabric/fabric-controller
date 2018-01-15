
package msf.fc.slice.slices;

import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.LeafRr;
import msf.fc.common.config.type.data.Rr;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.slice.slices.AbstractSliceScenarioBase;

/**
 * Abstract class to implement common process of L2/L3 slice-related processing
 * in slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class FcAbstractSliceScenarioBase<T extends RestRequestBase> extends AbstractSliceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractSliceScenarioBase.class);

  protected void checkRrConfiguration() throws MsfException {
    try {
      logger.methodStart();

      int myClusterId = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getSwClusterId();

      List<LeafRr> leafRrList = FcConfigManager.getInstance().getDataConfSwClusterData().getRrs().getLeafRr();
      boolean leafPeerTargetOwn = false;
      for (LeafRr leafRr : leafRrList) {

        if (leafRr.getLeafRrSwClusterId() == myClusterId) {
          leafPeerTargetOwn = true;
        }
      }
      if (leafPeerTargetOwn) {

        List<Rr> rrList = FcConfigManager.getInstance().getDataConfSwClusterData().getRrs().getRr();
        if (rrList == null || rrList.size() == 0) {
          String errorMessage = "RR configuration is not found ,even though peer target is own.";
          logger.error(errorMessage);
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, errorMessage);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }
}
