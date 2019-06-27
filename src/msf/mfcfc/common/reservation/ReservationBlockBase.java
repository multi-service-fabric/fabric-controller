
package msf.mfcfc.common.reservation;

import msf.mfcfc.common.FunctionBlockBase;

/**
 * Base interface class to implement reservation function blocks in FC. Be sure
 * to implement that implementation class returns instance by #getInstance ()
 * method (Singleton).
 *
 * @author NTT
 *
 */
public interface ReservationBlockBase extends FunctionBlockBase {

  public boolean startScheduler();

  public boolean stopScheduler();

}
