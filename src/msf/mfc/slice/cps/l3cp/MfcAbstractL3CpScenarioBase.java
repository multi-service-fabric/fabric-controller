
package msf.mfc.slice.cps.l3cp;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.slice.cps.MfcAbstractCpScenarioBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Abstract class to implement the common process of L3CP-related processing in
 * slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractL3CpScenarioBase<T extends RestRequestBase> extends MfcAbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractL3CpScenarioBase.class);

  protected L3CpRequest request;

  protected void checkL3CpPresence(MfcL3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      ParameterCheckUtil.checkNotNullTargetResource(l3Cp, new String[] { "sliceId", "cpId" },
          new Object[] { request.getSliceId(), request.getCpId() });
    } finally {
      logger.methodEnd();
    }
  }
}
