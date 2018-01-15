package msf.fc.rest.ec.node.nodes.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class NodeReadListEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("nodes")
  private List<NodeEcEntity> nodeList;

  
  public List<NodeEcEntity> getNodeList() {
    return nodeList;
  }

  
  public void setNodeList(List<NodeEcEntity> nodeList) {
    this.nodeList = nodeList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
