
package msf.mfcfc.common;

/**
 * Base interface class to implement function block in FC. Be sure to implement
 * that implementation class returns instance by #getInstance () method
 * (Singleton).
 *
 * @author NTT
 *
 */
public interface FunctionBlockBase {

  public boolean start();

  public boolean checkStatus();

  public boolean stop();

}
