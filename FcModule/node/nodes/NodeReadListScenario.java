package msf.fc.node.nodes;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.data.Rr;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.RrDao;
import msf.fc.node.nodes.data.NodeReadDetailListResponseBody;
import msf.fc.node.nodes.data.NodeReadListResponseBody;
import msf.fc.node.nodes.data.NodeRequest;

public class NodeReadListScenario extends AbstractNodeScenarioBase<NodeRequest> {

  private NodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(NodeReadListScenario.class);

  public NodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(NodeRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

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
        NodeDao nodeDao = new NodeDao();
        RrDao rrDao = new RrDao();
        List<Node> leafNodes = nodeDao.readList(sessionWrapper, request.getClusterId(), NodeType.LEAF.getCode());
        List<Node> spineNodes = nodeDao.readList(sessionWrapper, request.getClusterId(), NodeType.SPINE.getCode());
        List<Rr> rrs = rrDao.readList(sessionWrapper, request.getClusterId());

        if ((leafNodes.isEmpty() && (spineNodes.isEmpty()) && (rrs.isEmpty()))) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }
        responseBase = responseNodeReadListData(leafNodes, spineNodes, rrs, request.getFormat());

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

  private RestResponseBase responseNodeReadListData(List<Node> leafNodes, List<Node> spineNodes, List<Rr> rrs,
      String format) throws MsfException {
    try {
      logger.methodStart();

      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        NodeReadDetailListResponseBody body = new NodeReadDetailListResponseBody();
        body.setLeafEntityList(getLeafEntities(leafNodes));
        body.setSpineEntityList(getSpineEntities(spineNodes));
        body.setRrEntityList(getRrEntities(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
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
