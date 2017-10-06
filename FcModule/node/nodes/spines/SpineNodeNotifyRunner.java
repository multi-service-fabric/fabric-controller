package msf.fc.node.nodes.spines;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcEmControlStatus;
import msf.fc.common.constant.EcNodeOperationAction;
import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.NodeManager;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.nodes.spines.data.InternalSpineNodeRequest;
import msf.fc.rest.common.EcControlStatusUtil;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeUpdateEcRequestBody;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeUpdateEcResponseBody;
import msf.fc.rest.ec.node.nodes.spine.data.entity.AddNodeOptionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.EmEquipmentEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.EmNodeEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.IfEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.InternalLinkIfExpansionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.L2VpnEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.LagIfExpansionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.LoopbackInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.ManagementInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.MsdpEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.OppositeNodeExpansionEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.PeerEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.PhysicalIfIdEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.PimEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.VpnEcEntity;
import msf.fc.traffic.TrafficManager;

public class SpineNodeNotifyRunner extends AbstractSpineNodeRunnerBase {

  private InternalSpineNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  private ErrorCode ecResponseStatus = null;
  private EcEmControlStatus ecEmControlStatus = null;

  public SpineNodeNotifyRunner(InternalSpineNodeRequest request) {
    this.request = request;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.performance("start wait to node increasing/decreasing process.");
      synchronized (NodeManager.getInstance().getNodeCreateAndDeleteLockObject()) {
        logger.performance("end wait to node increasing/decreasing process.");
        SessionWrapper sessionWrapper = new SessionWrapper();
        Node node;
        AddNodeOptionEcEntity addNodeOptionEcEntity;
        try {
          sessionWrapper.openSession();
          NodeDao nodeDao = new NodeDao();
          node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.SPINE.getCode(),
              Integer.parseInt(request.getNodeId()));

          logger.performance("start get spine resources lock.");
          sessionWrapper.beginTransaction();
          List<Node> nodes = new ArrayList<>();
          nodes.add(node);
          DbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
          logger.performance("end get spine resources lock.");

          node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.SPINE.getCode(),
              Integer.parseInt(request.getNodeId()));

          addNodeOptionEcEntity = setSpineNodeCreateEcData(sessionWrapper, node);

          node.setProvisioningStatusEnum(ProvisioningStatus.SETTING);
          nodeDao.update(sessionWrapper, node);

          sessionWrapper.commit();
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        sendSpineNodeNotify(addNodeOptionEcEntity);

        sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          logger.performance("start get spine resources lock.");
          sessionWrapper.beginTransaction();
          List<Node> nodes = new ArrayList<>();
          nodes.add(node);
          DbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
          logger.performance("end get spine resources lock.");

          NodeDao nodeDao = new NodeDao();
          node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.SPINE.getCode(),
              Integer.parseInt(request.getNodeId()));

          checkEcControlError(ecResponseStatus, node);

          nodeDao.update(sessionWrapper, node);

