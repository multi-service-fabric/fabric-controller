package msf.fc.common.internal;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.internal.data.InternalOperationRequestBody;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.v1.internal.fabriccontroller.InternalOperationHandler;

public class InternalOperationScenario extends AbstractScenario<RestRequestBase> {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalOperationHandler.class);

  private InternalOperationRequestBody requestBody;

  public InternalOperationScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(RestRequestBase request) throws MsfException {
    logger.methodStart();
    try {
      this.requestBody = JsonUtil.fromJson(request.getRequestBody(), InternalOperationRequestBody.class);

      this.requestBody.validate();

    } catch (MsfException me) {
      logger.error("[ope_id={0}]: {1}", me, getOperationId(), me.getMessage());
      throw me;
    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.methodStart();
    try {
      return new RestResponseBase(HttpStatus.OK_200, this.requestBody.getAction());
    } finally {
      logger.methodEnd();
    }
  }
}