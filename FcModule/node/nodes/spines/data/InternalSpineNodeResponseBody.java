package msf.fc.node.nodes.spines.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractResponseBody;

public class InternalSpineNodeResponseBody extends AbstractResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
