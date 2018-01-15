package msf.fc.rest.ec.node.interfaces.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.data.entity.InterfacesEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class InterfaceReadListEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("ifs")
  private InterfacesEcEntity ifs;

  
  public InterfacesEcEntity getIfs() {
    return ifs;
  }

  
  public void setIfs(InterfacesEcEntity ifs) {
    this.ifs = ifs;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
