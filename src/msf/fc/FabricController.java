
package msf.fc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

import msf.fc.common.config.FcConfigManager;
import msf.fc.core.status.FcSystemStatusManager;
import msf.fc.db.FcDbManager;
import msf.fc.failure.FcFailureManager;
import msf.fc.node.FcNodeManager;
import msf.fc.slice.FcSliceManager;
import msf.fc.traffic.FcTrafficManager;
import msf.mfcfc.AbstractMain;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.CoreManager;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.rest.RestManager;

/**
 * 起動停止機能部 FabricControllerを起動、停止するクラス.
 *
 * @author NTT
 *
 */
public class FabricController extends AbstractMain {

  /** FC起動確認用ファイル. */
  private static final String FC_OUTPUT = "fc.output";

  /** ロガーインスタンス. */
  private static final MsfLogger logger = MsfLogger.getInstance(FabricController.class);

  /** 機能構成ファイル. */
  private static final String CONF_FUNCTIONAL_CONFIG_PATH = "services/sys/fc_extensions.cnf";

  /**
   * FabricControllerのコンストラクタ.
   */
  public FabricController() {
    // 処理なし
  }

  /**
   * FabricControllerのメインプロセスの起動処理.
   *
   * @param args
   *          第1引数：コンフィグファイル配置ディレクトリへのパス(省略可)
   */
  public static void main(String[] args) {
    printBuildTimestamp();

    logger.info("FabricController start.");

    FabricController fc = new FabricController();

    if (args.length == 1 && args[0].length() != 0) {
      // 指定されたパスを設定.パスが適切か(コンフィグファイルが読み込めるか)はConfigManagerがチェックする
      logger.debug("FC Config Path : " + args[0]);
      fc.confPath = args[0];

    } else if (args.length == 0 || args.length == 1) {
      // 未指定の場合はnullを設定.後程の処理でConfigManagerがデフォルト値を設定する
      // args.lenth == 1 になるのは、引数に空文字を指定した場合
      logger.debug("FC Config Path : (defalt)");
      fc.confPath = null;

    } else {
      // 引数が2つ以上存在する場合、異常終了する
      logger.error("The number of arguments is set two or more. FabricController start failure.");
      System.exit(ABNOMAL_EXIT_CODE);
    }

    PrintWriter writer = null;
    boolean isInitializationFailed = false;
    try {
      logger.performance("FabricController initialization start.");

      // 各機能ブロックのManagerクラスを起動
      if (fc.initializeFunctionBlocks()) {

        // 自プロセスのPIDを取得
        // ※ JDK 1.8 (Linux)で動作を確認
        // JavaのAPIドキュメント上は下記方法でPIDが取れることは保証されていないため
        // JVMの挙動が変わった時に動かない可能性がある.
        logger.debug(FC_OUTPUT + " create start.");
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String vmName = bean.getName();
        long pid = Long.valueOf(vmName.split("@")[0]);

        // 起動スクリプトに起動完了を通知するためのFC起動確認用ファイルに、自身のPIDを出力する.
        File file = new File(FC_OUTPUT);
        writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        writer.println(pid);

        logger.performance("FabricController initialization end.");

      } else {
        // 初期化処理が失敗した場合は "isInitializationFailedフラグをtrueにする.
        isInitializationFailed = true;
      }

    } catch (IOException error1) {
      // fc.outputファイルが作成できなかった場合
      logger.error("Couldn't create " + FC_OUTPUT + ".\nFabricController start failure.", error1);
      isInitializationFailed = true;

    } catch (Exception error1) {
      logger.error("Unexpected Exception occurred.", error1);
      isInitializationFailed = true;

    } finally {
      if (writer != null) {
        writer.close();
      }
    }

    if (isInitializationFailed) {
      try {
        logger.debug("FabricController abnormal termination start.");
        // 各機能ブロックを停止(起動完了したものにのみを停止)
        fc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("FabricController finalization failed.", error1);
      } finally {
        logger.error("FabricController terminated abnormally.");
        logger.info("FabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    // この時点で、システムが正常に起動できたと判断する
    fc.isSystemStartUp = true;

    try {
      // 無限ループを中断するためのシャットダウンフックを登録
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            logger.info("shutdown hook start.");

            synchronized (lockObj) {
              logger.performance("FabricController finalization start.");
              // 停止処理
              fc.finalizeFunctionBlocks();
              logger.performance("FabricController finalization end.");
            }

          } catch (Exception error1) {
            logger.error("FabricController finalization failed.", error1);

          } finally {
            logger.info("FabricController end.");

          }
        }
      });

    } catch (Throwable th) {
      // shutdownhookを登録でき無い場合
      logger.error("Unexpected Exception occurred.", th);

      try {
        logger.debug("FabricController abnormal termination start.");
        // 起動が完了していないため、finalizeFunctionBlocksを呼ぶ
        fc.finalizeFunctionBlocks();
      } catch (Exception error1) {
        logger.error("FabricController finalization failed.", error1);
      } finally {
        logger.error("FabricController terminated abnormally.");
        logger.info("FabricController end.");
        System.exit(ABNOMAL_EXIT_CODE);
      }
    }

