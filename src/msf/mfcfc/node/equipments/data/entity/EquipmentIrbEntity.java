
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentIrbEntity {

  @SerializedName("asymmetric")
  private Boolean asymmetric;

  @SerializedName("symmetric")
  private Boolean symmetric;

  public Boolean getAsymmetric() {
    return asymmetric;
  }

  public void setAsymmetric(Boolean asymmetric) {
    this.asymmetric = asymmetric;
  }

  public Boolean getSymmetric() {
    return symmetric;
  }

  public void setSymmetric(Boolean symmetric) {
    this.symmetric = symmetric;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
