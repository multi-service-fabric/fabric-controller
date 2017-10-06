package msf.fc.node.nodes.rrs;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Rr;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.RrDao;
import msf.fc.node.nodes.rrs.data.RrNodeReadDetailListResponseBody;
import msf.fc.node.nodes.rrs.data.RrNodeReadListResponseBody;
import msf.fc.node.nodes.rrs.data.RrNodeRequest;

public class RrNodeReadListScenario extends AbstractRrNodeScenarioBase<RrNodeRequest> {

  private RrNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(RrNodeReadListScenario.class);

  public RrNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(RrNodeRequest request) throws MsfException {

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
        RrDao rrDao = new RrDao();
        List<Rr> rrs = rrDao.readList(sessionWrapper, request.getClusterId());

        if (rrs.isEmpty()) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }

        responseBase = responseRrNodeReadListData(rrs, request.getFormat());

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

  private RestResponseBase responseRrNodeReadListData(List<Rr> rrs, String format) {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        RrNodeReadDetailListResponseBody body = new RrNodeReadDetailListResponseBody();
        body.setRrList(getRrEntities(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        RrNodeReadListResponseBody body = new RrNodeReadListResponseBody();
        body.setRrNodeIdList(getRrNodeIdList(rrs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
