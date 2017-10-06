package msf.fc.core.status.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.CoreManager;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.core.status.SystemStatusManager;
import msf.fc.core.status.scenario.data.SystemStatusReadRequest;
import msf.fc.core.status.scenario.data.SystemStatusReadResponseBody;
import msf.fc.db.DbManager;
import msf.fc.failure.FailureManager;
import msf.fc.node.NodeManager;
import msf.fc.rest.RestManager;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.SliceManager;
import msf.fc.traffic.TrafficManager;

public class SystemStatusReadScenario extends AbstractStatusScenarioBase<SystemStatusReadRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusReadScenario.class);

  public SystemStatusReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(SystemStatusReadRequest request) throws MsfException {
    logger.methodStart();




    logger.methodEnd();

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.info("[ope_id={0}]:Start SystemStatusReadScenario.", this.getOperationId());

    SystemStatus sysStat = SystemStatusManager.getInstance().getSystemStatus();
    if (sysStat.getServiceStatusEnum() == ServiceStatus.STARTED) {
      checkFunctionBlocks();
    }


    RestResponseBase ret = new RestResponseBase(HttpStatus.OK_200, createResponseBody(sysStat));

    logger.debug("[ope_id={0}]:RestResponse={1}", this.getOperationId(), ret);

    logger.info("[ope_id={0}]:End SystemStatusReadScenario.", this.getOperationId());
    return ret;
  }

  private String createResponseBody(SystemStatus sysStat) throws MsfException {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "sysStat" }, new Object[] { sysStat });
    }

    SystemStatusReadResponseBody responseBody = new SystemStatusReadResponseBody();
    try {

      responseBody.setServiceStatus(sysStat.getServiceStatusEnum().getMessage());
      responseBody.setBlockadeStatus(sysStat.getBlockadeStatusEnum().getMessage());

      String responseBodyJson = JsonUtil.toJson(responseBody);

      return responseBodyJson;

    } finally {
      logger.methodEnd(new String[] { "result" }, new Object[] { responseBody });
    }
  }

  private void checkFunctionBlocks() throws MsfException {

    List<FunctionBlockBase> fbList = new ArrayList<FunctionBlockBase>();

    try {
      fbList.add(CoreManager.getInstance());
      fbList.add(DbManager.getInstance());
      fbList.add(ConfigManager.getInstance());
      fbList.add(FailureManager.getInstance());
      fbList.add(NodeManager.getInstance());
      fbList.add(SliceManager.getInstance());
      fbList.add(TrafficManager.getInstance());
      fbList.add(RestManager.getInstance());

      for (FunctionBlockBase fbBase : fbList) {
        if (logger.isDebugEnabled()) {
          logger.debug("[ope_id={0}]:{1} Status={2}", this.getOperationId(), fbBase.getClass().getSimpleName(),
              fbBase.checkStatus());
        }

        if (!fbBase.checkStatus()) {
          String errorMessage = "The Function Block is not alive.(Function Block={0})";
          errorMessage = MessageFormat.format(errorMessage, fbBase.getClass());
          String opeId = "[ope_id={0}]:";
          opeId = MessageFormat.format(opeId, this.getOperationId());

          logger.error(opeId + errorMessage);
          throw new MsfException(ErrorCode.SYSTEM_CHECK_STATUS_ERROR, errorMessage);
        }
      }
    } catch (MsfException ex) {
      throw ex;

    } catch (Exception ex) {
      String errorMessage = "Failed to get the Function Block status.";
      String opeId = "[ope_id={0}]:";
      opeId = MessageFormat.format(opeId, this.getOperationId());

      logger.error(opeId + errorMessage, ex);
      throw new MsfException(ErrorCode.SYSTEM_CHECK_STATUS_ERROR, errorMessage);
    }

  }
}
