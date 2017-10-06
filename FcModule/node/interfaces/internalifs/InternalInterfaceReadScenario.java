package msf.fc.node.interfaces.internalifs;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.node.interfaces.internalifs.data.InternalIfReadResponseBody;
import msf.fc.node.interfaces.internalifs.data.InternalIfRequest;

public class InternalInterfaceReadScenario extends AbstractInternalInterfaceScenarioBase<InternalIfRequest> {

  private InternalIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(InternalInterfaceReadScenario.class);

  public InternalInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(InternalIfRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFabricType(request.getFabricType());

      checkNodeId(request.getNodeId());

      ParameterCheckUtil.checkNumericId(request.getInternalIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

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
        InternalLinkIfDao internalLinkIfDao = new InternalLinkIfDao();
        InternalLinkIf internalLinkIf = getInternalLinkIf(sessionWrapper, internalLinkIfDao,
            Integer.parseInt(request.getInternalIfId()));

        checkInternalLinkIf(internalLinkIf);

        responseBase = responseInternalInterfaceReadData(internalLinkIf);

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

  private InternalLinkIf getInternalLinkIf(SessionWrapper sessionWrapper, InternalLinkIfDao internalLinkIfDao,
      Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart();
      InternalLinkIf internalLinkIf = internalLinkIfDao.read(sessionWrapper, internalLinkIfId);
      if (internalLinkIf == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = internalLinkIf");
      }
      return internalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkInternalLinkIf(InternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      Node targetNode = internalLinkIf.getLagIf().getNode();
      if (!request.getClusterId().equals(targetNode.getEquipment().getId().getSwClusterId())) {
        String errorMsg = MessageFormat.format("SW cluster ID is different. request = {0}, targetNode = {1}",
            request.getClusterId(), targetNode.getEquipment().getId().getSwClusterId());
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, errorMsg);

      } else if (!request.getFabricTypeEnum().equals(targetNode.getNodeTypeEnum())) {
        String errorMsg = MessageFormat.format("Fabric Type is different. request = {0}, targetNode = {1}",
            request.getFabricTypeEnum().getSingularMessage(), targetNode.getNodeTypeEnum().getSingularMessage());
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

  private RestResponseBase responseInternalInterfaceReadData(InternalLinkIf internalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIf" }, new Object[] { internalLinkIf });
      InternalIfReadResponseBody body = new InternalIfReadResponseBody();
      body.setInternalIf(getInternalIf(internalLinkIf));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
