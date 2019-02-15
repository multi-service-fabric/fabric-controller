
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class to store edge information used by Dijkstra's algorithm.
 *
 * @author NTT
 *
 */
public class DiEdge extends Number implements Comparable<Double> {

  private Double linkCost = 1d;

  private DiNode from;

  private DiNode to;

  /**
   * Constructor.
   *
   * @param from
   *          Start point node
   * @param to
   *          End point node
   */
  public DiEdge(DiNode from, DiNode to) {
    this.from = from;
    this.to = to;
  }

  /**
   * Constructor. Also specify the link cost from the start point to the end
   * point.
   *
   * @param from
   *          Start point node
   * @param to
   *          End point node
   * @param linkCost
   *          Link cost
   */
  public DiEdge(DiNode from, DiNode to, Double linkCost) {
    this.from = from;
    this.to = to;
    this.linkCost = linkCost;
  }

  @Override
  public int compareTo(Double target) {
    return linkCost.compareTo(target);
  }

  @Override
  public int intValue() {
    return linkCost.intValue();
  }

  @Override
  public long longValue() {
    return linkCost.longValue();
  }

  @Override
  public float floatValue() {
    return linkCost.floatValue();
  }

  @Override
  public double doubleValue() {
    return linkCost;
  }

  /**
   * Get the start point node of the edge.
   *
   * @return Start point node
   */
  public DiNode getFrom() {
    return from;
  }

  /**
   * Set the start point node of the edge.
   *
   * @param from
   *          Start point node
   */
  public void setFrom(DiNode from) {
    this.from = from;
  }

  /**
   * Get the end point node of the edge.
   *
   * @return End point node
   */
  public DiNode getTo() {
    return to;
  }

  /**
   * Set the end point node of the edge.
   *
   * @param to
   *          End point node
   */
  public void setTo(DiNode to) {
    this.to = to;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
