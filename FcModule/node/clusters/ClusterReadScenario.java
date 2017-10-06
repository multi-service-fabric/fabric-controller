package msf.fc.node.clusters;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestUserTypeOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.node.clusters.data.SwClusterReadOwnerResponseBody;
import msf.fc.node.clusters.data.SwClusterReadUserResponseBody;
import msf.fc.node.clusters.data.SwClusterRequest;

public class ClusterReadScenario extends AbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(ClusterReadScenario.class);

  public ClusterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

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
        SwClusterDao swClusterDao = new SwClusterDao();
        SwCluster swCluster = getSwCluster(sessionWrapper, swClusterDao, request.getClusterId());

        responseBase = responseClusterReadData(sessionWrapper, swCluster, request.getUserType());

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

  private SwCluster getSwCluster(SessionWrapper sessionWrapper, SwClusterDao swClusterDao, String swClusterId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterDao", "swClusterId" },
          new Object[] { sessionWrapper, swClusterDao, swClusterId });
      SwCluster swCluster = swClusterDao.read(sessionWrapper, swClusterId);
      if (swCluster == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
      return swCluster;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterReadData(SessionWrapper sessionWrapper, SwCluster swCluster, String userType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster", "userType" }, new Object[] { swCluster, userType });
      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        SwClusterReadOwnerResponseBody body = new SwClusterReadOwnerResponseBody();
        body.setSwCluster(getSwClusterForOwner(swCluster, sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        SwClusterReadUserResponseBody body = new SwClusterReadUserResponseBody();
        body.setSwCluster(getSwClusterForUser(swCluster, sessionWrapper));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
