
package msf.mfc.node.nodes;

import java.util.List;
import java.util.regex.Matcher;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.AbstractNodeScenarioBase;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Abstract class to implement the common process of node-related processing in
 * configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractNodeScenarioBase<T extends RestRequestBase> extends AbstractNodeScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractNodeScenarioBase.class);

  protected void checkForExecNodeInfo(SessionWrapper sessionWrapper, HttpMethod method, String clusterId,
      NodeType nodeType, String nodeId) throws MsfException {

    try {
      logger.methodStart(new String[] { "method", "clusterId", "nodeType", "nodeId" },
          new Object[] { method, clusterId, nodeType, nodeId });
      MfcAsyncRequestsDao mfcAsyncRequestsDao = new MfcAsyncRequestsDao();
      List<MfcAsyncRequest> mfcAsyncRequests = mfcAsyncRequestsDao.readListExecNodeInfo(sessionWrapper);
      for (MfcAsyncRequest mfcAsyncRequest : mfcAsyncRequests) {

        if (!mfcAsyncRequest.getOperationId().equals(getOperationId())) {
          switch (method) {
            case POST:

              throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "node regist status check error.");

            case DELETE:

              checkForExecDeleteNodeInfo(mfcAsyncRequest, clusterId, nodeType, nodeId);
              break;

            case PUT:

              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, "node update status check error.");

            default:

              throw new MsfException(ErrorCode.UNDEFINED_ERROR, "method = " + method);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForExecDeleteNodeInfo(MfcAsyncRequest mfcAsyncRequest, String clusterId, NodeType nodeType,
      String nodeId) throws MsfException {
    try {

      logger.methodStart(new String[] { "mfcAsyncRequest", "clusterId", "nodeType", "nodeId" },
          new Object[] { mfcAsyncRequest, clusterId, nodeType, nodeId });
      if (HttpMethod.getEnumFromMessage(mfcAsyncRequest.getRequestMethod()).equals(HttpMethod.POST)) {
        String asyncClusterId = null;
        NodeType asyncNodeType = null;
        String asyncNodeId = null;
        Matcher matcher = MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri());
        if (matcher.matches()) {

          asyncClusterId = matcher.group(1);

          asyncNodeType = NodeType.LEAF;

          LeafNodeCreateRequestBody requestBody = JsonUtil.fromJson(mfcAsyncRequest.getRequestBody(),
              LeafNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
          asyncNodeId = requestBody.getNodeId();
        } else {
          matcher = MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(mfcAsyncRequest.getRequestUri());
          if (matcher.matches()) {

            asyncClusterId = matcher.group(1);

            asyncNodeType = NodeType.SPINE;

            SpineNodeCreateRequestBody requestBody = JsonUtil.fromJson(mfcAsyncRequest.getRequestBody(),
                SpineNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
            asyncNodeId = requestBody.getNodeId();
          } else {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "cluster status check error.");
          }
        }
        if ((!asyncClusterId.equals(clusterId)) || (!asyncNodeType.equals(nodeType)) || (!asyncNodeId.equals(nodeId))) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "node delete status check error.");
        }
      } else {

        throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "node delete status check error.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected MfcSwCluster getSwCluster(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao,
      Integer swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao", "swClusterId" },
          new Object[] { mfcSwClusterDao, swClusterId });
      MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(sessionWrapper, swClusterId);
      if (mfcSwCluster == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
      return mfcSwCluster;
    } finally {
      logger.methodEnd();
    }
  }

}
