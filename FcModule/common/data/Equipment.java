package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "equipments")
@NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM Equipment e")
public class Equipment implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private EquipmentPK id;

  @Column(name = "boot_complete_msg")
  private String bootCompleteMsg;

  @Column(name = "config_template")
  private String configTemplate;

  @Column(name = "firmware_version")
  private String firmwareVersion;

  @Column(name = "initial_config")
  private String initialConfig;

  @Column(name = "l2vpn_capability")
  private Boolean l2vpnCapability;

  @Column(name = "l3vpn_capability")
  private Boolean l3vpnCapability;

  @Column(name = "os_name")
  private String osName;

  @Column(name = "platform_name")
  private String platformName;

  @OneToMany(mappedBy = "equipment", cascade = { CascadeType.ALL })
  private List<BootErrorMessage> bootErrorMessages;

  @OneToMany(mappedBy = "equipment", cascade = { CascadeType.ALL })
  private List<EquipmentIf> equipmentIfs;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id", insertable = false, updatable = false)
  private SwCluster swCluster;

  @OneToMany(mappedBy = "equipment")
  private List<Node> nodes;

  public Equipment() {
  }

  public EquipmentPK getId() {
    return this.id;
  }

  public void setId(EquipmentPK id) {
    this.id = id;
  }

  public String getBootCompleteMsg() {
    return this.bootCompleteMsg;
  }

  public void setBootCompleteMsg(String bootCompleteMsg) {
    this.bootCompleteMsg = bootCompleteMsg;
  }

  public String getConfigTemplate() {
    return this.configTemplate;
  }

  public void setConfigTemplate(String configTemplate) {
    this.configTemplate = configTemplate;
  }

  public String getFirmwareVersion() {
    return this.firmwareVersion;
  }

  public void setFirmwareVersion(String firmwareVersion) {
    this.firmwareVersion = firmwareVersion;
  }

  public String getInitialConfig() {
    return this.initialConfig;
  }

  public void setInitialConfig(String initialConfig) {
    this.initialConfig = initialConfig;
  }

  public Boolean getL2vpnCapability() {
    return this.l2vpnCapability;
  }

  public void setL2vpnCapability(Boolean l2vpnCapability) {
    this.l2vpnCapability = l2vpnCapability;
  }

  public Boolean getL3vpnCapability() {
    return this.l3vpnCapability;
  }

  public void setL3vpnCapability(Boolean l3vpnCapability) {
    this.l3vpnCapability = l3vpnCapability;
  }

  public String getOsName() {
    return this.osName;
  }

  public void setOsName(String osName) {
    this.osName = osName;
  }

  public String getPlatformName() {
    return this.platformName;
  }

  public void setPlatformName(String platformName) {
    this.platformName = platformName;
  }

  public List<BootErrorMessage> getBootErrorMessages() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.bootErrorMessages);
  }

  public void setBootErrorMessages(List<BootErrorMessage> bootErrorMessages) {
    this.bootErrorMessages = bootErrorMessages;
  }

  public BootErrorMessage addBootErrorMessage(BootErrorMessage bootErrorMessage) throws MsfException {
    getBootErrorMessages().add(bootErrorMessage);
    bootErrorMessage.setEquipment(this);

    return bootErrorMessage;
  }

  public BootErrorMessage removeBootErrorMessage(BootErrorMessage bootErrorMessage) throws MsfException {
    getBootErrorMessages().remove(bootErrorMessage);
    bootErrorMessage.setEquipment(null);

    return bootErrorMessage;
  }

  public List<EquipmentIf> getEquipmentIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.equipmentIfs);
  }

  public void setEquipmentIfs(List<EquipmentIf> equipmentIfs) {
    this.equipmentIfs = equipmentIfs;
  }

  public EquipmentIf addEquipmentIf(EquipmentIf equipmentIf) throws MsfException {
    getEquipmentIfs().add(equipmentIf);
    equipmentIf.setEquipment(this);

    return equipmentIf;
  }

  public EquipmentIf removeEquipmentIf(EquipmentIf equipmentIf) throws MsfException {
    getEquipmentIfs().remove(equipmentIf);
    equipmentIf.setEquipment(null);

    return equipmentIf;
  }

  public SwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(SwCluster swCluster) {
    this.swCluster = swCluster;
  }

  public List<Node> getNodes() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.nodes);
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public Node addNode(Node node) throws MsfException {
    getNodes().add(node);
    node.setEquipment(this);

    return node;
  }

  public Node removeNode(Node node) throws MsfException {
    getNodes().remove(node);
    node.setEquipment(null);

    return node;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "bootErrorMessages", "equipmentIfs", "swCluster", "nodes" })
        .toString();
  }

}