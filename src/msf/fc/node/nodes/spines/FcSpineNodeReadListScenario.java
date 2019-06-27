
package msf.fc.node.nodes.spines;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.rest.ec.node.interfaces.data.InterfaceReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.NodeReadListEcResponseBody;
import msf.fc.rest.ec.node.nodes.data.entity.NodeEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
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
import msf.mfcfc.node.nodes.spines.data.SpineNodeReadListResponseBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeReadOwnerDetailListResponseBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;

/**
 * Implementation class for the Spine node information list acquisition.
 *
 * @author NTT
 *
 */
public class FcSpineNodeReadListScenario extends FcAbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;
  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeReadListScenario.class);

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
  public FcSpineNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodes = fcNodeDao.readList(sessionWrapper, NodeType.SPINE.getCode());

        responseBase = responseSpineNodeReadListData(fcNodes, request.getFormat(), request.getUserType());

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

  private RestResponseBase responseSpineNodeReadListData(List<FcNode> fcNodes, String format, String userType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodes", "format", "userType" }, new Object[] { fcNodes, format, userType });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {

          SpineNodeReadOwnerDetailListResponseBody body = new SpineNodeReadOwnerDetailListResponseBody();
          if (!fcNodes.isEmpty()) {
            NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
            List<SpineNodeEntity> spineNodeEntities = new ArrayList<>();

            for (FcNode fcNode : fcNodes) {

              InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(fcNode);
              for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                if (String.valueOf(fcNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                  spineNodeEntities.add(getSpainOwnerEntity(fcNode, nodeEcEntity, interfaceReadListEcResponseBody));
                  break;
                }
              }
            }
            body.setSpineList(spineNodeEntities);
          } else {

            body.setSpineList(new ArrayList<>());
          }
          return createRestResponse(body, HttpStatus.OK_200);
        }
        throw new IllegalArgumentException();
      } else {
        SpineNodeReadListResponseBody body = new SpineNodeReadListResponseBody();
        body.setSpineNodeIdList(getSpineNodeIdList(fcNodes));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
