
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class to store node information used by Dijkstra's algorithm.
 *
 * @author NTT
 *
 */
public abstract class DiNode implements Comparable<DiNode> {

  private int nodeNo;

  /**
   * Constructor.
   *
   * @param nodeNo
   *          Node number.
   */
  public DiNode(int nodeNo) {
    this.nodeNo = nodeNo;
  }

  @Override
  public int compareTo(DiNode target) {
    return nodeNo - target.getNodeNo();
  }

  /**
   * Get the node number.
   *
   * @return Node number.
   */
  public int getNodeNo() {
    return nodeNo;
  }

  /**
   * Set the node number.
   *
   * @param nodeNo
   *          Node number.
   */
  public void setNodeNo(int nodeNo) {
    this.nodeNo = nodeNo;
  }

  public abstract String getStrForNodeSpecify();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
