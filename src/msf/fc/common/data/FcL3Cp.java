
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "l3_cps")
@NamedQuery(name = "FcL3Cp.findAll", query = "SELECT f FROM FcL3Cp f")
public class FcL3Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcL3CpPK id;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "edge_point_id")
  private FcEdgePoint edgePoint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private FcL3Slice l3Slice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "node_info_id", referencedColumnName = "node_info_id"),
      @JoinColumn(name = "vlan_if_id", referencedColumnName = "vlan_if_id") })
  private FcVlanIf vlanIf;

  public FcL3Cp() {
  }

  public FcL3CpPK getId() {
    return this.id;
  }

  public void setId(FcL3CpPK id) {
    this.id = id;
  }

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public FcEdgePoint getEdgePoint() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoint);
  }

  public void setEdgePoint(FcEdgePoint edgePoint) {
    this.edgePoint = edgePoint;
  }

  public FcL3Slice getL3Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Slice);
  }

  public void setL3Slice(FcL3Slice l3Slice) {
    this.l3Slice = l3Slice;
  }

  public FcVlanIf getVlanIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.vlanIf);
  }

  public void setVlanIf(FcVlanIf vlanIf) {
    this.vlanIf = vlanIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "edgePoint", "l3Slice", "vlanIf" })
        .toString();
  }

}