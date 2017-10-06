
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Server", propOrder = {})
public class Server {

  @XmlElement(required = true)
  protected String listeningAddress;
  @XmlSchemaType(name = "integer")
  protected int listeningPort;

  public String getListeningAddress() {
    return listeningAddress;
  }

  public void setListeningAddress(String value) {
    this.listeningAddress = value;
  }

  public int getListeningPort() {
    return listeningPort;
  }

  public void setListeningPort(int value) {
    this.listeningPort = value;
  }

}
