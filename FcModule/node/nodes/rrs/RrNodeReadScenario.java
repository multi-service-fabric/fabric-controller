package msf.fc.node.nodes.rrs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Rr;
import msf.fc.common.data.RrPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.RrDao;
import msf.fc.node.nodes.rrs.data.RrNodeReadResponseBody;
import msf.fc.node.nodes.rrs.data.RrNodeRequest;

public class RrNodeReadScenario extends AbstractRrNodeScenarioBase<RrNodeRequest> {

  private RrNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(RrNodeReadScenario.class);

  public RrNodeReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(RrNodeRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkNodeId(request.getNodeId());

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
        Rr rr = getRrNode(sessionWrapper, rrDao, request.getClusterId(), Integer.parseInt(request.getNodeId()));

        responseBase = responseRrNodeReadData(rr);

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

  private Rr getRrNode(SessionWrapper sessionWrapper, RrDao rrDao, String swClusterId, Integer rrNodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "rrDao", "swClusterId", "rrNodeId" },
          new Object[] { sessionWrapper, rrDao, swClusterId, rrNodeId });

      RrPK rrPk = new RrPK();
      rrPk.setSwClusterId(swClusterId);
      rrPk.setRrNodeId(rrNodeId);
      Rr rr = rrDao.read(sessionWrapper, rrPk);
      if (rr == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = rr");
      }
      return rr;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseRrNodeReadData(Rr rr) {
    try {
      logger.methodStart(new String[] { "rr" }, new Object[] { rr });
      RrNodeReadResponseBody body = new RrNodeReadResponseBody();
      body.setRr(getRrEntity(rr));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
