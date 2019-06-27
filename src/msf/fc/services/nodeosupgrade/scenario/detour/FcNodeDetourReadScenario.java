
package msf.fc.services.nodeosupgrade.scenario.detour;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourReadResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourRequest;

/**
 * Implementation class for the node detour information acquisition.
 *
 * @author NTT
 *
 */
public class FcNodeDetourReadScenario extends FcAbstractNodeDetourScenarioBase<NodeDetourRequest> {

  @SuppressWarnings("unused")
  private NodeDetourRequest request;
  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeDetourReadScenario.class);

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
  public FcNodeDetourReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(NodeDetourRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodes = fcNodeDao.readList(sessionWrapper);

        responseBase = responseNodeDetourReadData(fcNodes);

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

  private RestResponseBase responseNodeDetourReadData(List<FcNode> fcNodes) throws MsfException {
    try {
      logger.methodStart();
      List<String> leafNodeIdList = new ArrayList<>();
      List<String> spineNodeIdList = new ArrayList<>();
      for (FcNode fcNode : fcNodes) {
        if (fcNode.getDetoured()) {

          switch (fcNode.getNodeTypeEnum()) {
            case LEAF:
              leafNodeIdList.add(String.valueOf(fcNode.getNodeId()));
              break;

            case SPINE:
              spineNodeIdList.add(String.valueOf(fcNode.getNodeId()));
              break;

            default:

              throw new MsfException(ErrorCode.UNDEFINED_ERROR,
                  "Illegal parameter. nodeType = " + fcNode.getNodeType());
          }
        }
      }

      NodeDetourReadResponseBody body = new NodeDetourReadResponseBody();
      body.setLeafNodeIdList(leafNodeIdList);
      body.setSpineNodeIdList(spineNodeIdList);
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }
}
