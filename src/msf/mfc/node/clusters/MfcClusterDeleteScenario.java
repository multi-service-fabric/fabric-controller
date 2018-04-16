
package msf.mfc.node.clusters;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterDeleteResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;

/**
 * Implementation class for the SW cluster reduction process.
 *
 * @author NTT
 *
 */
public class MfcClusterDeleteScenario extends MfcAbstractClusterScenarioBase<SwClusterRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterDeleteScenario.class);

  private SwClusterRequest request;

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
  public MfcClusterDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

      this.request = request;

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

        checkForExecClusterInfo(sessionWrapper, true);

        checkForAsyncOperationInfo(sessionWrapper);

        MfcClusterDeleteRunner mfcClusterDeleteRunner = new MfcClusterDeleteRunner(request);
        execAsyncRunner(mfcClusterDeleteRunner);

        responseBase = responseSwClusterDeleteData();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private void checkForAsyncOperationInfo(SessionWrapper sessionWrapper) throws MsfException {

    try {
      logger.methodStart();
      MfcAsyncRequestsDao mfcAsyncRequestsDao = new MfcAsyncRequestsDao();

      List<MfcAsyncRequest> mfcAsyncRequests = mfcAsyncRequestsDao.readListExecInfo(sessionWrapper);
      for (MfcAsyncRequest mfcAsyncRequest : mfcAsyncRequests) {
        if (mfcAsyncRequest.getAsyncRequestsForLowers() != null) {
          checkAsyncRequestsForLowers(mfcAsyncRequest);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkAsyncRequestsForLowers(MfcAsyncRequest mfcAsyncRequest) throws MsfException {
    try {
      logger.methodStart();
      for (MfcAsyncRequestsForLower mfcAsyncRequestsForLower : mfcAsyncRequest.getAsyncRequestsForLowers()) {
        if (request.getClusterId().equals(String.valueOf(mfcAsyncRequestsForLower.getId().getClusterId()))) {

          throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "other asynchronous operation exists.");
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSwClusterDeleteData() {
    try {
      logger.methodStart();
      SwClusterDeleteResponseBody body = new SwClusterDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
