
package msf.fc.node.clusters;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterReadOwnerResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadUserResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;

/**
 * Implementation class for the SW cluster information acquisition.
 *
 * @author NTT
 *
 */
public class FcClusterReadScenario extends FcAbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterReadScenario.class);

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
  public FcClusterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (request.getUserType() != null) {

        ParameterCheckUtil.checkNotNull(request.getUserTypeEnum());
      }

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

        responseBase = responseClusterReadData(sessionWrapper, request.getUserType());

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

  private RestResponseBase responseClusterReadData(SessionWrapper sessionWrapper, String userType) throws MsfException {
    try {
      logger.methodStart(new String[] { "userType" }, new Object[] { userType });
      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        SwClusterReadOwnerResponseBody body = new SwClusterReadOwnerResponseBody();
        body.setCluster(getSwClusterForOwner(sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        SwClusterReadUserResponseBody body = new SwClusterReadUserResponseBody();
        body.setCluster(getSwClusterForUser(sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
