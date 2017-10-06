package msf.fc.node.nodes.spines;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.FcRequestUri;
import msf.fc.common.constant.NodeBootStatus;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.BootErrorMessage;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentPK;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.NodeManager;
import msf.fc.node.nodes.spines.data.InternalSpineNodeRequest;
import msf.fc.node.nodes.spines.data.InternalSpineNodeRequestBody;
import msf.fc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.fc.node.nodes.spines.data.SpineNodeCreateResponseBody;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;
import msf.fc.node.nodes.spines.data.entity.PhysicalLinkSpineEntity;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeCreateEcRequestBody;
import msf.fc.rest.ec.node.nodes.spine.data.SpineNodeCreateEcResponseBody;
import msf.fc.rest.ec.node.nodes.spine.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.ManagementInterfaceEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.NodeEcEntity;

public class SpineNodeCreateScenario extends AbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;
  private SpineNodeCreateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeCreateScenario.class);

  public SpineNodeCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      SpineNodeCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SpineNodeCreateRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.performance("start wait to equipment decreasing process.");
      synchronized (NodeManager.getInstance().getEquipmentDeleteLockObject()) {
        logger.performance("end wait to equipment decreasing process.");
        logger.performance("start wait to node increasing/decreasing process.");
        synchronized (NodeManager.getInstance().getNodeCreateAndDeleteLockObject()) {
          logger.performance("end wait to node increasing/decreasing process.");
          SessionWrapper sessionWrapper = new SessionWrapper();
          Node node = new Node();
          Equipment equipment = new Equipment();
          try {
            sessionWrapper.openSession();
            NodeDao nodeDao = new NodeDao();
            List<Node> oppositeNodes = nodeDao.readList(sessionWrapper, request.getClusterId(),
                NodeType.LEAF.getCode());

            sessionWrapper.beginTransaction();
            if (!oppositeNodes.isEmpty()) {
              logger.performance("start get leaf resources lock.");
              DbManager.getInstance().getLeafsLock(oppositeNodes, sessionWrapper);
              logger.performance("end get leaf resources lock.");
            }

            List<Node> nodes = nodeDao.readList(sessionWrapper, request.getClusterId());
            if (!oppositeNodes.isEmpty()) {
              oppositeNodes = nodeDao.readList(sessionWrapper, request.getClusterId(), NodeType.LEAF.getCode());
            }

            checkNodeAfterLock(nodes, nodeDao, sessionWrapper, request.getClusterId(), NodeType.SPINE,
                requestBody.getNodeId());

            List<PhysicalLinkSpineEntity> physicalLinkList = requestBody.getInternalLink().getPhysicalLinkList();
            TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap = checkOppositeNode(oppositeNodes,
                sessionWrapper, physicalLinkList);

            EquipmentDao equipmentDao = new EquipmentDao();
            EquipmentPK equipmentPk = new EquipmentPK();
            equipmentPk.setSwClusterId(request.getClusterId());
            equipmentPk.setEquipmentTypeId(Integer.parseInt(requestBody.getEquipmentTypeId()));
            equipment = equipmentDao.read(sessionWrapper, equipmentPk);
            checkExpansionNode(equipment, physicalLinkList);

            setNode(sessionWrapper, node, equipment);

            List<PhysicalIf> physicalIfs = setPhysicalIfs(node, oppositeNodes, equipment, physicalLinkList);

            setLagIfs(sessionWrapper, node, oppositeNodeMap, physicalIfs);

            nodeDao.create(sessionWrapper, node);

            node = nodeDao.read(sessionWrapper, request.getClusterId(), NodeType.SPINE.getCode(),
                Integer.parseInt(requestBody.getNodeId()));
            PhysicalIfDao physicalIfDao = new PhysicalIfDao();
            LagIfDao lagIfDao = new LagIfDao();
            List<LagIf> oppositeLagIfs = createOppositeNode(sessionWrapper, physicalIfDao, lagIfDao, oppositeNodes,
                oppositeNodeMap, node);

            updateCreateNode(sessionWrapper, nodeDao, node, oppositeLagIfs);

            if (requestBody.isProvisioning()) {
              sendSpineNodeCreate(node, equipment);
            }

            sessionWrapper.commit();
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          if (!requestBody.isProvisioning()) {
            InternalSpineNodeRequest internalSpineNodeRequest = new InternalSpineNodeRequest();
            internalSpineNodeRequest.setClusterId(request.getClusterId());
            internalSpineNodeRequest.setNodeId(requestBody.getNodeId());

            InternalSpineNodeRequestBody internalSpineNodeRequestBody = new InternalSpineNodeRequestBody();
            internalSpineNodeRequestBody.setStatusEnum(NodeBootStatus.SUCCESS);
            internalSpineNodeRequest.setRequestBody(JsonUtil.toJson(internalSpineNodeRequestBody));

            internalSpineNodeRequest
                .setRequestUri(FcRequestUri.SPINE_NODE_NOTIFY.getUri(request.getClusterId(), requestBody.getNodeId()));

            internalSpineNodeRequest.setRequestMethod(FcRequestUri.SPINE_NODE_NOTIFY.getHttpMethod().toString());

            SpineNodeNotifyScenario scenario = new SpineNodeNotifyScenario(OperationType.NORMAL,
                SystemInterfaceType.INTERNAL);
            scenario.execute(internalSpineNodeRequest);
          }

          RestResponseBase responseBase = responseSpineNodeCreateData();
          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void setNode(SessionWrapper sessionWrapper, Node node, Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "node", "equipment" },
          new Object[] { sessionWrapper, node, equipment });
      node.setEquipment(equipment);
      node.setNodeTypeEnum(NodeType.SPINE);
      Integer nodeId = Integer.parseInt(requestBody.getNodeId());
      node.setNodeId(nodeId);
      node.setRpFlag((nodeId == RP_NUMBER_1) || (nodeId == RP_NUMBER_2));
      node.setProvisioning(requestBody.isProvisioning());
      node.setUsername(requestBody.getUsername());
      node.setPassward(requestBody.getPassword());
      node.setRouterId(
          createLoopbackIpAddress(sessionWrapper, request.getClusterId(), NodeType.SPINE, node.getNodeId()));
      node.setManagementIfAddress(
          createManagementIpAddress(sessionWrapper, request.getClusterId(), NodeType.SPINE, node.getNodeId()));
      node.setSnmpCommunity(requestBody.getSnmpCommunity());
      node.setNtpServerAddress(requestBody.getNtpServerAddress());
      node.setProvisioningStatusEnum(ProvisioningStatus.STOPPED);
      node.setHostName(requestBody.getHostName());
      node.setMacAddr(requestBody.getMacAddr());
    } finally {
      logger.methodEnd();
    }
  }

  private List<PhysicalIf> setPhysicalIfs(Node node, List<Node> oppositeNodes, Equipment equipment,
      List<PhysicalLinkSpineEntity> physicalLinkList) throws MsfException {
    try {
      logger.methodStart();
      List<PhysicalIf> physicalIfs = createPhysicalIfs(node, oppositeNodes, equipment, physicalLinkList);
      node.setPhysicalIfs(physicalIfs);
      return physicalIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private List<LagIf> setLagIfs(SessionWrapper sessionWrapper, Node node,
      TreeMap<Integer, List<PhysicalLinkSpineEntity>> oppositeNodeMap, List<PhysicalIf> physicalIfs)
      throws MsfException {
    try {
      logger.methodStart();
      List<LagIf> lagIfs = createLagIfs(sessionWrapper, node, oppositeNodeMap, physicalIfs, false);
      node.setLagIfs(lagIfs);
      return lagIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private SpineNodeCreateEcResponseBody sendSpineNodeCreate(Node node, Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "equipment" }, new Object[] { node, equipment });
      RestResponseBase restResponseBase = null;
      SpineNodeCreateEcRequestBody spineNodeCreateEcData = setSpineNodeCreateEcData(node, equipment);

      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(spineNodeCreateEcData));
      restResponseBase = RestClient.sendRequest(EcRequestUri.SPINE_NODE_CREATE.getHttpMethod(),
          EcRequestUri.SPINE_NODE_CREATE.getUri(), restRequest);

      SpineNodeCreateEcResponseBody spineNodeCreateEcResponseBody = new SpineNodeCreateEcResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.CREATED_201) {
        spineNodeCreateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            SpineNodeCreateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), spineNodeCreateEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return spineNodeCreateEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private SpineNodeCreateEcRequestBody setSpineNodeCreateEcData(Node node, Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "node", "equipment" }, new Object[] { node, equipment });
      EquipmentEcEntity equipmentEcEntity = new EquipmentEcEntity();
      equipmentEcEntity.setEquipmentTypeId(String.valueOf(equipment.getId().getEquipmentTypeId()));
      equipmentEcEntity.setConfigTemplate(equipment.getConfigTemplate());
      equipmentEcEntity.setInitialConfig(equipment.getInitialConfig());
      equipmentEcEntity.setBootCompleteMsg(equipment.getBootCompleteMsg());
      List<String> bootErrorMsgs = new ArrayList<>();
      for (BootErrorMessage bootErrorMessage : equipment.getBootErrorMessages()) {
        bootErrorMsgs.add(bootErrorMessage.getId().getBootErrorMsg());
      }
      equipmentEcEntity.setBootErrorMsgs(bootErrorMsgs);

      NodeEcEntity nodeEcEntity = new NodeEcEntity();
      nodeEcEntity.setNodeId(String.valueOf(node.getNodeId()));
      nodeEcEntity.setHostName(node.getHostName());
      nodeEcEntity.setMacAddr(node.getMacAddr());
      nodeEcEntity.setNtpServerAddress(node.getNtpServerAddress());
      ManagementInterfaceEcEntity managementInterface = new ManagementInterfaceEcEntity();
      managementInterface.setAddress(node.getManagementIfAddress());
      String prefix = String.valueOf(equipment.getSwCluster().getManagementAddressPrefix());
      managementInterface.setPrefix(prefix);
      nodeEcEntity.setManagementInterface(managementInterface);
      nodeEcEntity.setSnmpCommunity(node.getSnmpCommunity());

      SpineNodeCreateEcRequestBody spineNodeCreateEcRequestBody = new SpineNodeCreateEcRequestBody();
      spineNodeCreateEcRequestBody.setEquipment(equipmentEcEntity);
      spineNodeCreateEcRequestBody.setNode(nodeEcEntity);
      return spineNodeCreateEcRequestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeCreateData() {
    try {
      logger.methodStart();
      SpineNodeCreateResponseBody body = new SpineNodeCreateResponseBody();
      body.setNodeId(requestBody.getNodeId());
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