          try {
            sessionWrapper.commit();
          } catch (MsfException msfException) {
            logger.warn(msfException.getMessage(), msfException);
            ecEmControlStatus = EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus);
            try {
              sessionWrapper.rollback();
            } catch (MsfException msfException2) {
              logger.warn(msfException2.getMessage(), msfException2);
            }
          }
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        if (ecEmControlStatus != null) {
          commitErrorAfterEcControl(ecEmControlStatus);
        }

        if ((ecResponseStatus != null) && (ecResponseStatus != ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED)) {
          checkNodeNotifyEcError(ecResponseStatus);
        }

        TrafficManager.getInstance().setRenewTopology(true);

        if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED) {
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED,
              "Spine Node Notify Complete. EC Control Error.");
        }

        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Node getNode(SessionWrapper sessionWrapper, NodeDao nodeDao, String swClusterId, Integer nodeType,
      Integer nodeId) throws MsfException {
    try {
      logger.methodStart();
      Node node = nodeDao.read(sessionWrapper, swClusterId, nodeType, nodeId);
      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  private AddNodeOptionEcEntity setSpineNodeCreateEcData(SessionWrapper sessionWrapper, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "node" }, new Object[] { sessionWrapper, node });
      AddNodeOptionEcEntity addNodeOption = new AddNodeOptionEcEntity();
      Equipment equipment = node.getEquipment();

      setEmEquipmentEcEntity(addNodeOption, equipment);

      setEmNodeEcEntity(addNodeOption, node, equipment);

      setIfEcEntity(addNodeOption, node);

      NodeDao nodeDao = new NodeDao();
      List<Node> allNode = nodeDao.readList(sessionWrapper, request.getClusterId());

      setOppositeNodes(addNodeOption, node, allNode);

      setMsdpEcEntity(sessionWrapper, addNodeOption, node);

      setVpnEcEntity(addNodeOption, node, equipment);

      return addNodeOption;
    } finally {
      logger.methodEnd();
    }
  }

  private void setEmEquipmentEcEntity(AddNodeOptionEcEntity addNodeOption, Equipment equipment) {
    try {
      logger.methodStart(new String[] { "addNodeOption", "equipment" }, new Object[] { addNodeOption, equipment });
      EmEquipmentEcEntity emEquipmentEcEntity = new EmEquipmentEcEntity();
      emEquipmentEcEntity.setEquipmentTypeId(String.valueOf(equipment.getId().getEquipmentTypeId()));
      emEquipmentEcEntity.setPlatform(equipment.getPlatformName());
      emEquipmentEcEntity.setOs(equipment.getOsName());
      emEquipmentEcEntity.setFirmware(equipment.getFirmwareVersion());
      addNodeOption.setEquipment(emEquipmentEcEntity);
    } finally {
      logger.methodEnd();
    }
  }

  private void setEmNodeEcEntity(AddNodeOptionEcEntity addNodeOption, Node node, Equipment equipment)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "addNodeOption", "node", "equipment" },
          new Object[] { addNodeOption, node, equipment });
      EmNodeEcEntity emNodeEcEntity = new EmNodeEcEntity();
      emNodeEcEntity.setNodeId(String.valueOf(node.getNodeId()));
      emNodeEcEntity.setUsername(node.getUsername());
      emNodeEcEntity.setPassword(node.getPassward());
      emNodeEcEntity.setProvisioning(node.getProvisioning());
      emNodeEcEntity.setNtpServerAddress(node.getNtpServerAddress());
      ManagementInterfaceEcEntity managementInterface = new ManagementInterfaceEcEntity();
      managementInterface.setAddress(node.getManagementIfAddress());
      String prefix = String.valueOf(equipment.getSwCluster().getManagementAddressPrefix());
      managementInterface.setPrefix(prefix);
      emNodeEcEntity.setManagementInterface(managementInterface);
      LoopbackInterfaceEcEntity loopbackInterface = new LoopbackInterfaceEcEntity();
      loopbackInterface.setAddress(node.getRouterId());
      loopbackInterface.setPrefix(LOOPBACK_INTERFACE_PREFIX);
      emNodeEcEntity.setLoopbackInterface(loopbackInterface);
      emNodeEcEntity.setSnmpCommunity(node.getSnmpCommunity());
      addNodeOption.setNode(emNodeEcEntity);
    } finally {
      logger.methodEnd();
    }
  }

  private void setIfEcEntity(AddNodeOptionEcEntity addNodeOption, Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "addNodeOption", "node" }, new Object[] { addNodeOption, node });
      IfEcEntity ifs = new IfEcEntity();
      List<InternalLinkIfExpansionEcEntity> internalLinkIfList = new ArrayList<>();
      List<LagIf> lagIfs = node.getLagIfs();
      for (LagIf lagIf : lagIfs) {
        InternalLinkIfExpansionEcEntity internalLinkIfEcEntity = new InternalLinkIfExpansionEcEntity();
        setLagIfExpansionEcEntity(internalLinkIfEcEntity, lagIf);
        internalLinkIfList.add(internalLinkIfEcEntity);
      }
      ifs.setInternalLinkIfList(internalLinkIfList);
      addNodeOption.setIfs(ifs);
    } finally {
      logger.methodEnd();
    }
  }

  private void setLagIfExpansionEcEntity(InternalLinkIfExpansionEcEntity internalLinkIf, LagIf lagIf)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf", "lagIf" }, new Object[] { internalLinkIf, lagIf });
      internalLinkIf.setInternalLinkIfId(String.valueOf(lagIf.getInternalLinkIf().getInternalLinkIfId()));
      LagIfExpansionEcEntity lagIfExpansionEcEntity = new LagIfExpansionEcEntity();
      lagIfExpansionEcEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
      List<PhysicalIfIdEcEntity> physicalIfList = new ArrayList<>();
      for (LagConstruction lagConstruction : lagIf.getLagConstructions()) {
        PhysicalIfIdEcEntity physicalIfIdEcEntity = new PhysicalIfIdEcEntity();
        physicalIfIdEcEntity.setPhysicalIfId(lagConstruction.getPhysicalIf().getPhysicalIfId());
        physicalIfList.add(physicalIfIdEcEntity);
      }
      lagIfExpansionEcEntity.setPhysicalIfList(physicalIfList);
      lagIfExpansionEcEntity.setMinimumLinks(physicalIfList.size());
      lagIfExpansionEcEntity.setLinkSpeed(lagIf.getSpeed());
      lagIfExpansionEcEntity.setLinkIpAddress(lagIf.getIpv4Address());
      lagIfExpansionEcEntity.setPrefix(LAG_INTERFACE_PREFIX);
      internalLinkIf.setLagIf(lagIfExpansionEcEntity);
    } finally {
      logger.methodEnd();
    }
  }

  private void setOppositeNodes(AddNodeOptionEcEntity addNodeOption, Node node, List<Node> allNode)
      throws MsfException {
    try {
      logger.methodStart();
      List<OppositeNodeExpansionEcEntity> oppositeNodeList = new ArrayList<>();
      for (Node oppositeNode : allNode) {
        if (oppositeNode.getNodeTypeEnum().equals(NodeType.LEAF)) {
          OppositeNodeExpansionEcEntity oppositeNodeEcEntity = new OppositeNodeExpansionEcEntity();
          oppositeNodeEcEntity.setNodeId(String.valueOf(oppositeNode.getNodeId()));
          oppositeNodeEcEntity.setVpnType(oppositeNode.getVpnType());
          for (LagIf lagIf : oppositeNode.getLagIfs()) {
            if (lagIf.getOppositeNode() != null) {
              if (lagIf.getOppositeNode().getNodeId().equals(node.getNodeId())) {
                InternalLinkIfExpansionEcEntity internalLinkIf = new InternalLinkIfExpansionEcEntity();
                setLagIfExpansionEcEntity(internalLinkIf, lagIf);
                oppositeNodeEcEntity.setInternalLinkIf(internalLinkIf);
                break;
              }
            }
          }
          oppositeNodeList.add(oppositeNodeEcEntity);
        }
      }
      addNodeOption.setOppositeNodeList(oppositeNodeList);
    } finally {
      logger.methodEnd();
    }
  }

  private void setMsdpEcEntity(SessionWrapper sessionWrapper, AddNodeOptionEcEntity addNodeOption, Node node)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "addNodeOption", "node" },
          new Object[] { sessionWrapper, addNodeOption, node });
      if (node.getRpFlag()) {
        MsdpEcEntity msdpEcEntity = new MsdpEcEntity();
        PeerEcEntity peerEcEntity = new PeerEcEntity();
        peerEcEntity.setLocalAddress(node.getRouterId());
        if (node.getNodeId() == RP_NUMBER_1) {
          peerEcEntity.setAddress(createLoopbackIpAddress(sessionWrapper, request.getClusterId(), RP_NUMBER_2));
        } else if (node.getNodeId() == RP_NUMBER_2) {
          peerEcEntity.setAddress(createLoopbackIpAddress(sessionWrapper, request.getClusterId(), RP_NUMBER_1));
        }
        msdpEcEntity.setPeer(peerEcEntity);
        addNodeOption.setMsdp(msdpEcEntity);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setVpnEcEntity(AddNodeOptionEcEntity addNodeOption, Node node, Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "addNodeOption", "node", "equipment" },
          new Object[] { addNodeOption, node, equipment });
      VpnEcEntity vpnEcEntity = new VpnEcEntity();
      L2VpnEcEntity l2VpnEcEntity = new L2VpnEcEntity();
      PimEcEntity pimEcEntity = new PimEcEntity();
      if (node.getRpFlag()) {
        pimEcEntity.setRpAddress(equipment.getSwCluster().getRpLoopbackAddress());
      } else {
        pimEcEntity.setOtherRpAddress(equipment.getSwCluster().getRpLoopbackAddress());
      }
      l2VpnEcEntity.setPim(pimEcEntity);
      vpnEcEntity.setL2Vpn(l2VpnEcEntity);
      addNodeOption.setVpn(vpnEcEntity);
    } finally {
      logger.methodEnd();
    }
  }

  private SpineNodeUpdateEcResponseBody sendSpineNodeNotify(AddNodeOptionEcEntity addNodeOptionEcEntity)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "addNodeOptionEcEntity" }, new Object[] { addNodeOptionEcEntity });
      RestResponseBase restResponseBase = null;
      SpineNodeUpdateEcRequestBody spineNodeUpdateEcRequestBody = new SpineNodeUpdateEcRequestBody();
      spineNodeUpdateEcRequestBody.setActionEnum(EcNodeOperationAction.ADD_NODE);
      spineNodeUpdateEcRequestBody.setAddNodeOption(addNodeOptionEcEntity);

      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(spineNodeUpdateEcRequestBody));
      try {
        restResponseBase = RestClient.sendRequest(EcRequestUri.SPINE_NODE_UPDATE.getHttpMethod(),
            EcRequestUri.SPINE_NODE_UPDATE.getUri(request.getNodeId()), restRequest);
      } catch (MsfException msfException) {
        logger.warn(msfException.getMessage(), msfException);
        if ((msfException.getErrorCode() == ErrorCode.EC_CONTROL_TIMEOUT)
            || (msfException.getErrorCode() == ErrorCode.EC_CONNECTION_ERROR)) {
          ecResponseStatus = msfException.getErrorCode();
          return null;
        } else {
          throw msfException;
        }
      }

      SpineNodeUpdateEcResponseBody spineNodeCreateEcResponseBody = new SpineNodeUpdateEcResponseBody();

      if ((restResponseBase.getHttpStatusCode() != HttpStatus.OK_200)
          && (restResponseBase.getHttpStatusCode() != HttpStatus.CREATED_201)) {
        try {
          spineNodeCreateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
              SpineNodeUpdateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        } catch (MsfException msfException) {
          logger.warn(msfException.getMessage(), msfException);
          if (msfException.getErrorCode() == ErrorCode.EC_CONTROL_ERROR) {
            ecResponseStatus = msfException.getErrorCode();
            return null;
          } else {
            throw msfException;
          }
        }
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), spineNodeCreateEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        ecResponseStatus = EcControlStatusUtil.checkEcEmControlErrorCode(spineNodeCreateEcResponseBody.getErrorCode());
      }

      return spineNodeCreateEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
