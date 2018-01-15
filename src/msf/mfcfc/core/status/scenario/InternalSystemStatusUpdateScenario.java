
package msf.mfcfc.core.status.scenario;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.core.status.scenario.data.InternalSystemStatusRequest;
import msf.mfcfc.core.status.scenario.data.SystemStatusUpdateBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to provide the system status update function.
 *
 * @author NTT
 *
 */
public class InternalSystemStatusUpdateScenario extends AbstractStatusScenarioBase<InternalSystemStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalSystemStatusUpdateScenario.class);

  private SystemStatusUpdateBody requestBody = null;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public InternalSystemStatusUpdateScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(InternalSystemStatusRequest request) throws MsfException {
    try {
      logger.methodStart();

      SystemStatusUpdateBody requestBody = JsonUtil.fromJson(request.getRequestBody(), SystemStatusUpdateBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.requestBody = requestBody;

    } catch (MsfException ex) {
      String errorMessage = "Request Body is invalid.";
      String opeId = "[ope_id={0}]:";
      opeId = MessageFormat.format(opeId, this.getOperationId());

      logger.error(opeId + errorMessage, ex);
      throw ex;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.info("[ope_id={0}]:Start SystemStatusUpdateScenario.", this.getOperationId());

      String serviceStatString = requestBody.getServiceStatus();
      String blockadeStatString = requestBody.getBlockadeStatus();
      logger.debug("[ope_id={0}]:RequestBody={1}", this.getOperationId(), requestBody);

      if (serviceStatString == null && blockadeStatString == null) {

        RestResponseBase ret = new RestResponseBase(HttpStatus.OK_200, new String());
        logger.debug("[ope_id={0}]:RestResponse={1}", this.getOperationId(), ret);
        logger.info("[ope_id={0}]:End SystemStatusUpdateScenario.", this.getOperationId());
        return ret;
      }

      SystemStatusManager ssManager = SystemStatusManager.getInstance();

      SystemStatus sysStat = new SystemStatus();

      if (serviceStatString != null) {
        ServiceStatus serviceStatEnum = ServiceStatus.getEnumFromMessage(serviceStatString);
        sysStat.setServiceStatusEnum(serviceStatEnum);
      }
      if (blockadeStatString != null) {
        BlockadeStatus blockadeStatEnum = BlockadeStatus.getEnumFromMessage(blockadeStatString);
        sysStat.setBlockadeStatusEnum(blockadeStatEnum);
      }

      ssManager.changeSystemStatus(sysStat);

      RestResponseBase ret = new RestResponseBase(HttpStatus.OK_200, new String());

      logger.debug("[ope_id={0}]:RestResponse={1}", this.getOperationId(), ret);
      logger.info("[ope_id={0}]:End SystemStatusUpdateScenario.", this.getOperationId());
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

}
