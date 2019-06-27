
package msf.fc.node.interfaces.lagifs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.rest.ec.node.interfaces.lag.data.LagIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfPhysicalIfUpdateEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfUpdateEcEntity;
import msf.mfcfc.common.constant.EcLagIfUpdateAction;
import msf.mfcfc.common.constant.EcLagLinkType;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.LagIfUpdateAction;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfUpdateRequestBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the Lag interface
 * modification.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceUpdateRunner extends FcAbstractLagInterfaceRunnerBase {
  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceUpdateRunner.class);

  private LagIfRequest request;
  private LagIfUpdateRequestBody requestBody;

  /**
   * Constructor.
   *
   * <p>
   * Set the request information from scenario as arguments
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcLagInterfaceUpdateRunner(LagIfRequest request, LagIfUpdateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.methodStart();
    try {
      RestResponseBase response = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        FcLagIf updateLagIf = checkLagIf(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.valueOf(request.getNodeId()), Integer.valueOf(request.getLagIfId()));
        sessionWrapper.beginTransaction();

        getTableResourceLock(updateLagIf.getNode(), sessionWrapper);

        String linkType;
        if (CollectionUtils.isNotEmpty(updateLagIf.getInternalLinkIfs())) {
          linkType = EcLagLinkType.INTERNAL_LINK.getMessage();
        } else {
          linkType = EcLagLinkType.OTHER.getMessage();
        }

        if (CollectionUtils.isNotEmpty(requestBody.getPhysicalIfIdList())) {
          readPhysicalIfs(sessionWrapper, requestBody.getPhysicalIfIdList());
        }

        if (CollectionUtils.isNotEmpty(requestBody.getBreakoutIfIdList())) {
          readBreakoutIfs(sessionWrapper, requestBody.getBreakoutIfIdList());
        }

        RestRequestBase base = createRequest(linkType, requestBody);

        sendRequest(base, request, updateLagIf.getNode());

        response = createResponse();

        sessionWrapper.rollback();

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  private FcLagIf checkLagIf(SessionWrapper sessionWrapper, Integer nodeType, Integer nodeId, Integer lagIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "nodeType", "nodeId", "lagIfId" },
          new Object[] { NodeType.getEnumFromCode(nodeType), nodeId, lagIfId });
      FcLagIfDao lagIfDao = new FcLagIfDao();
      FcLagIf lagIf = lagIfDao.read(sessionWrapper, nodeType, nodeId, lagIfId);
      if (lagIf == null) {
        String errMsg = "The specified Lag IF does not exist. LagIfID = " + lagIfId;
        logger.error(errMsg);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errMsg);
      }
      return lagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private void getTableResourceLock(FcNode node, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      List<FcNode> nodes = new ArrayList<>();
      nodes.add(node);
      switch (request.getFabricTypeEnum()) {
        case LEAF:
          logger.performance("start get leaf resources lock.");
          FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
          logger.performance("end get leaf resources lock.");
          break;
        case SPINE:
          logger.performance("start get spine resources lock.");
          FcDbManager.getInstance().getSpinesLock(nodes, sessionWrapper);
          logger.performance("end get spine resources lock.");
          break;
        default:

          throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, "specified node type is not Leaf and Spine.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void readPhysicalIfs(SessionWrapper sessionWrapper, List<String> physicalIfIds) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIfIds" }, new Object[] { physicalIfIds });
      FcPhysicalIfDao physicalIfDao = new FcPhysicalIfDao();

      for (String physicalIfId : physicalIfIds) {
        FcPhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.valueOf(request.getNodeId()), physicalIfId);

        if (physicalIf == null) {
          String logMsg = "The specified Physical IF does not exist. PhysicalIfId = " + physicalIfId;
          logger.error(logMsg);
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
        } else if (requestBody.getActionEnum().equals(LagIfUpdateAction.ADD_IF)) {

          if (!physicalIf.getClusterLinkIfs().isEmpty() || !physicalIf.getInternalLinkIfs().isEmpty()
              || !physicalIf.getEdgePoints().isEmpty()) {
            String logMsg = "The specified Physical IF is used. PhysicalIfId = " + physicalIfId;
            logger.error(logMsg);
            throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void readBreakoutIfs(SessionWrapper sessionWrapper, List<String> breakoutIfIds) throws MsfException {
    try {
      logger.methodStart(new String[] { "breakoutIfIds" }, new Object[] { breakoutIfIds });
      FcBreakoutIfDao breakoutIfDao = new FcBreakoutIfDao();

      for (String breakoutIfId : breakoutIfIds) {
        FcBreakoutIf breakoutIf = breakoutIfDao.read(sessionWrapper, request.getFabricTypeEnum().getCode(),
            Integer.valueOf(request.getNodeId()), breakoutIfId);

        if (breakoutIf == null) {
          String logMsg = "The specified Breakout IF does not exist. BreakoutIfId = " + breakoutIfId;
          logger.error(logMsg);
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, logMsg);
        } else if (requestBody.getActionEnum().equals(LagIfUpdateAction.ADD_IF)) {

          if (!breakoutIf.getClusterLinkIfs().isEmpty() || !breakoutIf.getInternalLinkIfs().isEmpty()
              || !breakoutIf.getEdgePoints().isEmpty()) {
            String logMsg = "The specified Breakout IF is used. BreakoutIfId = " + breakoutIfId;
            logger.error(logMsg);
            throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestRequestBase createRequest(String linkType, LagIfUpdateRequestBody requestBody) {
    try {
      logger.methodStart(new String[] { "linkType", "requestBody" }, new Object[] { linkType, requestBody });

      List<LagIfPhysicalIfUpdateEcEntity> entityList = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(requestBody.getPhysicalIfIdList())) {
        for (String ifId : requestBody.getPhysicalIfIdList()) {
          LagIfPhysicalIfUpdateEcEntity physicalIfEntity = new LagIfPhysicalIfUpdateEcEntity();
          physicalIfEntity.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          physicalIfEntity.setIfId(ifId);
          entityList.add(physicalIfEntity);
        }
      }

      if (CollectionUtils.isNotEmpty(requestBody.getBreakoutIfIdList())) {
        for (String ifId : requestBody.getBreakoutIfIdList()) {
          LagIfPhysicalIfUpdateEcEntity physicalIfEntity2 = new LagIfPhysicalIfUpdateEcEntity();
          physicalIfEntity2.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          physicalIfEntity2.setIfId(ifId);
          entityList.add(physicalIfEntity2);
        }
      }
      LagIfUpdateEcEntity entity = new LagIfUpdateEcEntity();
      entity.setLinkType(linkType);
      entity.setPhysicalIfsList(entityList);
      LagIfUpdateEcRequestBody body = new LagIfUpdateEcRequestBody();
      body.setAction(EcLagIfUpdateAction.getEnumFromLagIfUpdateAction(requestBody.getActionEnum()).getMessage());
      body.setLagIf(entity);
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(JsonUtil.toJson(body));
      return requestBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendRequest(RestRequestBase requestBase, LagIfRequest request, FcNode fcNode)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { requestBase });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase response = RestClient.sendRequest(EcRequestUri.LAG_IF_UPDATE.getHttpMethod(),
          EcRequestUri.LAG_IF_UPDATE.getUri(String.valueOf(fcNode.getEcNodeId()), request.getLagIfId()), requestBase,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(response.getResponseBody())) {
        ErrorInternalResponseBody body = JsonUtil.fromJson(response.getResponseBody(), ErrorInternalResponseBody.class,
            ErrorCode.EC_CONTROL_ERROR);
        errorCode = body.getErrorCode();
      }

      checkRestResponseHttpStatusCode(response.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse() {
    try {
      logger.methodStart();
      RestResponseBase responseBase = new RestResponseBase();
      responseBase.setHttpStatusCode(HttpStatus.OK_200);
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }
}
