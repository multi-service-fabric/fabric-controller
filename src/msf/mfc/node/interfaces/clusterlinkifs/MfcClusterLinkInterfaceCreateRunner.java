
package msf.mfc.node.interfaces.clusterlinkifs;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcClusterLinkIfId;
import msf.mfc.common.util.MfcIpAddressUtil;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfIdDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.node.MfcNodeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreateRequestBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreaterAsyncResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to implement the asynchronous processing in inter-cluster link
 * interface registration.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceCreateRunner extends MfcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceCreateRunner.class);

  private ClusterLinkIfRequest request;
  private ClusterLinkIfCreateRequestBody requestBody;

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
  public MfcClusterLinkInterfaceCreateRunner(ClusterLinkIfRequest request, ClusterLinkIfCreateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to cluster link interface increasing process.");
      synchronized (MfcNodeManager.getInstance().getMfcClusterLinkCreateLockObject()) {
        logger.performance("end wait to cluster link interface increasing process.");

        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        String ipv4Address = null;
        Integer clusterLinkInterfaceId = null;

        try {
          sessionWrapper.openSession();
          MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

          Integer swClusterId = Integer.valueOf(request.getClusterId());
          getSwCluster(sessionWrapper, mfcSwClusterDao, swClusterId);

          String ipv4ReqAddr = requestBody.getIpv4Address();
          if (ipv4ReqAddr != null) {
            ipv4Address = ipv4ReqAddr;
          } else {

            ipv4Address = MfcIpAddressUtil.getClusterIpAddress(swClusterId,
                Integer.valueOf(requestBody.getOppositeClusterId()));
          }

          sessionWrapper.beginTransaction();
          MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();

          List<MfcClusterLinkIf> mfcClusterLinkIfs = mfcClusterLinkIfDao.readList(sessionWrapper);
          Set<Integer> clusterLinkIfIdSet = createClusterLinkIfIdSet(mfcClusterLinkIfs);
          clusterLinkInterfaceId = getNextClusterLinkInterfaceId(sessionWrapper, clusterLinkIfIdSet);

          sessionWrapper.commit();
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } finally {
          sessionWrapper.closeSession();
        }

        responseBase = sendClusterLinkInterfaceCreate(ipv4Address, clusterLinkInterfaceId);

        return responseBase;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Set<Integer> createClusterLinkIfIdSet(List<MfcClusterLinkIf> mfcClusterLinkIfs) {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIfs" }, new Object[] { mfcClusterLinkIfs });
      Set<Integer> clusterLinkIfIdSet = new HashSet<>();
      for (MfcClusterLinkIf mfcClusterLinkIf : mfcClusterLinkIfs) {
        clusterLinkIfIdSet.add(mfcClusterLinkIf.getClusterLinkIfId());
      }
      return clusterLinkIfIdSet;
    } finally {
      logger.methodEnd();
    }
  }

  private int getNextClusterLinkInterfaceId(SessionWrapper sessionWrapper, Set<Integer> clusterLinkIfIdSet)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIfIdSet" }, new Object[] { clusterLinkIfIdSet });
      MfcClusterLinkIfIdDao mfcClusterLinkIfIdDao = new MfcClusterLinkIfIdDao();
      List<MfcClusterLinkIfId> mfcClusterLinkIfIds = mfcClusterLinkIfIdDao.readList(sessionWrapper);

      int firstNextId = mfcClusterLinkIfIds.get(0).getNextId();

      int targetNextId = firstNextId;
      logger.performance("start get available cluster link interface id.");
      do {
        if (clusterLinkIfIdSet.contains(targetNextId)) {

          targetNextId++;

          if (!checkClusterLinkInterfaceIdRange(targetNextId)) {

            targetNextId = 1;
          }
        } else {

          updateClusterLinkInterfaceId(sessionWrapper, mfcClusterLinkIfIdDao, targetNextId + 1, firstNextId);
          logger.performance("end get available cluster link interface id.");
          return targetNextId;
        }

      } while (targetNextId != firstNextId);
      logger.performance("end get available cp id.");

      String logMsg = MessageFormat.format("threre is no available cluster link interface id. firstCheckId = {0}",
          firstNextId);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  private void updateClusterLinkInterfaceId(SessionWrapper sessionWrapper, MfcClusterLinkIfIdDao mfcClusterLinkIfIdDao,
      int nextId, int firstNextId) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIfIdDao", "nextId", "firstNextId" },
          new Object[] { mfcClusterLinkIfIdDao, nextId, firstNextId });

      if (!checkClusterLinkInterfaceIdRange(nextId)) {

        nextId = 1;
      }

      MfcClusterLinkIfId mfcClusterLinkIfId = new MfcClusterLinkIfId();
      mfcClusterLinkIfId.setNextId(nextId);
      mfcClusterLinkIfIdDao.create(sessionWrapper, mfcClusterLinkIfId);
      mfcClusterLinkIfIdDao.delete(sessionWrapper, firstNextId);
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkClusterLinkInterfaceIdRange(int checkTargetId) {
    try {
      logger.methodStart(new String[] { "checkTargetId" }, new Object[] { checkTargetId });
      if (checkTargetId >= 0 && checkTargetId < Integer.MAX_VALUE) {
        return true;
      } else {
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendClusterLinkInterfaceCreate(String ipv4Address, Integer clusterLinkInterfaceId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "ipv4_address", "clusterLinkInterfaceId" },
          new Object[] { ipv4Address, clusterLinkInterfaceId });

      requestBody.setClusterLinkIfId(String.valueOf(clusterLinkInterfaceId));
      requestBody.setIpv4Address(ipv4Address);
      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(requestBody));

      SwCluster swCluster = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(request.getClusterId())).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestRequestData requestData = new RestRequestData(Integer.valueOf(request.getClusterId()), fcControlAddress,
          fcControlPort, MfcFcRequestUri.CLUSTER_LINK_IF_CREATE.getHttpMethod(),
          MfcFcRequestUri.CLUSTER_LINK_IF_CREATE.getUri(request.getClusterId()), restRequest, HttpStatus.ACCEPTED_202);
      RestResponseData restResponseData = sendRequest(requestData, RequestType.REQUEST);

      if (restResponseData.getResponse().getHttpStatusCode() != HttpStatus.ACCEPTED_202) {
        ClusterLinkIfCreaterAsyncResponseBody body = JsonUtil.fromJson(restResponseData.getResponse().getResponseBody(),
            ClusterLinkIfCreaterAsyncResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseData.getResponse().getHttpStatusCode(), body.getErrorCode());
        logger.error(errorMsg);
        return createErrorResponse(restResponseData);
      }

      return restResponseData.getResponse();
    } finally {
      logger.methodEnd();
    }
  }
}
