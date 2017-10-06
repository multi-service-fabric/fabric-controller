package msf.fc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractResponseBody;

public class L2SliceDeleteResponseBody extends AbstractResponseBody {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
