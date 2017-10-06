package msf.fc.core.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import msf.fc.common.config.ConfigManager;
import msf.fc.core.scenario.AbstractAsyncRunner;

public final class AsyncExecutor {

  private static final int SHUTDOWN_AWAIT_TIME = 10;

  private static final ExecutorService EXEC_SERVICE = Executors
      .newFixedThreadPool(ConfigManager.getInstance().getMaxAsyncRunnerThreadNum());

  private static final AsyncExecutor instance = new AsyncExecutor();

  private AsyncExecutor() {
  }

  public static AsyncExecutor getInstance() {
    return instance;
  }

  public void submit(AbstractAsyncRunner task) {
    AsyncExecutor.EXEC_SERVICE.submit(task);
  }

  public boolean shutdown() throws InterruptedException {
    AsyncExecutor.EXEC_SERVICE.shutdown();
    return AsyncExecutor.EXEC_SERVICE.awaitTermination(SHUTDOWN_AWAIT_TIME, TimeUnit.SECONDS);

  }
}
