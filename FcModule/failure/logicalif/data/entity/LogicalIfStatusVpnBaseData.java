package msf.fc.failure.logicalif.data.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LogicalIfStatusVpnBaseData {

  @SerializedName("slice_id")
  private String sliceId;
  @SerializedName("cps")
  private List<LogicalIfStatusCpBaseData> cps;

  public LogicalIfStatusVpnBaseData() {

  }

  public LogicalIfStatusVpnBaseData(String sliceId, List<LogicalIfStatusCpBaseData> cps) {
    this.sliceId = sliceId;
    this.cps = cps;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public List<LogicalIfStatusCpBaseData> getCps() {
    return cps;
  }

  public void setCps(List<LogicalIfStatusCpBaseData> cps) {
    this.cps = cps;
  }

  @Override
  public String toString() {
    return "LogicalIfStatusVpnBaseData [sliceId=" + sliceId + ", cps=" + cps + "]";
  }

}
