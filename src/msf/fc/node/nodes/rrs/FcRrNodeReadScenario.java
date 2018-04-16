
package msf.fc.node.nodes.rrs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.Rr;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.rrs.data.RrNodeReadOwnerResponseBody;
import msf.mfcfc.node.nodes.rrs.data.RrNodeRequest;

/**
 * Implementation class for RR node information acquisition.
 *
 * @author NTT
 *
 */
public class FcRrNodeReadScenario extends FcAbstractRrNodeScenarioBase<RrNodeRequest> {

  private RrNodeRequest request;
  private static final MsfLogger logger = MsfLogger.getInstance(FcRrNodeReadScenario.class);

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
  public FcRrNodeReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(RrNodeRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      checkUserTypeOperator(request.getUserTypeEnum());

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
      try {
        FcConfigManager fcConfigManager = FcConfigManager.getInstance();

        Rr rr = getRrNode(fcConfigManager, Integer.parseInt(request.getNodeId()));

        responseBase = responseFcRrNodeReadData(rr, request.getUserType());

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private Rr getRrNode(FcConfigManager fcConfigManager, Integer rrNodeId) throws MsfException {

    try {
      logger.methodStart(new String[] { "fcConfigManager", "rrNodeId" }, new Object[] { fcConfigManager, rrNodeId });
      for (Rr rr : fcConfigManager.getDataConfSwClusterData().getRrs().getRr()) {
        if (rr.getRrNodeId() == rrNodeId) {
          return rr;
        }
      }

      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = rr");
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseFcRrNodeReadData(Rr rr, String userType) throws MsfException {
    try {
      logger.methodStart(new String[] { "rr", "userType" }, new Object[] { rr, userType });
      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        RrNodeReadOwnerResponseBody body = new RrNodeReadOwnerResponseBody();
        body.setRr(getRrEntity(rr));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "userType not OPERATOR.");
      }
    } finally {
      logger.methodEnd();
    }
  }
}
