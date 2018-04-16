
package msf.mfc.common.config.type.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rrs", propOrder = { "peerCluster", "accommodatedClusters" })
public class Rrs {

  @XmlElement(name = "peer_cluster")
  protected int peerCluster;
  @XmlElement(name = "accommodated_clusters", type = Integer.class)
  protected List<Integer> accommodatedClusters;

  public int getPeerCluster() {
    return peerCluster;
  }

  public void setPeerCluster(int value) {
    this.peerCluster = value;
  }

  public List<Integer> getAccommodatedClusters() {
    if (accommodatedClusters == null) {
      accommodatedClusters = new ArrayList<Integer>();
    }
    return this.accommodatedClusters;
  }

}
