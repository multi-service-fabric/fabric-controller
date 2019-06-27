
package msf.fc.services.ctrlstsnotify.scenario;

import msf.mfcfc.core.scenario.AbstractProcessThread;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.CtrlStsNotifyCommonData;

/**
 * Abstract class to implement the common process of the monitoring in the
 * controller status notification function.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractCtrlStsNotifyMonitorThread extends AbstractProcessThread {

  protected CtrlStsNotifyCommonData ctrlStsNotifyCommonData = CtrlStsNotifyCommonData.getInstance();
}
