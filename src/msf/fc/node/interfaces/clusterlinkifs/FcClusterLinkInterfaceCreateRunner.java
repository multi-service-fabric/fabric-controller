
package msf.fc.node.interfaces.clusterlinkifs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.node.interfaces.operation.data.OperationRequestBody;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationAddInterClusterLinkOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationClusterLinkEcEntity;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationTargetIfEcEntity;
import msf.mfcfc.common.constant.ClusterLinkIfPortStatus;
import msf.mfcfc.common.constant.EcInterfaceOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreateRequestBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreaterAsyncResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement asynchronous processing in intercluster link interface
 * registration.
 *
 * @author NTT
 *
 */
public class FcClusterLinkInterfaceCreateRunner extends FcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkInterfaceCreateRunner.class);

  private ClusterLinkIfCreateRequestBody requestBody;

  protected static final Integer CLUSTER_LINK_IF_ADDRESS_PREFIX = 30;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request Body
   */
  public FcClusterLinkInterfaceCreateRunner(ClusterLinkIfRequest request, ClusterLinkIfCreateRequestBody requestBody) {

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        String nodeId = null;
        if (requestBody.getPhysicalLink() != null) {
          nodeId = requestBody.getPhysicalLink().getNodeId();
        } else {
          nodeId = requestBody.getLagLink().getNodeId();
        }
        FcNode fcNode = getNode(sessionWrapper, fcNodeDao, NodeType.LEAF, Integer.valueOf(nodeId));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> nodes = new ArrayList<>();
        nodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();

        checkForDuplicate(sessionWrapper, fcClusterLinkIfDao, Long.valueOf(requestBody.getClusterLinkIfId()));

        OperationAddInterClusterLinkOptionEcEntity linkAddOption = createClusterLinkInterface(sessionWrapper,
            fcClusterLinkIfDao, fcNode);

        sendClusterLinkInterfaceDelete(linkAddOption);

        responseBase = responseClusterLinkInterfaceCreateAsyncData();

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

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, NodeType nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "nodeType", "nodeId" },
          new Object[] { fcNodeDao, nodeType, nodeId });
      FcNode fcNode = fcNodeDao.read(sessionWrapper, nodeType.getCode(), nodeId);
      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForDuplicate(SessionWrapper sessionWrapper, FcClusterLinkIfDao fcClusterLinkIfDao,
      Long clusterLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcClusterLinkIfDao", "clusterLinkIfId" },
          new Object[] { fcClusterLinkIfDao, clusterLinkIfId });
      FcClusterLinkIf clusterLinkIf = fcClusterLinkIfDao.read(sessionWrapper, clusterLinkIfId);
      if (clusterLinkIf != null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
            "target resouece already exist. target = clusterLinkIf");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private OperationAddInterClusterLinkOptionEcEntity createClusterLinkInterface(SessionWrapper sessionWrapper,
      FcClusterLinkIfDao fcClusterLinkIfDao, FcNode fcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcClusterLinkIfDao", "fcNode" }, new Object[] { fcClusterLinkIfDao, fcNode });
      FcClusterLinkIf fcClusterLinkIf = new FcClusterLinkIf();
      fcClusterLinkIf.setClusterLinkIfId(Long.valueOf(requestBody.getClusterLinkIfId()));
      OperationAddInterClusterLinkOptionEcEntity linkAddOption = new OperationAddInterClusterLinkOptionEcEntity();

      OperationTargetIfEcEntity targetIf = new OperationTargetIfEcEntity();
      targetIf.setNodeId(String.valueOf(fcNode.getEcNodeId()));
      if (requestBody.getPhysicalLink() != null) {
        if (requestBody.getPhysicalLink().getPhysicalIfId() != null) {
          String physicalIfId = requestBody.getPhysicalLink().getPhysicalIfId();
          FcPhysicalIf fcPhysicalIf = checkPhisicalIf(sessionWrapper, fcNode, physicalIfId);
          fcClusterLinkIf.setPhysicalIf(fcPhysicalIf);

          targetIf.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
          targetIf.setIfId(physicalIfId);
        } else if (requestBody.getPhysicalLink().getBreakoutIfId() != null) {
          String breakoutIfId = requestBody.getPhysicalLink().getBreakoutIfId();
          FcBreakoutIf fcBreakoutIf = checkBreakoutIf(sessionWrapper, fcNode, breakoutIfId);
          fcClusterLinkIf.setBreakoutIf(fcBreakoutIf);

          targetIf.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
          targetIf.setIfId(breakoutIfId);
        }
      } else {
        String lagIfId = requestBody.getLagLink().getLagIfId();
        FcLagIf fcLagIf = checkLagIf(sessionWrapper, fcNode, Integer.valueOf(lagIfId));
        fcClusterLinkIf.setLagIf(fcLagIf);

        targetIf.setIfType(InterfaceType.LAG_IF.getMessage());
        targetIf.setIfId(lagIfId);
      }
      fcClusterLinkIf.setTrafficThreshold(requestBody.getTrafficThreshold());

      linkAddOption.setTargetIf(targetIf);

      OperationClusterLinkEcEntity clusterLink = new OperationClusterLinkEcEntity();
      clusterLink.setCost(requestBody.getIgpCost());
      clusterLink.setAddress(requestBody.getIpv4Address());
      clusterLink.setPrefix(CLUSTER_LINK_IF_ADDRESS_PREFIX);
      clusterLink.setCondition((requestBody.getPortStatus() == null) ? ClusterLinkIfPortStatus.ENABLE.getStringMessage()
          : ClusterLinkIfPortStatus.getEnumFromMessage(requestBody.getPortStatus()).getStringMessage());
      linkAddOption.setClusterLink(clusterLink);

      fcClusterLinkIfDao.create(sessionWrapper, fcClusterLinkIf);
      return linkAddOption;
    } finally {
      logger.methodEnd();
    }
  }

  private FcLagIf checkLagIf(SessionWrapper sessionWrapper, FcNode fcNode, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "lagIfId" }, new Object[] { fcNode, lagIfId });
      FcLagIfDao fcLagIfDao = new FcLagIfDao();
      FcLagIf fcLagIf = fcLagIfDao.read(sessionWrapper, fcNode.getNodeType(), fcNode.getNodeId(), lagIfId);
      if (fcLagIf == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "target resource not found. parameters = lagIf");
      }
      if ((CollectionUtils.isNotEmpty(fcLagIf.getClusterLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs()))
          || (CollectionUtils.isNotEmpty(fcLagIf.getEdgePoints()))) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Specified lag If is used for other.");
      }
      return fcLagIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendClusterLinkInterfaceDelete(OperationAddInterClusterLinkOptionEcEntity linkAddOption)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "linkAddOption" }, new Object[] { linkAddOption });

      OperationRequestBody operationRequestBody = new OperationRequestBody();
      operationRequestBody.setAction(EcInterfaceOperationAction.ADD_INTER_CLUSTER_LINK.getMessage());
      operationRequestBody.setAddInterClusterLink(linkAddOption);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(operationRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.IF_OPERATION_REQUEST.getHttpMethod(),
          EcRequestUri.IF_OPERATION_REQUEST.getUri(), restRequest, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = body.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.CREATED_201, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterLinkInterfaceCreateAsyncData() {
    try {
      logger.methodStart();
      ClusterLinkIfCreaterAsyncResponseBody body = new ClusterLinkIfCreaterAsyncResponseBody();
      body.setClusterLinkIfId(requestBody.getClusterLinkIfId());
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
