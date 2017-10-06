package msf.fc.node.interfaces.lagifs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.interfaces.lagifs.data.LagIfCreateAsyncResponseBody;
import msf.fc.node.interfaces.lagifs.data.LagIfCreateRequestBody;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.EcControlStatusUtil;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfCreateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfCreateEcResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.CreateLagIfEcEntity;

public class LagInterfaceCreateRunner extends AbstractLagInterfaceRunnerBase {

  private LagIfRequest request;
  private LagIfCreateRequestBody requestBody;

  private ErrorCode ecResponseStatus = null;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public LagInterfaceCreateRunner(LagIfRequest request, LagIfCreateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
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
        List<PhysicalIf> physicalIfs = checkForPhysicalInterface(sessionWrapper, physicalIfDao,
            requestBody.getPhysicalIfIdList());

        checkForLagInterfaceRelation(physicalIfs, sessionWrapper);

        LagIfDao lagIfDao = new LagIfDao();
        LagIf lagIf = createLagConstructionWithLagIf(sessionWrapper, lagIfDao, physicalIfs, node);

        sendLagInterfaceCreate(lagIf);

        responseBase = responseLagInterfaceCreateAsyncData(lagIf.getLagIfId());

        sessionWrapper.commit(EcControlStatusUtil.getStatusFromFcErrorCode(ecResponseStatus));
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED) {
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED,
            "Lag Interface Create Complete. EC Control Error.");
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

  private List<PhysicalIf> checkForPhysicalInterface(SessionWrapper sessionWrapper, PhysicalIfDao physicalIfDao,
      List<String> physicalIfIds) throws MsfException {
    try {
      logger.methodStart();
      List<PhysicalIf> physicalIfs = new ArrayList<PhysicalIf>();
      for (String physicalIfId : physicalIfIds) {
        PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), physicalIfId);
        if (physicalIf == null) {
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
              "target resource not found. parameters = physicalIf");
        }
        physicalIfs.add(physicalIf);
      }

      return physicalIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForLagInterfaceRelation(List<PhysicalIf> physicalIfs, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart();
      EdgePointDao edgePointDao = new EdgePointDao();
      String physicalIfSpeed = null;
      for (PhysicalIf physicalIf : physicalIfs) {
        if (null == physicalIfSpeed) {
          physicalIfSpeed = physicalIf.getSpeed();
        }
        if (null != edgePointDao.readByPhysicalIfInfoId(sessionWrapper, physicalIf.getPhysicalIfInfoId())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the edge point.");
        } else if (null != physicalIf.getLagConstruction()) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Registered to the lag if.");
        } else if (null == physicalIf.getSpeed()) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Speed registration is not.");
        } else if (!physicalIfSpeed.equals(physicalIf.getSpeed())) {
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "Speed information is different.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private LagIf createLagConstructionWithLagIf(SessionWrapper sessionWrapper, LagIfDao lagIfDao,
      List<PhysicalIf> physicalIfs, Node node) throws MsfException {
    try {
      logger.methodStart();
      LagIf lagIf = new LagIf();
      lagIf.setLagIfId(getNextLagIfId(sessionWrapper, lagIfDao));
      lagIf.setMinimumLinks(physicalIfs.size());
      lagIf.setSpeed(physicalIfs.get(0).getSpeed());
      List<LagConstruction> lagConstructions = new ArrayList<>();
      for (PhysicalIf physicalIf : physicalIfs) {
        LagConstruction lagConstruction = new LagConstruction();
        lagConstruction.setPhysicalIf(physicalIf);
        lagConstruction.setPhysicalIfInfoId(physicalIf.getPhysicalIfInfoId());
        lagConstruction.setLagIf(lagIf);
        lagConstructions.add(lagConstruction);
      }
      lagIf.setLagConstructions(lagConstructions);
      lagIf.setNode(node);
      lagIfDao.create(sessionWrapper, lagIf);
      return lagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfCreateEcResponseBody sendLagInterfaceCreate(LagIf lagIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "lagIf" }, new Object[] { lagIf });
      CreateLagIfEcEntity lagIfEcEntity = new CreateLagIfEcEntity();
      lagIfEcEntity.setLagIfId(String.valueOf(lagIf.getLagIfId()));
      lagIfEcEntity.setMinimumLink(lagIf.getLagConstructions().size());
      lagIfEcEntity.setLinkSpeed(lagIf.getSpeed());
      lagIfEcEntity.setPhysicalIfIdList(requestBody.getPhysicalIfIdList());
      LagIfCreateEcRequestBody ecRequestBody = new LagIfCreateEcRequestBody();
      ecRequestBody.setLagIf(lagIfEcEntity);
      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(ecRequestBody));

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_CREATE.getHttpMethod(),
          EcRequestUri.LAG_IF_CREATE.getUri(request.getFabricType(), request.getNodeId()), restRequest);

      LagIfCreateEcResponseBody lagIfCreateEcResponseBody = new LagIfCreateEcResponseBody();

      if (restResponseBase.getHttpStatusCode() != HttpStatus.CREATED_201) {
        lagIfCreateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            LagIfCreateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        ecResponseStatus = EcControlStatusUtil.checkEcEmControlErrorCode(lagIfCreateEcResponseBody.getErrorCode());
        if (ecResponseStatus == ErrorCode.EC_CONTROL_ERROR) {
          String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
              restResponseBase.getHttpStatusCode(), lagIfCreateEcResponseBody.getErrorCode());
          logger.error(errorMsg);
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
        }

      }

      return lagIfCreateEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceCreateAsyncData(Integer lagIfId) {
    try {
      logger.methodStart(new String[] { "lagIfId" }, new Object[] { lagIfId });
      if (ecResponseStatus != null) {
        return null;
      }
      LagIfCreateAsyncResponseBody body = new LagIfCreateAsyncResponseBody();
      body.setLagIfId(String.valueOf(lagIfId));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
