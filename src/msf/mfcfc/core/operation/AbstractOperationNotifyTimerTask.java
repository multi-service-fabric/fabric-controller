package msf.mfcfc.core.operation;

import java.util.TimerTask;


public abstract class AbstractOperationNotifyTimerTask extends TimerTask {

  
  private String clusterId;
  
  private String lowerOperationId;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getLowerOperationId() {
    return lowerOperationId;
  }

  public void setLowerOperationId(String lowerOperationId) {
    this.lowerOperationId = lowerOperationId;
  }

}
