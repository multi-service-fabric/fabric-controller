
package msf.fc.node.nodes.spines;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
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
import msf.mfcfc.node.nodes.spines.data.SpineNodeReadOwnerResponseBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;

/**
 * Implementation class for Spine node information acquisition.
 *
 * @author NTT
 *
 */
public class FcSpineNodeReadScenario extends FcAbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;
  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeReadListScenario.class);

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
  public FcSpineNodeReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {

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
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        FcNode fcNode = getNode(sessionWrapper, NodeType.SPINE.getCode(), Integer.parseInt(request.getNodeId()));

        responseBase = responseSpineNodeReadData(fcNode);

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

  private RestResponseBase responseSpineNodeReadData(FcNode fcNode) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode" }, new Object[] { fcNode });
      NodeReadEcResponseBody nodeReadEcResponseBody = sendNodeRead(fcNode);
      InterfaceReadListEcResponseBody interfaceInfoReadEcResponseBody = sendInterfaceInfoReadList(fcNode);

      SpineNodeReadOwnerResponseBody body = new SpineNodeReadOwnerResponseBody();
      body.setSpine(getSpainOwnerEntity(fcNode, nodeReadEcResponseBody.getNode(), interfaceInfoReadEcResponseBody));
      return createRestResponse(body, HttpStatus.OK_200);

    } finally {
      logger.methodEnd();
    }
  }
}
