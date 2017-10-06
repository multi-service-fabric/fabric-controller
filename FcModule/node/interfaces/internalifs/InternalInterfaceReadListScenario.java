package msf.fc.node.interfaces.internalifs;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.InternalLinkIfDao;
import msf.fc.node.interfaces.internalifs.data.InternalIfReadDetailListResponseBody;
import msf.fc.node.interfaces.internalifs.data.InternalIfReadListResponseBody;
import msf.fc.node.interfaces.internalifs.data.InternalIfRequest;

public class InternalInterfaceReadListScenario extends AbstractInternalInterfaceScenarioBase<InternalIfRequest> {

  private InternalIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(InternalInterfaceReadListScenario.class);

  public InternalInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(InternalIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkNodeId(request.getNodeId());

      checkFabricType(request.getFabricType());

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

    } finally {
      logger.methodEnd();
    }

    this.request = request;
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
        List<InternalLinkIf> internalLinkIfs = internalLinkIfDao.readList(sessionWrapper, request.getClusterId(),
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()));

        if (internalLinkIfs.isEmpty()) {
          checkNode(sessionWrapper, request.getClusterId(), request.getFabricTypeEnum().getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseInternalInterfaceReadListData(internalLinkIfs, request.getFormat());

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

  private RestResponseBase responseInternalInterfaceReadListData(List<InternalLinkIf> internalLinkIfs, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        InternalIfReadDetailListResponseBody body = new InternalIfReadDetailListResponseBody();
        body.setInternalIfList(getInternalIfEntities(internalLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        InternalIfReadListResponseBody body = new InternalIfReadListResponseBody();
        body.setInternalIfIdList(getInternalIfIdList(internalLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
