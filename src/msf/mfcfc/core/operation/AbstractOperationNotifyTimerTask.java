
package msf.mfcfc.core.operation;

import java.util.TimerTask;

/**
 * TimerTask class for the operation result notification.
 *
 * @author NTT
 *
 */
public abstract class AbstractOperationNotifyTimerTask extends TimerTask {

  private String clusterId;

  private String lowerOperationId;

  /**
   * Get the target Cluster ID.
   *
   * @return clusterId
   */
  public String getClusterId() {
    return clusterId;
  }

  /**
   * Set the target Cluster ID.
   *
   * @param clusterId
   *          Target Cluster ID
   */
  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  /**
   * Get the target lower system operation ID.
   *
   * @return lowerOperationId
   */
  public String getLowerOperationId() {
    return lowerOperationId;
  }

  /**
   * Set the target lower system operation ID.
   *
   * @param lowerOperationId
   *          Target lower system operation ID
   */
  public void setLowerOperationId(String lowerOperationId) {
    this.lowerOperationId = lowerOperationId;
  }

}
