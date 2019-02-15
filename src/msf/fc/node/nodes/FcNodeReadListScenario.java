
package msf.fc.node.nodes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.data.Rr;
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
import msf.mfcfc.node.nodes.data.NodeReadListResponseBody;
import msf.mfcfc.node.nodes.data.NodeReadOwnerDetailListResponseBody;
import msf.mfcfc.node.nodes.data.NodeReadUserDetailListResponseBody;
import msf.mfcfc.node.nodes.data.NodeRequest;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForOwnerEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeForUserEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;

/**
 * Implementation class for the node information list acquisition.
 *
 * @author NTT
 *
 */
public class FcNodeReadListScenario extends FcAbstractNodeScenarioBase<NodeRequest> {

  private NodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeReadListScenario.class);

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
  public FcNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(NodeRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }
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
        FcNodeDao fcNodeDao = new FcNodeDao();
        FcConfigManager fcConfigManager = FcConfigManager.getInstance();

        List<FcNode> leafNodes = fcNodeDao.readList(sessionWrapper, NodeType.LEAF.getCode());
        List<FcNode> spineNodes = fcNodeDao.readList(sessionWrapper, NodeType.SPINE.getCode());
        List<Rr> rrs = fcConfigManager.getDataConfSwClusterData().getRrs().getRr();

        responseBase = responseNodeReadListData(leafNodes, spineNodes, rrs, request.getFormat(), request.getUserType());

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

  private RestResponseBase responseNodeReadListData(List<FcNode> leafNodes, List<FcNode> spineNodes, List<Rr> rrs,
      String format, String userType) throws MsfException {
    try {
      logger.methodStart(new String[] { "leafNodes", "spineNodes", "rrs", "format", "userType" },
          new Object[] { leafNodes, spineNodes, rrs, format, userType });

      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
          NodeReadOwnerDetailListResponseBody body = new NodeReadOwnerDetailListResponseBody();
          if ((CollectionUtils.isNotEmpty(leafNodes)) || (CollectionUtils.isNotEmpty(spineNodes))
              || (CollectionUtils.isNotEmpty(rrs))) {
            NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
            List<LeafNodeForOwnerEntity> leafNodeForOwnerEntities = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(leafNodes)) {
              for (FcNode leafNode : leafNodes) {

                InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(leafNode);
                for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                  if (String.valueOf(leafNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                    leafNodeForOwnerEntities
                        .add(getLeafOwnerEntity(leafNode, nodeEcEntity, interfaceReadListEcResponseBody));
                    break;
                  }
                }
              }
              body.setLeafList(leafNodeForOwnerEntities);
            } else {

              body.setLeafList(new ArrayList<>());
            }

            if (CollectionUtils.isNotEmpty(spineNodes)) {
              List<SpineNodeEntity> spineNodeEntities = new ArrayList<>();
              for (FcNode spineNode : spineNodes) {

                InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(spineNode);
                for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                  if (String.valueOf(spineNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                    spineNodeEntities
                        .add(getSpainOwnerEntity(spineNode, nodeEcEntity, interfaceReadListEcResponseBody));
                    break;
                  }
                }
              }
              body.setSpineList(spineNodeEntities);
            } else {

              body.setSpineList(new ArrayList<>());
            }

            body.setRrList(getRrEntities(rrs));

            return createRestResponse(body, HttpStatus.OK_200);
          } else {

            body.setLeafList(new ArrayList<>());
            body.setSpineList(new ArrayList<>());
            body.setRrList(new ArrayList<>());
            return createRestResponse(body, HttpStatus.OK_200);
          }
        } else {

          NodeReadUserDetailListResponseBody body = new NodeReadUserDetailListResponseBody();
          if ((CollectionUtils.isNotEmpty(leafNodes)) || (CollectionUtils.isNotEmpty(spineNodes))
              || (CollectionUtils.isNotEmpty(rrs))) {
            if (!leafNodes.isEmpty()) {
              NodeReadListEcResponseBody nodeReadListEcResponseBody = sendNodeReadList();
              List<LeafNodeForUserEntity> leafNodeForUserEntities = new ArrayList<>();

              for (FcNode fcNode : leafNodes) {

                InterfaceReadListEcResponseBody interfaceReadListEcResponseBody = sendInterfaceInfoReadList(fcNode);
                for (NodeEcEntity nodeEcEntity : nodeReadListEcResponseBody.getNodeList()) {
                  if (String.valueOf(fcNode.getEcNodeId()).equals(nodeEcEntity.getNodeId())) {
                    leafNodeForUserEntities
                        .add(getLeafUserEntity(fcNode, nodeEcEntity, interfaceReadListEcResponseBody));
                    break;
                  }
                }
              }
              body.setLeafList(leafNodeForUserEntities);
            } else {

              body.setLeafList(new ArrayList<>());
            }

            body.setSpineList(new ArrayList<>());
            body.setRrList(new ArrayList<>());
            return createRestResponse(body, HttpStatus.OK_200);
          } else {

            body.setLeafList(new ArrayList<>());
            body.setSpineList(new ArrayList<>());
            body.setRrList(new ArrayList<>());
            return createRestResponse(body, HttpStatus.OK_200);
          }
        }
      } else {
        NodeReadListResponseBody body = new NodeReadListResponseBody();
        body.setLeafNodeIdList(getLeafNodeIdList(leafNodes));
        body.setSpineNodeIdList(getSpineNodeIdList(spineNodes));
        body.setRrNodeIdList(getRrNodeIdList(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
