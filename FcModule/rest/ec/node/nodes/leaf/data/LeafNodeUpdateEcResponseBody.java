package msf.fc.rest.ec.node.nodes.leaf.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractInternalResponseBody;

public class LeafNodeUpdateEcResponseBody extends AbstractInternalResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
