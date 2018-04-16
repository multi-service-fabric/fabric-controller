
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DiEdge extends Number implements Comparable<Double> {

  private Double linkCost = 1d;

  private DiNode from;

  private DiNode to;

  public DiEdge(DiNode from, DiNode to) {
    this.from = from;
    this.to = to;
  }

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

  public DiNode getFrom() {
    return from;
  }

  public void setFrom(DiNode from) {
    this.from = from;
  }

  public DiNode getTo() {
    return to;
  }

  public void setTo(DiNode to) {
    this.to = to;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
