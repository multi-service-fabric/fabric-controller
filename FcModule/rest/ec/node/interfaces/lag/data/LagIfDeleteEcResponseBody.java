package msf.fc.rest.ec.node.interfaces.lag.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractInternalResponseBody;

public class LagIfDeleteEcResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
