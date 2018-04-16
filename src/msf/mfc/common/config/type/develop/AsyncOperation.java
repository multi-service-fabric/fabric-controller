
package msf.mfc.common.config.type.develop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AsyncOperation", propOrder = {})
public class AsyncOperation {

  @XmlSchemaType(name = "integer")
  protected int dataRetentionPeriod;
  @XmlSchemaType(name = "integer")
  protected int maxAsyncRunnerThreadNum;
  @XmlSchemaType(name = "integer")
  protected int invokeAllTimout;
  @XmlSchemaType(name = "integer")
  protected int waitOperationResultTimeout;

  public int getDataRetentionPeriod() {
    return dataRetentionPeriod;
  }

  public void setDataRetentionPeriod(int value) {
    this.dataRetentionPeriod = value;
  }

  public int getMaxAsyncRunnerThreadNum() {
    return maxAsyncRunnerThreadNum;
  }

  public void setMaxAsyncRunnerThreadNum(int value) {
    this.maxAsyncRunnerThreadNum = value;
  }

  public int getInvokeAllTimout() {
    return invokeAllTimout;
  }

  public void setInvokeAllTimout(int value) {
    this.invokeAllTimout = value;
  }

  public int getWaitOperationResultTimeout() {
    return waitOperationResultTimeout;
  }

  public void setWaitOperationResultTimeout(int value) {
    this.waitOperationResultTimeout = value;
  }

}
