package msf.fc.rest.ec.node.interfaces.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;

public class InterfaceReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("ifs")
  private InterfacesEcEntity interfacesEcEntity;

  public InterfacesEcEntity getInterfacesEcEntity() {
    return interfacesEcEntity;
  }

  public void setInterfacesEcEntity(InterfacesEcEntity interfacesEcEntity) {
    this.interfacesEcEntity = interfacesEcEntity;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
