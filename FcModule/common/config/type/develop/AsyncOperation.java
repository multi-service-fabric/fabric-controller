
package msf.fc.common.config.type.develop;

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

}
