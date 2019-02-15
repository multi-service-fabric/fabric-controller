
package msf.fc.slice.slices;

import java.text.MessageFormat;
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
 * Abstract class to implement the common process of the L2/L3 slice-related
 * processing in the slice management function.
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

  protected void checkRemarkMenuList(String remarkMenu) throws MsfException {
    try {
      logger.methodStart(new String[] { "remarkMenu" }, new Object[] { remarkMenu });
      List<String> remarkMenuList = FcConfigManager.getInstance().getQosRemarkMenuList();
      if (remarkMenu == null) {
        return;
      }
      for (String remarkMenuConf : remarkMenuList) {
        if (remarkMenu.equals(remarkMenuConf)) {
          logger.debug("remark menu matches config value.");
          return;
        }
      }
      String logMsg = MessageFormat.format("remark menu did not match config value, value = {0}", remarkMenu);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);

    } finally {
      logger.methodEnd();
    }
  }
}
