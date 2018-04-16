
package msf.mfcfc.core.scenario;

import java.util.TimerTask;

/**
 * Interface for the creation process of timer task.
 *
 * @author NTT
 *
 */
public interface TimerTaskMaker {

  public TimerTask makeTimerTask();
}
