
package msf.fc.node.interfaces.clusterlinkifs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfReadListResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.entity.ClusterLinkIfEntity;

/**
 * Implementation class for the inter-cluster link interface information list
 * acquisition.
 *
 * @author NTT
 */
public class FcClusterLinkInterfaceReadListScenario
    extends FcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {
  private ClusterLinkIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkInterfaceReadListScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments.
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcClusterLinkInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
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
        FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();

        List<FcClusterLinkIf> fcClusterLinkIfs = fcClusterLinkIfDao.readList(sessionWrapper);

        responseBase = responseClusterLinkInterfaceReadListData(fcClusterLinkIfs, request.getFormat());
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

  private RestResponseBase responseClusterLinkInterfaceReadListData(List<FcClusterLinkIf> fcClusterLinkIfs,
      String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        ClusterLinkIfReadDetailListResponseBody body = new ClusterLinkIfReadDetailListResponseBody();
        body.setClusterLinkIfIdList(getClusterLinkInterfaces(fcClusterLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        ClusterLinkIfReadListResponseBody body = new ClusterLinkIfReadListResponseBody();
        body.setClusterLinkIfIdList(getClusterLinkIfIds(fcClusterLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<ClusterLinkIfEntity> getClusterLinkInterfaces(List<FcClusterLinkIf> fcClusterLinkIfs)
      throws MsfException {
    try {
      logger.methodStart();
      List<ClusterLinkIfEntity> clusterLinkIfs = new ArrayList<>();
      for (FcClusterLinkIf fcClusterLinkIf : fcClusterLinkIfs) {
        clusterLinkIfs.add(getClusterLinkInterfaceEntity(fcClusterLinkIf));
      }

      return clusterLinkIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getClusterLinkIfIds(List<FcClusterLinkIf> fcClusterLinkIfs) {
    try {
      logger.methodStart();
      List<String> clusterLinkIfIds = new ArrayList<>();
      for (FcClusterLinkIf fcClusterLinkIf : fcClusterLinkIfs) {
        clusterLinkIfIds.add(String.valueOf(fcClusterLinkIf.getClusterLinkIfId()));
      }
      return clusterLinkIfIds;
    } finally {
      logger.methodEnd();
    }
  }
}
