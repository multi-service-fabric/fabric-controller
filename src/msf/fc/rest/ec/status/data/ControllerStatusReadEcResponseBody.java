
package msf.fc.rest.ec.status.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.status.data.entity.ControllerStatusEcStatusEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusEmStatusEcEntity;
import msf.fc.rest.ec.status.data.entity.ControllerStatusInformationsEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class ControllerStatusReadEcResponseBody extends AbstractInternalResponseBody {
  
  @SerializedName("ec_status")
  private ControllerStatusEcStatusEcEntity ecStatus;

  
  @SerializedName("em_status")
  private ControllerStatusEmStatusEcEntity emStatus;

  
  @SerializedName("informations")
  private List<ControllerStatusInformationsEcEntity> informationList;

  
  public ControllerStatusEcStatusEcEntity getEcStatus() {
    return ecStatus;
  }

  
  public void setEcStatus(ControllerStatusEcStatusEcEntity ecStatus) {
    this.ecStatus = ecStatus;
  }

  
  public ControllerStatusEmStatusEcEntity getEmStatus() {
    return emStatus;
  }

  
  public void setEmStatus(ControllerStatusEmStatusEcEntity emStatus) {
    this.emStatus = emStatus;
  }

  
  public List<ControllerStatusInformationsEcEntity> getInformationList() {
    return informationList;
  }

  
  public void setInformationList(List<ControllerStatusInformationsEcEntity> informationList) {
    this.informationList = informationList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
