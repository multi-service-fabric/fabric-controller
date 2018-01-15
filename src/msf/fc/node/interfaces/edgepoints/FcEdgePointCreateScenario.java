
package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcEdgePointDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfBreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfPhysicalIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfReadEcResponseBody;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointCreateRequestBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointCreateResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for Edge-Point interface registration.
 *
 * @author NTT
 *
 */
public class FcEdgePointCreateScenario extends FcAbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointCreateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEdgePointReadListScenario.class);

  private static final Integer FIRST_ID_NUM = 1;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcEdgePointCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getClusterId());

      EdgePointCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          EdgePointCreateRequestBody.class);

      requestBody.validate();

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        FcNode fcNode = getNode(sessionWrapper, fcNodeDao, NodeType.LEAF.getCode(),
            Integer.parseInt(requestBody.getLeafNodeId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();
        fcNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        FcEdgePointDao fcdgePointDao = new FcEdgePointDao();
        FcEdgePoint fcEdgePoint = createEdgePoint(sessionWrapper, fcdgePointDao, fcNode);

        responseBase = responseEdgePointCreateData(fcEdgePoint.getEdgePointId());

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

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType, nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private FcEdgePoint createEdgePoint(SessionWrapper sessionWrapper, FcEdgePointDao fcEdgePointDao, FcNode fcNode)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "fcEdgePointDao" },
          new Object[] { sessionWrapper, fcEdgePointDao });

      FcEdgePoint fcEdgePoint = new FcEdgePoint();
      fcEdgePoint.setEdgePointId(getNextEdgePointId(sessionWrapper, fcEdgePointDao));

      if (requestBody.getLagIfId() != null) {

        FcLagIfDao fcLagIfDao = new FcLagIfDao();
        FcLagIf fcLagIf = getLagInterface(sessionWrapper, fcLagIfDao, fcNode,
            Integer.valueOf(requestBody.getLagIfId()));
        fcEdgePoint.setLagIf(fcLagIf);
      } else if (requestBody.getPhysicalIfId() != null) {

        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
        FcPhysicalIf fcPhysicalIf = getPhysicalInterface(sessionWrapper, fcPhysicalIfDao, fcNode,
            requestBody.getPhysicalIfId());
        checkLagIfForPhysicalIfRelation(fcNode, fcPhysicalIf);
        fcEdgePoint.setPhysicalIf(fcPhysicalIf);
      } else {

        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();
        FcBreakoutIf fcBreakoutIf = getBreakoutInterface(sessionWrapper, fcBreakoutIfDao, fcNode,
            requestBody.getBreakoutIfId());
        checkLagIfForBreakoutIfRelation(fcNode, fcBreakoutIf);
        fcEdgePoint.setBreakoutIf(fcBreakoutIf);
      }
      fcEdgePoint.setTrafficThreshold(requestBody.getTrafficThreshold());

      fcEdgePointDao.create(sessionWrapper, fcEdgePoint);

      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextEdgePointId(SessionWrapper sessionWrapper, FcEdgePointDao fcdgePointDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "fcdgePointDao" },
          new Object[] { sessionWrapper, fcdgePointDao });
      FcEdgePoint biggestIdEdgePoint = fcdgePointDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdEdgePoint) {

        return FIRST_ID_NUM;
      } else {
        return biggestIdEdgePoint.getEdgePointId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected FcLagIf getLagInterface(SessionWrapper sessionWrapper, FcLagIfDao fcLagIfDao, FcNode fcNode,
      Integer lagIfId) throws MsfException {
    try {
      logger.methodStart();
      FcLagIf fcLagIf = fcLagIfDao.read(sessionWrapper, fcNode.getNodeTypeEnum().getCode(), fcNode.getNodeId(),
          lagIfId);
      if (fcLagIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "Related resource not found. parameters = lagIf");
      }

      if (CollectionUtils.isNotEmpty(fcLagIf.getEdgePoints())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the edge point.");

      } else if (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the internal link interface.");

      } else if (CollectionUtils.isNotEmpty(fcLagIf.getClusterLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the cluster link.");

      }
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected FcPhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, FcPhysicalIfDao fcPhysicalIfDao,
      FcNode fcNode, String physicalIfId) throws MsfException {
    try {
      logger.methodStart();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(sessionWrapper, fcNode.getNodeTypeEnum().getCode(),
          fcNode.getNodeId(), physicalIfId);
      if (fcPhysicalIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "Related resource not found. parameters = physicalIf");
      }

      PhysicalIfReadEcResponseBody physicalIfReadEc = sendPhysicalInterfaceRead(fcPhysicalIf, fcNode);

      if (physicalIfReadEc.getPhysicalIf().getLinkSpeed() == null) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "Registered to the physical interface. parameters = linkSpeed");
      }
      if (CollectionUtils.isNotEmpty(physicalIfReadEc.getPhysicalIf().getBreakoutIfList())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "Registered to the physical interface. parameters = breakoutIfList");
      }

      if (CollectionUtils.isNotEmpty(fcPhysicalIf.getEdgePoints())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the edge point.");

      } else if (CollectionUtils.isNotEmpty(fcPhysicalIf.getInternalLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the internal link interface.");

      } else if (CollectionUtils.isNotEmpty(fcPhysicalIf.getClusterLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the cluster link.");

      }
      return fcPhysicalIf;

    } finally {
      logger.methodEnd();
    }
  }

  private FcBreakoutIf getBreakoutInterface(SessionWrapper sessionWrapper, FcBreakoutIfDao fcBreakoutIfDao,
      FcNode fcNode, String breakoutIfId) throws MsfException {
    try {
      logger.methodStart();
      FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, fcNode.getNodeTypeEnum().getCode(),
          fcNode.getNodeId(), breakoutIfId);
      if (fcBreakoutIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "Related resource not found. parameters = breakoutIf");
      }

      if (CollectionUtils.isNotEmpty(fcBreakoutIf.getEdgePoints())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the edge point.");

      } else if (CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the internal link interface.");

      } else if (CollectionUtils.isNotEmpty(fcBreakoutIf.getClusterLinkIfs())) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the cluster link.");

      }
      return fcBreakoutIf;
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIfReadEcResponseBody sendPhysicalInterfaceRead(FcPhysicalIf fcPhysicalIf, FcNode fcNode)
      throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.PHYSICAL_IF_READ.getHttpMethod(),
          EcRequestUri.PHYSICAL_IF_READ.getUri(String.valueOf(fcNode.getEcNodeId()), fcPhysicalIf.getPhysicalIfId()),
          null, ecControlIpAddress, ecControlPort);

      PhysicalIfReadEcResponseBody physicalIfReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          PhysicalIfReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          physicalIfReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return physicalIfReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLagIfForPhysicalIfRelation(FcNode fcNode, FcPhysicalIf fcPhysicalIf) throws MsfException {
    try {
      logger.methodStart();

      LagIfReadListEcResponseBody lagIfReadListEc = sendLagInterfaceReadList(fcNode);

      if (lagIfReadListEc.getLagIf() != null) {

        for (LagIfEcEntity lagIfEcEntity : lagIfReadListEc.getLagIf()) {
          List<LagIfPhysicalIfEcEntity> lagIfPhysicalIfEcEntityList = lagIfEcEntity.getLagMember().getPhysicalIfList();
          if (lagIfPhysicalIfEcEntityList != null && !lagIfPhysicalIfEcEntityList.isEmpty()) {
            for (LagIfPhysicalIfEcEntity lagIfPhysicalIfEcEntity : lagIfPhysicalIfEcEntityList) {
              if (lagIfPhysicalIfEcEntity.getPhysicalIfId().equals(fcPhysicalIf.getPhysicalIfId())) {

                throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the physical interface id.");
              }
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkLagIfForBreakoutIfRelation(FcNode fcNode, FcBreakoutIf fcBreakoutIf) throws MsfException {
    try {
      logger.methodStart();

      LagIfReadListEcResponseBody lagIfReadListEc = sendLagInterfaceReadList(fcNode);

      if (lagIfReadListEc.getLagIf() != null) {

        for (LagIfEcEntity lagIfEcEntity : lagIfReadListEc.getLagIf()) {
          List<LagIfBreakoutIfEcEntity> lagIfBreakoutIfEcEntityList = lagIfEcEntity.getLagMember().getBreakoutIfList();
          if (lagIfBreakoutIfEcEntityList != null && !lagIfBreakoutIfEcEntityList.isEmpty()) {
            for (LagIfBreakoutIfEcEntity lagIfBreakoutIfEcEntity : lagIfBreakoutIfEcEntityList) {
              if (lagIfBreakoutIfEcEntity.getBreakoutIfId().equals(fcBreakoutIf.getBreakoutIfId())) {

                throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the breakout interface id.");
              }
            }
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private LagIfReadListEcResponseBody sendLagInterfaceReadList(FcNode fcNode) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.LAG_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.LAG_IF_READ_LIST.getUri(String.valueOf(fcNode.getEcNodeId())), null, ecControlIpAddress,
          ecControlPort);

      LagIfReadListEcResponseBody lagIfReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          LagIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          lagIfReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return lagIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEdgePointCreateData(Integer edgePointId) {
    try {
      logger.methodStart(new String[] { "edgePointId" }, new Object[] { edgePointId });
      EdgePointCreateResponseBody body = new EdgePointCreateResponseBody();
      body.setEdgePointId(String.valueOf(edgePointId));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
