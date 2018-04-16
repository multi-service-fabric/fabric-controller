
package msf.mfcfc.core.status.scenario.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerBlockadeStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerServiceStatusEntity;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusInformationEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class SystemStatusReadResponseBody extends AbstractResponseBody {

  @SerializedName("service_status")
  private String serviceStatus;

  @SerializedName("controller_service_statuses")
  private List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList;

  @SerializedName("blockade_status")
  private String blockadeStatus;

  @SerializedName("controller_blockade_statuses")
  private List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList;

  @SerializedName("informations")
  private List<SystemStatusInformationEntity> informationList;

  public String getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(String serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public List<SystemStatusControllerServiceStatusEntity> getControllerServiceStatusList() {
    return controllerServiceStatusList;
  }

  public void setControllerServiceStatusList(
      List<SystemStatusControllerServiceStatusEntity> controllerServiceStatusList) {
    this.controllerServiceStatusList = controllerServiceStatusList;
  }

  public String getBlockadeStatus() {
    return blockadeStatus;
  }

  public void setBlockadeStatus(String blockadeStatus) {
    this.blockadeStatus = blockadeStatus;
  }

  public List<SystemStatusControllerBlockadeStatusEntity> getControllerBlockadeStatusList() {
    return controllerBlockadeStatusList;
  }

  public void setControllerBlockadeStatusList(
      List<SystemStatusControllerBlockadeStatusEntity> controllerBlockadeStatusList) {
    this.controllerBlockadeStatusList = controllerBlockadeStatusList;
  }

  public List<SystemStatusInformationEntity> getInformationList() {
    return informationList;
  }

  public void setInformationList(List<SystemStatusInformationEntity> informationList) {
    this.informationList = informationList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
