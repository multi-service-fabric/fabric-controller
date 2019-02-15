
package msf.fc.node.interfaces.clusterlinkifs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
import msf.fc.rest.ec.node.interfaces.operation.data.OperationRequestBody;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationDelInterClusterLinkOptionEcEntity;
import msf.fc.rest.ec.node.interfaces.operation.data.entity.OperationTargetIfEcEntity;
import msf.mfcfc.common.constant.EcInterfaceOperationAction;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to implement the asynchronous processing in the inter-cluster link
 * interface deletion.
 *
 * @author NTT
 *
 */
public class FcClusterLinkInterfaceDeleteRunner extends FcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkInterfaceDeleteRunner.class);

  private ClusterLinkIfRequest request;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   */
  public FcClusterLinkInterfaceDeleteRunner(ClusterLinkIfRequest request) {

    this.request = request;

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();

        FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();

        FcClusterLinkIf clusterLinkIf = getClusterLinkIf(sessionWrapper, fcClusterLinkIfDao,
            Long.valueOf(request.getClusterLinkIfId()));
        FcNode fcNode = getNodeForClusterLinkIf(clusterLinkIf);

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> nodes = new ArrayList<>();
        nodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        OperationDelInterClusterLinkOptionEcEntity linkDeleteOption = getClusterLinkInterfaceDeleteEcData(clusterLinkIf,
            fcNode);

        fcClusterLinkIfDao.delete(sessionWrapper, Long.valueOf(request.getClusterLinkIfId()));

        sendClusterLinkInterfaceDelete(linkDeleteOption);

        responseBase = responseClusterLinkInterfaceDeleteAsyncData();

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

  private FcClusterLinkIf getClusterLinkIf(SessionWrapper sessionWrapper, FcClusterLinkIfDao fcClusterLinkIfDao,
      Long clusterLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcClusterLinkIfDao", "clusterLinkIfId" },
          new Object[] { fcClusterLinkIfDao, clusterLinkIfId });
      FcClusterLinkIf fcClusterLinkIf = fcClusterLinkIfDao.read(sessionWrapper, clusterLinkIfId);
      if (fcClusterLinkIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = clusterLinkIf");
      }
      return fcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNodeForClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIf" }, new Object[] { clusterLinkIf });
      FcNode fcNode = null;

      if (clusterLinkIf.getPhysicalIf() != null) {
        fcNode = clusterLinkIf.getPhysicalIf().getNode();
      } else if (clusterLinkIf.getBreakoutIf() != null) {
        fcNode = clusterLinkIf.getBreakoutIf().getNode();
      } else {
        fcNode = clusterLinkIf.getLagIf().getNode();
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationDelInterClusterLinkOptionEcEntity getClusterLinkInterfaceDeleteEcData(FcClusterLinkIf clusterLinkIf,
      FcNode fcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIf", "fcNode" }, new Object[] { clusterLinkIf, fcNode });
      OperationDelInterClusterLinkOptionEcEntity linkDeleteOption = new OperationDelInterClusterLinkOptionEcEntity();

      OperationTargetIfEcEntity targetIf = new OperationTargetIfEcEntity();
      targetIf.setNodeId(String.valueOf(fcNode.getEcNodeId()));
      if (clusterLinkIf.getPhysicalIf() != null) {
        targetIf.setIfType(InterfaceType.PHYSICAL_IF.getMessage());
        targetIf.setIfId(clusterLinkIf.getPhysicalIf().getPhysicalIfId());
      } else if (clusterLinkIf.getBreakoutIf() != null) {
        targetIf.setIfType(InterfaceType.BREAKOUT_IF.getMessage());
        targetIf.setIfId(clusterLinkIf.getBreakoutIf().getBreakoutIfId());
      } else {
        targetIf.setIfType(InterfaceType.LAG_IF.getMessage());
        targetIf.setIfId(String.valueOf(clusterLinkIf.getLagIf().getLagIfId()));
      }
      linkDeleteOption.setTargetIf(targetIf);
      return linkDeleteOption;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendClusterLinkInterfaceDelete(OperationDelInterClusterLinkOptionEcEntity linkDeleteOption)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "linkDeleteOption" }, new Object[] { linkDeleteOption });

      OperationRequestBody operationRequestBody = new OperationRequestBody();
      operationRequestBody.setAction(EcInterfaceOperationAction.DEL_INTER_CLUSTER_LINK.getMessage());
      operationRequestBody.setDelInterClusterLink(linkDeleteOption);

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

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.NO_CONTENT_204, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterLinkInterfaceDeleteAsyncData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