    try {

      logger.debug("FabricController loop processing start.");

      // SIGTERMによるシャットダウンフックが呼び出されるまで無限ループ
      while (true) {

        // 24時間に1回、不要となった非同期オペレーションのレコードを削除
        synchronized (lockObj) {
          logger.performance("Delete AsyncOperationIds start.");
          fc.deleteAsyncOperationIds();
          logger.performance("Delete AsyncOperationIds end.");
        }

        logger.debug("sleep 1 day.");
        try {

          // TimeUnit.DAYS.sleep(1);
          // 待機時間の設定
          long sleepTime = TimeUnit.DAYS.toMillis(1);
          // 指定時間待機する
          TimeUnit.MILLISECONDS.sleep(sleepTime);

        } catch (InterruptedException ex) {
          // 24時間周期がずれるが、処理は続行
          logger.warn("Sleep interrupted in main loop.");
        }
      }

    } catch (Throwable th) {
      // OutOfMemory等の予期せぬエラーが発生した場合
      logger.error("Unexpected Exception occurred.", th);

      // addShutdownhook登録済みのため、System.exitを呼ぶのみ
      System.exit(ABNOMAL_EXIT_CODE);

    }
  }

  /**
   * FCを構成する各機能を順次起動する. 起動が正常に終了した機能部は、機能部のインスタンスを保持するクラス変数に代入する(null以外となる).
   * 起動に失敗した機能部がある場合は、その時点で起動終了とする.
   *
   * @return 処理結果 (true:成功、false:失敗)
   */
  private boolean initializeFunctionBlocks() {

    try {
      logger.methodStart();

      // ConfigManagerの起動する.
      // ・起動時の引数にコンフィグファイル配置ディレクトリを指定された場合は設定する
      logger.info("ConfigManager start.");
      this.conf = FcConfigManager.getInstance();

      if (this.confPath != null) {
        logger.debug("Set Config file path.");
        this.conf.setConfigPath(this.confPath);
      }

      if (!this.conf.start()) {
        logger.error("ConfigManager start processing failed.");
        return false;
      }

      // DBManagerの起動する.
      // ・DBが正常に起動した場合は、サービス起動状態の書き換えを行う
      logger.info("DbManager start.");
      this.db = FcDbManager.getInstance();
      if (!this.db.start()) {
        logger.error("DbManager start processing failed.");
        return false;
      }

      if (!this.updateDbRecords()) {
        logger.error("DbManager: Change Service Status processing failed.");
        return false;
      }

      // CoreManagerを起動する.
      logger.info("CoreManager start.");
      // CoreManager内で呼び出されるシステム状態管理クラスのインスタンスを設定する.
      FcSystemStatusManager.getInstance();
      this.core = CoreManager.getInstance();
      OperationManager.getInstance().setClusterIdForOperationId(
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      if (!this.core.start()) {
        logger.error("CoreManager start processing failed.");
        return false;
      }

      // NodeManagerを起動する.
      logger.info("NodeManager start.");
      this.node = FcNodeManager.getInstance();
      if (!this.node.start()) {
        logger.error("NodeManager start processing failed.");
        return false;
      }

      // SliceManagerを起動する.
      logger.info("SliceManager start.");
      this.slice = FcSliceManager.getInstance();
      if (!this.slice.start()) {
        logger.error("SliceManager start processing failed.");
        return false;
      }

      // FailureManagerを起動する.
      logger.info("FailureManager start.");
      this.failure = FcFailureManager.getInstance();
      if (!this.failure.start()) {
        logger.error("FailureManager start processing failed.");
        return false;
      }

      // TrafficManagerを起動する.
      logger.info("TrafficManager start.");
      this.traffic = FcTrafficManager.getInstance();
      if (!this.traffic.start()) {
        logger.error("TrafficManager start processing failed.");
        return false;
      }

      // 拡張機能の機能構成ファイルパスを指定する.
      setFunctionalConfigPath(CONF_FUNCTIONAL_CONFIG_PATH);
      // 拡張機能の初期化処理を実施する.
      if (!initExtensionFunctions()) {
        logger.error("ExtensionFunction Initialization processing failed.");
        return false;
      }
      // 拡張機能の起動制御を追加する.
      if (!startExtensionFunctions()) {
        return false;
      }

      // RestManagerを起動する.
      this.rest = RestManager.getInstance();
      // RESTリソースクラスの格納パッケージを設定する
      rest.setRestResourcePackage(RestManager.REST_RESOURCE_PACKAGE_FC, RestManager.REST_RESOURCE_PACKAGE_FC_SERVICES);
      // RESTリクエスト数カウント用単位時間を設定する
      rest.setRestRequestTimeouts(FcConfigManager.getInstance().getSystemConfStatus().getRecvRestRequestUnitTime(),
          FcConfigManager.getInstance().getSystemConfStatus().getSendRestRequestUnitTime());
      // リクエスト送信処理エラー発生時のエラーコードを設定する
      rest.setErrorCodes(ErrorCode.EC_CONTROL_ERROR, ErrorCode.EC_CONNECTION_ERROR);
      logger.info("RestManager start.");
      if (!this.rest.start()) {
        logger.error("RestManager start processing failed.");
        return false;
      }

      // 全機能部が正常起動した場合に、サービス起動状態を「起動」に変更する.
      try {
        SystemStatus changeSystemStatus = new SystemStatus();
        changeSystemStatus.setServiceStatusEnum(ServiceStatus.STARTED);

        FcSystemStatusManager.getInstance().changeSystemStatus(changeSystemStatus);

      } catch (MsfException ex) {
        logger.error("Couldn't change Service Status.", ex);
        return false;
      }

      // サービス起動状態が起動状態の後に実施する必要があるため、このタイミングで予約機能のあるマネージャクラスに対して、
      // 非同期リクエスト情報内にある未実施予約オペレーションの登録処理を実施する.機能の起動制御を追加する.
      if (!startReservationScheduler()) {
        return false;
      }

      logger.info("All Managers started successfully.");
      return true;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected boolean isStartAsMultiCluster() {
    return false;
  }

}
