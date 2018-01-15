
package msf.fc.common.config.type.system;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rest", propOrder = {})
public class Rest {

  @XmlElement(required = true)
  protected Server server;
  @XmlElement(required = true)
  protected Client client;
  @XmlElement(required = true)
  protected Json json;

  public Server getServer() {
    return server;
  }

  public void setServer(Server value) {
    this.server = value;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client value) {
    this.client = value;
  }

  public Json getJson() {
    return json;
  }

  public void setJson(Json value) {
    this.json = value;
  }

}
