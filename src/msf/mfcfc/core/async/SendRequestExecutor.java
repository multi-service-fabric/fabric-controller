
package msf.mfcfc.core.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RequestSendRunner;
import msf.mfcfc.core.scenario.RestResponseData;

/**
 * Execution management class for request send runner.
 *
 * @author NTT
 *
 */
public final class SendRequestExecutor {

  private static final MsfLogger logger = MsfLogger.getInstance(SendRequestExecutor.class);

  private static final ExecutorService EXEC_SERVICE = Executors.newWorkStealingPool();

  private static final SendRequestExecutor instance = new SendRequestExecutor();

  private SendRequestExecutor() {

  }

  /**
   * Get the instance of SendRequestExecutor.
   *
   * @return SendRequestExecutor instance
   */
  public static SendRequestExecutor getInstance() {
    return instance;
  }

  /**
   * Execute the task (request send runner).
   *
   * @param taskList
   *          Task list to execute
   * @param timeout
   *          Timeout value
   * @param unit
   *          Unit of timeout value
   * @return response information list of executed tasks
   * @throws MsfException
   *           If task execution by invokeAll fails
   *
   */
  public List<RestResponseData> invokeAll(List<RequestSendRunner> taskList, long timeout, TimeUnit unit)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "taskList", "timeout", "unit" }, new Object[] { taskList, timeout, unit });
      List<Future<RestResponseData>> responseFutureList = EXEC_SERVICE.invokeAll(taskList, timeout, unit);
      List<RestResponseData> responseDataList = new ArrayList<>();
      for (Future<RestResponseData> future : responseFutureList) {
        RestResponseData restResponseData = future.get();
        responseDataList.add(restResponseData);
      }

      return responseDataList;
    } catch (Exception exp) {
      String errorMessage = "failed invokeAll tasks.";
      logger.warn(errorMessage, exp);

      throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
    } finally {
      logger.methodEnd();
    }

  }
}
