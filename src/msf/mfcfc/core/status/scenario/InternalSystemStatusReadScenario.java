
package msf.mfcfc.core.status.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.core.status.scenario.data.InternalSystemStatusReadResponseBody;
import msf.mfcfc.core.status.scenario.data.InternalSystemStatusRequest;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.failure.FailureManager;
import msf.mfcfc.node.NodeManager;
import msf.mfcfc.rest.RestManager;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.SliceManager;
import msf.mfcfc.traffic.TrafficManager;

/**
 * Implementation class for status check.
 *
 * @author NTT
 *
 */
public class InternalSystemStatusReadScenario extends AbstractStatusScenarioBase<InternalSystemStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalSystemStatusReadScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public InternalSystemStatusReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(InternalSystemStatusRequest request) throws MsfException {
    try {
      logger.methodStart();

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.info("[ope_id={0}]:Start SystemStatusReadScenario.", this.getOperationId());

      SystemStatus sysStat = SystemStatusManager.getInstance().getSystemStatus();
      if (sysStat.getServiceStatusEnum() == ServiceStatus.STARTED) {
        checkFunctionBlocks();
      }

      RestResponseBase ret = new RestResponseBase(HttpStatus.OK_200, createResponseBody(sysStat));

      logger.info("[ope_id={0}]:End SystemStatusReadScenario.", this.getOperationId());
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private String createResponseBody(SystemStatus sysStat) throws MsfException {
    try {
      logger.methodStart(new String[] { "sysStat" }, new Object[] { sysStat });

      InternalSystemStatusReadResponseBody responseBody = new InternalSystemStatusReadResponseBody();

      responseBody.setServiceStatus(sysStat.getServiceStatusEnum().getMessage());
      responseBody.setBlockadeStatus(sysStat.getBlockadeStatusEnum().getMessage());

      String responseBodyJson = JsonUtil.toJson(responseBody);

      return responseBodyJson;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkFunctionBlocks() throws MsfException {

    List<FunctionBlockBase> fbList = new ArrayList<FunctionBlockBase>();

    try {

      fbList.add(CoreManager.getInstance());
      fbList.add(DbManager.getInstance());
      fbList.add(ConfigManager.getInstance());
      fbList.add(NodeManager.getInstance());
      fbList.add(SliceManager.getInstance());
      fbList.add(FailureManager.getInstance());
      fbList.add(TrafficManager.getInstance());
      fbList.add(RestManager.getInstance());

      for (FunctionBlockBase fbBase : fbList) {

        if (fbBase != null) {
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
