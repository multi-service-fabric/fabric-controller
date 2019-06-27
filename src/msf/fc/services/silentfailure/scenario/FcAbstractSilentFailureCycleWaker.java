
package msf.fc.services.silentfailure.scenario;

import msf.mfcfc.core.scenario.AbstractCycleWaker;

/**
 * Abstract class to implement the common process of the periodic execution task
 * for the monitoring.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractSilentFailureCycleWaker extends AbstractCycleWaker {

  protected FcAbstractSilentFailureCycleWaker(int interval) {
    super(interval);
  }

}
