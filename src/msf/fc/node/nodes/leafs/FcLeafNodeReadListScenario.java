
package msf.fc.node.nodes.leafs;

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
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadOwnerDetailListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadUserDetailListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForUserEntity;

/**
 * Implementation class for Leaf node information list acquisition.
 *
 * @author NTT
 *
 */
public class FcLeafNodeReadListScenario extends FcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeReadListScenario.class);

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
  public FcLeafNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

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
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodes = fcNodeDao.readList(sessionWrapper, NodeType.LEAF.getCode());

        responseBase = responseFcLeafNodeReadListData(fcNodes, request.getFormat(), request.getUserType(),
            request.getNodeId());

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

  private RestResponseBase responseFcLeafNodeReadListData(List<FcNode> fcNodes, String format, String userType,
      String nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNode", "format", "userType", "nodeId" },
          new Object[] { fcNodes, format, userType, nodeId });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {

          LeafNodeReadOwnerDetailListResponseBody body = new LeafNodeReadOwnerDetailListResponseBody();
          if (!fcNodes.isEmpty()) {
            NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
            List<LeafNodeForOwnerEntity> leafNodeForOwnerEntities = new ArrayList<>();

            for (FcNode fcNode : fcNodes) {

              InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(fcNode);
              for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                if (String.valueOf(fcNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                  leafNodeForOwnerEntities
                      .add(getLeafOwnerEntity(fcNode, nodeEcEntity, interfaceReadListEcResponseBody));
                  break;
                }
              }
            }
            body.setLeafList(leafNodeForOwnerEntities);
          } else {

            body.setLeafList(new ArrayList<>());
          }
          return createRestResponse(body, HttpStatus.OK_200);
        } else {

          LeafNodeReadUserDetailListResponseBody body = new LeafNodeReadUserDetailListResponseBody();
          if (!fcNodes.isEmpty()) {
            NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
            List<LeafNodeForUserEntity> leafNodeForUserEntities = new ArrayList<>();

            for (FcNode fcNode : fcNodes) {

              InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(fcNode);
              for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                if (String.valueOf(fcNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                  leafNodeForUserEntities.add(getLeafUserEntity(fcNode, nodeEcEntity, interfaceReadListEcResponseBody));
                  break;
                }
              }
            }
            body.setLeafList(leafNodeForUserEntities);
          } else {

            body.setLeafList(new ArrayList<>());
          }
          return createRestResponse(body, HttpStatus.OK_200);
        }
      } else {
        LeafNodeReadListResponseBody body = new LeafNodeReadListResponseBody();
        body.setLeafNodeIdList(getLeafNodeIdList(fcNodes));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
