
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DiNodePair implements Comparable<DiNodePair> {

  private DiNode from;

  private DiNode to;

  private String nodePairNo;

  private boolean isReachable = false;

  private String shortestPath;

  public DiNodePair(DiNode from, DiNode to) {
    this.from = from;
    this.to = to;
    nodePairNo = from.getNodeNo() + "-" + to.getNodeNo();
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

  public boolean isReachable() {
    return isReachable;
  }

  public void setReachable(boolean isReachable) {
    this.isReachable = isReachable;
  }

  public String getShortestPath() {
    return shortestPath;
  }

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
