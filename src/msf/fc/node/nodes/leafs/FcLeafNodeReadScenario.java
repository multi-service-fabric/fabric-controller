
package msf.fc.node.nodes.leafs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadEcResponseBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadOwnerResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;

/**
 * Implementation class for Leaf node information acquisition.
 *
 * @author NTT
 *
 */
public class FcLeafNodeReadScenario extends FcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeReadScenario.class);

  private LeafNodeRequest request;

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
  public FcLeafNodeReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

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

        FcNode fcNode = getNode(sessionWrapper, NodeType.LEAF.getCode(), Integer.parseInt(request.getNodeId()));

        responseBase = responseFcLeafNodeReadData(fcNode, request.getUserType());

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

  private RestResponseBase responseFcLeafNodeReadData(FcNode fcNode, String userType) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "userType" }, new Object[] { fcNode, userType });

      NodeReadEcResponseBody nodeReadEcResponseBody = sendNodeRead(fcNode);
      InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(fcNode);

      if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
        LeafNodeReadOwnerResponseBody body = new LeafNodeReadOwnerResponseBody();
        body.setLeaf(getLeafOwnerEntity(fcNode, nodeReadEcResponseBody.getNode(), interfaceReadListEcResponseBody));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        LeafNodeReadResponseBody body = new LeafNodeReadResponseBody();
        body.setLeaf(getLeafUserEntity(fcNode, nodeReadEcResponseBody.getNode(), interfaceReadListEcResponseBody));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
