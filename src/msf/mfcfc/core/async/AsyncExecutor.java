
package msf.mfcfc.core.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.core.scenario.AbstractAsyncRunner;

/**
 * Execution management class for asynchronous runner.
 *
 * @author NTT
 *
 */
public final class AsyncExecutor {

  private static final int SHUTDOWN_AWAIT_TIME = 10;

  private static final ExecutorService EXEC_SERVICE = Executors
      .newFixedThreadPool(ConfigManager.getInstance().getMaxAsyncRunnerThreadNum());

  private static final AsyncExecutor instance = new AsyncExecutor();

  private AsyncExecutor() {
  }

  /**
   * Get the instance of AsyncExecutor.
   *
   * @return AsyncExecutor instance
   */
  public static AsyncExecutor getInstance() {
    return instance;
  }

  /**
   * Execute the task (asynchronous runner).
   *
   * @param task
   *          Task to execute (asynchronous runner)
   */
  public void submit(AbstractAsyncRunner task) {
    AsyncExecutor.EXEC_SERVICE.submit(task);
  }

  /**
   * Shut down the Executor that executes and manages the task (asynchronous
   * runner). <br>
   * Wait for the task to complete for a certain period of time.
   *
   * @return true; if this executor is terminated, false; if timeout has elapsed
   *         before end
   * @throws InterruptedException
   *           If an interruption occurs while waiting
   */
  public boolean shutdown() throws InterruptedException {
    AsyncExecutor.EXEC_SERVICE.shutdown();
    return AsyncExecutor.EXEC_SERVICE.awaitTermination(SHUTDOWN_AWAIT_TIME, TimeUnit.SECONDS);

  }
}
