
package msf.mfcfc.services.priorityroutes.scenario.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.entity.PrioritySystemGroupReadResponseEntity;

public class NodeGroupPriorityReadResponseBody extends AbstractResponseBody {

  @SerializedName("nodes")
  private PrioritySystemGroupReadResponseEntity nodes;

  public PrioritySystemGroupReadResponseEntity getNodes() {
    return nodes;
  }

  public void setNodes(PrioritySystemGroupReadResponseEntity nodes) {
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
