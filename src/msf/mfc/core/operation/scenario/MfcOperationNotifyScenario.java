
package msf.mfc.core.operation.scenario;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.common.data.MfcAsyncRequestsForLowerRollback;
import msf.mfc.db.dao.common.MfcAsyncRequestsForLowerDao;
import msf.mfc.db.dao.common.MfcAsyncRequestsForLowerRollbackDao;
import msf.mfc.node.clusters.MfcClusterDeleteNotifyRunner;
import msf.mfc.node.interfaces.breakoutifs.MfcBreakoutInterfaceCreateDeleteNotifyRunner;
import msf.mfc.node.interfaces.clusterlinkifs.MfcClusterLinkInterfaceCreateNotifyRunner;
import msf.mfc.node.interfaces.clusterlinkifs.MfcClusterLinkInterfaceDeleteNotifyRunner;
import msf.mfc.node.interfaces.lagifs.MfcLagInterfaceCreateNotifyRunner;
import msf.mfc.node.interfaces.lagifs.MfcLagInterfaceDeleteNotifyRunner;
import msf.mfc.node.nodes.leafs.MfcLeafNodeCreateNotifyRunner;
import msf.mfc.node.nodes.leafs.MfcLeafNodeDeleteNotifyRunner;
import msf.mfc.node.nodes.leafs.MfcLeafNodeUpdateNotifyRunner;
import msf.mfc.node.nodes.spines.MfcSpineNodeCreateNotifyRunner;
import msf.mfc.node.nodes.spines.MfcSpineNodeDeleteNotifyRunner;
import msf.mfc.slice.cps.l2cp.MfcL2CpCreateDeleteNotifyRunner;
import msf.mfc.slice.cps.l2cp.MfcL2CpCreateNotifyRunner;
import msf.mfc.slice.cps.l2cp.MfcL2CpDeleteNotifyRunner;
import msf.mfc.slice.cps.l2cp.MfcL2CpUpdateNotifyRunner;
import msf.mfc.slice.cps.l3cp.MfcL3CpCreateDeleteNotifyRunner;
import msf.mfc.slice.cps.l3cp.MfcL3CpCreateNotifyRunner;
import msf.mfc.slice.cps.l3cp.MfcL3CpDeleteNotifyRunner;
import msf.mfc.slice.cps.l3cp.MfcL3CpStaticRouteCreateDeleteNotifyRunner;
import msf.mfc.slice.cps.l3cp.MfcL3CpUpdateNotifyRunner;
import msf.mfc.slice.slices.l2slice.MfcL2SliceUpdateNotifyRunner;
import msf.mfc.slice.slices.l3slice.MfcL3SliceUpdateNotifyRunner;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestRequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.operation.scenario.data.OperationNotifyRequestBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.scenario.AbstractAsyncRunner;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the operation result notification process.
 *
 * @author NTT
 *
 */
