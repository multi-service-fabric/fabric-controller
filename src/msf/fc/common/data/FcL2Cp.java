
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "l2_cps")
@NamedQuery(name = "FcL2Cp.findAll", query = "SELECT f FROM FcL2Cp f")
public class FcL2Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcL2CpPK id;

  @Column(name = "clag_id")
  private Integer clagId;

  private String esi;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @OneToMany(mappedBy = "l2Cp")
  private List<FcCpFilterInfo> cpFilterInfos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "edge_point_id")
  private FcEdgePoint edgePoint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private FcL2Slice l2Slice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "node_info_id", referencedColumnName = "node_info_id"),
      @JoinColumn(name = "vlan_if_id", referencedColumnName = "vlan_if_id") })
  private FcVlanIf vlanIf;

  public FcL2Cp() {
  }

  public FcL2CpPK getId() {
    return this.id;
  }

  public void setId(FcL2CpPK id) {
    this.id = id;
  }

  public Integer getClagId() {
    return this.clagId;
  }

  public void setClagId(Integer clagId) {
    this.clagId = clagId;
  }

  public String getEsi() {
    return this.esi;
  }

  public void setEsi(String esi) {
    this.esi = esi;
  }

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public List<FcCpFilterInfo> getCpFilterInfos() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.cpFilterInfos);
  }

  public void setCpFilterInfos(List<FcCpFilterInfo> cpFilterInfos) {
    this.cpFilterInfos = cpFilterInfos;
  }

  public FcCpFilterInfo addCpFilterInfo(FcCpFilterInfo cpFilterInfo) throws MsfException {
    getCpFilterInfos().add(cpFilterInfo);
    cpFilterInfo.setL2Cp(this);

    return cpFilterInfo;
  }

  public FcCpFilterInfo removeCpFilterInfo(FcCpFilterInfo cpFilterInfo) throws MsfException {
    getCpFilterInfos().remove(cpFilterInfo);
    cpFilterInfo.setL2Cp(null);

    return cpFilterInfo;
  }

  public FcEdgePoint getEdgePoint() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoint);
  }

  public void setEdgePoint(FcEdgePoint edgePoint) {
    this.edgePoint = edgePoint;
  }

  public FcL2Slice getL2Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Slice);
  }

  public void setL2Slice(FcL2Slice l2Slice) {
    this.l2Slice = l2Slice;
  }

  public FcVlanIf getVlanIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.vlanIf);
  }

  public void setVlanIf(FcVlanIf vlanIf) {
    this.vlanIf = vlanIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "edgePoint", "l2Slice", "vlanIf", "cpFilterInfos" }).toString();
  }

}
