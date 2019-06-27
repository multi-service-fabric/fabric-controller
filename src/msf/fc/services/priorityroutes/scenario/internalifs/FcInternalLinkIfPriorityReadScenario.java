
package msf.fc.services.priorityroutes.scenario.internalifs;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityReadResponseBody;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityRequest;

/**
 * Implementation class for the internal-link interface priority information
 * acquisition.
 *
 * @author NTT
 *
 */
public class FcInternalLinkIfPriorityReadScenario
    extends FcAbstractInternalLinkIfPriorityScenarioBase<InternalLinkIfPriorityRequest> {

  private InternalLinkIfPriorityRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalLinkIfPriorityReadScenario.class);

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
  public FcInternalLinkIfPriorityReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(InternalLinkIfPriorityRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNumericId(request.getInternalLinkIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

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
        FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();

        FcInternalLinkIf fcInternalLinkIf = getInternalLinkIf(sessionWrapper, fcInternalLinkIfDao,
            Integer.parseInt(request.getInternalLinkIfId()));

        checkInternalLinkIf(fcInternalLinkIf);

        responseBase = responseInternalLinkIfPriorityReadData(fcInternalLinkIf);

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

  protected FcInternalLinkIf getInternalLinkIf(SessionWrapper sessionWrapper, FcInternalLinkIfDao fcInternalLinkIfDao,
      Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart();
      FcInternalLinkIf internalLinkIf = fcInternalLinkIfDao.read(sessionWrapper, internalLinkIfId);
      if (internalLinkIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. internalLinkIfId = {0}.", internalLinkIfId));
      }
      return internalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });

      FcNode targetNode;
      if (internalLinkIf.getPhysicalIf() != null) {
        targetNode = internalLinkIf.getPhysicalIf().getNode();
      } else if (internalLinkIf.getLagIf() != null) {
        targetNode = internalLinkIf.getLagIf().getNode();
      } else {
        targetNode = internalLinkIf.getBreakoutIf().getNode();
      }

      if (!request.getFabricTypeEnum().equals(targetNode.getNodeTypeEnum())) {

        String errorMsg = MessageFormat.format("Fabric Type is different. request = {0}, targetNode = {1}",
            request.getFabricType(), targetNode.getNodeTypeEnum().getPluralMessage());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errorMsg);

      } else if (!request.getNodeId().equals(String.valueOf(targetNode.getNodeId()))) {

        String errorMsg = MessageFormat.format("Node ID is different. request = {0}, targetNode = {1}",
            request.getNodeId(), targetNode.getNodeId());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errorMsg);

      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseInternalLinkIfPriorityReadData(FcInternalLinkIf fcInternalLinkIf) {
    try {
      logger.methodStart(new String[] { "fcInternalLinkIf" }, new Object[] { fcInternalLinkIf });
      InternalLinkIfPriorityReadResponseBody body = new InternalLinkIfPriorityReadResponseBody();
      body.setInternalLinkIf(getInternalLinkIfPriority(fcInternalLinkIf));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
