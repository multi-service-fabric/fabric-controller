
package msf.mfc.slice.cps.l2cp;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.slice.cps.MfcAbstractCpScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Abstract class to implement the common process of L2CP-related processing in
 * slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractL2CpScenarioBase<T extends RestRequestBase> extends MfcAbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL2CpScenarioBase.class);

  protected L2CpRequest request;

  protected void checkL2CpPresence(MfcL2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l2Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }

}
