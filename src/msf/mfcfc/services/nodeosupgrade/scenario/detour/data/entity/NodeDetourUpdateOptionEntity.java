
package msf.mfcfc.services.nodeosupgrade.scenario.detour.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeDetourUpdateOptionEntity {

  @SerializedName("detoured")
  private Boolean detoured;

  public Boolean getDetoured() {
    return detoured;
  }

  public void setDetoured(Boolean detoured) {
    this.detoured = detoured;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
