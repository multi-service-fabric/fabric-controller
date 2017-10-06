
package msf.fc.common.config.type.develop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "System", propOrder = {})
public class System {

  @XmlElement(required = true)
  protected AsyncOperation asyncOperation;
  @XmlSchemaType(name = "integer")
  protected int executingOperationCheckCycle;
  @XmlElement(required = true)
  protected Lock lock;

  public AsyncOperation getAsyncOperation() {
    return asyncOperation;
  }

  public void setAsyncOperation(AsyncOperation value) {
    this.asyncOperation = value;
  }

  public int getExecutingOperationCheckCycle() {
    return executingOperationCheckCycle;
  }

  public void setExecutingOperationCheckCycle(int value) {
    this.executingOperationCheckCycle = value;
  }

  public Lock getLock() {
    return lock;
  }

  public void setLock(Lock value) {
    this.lock = value;
  }

}
