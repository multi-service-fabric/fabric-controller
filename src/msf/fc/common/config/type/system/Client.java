
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Client", propOrder = {})
public class Client {

  @XmlSchemaType(name = "integer")
  protected int waitConnectionTimeout;
  @XmlSchemaType(name = "integer")
  protected int requestTimeout;
  @XmlSchemaType(name = "integer")
  protected int responseBufferSize;

  public int getWaitConnectionTimeout() {
    return waitConnectionTimeout;
  }

  public void setWaitConnectionTimeout(int value) {
    this.waitConnectionTimeout = value;
  }

  public int getRequestTimeout() {
    return requestTimeout;
  }

  public void setRequestTimeout(int value) {
    this.requestTimeout = value;
  }

  public int getResponseBufferSize() {
    return responseBufferSize;
  }

  public void setResponseBufferSize(int value) {
    this.responseBufferSize = value;
  }
}
