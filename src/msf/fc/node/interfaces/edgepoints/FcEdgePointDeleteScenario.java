
package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcNode;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcEdgePointDao;
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
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;

/**
 * Implementation class for Edge-Point interface deletion.
 *
 * @author NTT
 *
 */
public class FcEdgePointDeleteScenario extends FcAbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEdgePointDeleteScenario.class);

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
  public FcEdgePointDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getClusterId());
      ParameterCheckUtil.checkNumericId(request.getEdgePointId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

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

        FcNode fcNode = getNodeByEdgePoint(sessionWrapper, fcNodeDao, Integer.parseInt(request.getEdgePointId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();
        fcNodes.add(fcNode);
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();
        FcEdgePoint fcEdgePoint = checkEdgePoint(sessionWrapper, fcEdgePointDao,
            Integer.parseInt(request.getEdgePointId()));

        fcEdgePointDao.delete(sessionWrapper, fcEdgePoint.getEdgePointId());

        responseBase = responsEdgePointDeleteData();

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNodeByEdgePoint(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, Integer edgePointId)
      throws MsfException {
    try {
      logger.methodStart();
      FcNode fcNode = fcNodeDao.read(sessionWrapper, edgePointId);

      if (fcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private FcEdgePoint checkEdgePoint(SessionWrapper sessionWrapper, FcEdgePointDao fcEdgePointDao, Integer edgePointId)
      throws MsfException {
    try {
      logger.methodStart();
      FcEdgePoint fcEdgePoint = getEdgePoint(sessionWrapper, fcEdgePointDao, edgePointId);
      if (fcEdgePoint == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = edgePoint");
      } else if ((!fcEdgePoint.getL2Cps().isEmpty()) || (!fcEdgePoint.getL3Cps().isEmpty())) {

        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "cps exist related to the edgepoint.");
      }
      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responsEdgePointDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