public class MfcOperationNotifyScenario extends MfcAbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcOperationNotifyScenario.class);

  private OperationNotifyRequestBody requestBody;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public MfcOperationNotifyScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.ASYNC;
    this.operationType = operationType;
    this.systemIfType = systemIfType;

    this.restRequestType = RestRequestType.NOTIFICATION;

    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      processNotify(null, requestBody.getOperationId());
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(OperationRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      String operationId = request.getOperationId();
      ParameterCheckUtil.checkNotNullAndLength(operationId);

      OperationNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          OperationNotifyRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;
      logger.debug("Operation id=" + operationId);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Operation result notification reception process.
   *
   * @param clusterId
   *          Source cluster ID of operation result notification
   * @param lowerOperationId
   *          Lower operation ID
   * @throws MsfException
   *           When a DB error occurs
   */
  public void processNotify(String clusterId, String lowerOperationId) throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart(new String[] { "clusterId", "lowerOperationId" },
          new Object[] { clusterId, lowerOperationId });
      sessionWrapper.openSession();

      MfcAsyncRequestsForLower lower = getMfcAsyncRequestsForLowerFromDb(sessionWrapper, lowerOperationId);
      if (lower != null) {
        clusterId = String.valueOf(lower.getId().getClusterId());

        logger.debug("uppper operation id =" + lower.getAsyncRequest().getOperationId());
        Object lockObject = OperationManager.getInstance()
            .getOperationIdObject(lower.getAsyncRequest().getOperationId());
        if (lockObject == null) {

          logger.warn("upper operation lock object is not found. cluster id = {0}, lower operation id = {1}", clusterId,
              lowerOperationId);
          return;
        }
        synchronized (lockObject) {
          sessionWrapper.beginTransaction();

          updateMfcAsyncRequestsForLower(sessionWrapper, lower, requestBody.getResponse().getBody(),
              requestBody.getResponse().getStatusCode());
          sessionWrapper.commit();

          OperationManager.getInstance().cancelTimerForOperation(lower.getAsyncRequest().getOperationId(), clusterId,
              lower.getId().getRequestOperationId());

          if (!lower.getAsyncRequest().getStatusEnum().equals(AsyncProcessStatus.RUNNING)) {

            logger.warn("upper operation is not running. operation id = {0}, async process status = {1}",
                lower.getAsyncRequest().getOperationId(), lower.getAsyncRequest().getStatusEnum().getMessage());
            return;
          }
          if (checkAllLowerRequestCompleted(lower.getAsyncRequest())) {
            AbstractAsyncRunner runner = getNotifyRunner(lower.getAsyncRequest());
            if (runner != null) {

              execAsyncRunner(runner, lower.getAsyncRequest().getOperationId());

            } else {

              logger.error("notify runner is not found. request uri = {0}", lower.getAsyncRequest().getRequestUri());
              return;
            }
          }

        }
      } else {

        MfcAsyncRequestsForLowerRollback rollback = getMfcAsyncRequestsForLowerRollbackFromDb(sessionWrapper,
            lowerOperationId);
        if (rollback != null) {
          clusterId = String.valueOf(rollback.getId().getClusterId());

          logger
              .debug("uppper operation id =" + rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId());
          Object lockObject = OperationManager.getInstance()
              .getOperationIdObject(rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId());
          if (lockObject == null) {

            logger.warn("upper operation lock object is not found. cluster id = {0}, rollback operation id = {1}",
                clusterId, lowerOperationId);
            return;
          }
          synchronized (OperationManager.getInstance()
              .getOperationIdObject(rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId())) {
            sessionWrapper.beginTransaction();

            updateMfcAsyncRequestsForLowerRollback(sessionWrapper, rollback, requestBody.getResponse().getBody(),
                requestBody.getResponse().getStatusCode());
            sessionWrapper.commit();

            OperationManager.getInstance().cancelTimerForOperation(
                rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId(), clusterId,
                rollback.getRollbackOperationId());

            if (!rollback.getAsyncRequestsForLower().getAsyncRequest().getStatusEnum()
                .equals(AsyncProcessStatus.RUNNING)) {

              logger.warn("upper operation is not running. operation id = {0}, async process status = {1}",
                  rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId(),
                  rollback.getAsyncRequestsForLower().getAsyncRequest().getStatusEnum().getMessage());
              return;
            }
            if (checkAllLowerRollbackCompleted(rollback.getAsyncRequestsForLower().getAsyncRequest())) {
              AbstractAsyncRunner runner = getNotifyRunner(rollback.getAsyncRequestsForLower().getAsyncRequest());
              if (runner != null) {

                execAsyncRunner(runner, rollback.getAsyncRequestsForLower().getAsyncRequest().getOperationId());
              } else {

                logger.error("notify runner is not found. request uri = {0}",
                    rollback.getAsyncRequestsForLower().getAsyncRequest().getRequestUri());
                return;
              }
            }
          }
        } else {

          String errorMessage = MessageFormat.format(
              "target operation is not found in database. cluster id = {0}, operation id = {1}", clusterId,
              lowerOperationId);
          logger.warn(errorMessage);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
        }
      }

    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private MfcAsyncRequestsForLower getMfcAsyncRequestsForLowerFromDb(SessionWrapper sessionWrapper, String operationId)
      throws MsfException {
    try {
      logger.methodStart();
      MfcAsyncRequestsForLowerDao lowerDao = new MfcAsyncRequestsForLowerDao();
      return lowerDao.read(sessionWrapper, operationId);
    } finally {
      logger.methodEnd();
    }
  }

  private MfcAsyncRequestsForLowerRollback getMfcAsyncRequestsForLowerRollbackFromDb(SessionWrapper sessionWrapper,
      String operationId) throws MsfException {
    try {
      logger.methodStart();
      MfcAsyncRequestsForLowerRollbackDao rollbackDao = new MfcAsyncRequestsForLowerRollbackDao();
      return rollbackDao.read(sessionWrapper, operationId);
    } finally {
      logger.methodEnd();
    }
  }

  private void updateMfcAsyncRequestsForLower(SessionWrapper sessionWrapper, MfcAsyncRequestsForLower lower,
      String responseBody, int responseStatusCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "lower", "responseBody", "responseStatusCode" },
          new Object[] { sessionWrapper, lower, responseBody, responseStatusCode });

      lower.setResponseBody(responseBody);
      lower.setResponseStatusCode(responseStatusCode);
      lower.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
      MfcAsyncRequestsForLowerDao lowerDao = new MfcAsyncRequestsForLowerDao();
      lowerDao.update(sessionWrapper, lower);
    } finally {
      logger.methodEnd();
    }
  }

  private void updateMfcAsyncRequestsForLowerRollback(SessionWrapper sessionWrapper,
      MfcAsyncRequestsForLowerRollback rollback, String responseBody, int responseStatusCode) throws MsfException {
    try {
      logger.methodStart();

      rollback.setResponseBody(responseBody);
      rollback.setResponseStatusCode(responseStatusCode);
      rollback.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
      MfcAsyncRequestsForLowerRollbackDao rollbackDao = new MfcAsyncRequestsForLowerRollbackDao();
      rollbackDao.update(sessionWrapper, rollback);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkAllLowerRequestCompleted(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart();
      for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {

        if (lower.getResponseStatusCode() == null) {

          return false;
        }
      }

      Integer requestNumber = OperationManager.getInstance().getLowerRequestNumber(asyncRequest.getOperationId());
      logger.debug("requestNumber:{0}, recordNumber:{1}", requestNumber,
          asyncRequest.getAsyncRequestsForLowers().size());

      if (requestNumber == null || asyncRequest.getAsyncRequestsForLowers().size() != requestNumber) {
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkAllLowerRollbackCompleted(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart();
      int rollbackRecordNum = 0;
      for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {
        if (lower.getAsyncRequestsForLowerRollback() != null) {

          rollbackRecordNum++;

          if (lower.getAsyncRequestsForLowerRollback().getResponseStatusCode() == null) {
            return false;
          }
        }
      }

      Integer requestNumber = OperationManager.getInstance().getLowerRequestNumber(asyncRequest.getOperationId());
      logger.debug("requestNumber:{0}, rollbackRecordNumber:{1}", requestNumber, rollbackRecordNum);

      if (requestNumber == null || rollbackRecordNum != requestNumber) {
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private AbstractAsyncRunner getNotifyRunner(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      String uri = asyncRequest.getRequestUri();
      HttpMethod httpMethod = asyncRequest.getRequestMethodEnum();

      if (MfcFcRequestUri.SW_CLUSTER_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.SW_CLUSTER_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcClusterDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.BREAKOUT_IF_CREATE_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.BREAKOUT_IF_CREATE_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcBreakoutInterfaceCreateDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.CLUSTER_LINK_IF_CREATE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.CLUSTER_LINK_IF_CREATE.getHttpMethod().equals(httpMethod)) {
        return new MfcClusterLinkInterfaceCreateNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcClusterLinkInterfaceDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.LAG_IF_CREATE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.LAG_IF_CREATE.getHttpMethod().equals(httpMethod)) {
        return new MfcLagInterfaceCreateNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.LAG_IF_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.LAG_IF_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcLagInterfaceDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.LEAF_NODE_CREATE.getHttpMethod().equals(httpMethod)) {
        return new MfcLeafNodeCreateNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.LEAF_NODE_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.LEAF_NODE_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcLeafNodeDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.LEAF_NODE_UPDATE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.LEAF_NODE_UPDATE.getHttpMethod().equals(httpMethod)) {
        return new MfcLeafNodeUpdateNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.SPINE_NODE_CREATE.getHttpMethod().equals(httpMethod)) {
        return new MfcSpineNodeCreateNotifyRunner(asyncRequest.getCommonEntity());
      }

      if (MfcFcRequestUri.SPINE_NODE_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.SPINE_NODE_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcSpineNodeDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      Matcher sliceUpdateMatcher = MfcFcRequestUri.SLICE_UPDATE.getUriPattern().matcher(uri);
      if (sliceUpdateMatcher.matches() && MfcFcRequestUri.SLICE_UPDATE.getHttpMethod().equals(httpMethod)) {
        String vpnType = sliceUpdateMatcher.group(1);
        if (vpnType.equals(SliceType.L2_SLICE.getMessage())) {
          return new MfcL2SliceUpdateNotifyRunner(asyncRequest.getCommonEntity());
        } else {
          return new MfcL3SliceUpdateNotifyRunner(asyncRequest.getCommonEntity());
        }
      }

      Matcher cpCreateDeleteMatcher = MfcFcRequestUri.CP_CREATE_DELETE.getUriPattern().matcher(uri);
      if (cpCreateDeleteMatcher.matches() && MfcFcRequestUri.CP_CREATE_DELETE.getHttpMethod().equals(httpMethod)) {
        String vpnType = cpCreateDeleteMatcher.group(1);
        if (vpnType.equals(SliceType.L2_SLICE.getMessage())) {
          return new MfcL2CpCreateDeleteNotifyRunner(asyncRequest.getCommonEntity());
        } else {
          return new MfcL3CpCreateDeleteNotifyRunner(asyncRequest.getCommonEntity());
        }
      }

      Matcher cpCreateMatcher = MfcFcRequestUri.CP_CREATE.getUriPattern().matcher(uri);
      if (cpCreateMatcher.matches() && MfcFcRequestUri.CP_CREATE.getHttpMethod().equals(httpMethod)) {
        String vpnType = cpCreateMatcher.group(1);
        if (vpnType.equals(SliceType.L2_SLICE.getMessage())) {
          return new MfcL2CpCreateNotifyRunner(asyncRequest.getCommonEntity());
        } else {
          return new MfcL3CpCreateNotifyRunner(asyncRequest.getCommonEntity());
        }
      }

      Matcher cpDeleteMatcher = MfcFcRequestUri.CP_DELETE.getUriPattern().matcher(uri);
      if (cpDeleteMatcher.matches() && MfcFcRequestUri.CP_DELETE.getHttpMethod().equals(httpMethod)) {
        String vpnType = cpDeleteMatcher.group(1);
        if (vpnType.equals(SliceType.L2_SLICE.getMessage())) {
          return new MfcL2CpDeleteNotifyRunner(asyncRequest.getCommonEntity());
        } else {
          return new MfcL3CpDeleteNotifyRunner(asyncRequest.getCommonEntity());
        }
      }

      Matcher cpUpdateMatcher = MfcFcRequestUri.CP_UPDATE.getUriPattern().matcher(uri);
      if (cpUpdateMatcher.matches() && MfcFcRequestUri.CP_UPDATE.getHttpMethod().equals(httpMethod)) {
        String vpnType = cpUpdateMatcher.group(1);
        if (vpnType.equals(SliceType.L2_SLICE.getMessage())) {
          return new MfcL2CpUpdateNotifyRunner(asyncRequest.getCommonEntity());
        } else {
          return new MfcL3CpUpdateNotifyRunner(asyncRequest.getCommonEntity());
        }
      }

      if (MfcFcRequestUri.STATIC_ROUTE_CREATE_DELETE.getUriPattern().matcher(uri).matches()
          && MfcFcRequestUri.STATIC_ROUTE_CREATE_DELETE.getHttpMethod().equals(httpMethod)) {
        return new MfcL3CpStaticRouteCreateDeleteNotifyRunner(asyncRequest.getCommonEntity());
      }

      return null;
    } finally {
      logger.methodEnd();
    }
  }

  public void setRequestBody(OperationNotifyRequestBody requestBody) {
    this.requestBody = requestBody;
  }

}
