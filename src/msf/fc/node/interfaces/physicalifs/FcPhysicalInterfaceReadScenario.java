
package msf.fc.node.interfaces.physicalifs;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.fc.rest.ec.node.interfaces.breakout.data.BreakoutIfReadListEcResponseBody;
import msf.fc.rest.ec.node.interfaces.physical.data.PhysicalIfReadEcResponseBody;
import msf.mfcfc.common.constant.EcRequestUri;
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
import msf.mfcfc.node.interfaces.physicalifs.data.PhysicalIfReadResponseBody;
import msf.mfcfc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for the physical interface information acquisition.
 *
 * @author NTT
 *
 */
public class FcPhysicalInterfaceReadScenario extends FcAbstractPhysicalInterfaceScenarioBase<PhysicalIfRequest> {

  private PhysicalIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalInterfaceReadScenario.class);

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
  public FcPhysicalInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(PhysicalIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkIdSpecifiedByUri(request.getIfId());

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
        FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();

        FcPhysicalIf fcPhysicalIf = getPhysicalInterface(sessionWrapper, fcPhysicalIfDao,
            request.getFabricTypeEnum().getCode(), Integer.parseInt(request.getNodeId()), request.getIfId());

        responseBase = responsePhysicalInterfaceReadData(fcPhysicalIf);

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

  protected RestResponseBase responsePhysicalInterfaceReadData(FcPhysicalIf physicalIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "physicalIf" }, new Object[] { physicalIf });

      FcNode node = physicalIf.getNode();
      PhysicalIfReadEcResponseBody physicalIfReadEcResponseBody = sendPhysicalInterfaceRead(node, request.getIfId());
      BreakoutIfReadListEcResponseBody breakoutIfReadListEcResponseBody = new BreakoutIfReadListEcResponseBody();
      if (CollectionUtils.isNotEmpty(physicalIfReadEcResponseBody.getPhysicalIf().getBreakoutIfList())) {
        breakoutIfReadListEcResponseBody = sendBreakoutIfReadList(node);
      }

      PhysicalIfReadResponseBody body = new PhysicalIfReadResponseBody();
      body.setPhysicalIf(getPhysicalIfData(physicalIf, physicalIfReadEcResponseBody.getPhysicalIf(),
          breakoutIfReadListEcResponseBody.getBreakoutIfList()));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

  private BreakoutIfReadListEcResponseBody sendBreakoutIfReadList(FcNode node) throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.BREAKOUT_IF_READ_LIST.getHttpMethod(),
          EcRequestUri.BREAKOUT_IF_READ_LIST.getUri(String.valueOf(node.getEcNodeId())), null, ecControlIpAddress,
          ecControlPort);

      BreakoutIfReadListEcResponseBody breakoutIfReadListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), BreakoutIfReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          breakoutIfReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return breakoutIfReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
