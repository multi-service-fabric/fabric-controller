package msf.fc.node.nodes.spines;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.nodes.spines.data.SpineNodeReadDetailListResponseBody;
import msf.fc.node.nodes.spines.data.SpineNodeReadListResponseBody;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;

public class SpineNodeReadListScenario extends AbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeReadListScenario.class);

  public SpineNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {
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
        List<Node> nodes = nodeDao.readList(sessionWrapper, request.getClusterId(), NodeType.SPINE.getCode());

        if (nodes.isEmpty()) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }

        responseBase = responseSpineNodeReadListData(nodes, request.getFormat());

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

  private RestResponseBase responseSpineNodeReadListData(List<Node> nodes, String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        SpineNodeReadDetailListResponseBody body = new SpineNodeReadDetailListResponseBody();
        body.setSpineList(getSpineEntities(nodes));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        SpineNodeReadListResponseBody body = new SpineNodeReadListResponseBody();
        body.setSpineNodeIdList(getSpineNodeIdList(nodes));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
