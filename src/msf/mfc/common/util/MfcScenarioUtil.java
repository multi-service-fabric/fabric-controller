
package msf.mfc.common.util;

import java.text.MessageFormat;
import java.util.TimerTask;

import msf.mfc.core.operation.scenario.MfcOperationNotifyScenario;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.AbstractOperationNotifyTimerTask;
import msf.mfcfc.core.operation.scenario.data.OperationNotifyRequestBody;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.scenario.ErrorResponse;
import msf.mfcfc.core.scenario.TimerTaskMaker;

/**
 * MFC scenario related utility class.
 *
 * @author NTT
 *
 */
public class MfcScenarioUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcScenarioUtil.class);

  /**
   * Return the creation process of timer task for receiving the operation
   * result notification.
   *
   * @return timer task for receiving the operation result notification
   */
  public static TimerTaskMaker createTimerTaskForMfc() {
    try {
      logger.methodStart();
      TimerTaskMaker maker = new TimerTaskMaker() {
        @Override
        public TimerTask makeTimerTask() {
          TimerTask timerTask = new AbstractOperationNotifyTimerTask() {
            @Override
            public void run() {
              try {
                logger.methodStart();

                ErrorResponse errorResponse = new ErrorResponse(ErrorCode.FC_CONTROL_TIMEOUT,
                    SystemInterfaceType.EXTERNAL);

                OperationResponseEntity response = new OperationResponseEntity();
                response.setStatusCode(errorResponse.getHttpStatusCode());
                response.setBody(errorResponse.getResponseBody());
                OperationNotifyRequestBody body = new OperationNotifyRequestBody();
                body.setResponse(response);

                MfcOperationNotifyScenario scenario = new MfcOperationNotifyScenario(OperationType.NORMAL,
                    SystemInterfaceType.EXTERNAL);
                scenario.setRequestBody(body);

                scenario.processNotify(getClusterId(), getLowerOperationId());
              } catch (Exception ex) {

                String errorMessage = MessageFormat.format(
                    "failed operation notify timeout task. cluster id = {0}, lower operation id = {1}.", getClusterId(),
                    getLowerOperationId());
                logger.error(errorMessage, ex);
              } finally {
                logger.methodEnd();
              }
            }
          };
          return timerTask;
        }
      };
      return maker;
    } finally {
      logger.methodEnd();
    }
  }
}
