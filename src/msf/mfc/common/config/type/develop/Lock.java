
package msf.mfc.common.config.type.develop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Lock", propOrder = {})
public class Lock {

  @XmlSchemaType(name = "integer")
  protected int lockRetryNum;
  @XmlSchemaType(name = "integer")
  protected int lockTimeout;

  public int getLockRetryNum() {
    return lockRetryNum;
  }

  public void setLockRetryNum(int value) {
    this.lockRetryNum = value;
  }

  public int getLockTimeout() {
    return lockTimeout;
  }

  public void setLockTimeout(int value) {
    this.lockTimeout = value;
  }

}
