
package msf.fc.services.ctrlstsnotify.scenario;

import msf.mfcfc.core.scenario.AbstractCycleWaker;

/**
 * Abstract class to implement the common process of the periodic execution task
 * for the monitoring.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractCtrlStsNotifyCycleWaker extends AbstractCycleWaker {

  protected FcAbstractCtrlStsNotifyCycleWaker(int interval) {
    super(interval);
  }

}
