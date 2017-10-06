package msf.fc.node.interfaces.physicalifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.PhysicalIfUpdateAction;
import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfUpdateRequestBody;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfUpdateEcResponseBody;

public class PhysicalInterfaceUpdateScenario extends AbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;
  private PhysicalIfUpdateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(PhysicalInterfaceUpdateScenario.class);

  public PhysicalInterfaceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(PhysicalIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFabricTypeLeaf(request.getFabricType());

      checkNodeId(request.getNodeId());

      checkPhysicalIfId(request.getIfId());

      PhysicalIfUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          PhysicalIfUpdateRequestBody.class);
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

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        NodeDao nodeDao = new NodeDao();
        Node node = getNode(sessionWrapper, nodeDao, request.getClusterId(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        DbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        PhysicalIfDao physicalIfDao = new PhysicalIfDao();
        PhysicalIf physicalIf = getPhysicalInterface(sessionWrapper, physicalIfDao, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        node = getNode(sessionWrapper, nodeDao, request.getClusterId(), request.getFabricTypeEnum().getCode(),
            Integer.parseInt(request.getNodeId()));

        checkForProvision(node);

        checkForPhysicalInterface(physicalIf, sessionWrapper);

        physicalIf.setSpeed(requestBody.getSpeed());
        if (requestBody.getActionEnum().equals(PhysicalIfUpdateAction.SPEED_SET)) {
          physicalIf.setPhysicalPortFlag(true);
        } else if (requestBody.getActionEnum().equals(PhysicalIfUpdateAction.SPEED_DELETE)) {
          physicalIf.setPhysicalPortFlag(false);
        }

        physicalIfDao.update(sessionWrapper, physicalIf);

        responseBase = responsePhysicalInterfaceUpdateData();

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
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

  private void checkForProvision(Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      if (!node.getProvisioningStatusEnum().equals(ProvisioningStatus.BOOT_COMPLETE)) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
            "There is an nodes of the other in a different state.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForPhysicalInterface(PhysicalIf physicalIf, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf", "sessionWrapper" }, new Object[] { physicalIf, sessionWrapper });
      EdgePointDao edgePointDao = new EdgePointDao();
      if (null != physicalIf.getLagConstruction()) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Already physical IF is used in Lag IF.");
      } else if (null != edgePointDao.readByPhysicalIfInfoId(sessionWrapper, physicalIf.getPhysicalIfInfoId())) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Already physical IF is used in edge point.");
      }
      if (requestBody.getActionEnum().equals(PhysicalIfUpdateAction.SPEED_SET)) {
        if (null != physicalIf.getSpeed()) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "It has already been registered speed.");
        }

        boolean speedCapabilityFlag = false;
        for (EquipmentIf equipmentIf : physicalIf.getNode().getEquipment().getEquipmentIfs()) {
          if (equipmentIf.getId().getSpeedCapability().equals(requestBody.getSpeed())) {
            speedCapabilityFlag = true;
            break;
          }
        }
        if (!speedCapabilityFlag) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Not a compatible speed.");
        }
      }
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responsePhysicalInterfaceUpdateData() throws MsfException {
    try {
      logger.methodStart();
      sendPhysicalInterfaceUpdate();

      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfUpdateEcResponseBody sendPhysicalInterfaceUpdate() throws MsfException {
    try {
      logger.methodStart();
      PhysicalIfUpdateEcRequestBody physicalIfUpdateEcRequestBody = new PhysicalIfUpdateEcRequestBody();
      physicalIfUpdateEcRequestBody.setAction(requestBody.getAction());
      physicalIfUpdateEcRequestBody.setSpeed(requestBody.getSpeed());
      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(physicalIfUpdateEcRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_UPDATE.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_UPDATE.getUri(request.getFabricType(), request.getNodeId(), request.getIfId()),
          restRequest);

      PhysicalIfUpdateEcResponseBody physicalIfUpdateEcResponseBody = new PhysicalIfUpdateEcResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        physicalIfUpdateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            PhysicalIfUpdateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), physicalIfUpdateEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return physicalIfUpdateEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
