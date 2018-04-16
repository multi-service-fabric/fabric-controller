
package msf.fc.node.interfaces.internalifs;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.internalifs.data.InternalIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.internalifs.data.InternalIfReadListResponseBody;
import msf.mfcfc.node.interfaces.internalifs.data.InternalIfRequest;

/**
 * Implementation class for intra-cluster link interface information list
 * acquisition.
 *
 * @author NTT
 *
 */
public class FcInternalInterfaceReadListScenario extends FcAbstractInternalInterfaceScenarioBase<InternalIfRequest> {

  private InternalIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalInterfaceReadListScenario.class);

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
  public FcInternalInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(InternalIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));

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
        FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();

        List<FcInternalLinkIf> fcInternalLinkIfs = fcInternalLinkIfDao.readList(sessionWrapper,
            NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
            Integer.parseInt(request.getNodeId()));

        if (fcInternalLinkIfs.isEmpty()) {

          checkNode(sessionWrapper, NodeType.getEnumFromPluralMessage(request.getFabricType()).getCode(),
              Integer.parseInt(request.getNodeId()));
        }

        responseBase = responseInternalInterfaceReadListData(fcInternalLinkIfs, request.getFormat());

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

  private RestResponseBase responseInternalInterfaceReadListData(List<FcInternalLinkIf> fcInternalLinkIfs,
      String format) throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        InternalIfReadDetailListResponseBody body = new InternalIfReadDetailListResponseBody();
        body.setInternalLinkIfList(getInternalIfEntities(fcInternalLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        InternalIfReadListResponseBody body = new InternalIfReadListResponseBody();
        body.setInternalLinkIfIdList(getInternalIfIdList(fcInternalLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }
}
