
package msf.fc.node.interfaces.breakoutifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.db.dao.clusters.FcBreakoutIfDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.BreakoutIfReadEcResponseBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfReadResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;

/**
 * Implementation class for breakout interface information acquisition.
 *
 * @author NTT
 *
 */
public class FcBreakoutInterfaceReadScenario extends FcAbstractBreakoutInterfaceScenarioBase<BreakoutIfRequest> {

  private BreakoutIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutInterfaceReadScenario.class);

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
  public FcBreakoutInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(BreakoutIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))
          && !NodeType.SPINE.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }

      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkIdSpecifiedByUri(request.getBreakoutIfId());

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
        FcBreakoutIfDao fcBreakoutIfDao = new FcBreakoutIfDao();

        FcBreakoutIf fcBreakoutIf = getBreakoutInterface(sessionWrapper, fcBreakoutIfDao,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(), Integer.parseInt(request.getNodeId()),
            request.getBreakoutIfId());

        responseBase = responseBreakoutInterfaceReadData(fcBreakoutIf);

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

  private FcBreakoutIf getBreakoutInterface(SessionWrapper sessionWrapper, FcBreakoutIfDao fcBreakoutIfDao,
      Integer nodeType, Integer nodeId, String breakoutIfId) throws MsfException {
    try {
      logger.methodStart();
      FcBreakoutIf fcBreakoutIf = fcBreakoutIfDao.read(sessionWrapper, nodeType, nodeId, breakoutIfId);
      if (fcBreakoutIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = fcBreakoutIf");
      }
      return fcBreakoutIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseBreakoutInterfaceReadData(FcBreakoutIf fcBreakoutIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcBreakoutIf" }, new Object[] { fcBreakoutIf });
      BreakoutIfReadResponseBody body = new BreakoutIfReadResponseBody();

      BreakoutIfReadEcResponseBody breakoutIfReadEcResponseBody = sendBreakoutInterfaceRead(fcBreakoutIf.getNode(),
          request.getBreakoutIfId());

      body.setBreakoutIf(getBreakoutIfData(fcBreakoutIf, breakoutIfReadEcResponseBody.getBreakoutIf()));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }
}
