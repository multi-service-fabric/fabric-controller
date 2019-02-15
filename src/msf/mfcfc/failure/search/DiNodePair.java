
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class to store information of a start point node-end point node pair of a
 * path used by Dijkstra's algorithm.
 *
 * @author NTT
 *
 */
public class DiNodePair implements Comparable<DiNodePair> {

  private DiNode from;

  private DiNode to;

  private String nodePairNo;

  private boolean isReachable = false;

  private String shortestPath;

  /**
   * Constructor.
   *
   * @param from
   *          Start point node
   * @param to
   *          End point node
   */
  public DiNodePair(DiNode from, DiNode to) {
    this.from = from;
    this.to = to;
    nodePairNo = from.getNodeNo() + "-" + to.getNodeNo();
  }

  /**
   * Get the start point node.
   *
   * @return Start point node
   */
  public DiNode getFrom() {
    return from;
  }

  /**
   * Set the start point node.
   *
   * @param from
   *          Start point node
   */
  public void setFrom(DiNode from) {
    this.from = from;
  }

  /**
   * Get the end point node.
   *
   * @return End point node
   */
  public DiNode getTo() {
    return to;
  }

  /**
   * Set the end point node.
   *
   * @param to
   *          End point node
   */
  public void setTo(DiNode to) {
    this.to = to;
  }

  /**
   * Returns reachability between the start point node and the end point node
   * registered with this instance.
   *
   * @return Reachability between the start point node and the end point node.
   */
  public boolean isReachable() {
    return isReachable;
  }

  /**
   * Register reachability between the start point node and the end point node
   * registered with this instance.
   *
   * @param isReachable
   *          Reachability between the start point node and the end point node.
   */
  public void setReachable(boolean isReachable) {
    this.isReachable = isReachable;
  }

  /**
   * Get a string which shows the shortest path from the start point node to the
   * end point node.
   *
   * @return Shortest path string
   */
  public String getShortestPath() {
    return shortestPath;
  }

  /**
   * Set a string which shows the shortest path from the start point node to the
   * end point node.
   *
   * @param shortestPath
   *          Shortest path string
   */
  public void setShortestPath(String shortestPath) {
    this.shortestPath = shortestPath;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int compareTo(DiNodePair target) {
    return nodePairNo.compareTo(target.nodePairNo);
  }

  public String getNodePairNo() {
    return nodePairNo;
  }

}
