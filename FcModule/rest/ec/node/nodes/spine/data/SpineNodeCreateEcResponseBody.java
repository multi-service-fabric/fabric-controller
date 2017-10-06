package msf.fc.rest.ec.node.nodes.spine.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractInternalResponseBody;

public class SpineNodeCreateEcResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
