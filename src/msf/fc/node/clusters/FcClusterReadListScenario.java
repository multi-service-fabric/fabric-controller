
package msf.fc.node.clusters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.SwClusterData;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterReadListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadOwnerDetailListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadUserDetailListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterForUserEntity;

/**
 * Implementation class for SW cluster information list acquisition.
 *
 * @author NTT
 *
 */
public class FcClusterReadListScenario extends FcAbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterReadListScenario.class);

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
  public FcClusterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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

        if (!RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum())) {
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

        responseBase = responseClusterReadListData(sessionWrapper, request.getFormat(), request.getUserType());

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

  private RestResponseBase responseClusterReadListData(SessionWrapper sessionWrapper, String format, String userType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "format", "userType" }, new Object[] { format, userType });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
          SwClusterReadOwnerDetailListResponseBody body = new SwClusterReadOwnerDetailListResponseBody();
          List<SwClusterForOwnerEntity> swClusterList = new ArrayList<>();
          swClusterList.add(getSwClusterForOwner(sessionWrapper));
          body.setClusterList(swClusterList);
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          SwClusterReadUserDetailListResponseBody body = new SwClusterReadUserDetailListResponseBody();
          List<SwClusterForUserEntity> swClusterList = new ArrayList<>();
          swClusterList.add(getSwClusterForUser(sessionWrapper));
          body.setClusterList(swClusterList);
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {
        SwClusterReadListResponseBody body = new SwClusterReadListResponseBody();
        ArrayList<String> swClusterIdList = new ArrayList<>();
        SwClusterData dataConfSwClusterData = FcConfigManager.getInstance().getDataConfSwClusterData();
        swClusterIdList.add(String.valueOf(dataConfSwClusterData.getSwCluster().getSwClusterId()));
        body.setClusterIdList(swClusterIdList);
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
