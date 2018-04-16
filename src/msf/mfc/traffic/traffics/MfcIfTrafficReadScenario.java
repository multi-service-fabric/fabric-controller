
package msf.mfc.traffic.traffics;

import java.text.MessageFormat;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;

/**
 * Implementation class for IF traffic information acquisition.
 *
 * @author NTT
 *
 */
public class MfcIfTrafficReadScenario extends MfcAbstractIfTrafficScenarioBase<IfTrafficRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcIfTrafficReadScenario.class);

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
  public MfcIfTrafficReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(IfTrafficRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(request.getFabricType());
      if ((!NodeType.LEAF.getPluralMessage().equals(request.getFabricType()))
          && (!NodeType.SPINE.getPluralMessage().equals(request.getFabricType()))) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "fabric_type",
            request.getFabricType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotNull(request.getIfType());
      if (InterfaceType.PHYSICAL_IF != request.getIfTypeEnum() && InterfaceType.BREAKOUT_IF != request.getIfTypeEnum()
          && InterfaceType.LAG_IF != request.getIfTypeEnum()) {
        String logMsg = MessageFormat.format("parameter is undefined. parameter={0}, value={1}", "if_type",
            request.getIfType());
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
      ParameterCheckUtil.checkNotNull(request.getIfId());

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      try {
        session.openSession();

        getMfcSwCluster(session);

        RestResponseBase fcTrafficReadResponse = sendTrafficRead();

        return fcTrafficReadResponse;

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        session.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

}
