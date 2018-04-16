
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class DiNode implements Comparable<DiNode> {

  private int nodeNo;

  public DiNode(int nodeNo) {
    this.nodeNo = nodeNo;
  }

  @Override
  public int compareTo(DiNode target) {
    return nodeNo - target.getNodeNo();
  }

  public int getNodeNo() {
    return nodeNo;
  }

  public void setNodeNo(int nodeNo) {
    this.nodeNo = nodeNo;
  }

  public abstract String getStrForNodeSpecify();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
