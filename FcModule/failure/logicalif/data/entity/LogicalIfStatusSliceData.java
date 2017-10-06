package msf.fc.failure.logicalif.data.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LogicalIfStatusSliceData {

  @SerializedName("l2vpn")
  private List<LogicalIfStatusVpnBaseData> l2Vpn;
  @SerializedName("l3vpn")
  private List<LogicalIfStatusVpnBaseData> l3Vpn;

  public LogicalIfStatusSliceData() {

  }

  public LogicalIfStatusSliceData(List<LogicalIfStatusVpnBaseData> l2Vpn, List<LogicalIfStatusVpnBaseData> l3Vpn) {
    this.l2Vpn = l2Vpn;
    this.l3Vpn = l3Vpn;
  }

  public List<LogicalIfStatusVpnBaseData> getL2Vpn() {
    return l2Vpn;
  }

  public void setL2Vpn(List<LogicalIfStatusVpnBaseData> l2Vpn) {
    this.l2Vpn = l2Vpn;
  }

  public List<LogicalIfStatusVpnBaseData> getL3Vpn() {
    return l3Vpn;
  }

  public void setL3Vpn(List<LogicalIfStatusVpnBaseData> l3Vpn) {
    this.l3Vpn = l3Vpn;
  }

  @Override
  public String toString() {
    return "LogicalIfStatusSliceData [l2Vpn=" + l2Vpn + ", l3Vpn=" + l3Vpn + "]";
  }

}
