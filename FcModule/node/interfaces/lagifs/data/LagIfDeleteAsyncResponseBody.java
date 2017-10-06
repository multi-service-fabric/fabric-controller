package msf.fc.node.interfaces.lagifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractResponseBody;

public class LagIfDeleteAsyncResponseBody extends AbstractResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
