package msf.fc.slice.cps.l3cp.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractResponseBody;

public class L3CpUpdateAsyncResponseBody extends AbstractResponseBody {
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
