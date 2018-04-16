
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ControllerStatusInterfaceEcEntity {

  @SerializedName("ifname")
  private String ifname;

  @SerializedName("rxpck")
  private Float rxpck;

  @SerializedName("txpck")
  private Float txpck;

  @SerializedName("rxkb")
  private Float rxkb;

  @SerializedName("txkb")
  private Float txkb;

  public String getIfname() {
    return ifname;
  }

  public void setIfname(String ifname) {
    this.ifname = ifname;
  }

  public Float getRxpck() {
    return rxpck;
  }

  public void setRxpck(Float rxpck) {
    this.rxpck = rxpck;
  }

  public Float getTxpck() {
    return txpck;
  }

  public void setTxpck(Float txpck) {
    this.txpck = txpck;
  }

  public Float getRxkb() {
    return rxkb;
  }

  public void setRxkb(Float rxkb) {
    this.rxkb = rxkb;
  }

  public Float getTxkb() {
    return txkb;
  }

  public void setTxkb(Float txkb) {
    this.txkb = txkb;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
