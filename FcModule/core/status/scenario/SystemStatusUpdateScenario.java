package msf.fc.core.status.scenario;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.BlockadeStatus;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.core.status.SystemStatusManager;
import msf.fc.core.status.scenario.data.SystemStatusUpdateBody;
import msf.fc.core.status.scenario.data.SystemStatusUpdateRequest;
import msf.fc.rest.common.JsonUtil;

public class SystemStatusUpdateScenario extends AbstractStatusScenarioBase<SystemStatusUpdateRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusUpdateScenario.class);

  private SystemStatusUpdateBody requestBody = null;

  public SystemStatusUpdateScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(SystemStatusUpdateRequest request) throws MsfException {
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
  }

}
