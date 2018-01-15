
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

  /**
   * Procedure to start function block at FC startup process.
   *
   * @return success/failure of initialization request
   */
  public boolean start();

  /**
   * Procedure for living confirmation of function block.
   *
   * @return success/failure of living confirmation request
   */
  public boolean checkStatus();

  /**
   * Procedure to terminate function block at FC shutdown process.
   *
   * @return success/failure of shutdown request
   */
  public boolean stop();

}
