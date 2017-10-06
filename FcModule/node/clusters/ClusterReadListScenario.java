package msf.fc.node.clusters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
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
import msf.fc.node.clusters.data.SwClusterReadListResponseBody;
import msf.fc.node.clusters.data.SwClusterReadOwnerDetailListResponseBody;
import msf.fc.node.clusters.data.SwClusterReadUserDetailListResponseBody;
import msf.fc.node.clusters.data.SwClusterRequest;
import msf.fc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.fc.node.clusters.data.entity.SwClusterForUserEntity;

public class ClusterReadListScenario extends AbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(ClusterReadListScenario.class);

  public ClusterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {

        ParameterCheckUtil.checkNotNull(request.getFormatEnum());

      }

      if (request.getUserType() != null) {
        if (request.getFormat() == null
            || !(RestFormatOption.DETAIL_LIST.equals(RestFormatOption.getEnumFromMessage(request.getFormat())))) {
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
              "To set the \"userType\" must be set to \"format\" = detail-list. ");
        }

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
        List<SwCluster> swClusters = swClusterDao.readList(sessionWrapper);

        responseBase = responseClusterReadListData(sessionWrapper, swClusters, request.getFormat(),
            request.getUserType());

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

  private RestResponseBase responseClusterReadListData(SessionWrapper sessionWrapper, List<SwCluster> swClusters,
      String format, String userType) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
          SwClusterReadOwnerDetailListResponseBody body = new SwClusterReadOwnerDetailListResponseBody();
          List<SwClusterForOwnerEntity> swClusterList = new ArrayList<>();
          for (SwCluster swCluster : swClusters) {
            swClusterList.add(getSwClusterForOwner(swCluster, sessionWrapper));
          }
          body.setSwClusterList(swClusterList);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          SwClusterReadUserDetailListResponseBody body = new SwClusterReadUserDetailListResponseBody();
          List<SwClusterForUserEntity> swClusterList = new ArrayList<>();
          for (SwCluster swCluster : swClusters) {
            swClusterList.add(getSwClusterForUser(swCluster, sessionWrapper));
          }
          body.setSwClusterList(swClusterList);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {
        SwClusterReadListResponseBody body = new SwClusterReadListResponseBody();
        ArrayList<String> swClusterIdList = new ArrayList<>();
        for (SwCluster swCluster : swClusters) {
          swClusterIdList.add(swCluster.getSwClusterId());
        }
        body.setSwClusterIdList(swClusterIdList);
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
