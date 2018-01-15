package msf.fc.rest.ec.node.nodes.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class NodeReadEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("node")
  private NodeEcEntity node;

  
  public NodeEcEntity getNode() {
    return node;
  }

  
  public void setNode(NodeEcEntity node) {
    this.node = node;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
