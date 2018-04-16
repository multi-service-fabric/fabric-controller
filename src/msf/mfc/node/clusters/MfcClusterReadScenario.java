
package msf.mfc.node.clusters;

import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
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
import msf.mfcfc.node.clusters.data.SwClusterRequest;

/**
 * Implementation class for SW cluster information acquisition.
 *
 * @author NTT
 *
 */
public class MfcClusterReadScenario extends MfcAbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterReadScenario.class);

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
  public MfcClusterReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

        MfcSwCluster mfcSwCluster = getSwCluster(sessionWrapper, mfcSwClusterDao,
            Integer.valueOf(request.getClusterId()));

        responseBase = responseClusterReadData(mfcSwCluster);

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

  private MfcSwCluster getSwCluster(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao, Integer swClusterId)
      throws MsfException {
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

  private RestResponseBase responseClusterReadData(MfcSwCluster mfcSwCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwCluster" }, new Object[] { mfcSwCluster });

      boolean isOwner = RestUserTypeOption.OPERATOR
          .equals(RestUserTypeOption.getEnumFromMessage(request.getUserType()));
      return sendSwClusterRead(mfcSwCluster, request, isOwner);
    } finally {
      logger.methodEnd();
    }
  }

}
