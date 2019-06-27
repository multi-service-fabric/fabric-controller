package msf.event;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class EventServer {
	private Server jettyServer;
	private List<String> restResourcePackages = new ArrayList<>();

	MsfLogger logger = MsfLogger.getInstance(EntryPoint.class);

	public EventServer() {
		DOMConfigurator.configure("log4j.xml");

		try {
			logger.methodStart();
			String dir = "conf";
			String source = "event";
			URLClassLoader urlLoader = new URLClassLoader(new URL[] { new File(dir).toURI().toURL() });

			ResourceBundle rb = ResourceBundle.getBundle(source, Locale.getDefault(), urlLoader);
			new EventKeeper(Integer.parseInt(rb.getString("maxEventNum")));

			String pidFile = rb.getString("pidFile");

			setRestResourcePackage("msf.event");

			jettyServer = new Server();
			// ハンドラをセットする
			jettyServer.setHandler(createHandler());
			// jettyServer.addBean(new MsfErrorHandler());

			// コネクタ作成のためのコンフィグ情報を取得
			int port = Integer.parseInt(rb.getString("serverPort"));
			// コネクタをセットする
			jettyServer.addConnector(createConnector(jettyServer, port));

			// Jettyサーバを起動する
			jettyServer.start();

			PrintWriter writer = null;
			try {
				RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
				String vmName = bean.getName();
				long pid = Long.valueOf(vmName.split("@")[0]);

				// 起動スクリプトに起動完了を通知するためのFC起動確認用ファイルに、自身のPIDを出力する.
				File file = new File(pidFile);
				writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				writer.println(pid);
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		} catch (Exception exp) {
			logger.warn("exp", exp);
		} finally {
			logger.methodEnd();
		}
	}

	private Handler createHandler() {
		try {
			logger.methodStart();
			// 各種JAX-RSメソッド提供クラス配備用のクラスを生成
			ResourceConfig resourceConfig = new ResourceConfig();
			// 基本機能、拡張機能のパッケージを複数指定（List形式⇒可変長パラメータにして設定）
			resourceConfig.packages(restResourcePackages.toArray(new String[0]));
			resourceConfig.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
			// resourceConfig.property(ServerProperties.PROVIDER_PACKAGES, new
			// ClientErrorExceptionMapper());

			// JAX-RSのフィルタ機能によりリクエストへ一律フィルタ付与(クロスドメイン制約対応用のHTTPメソッドがOPTIONSの場合の処理)
			resourceConfig.register(CorsRequestFilter.class);
			// JAX-RSのフィルタ機能によりレスポンスへ一律フィルタ付与(クロスドメイン制約対応用のHTTPヘッダ付与)
			resourceConfig.register(CorsResponseFilter.class);

			// ハンドラに登録できる形にラッピング
			ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));

			// JAX-RS(REST)によるアプリケーションサーブレットの登録用ハンドラ
			ServletContextHandler servletContextHandler = new ServletContextHandler();

			// 各RESTサービス定義クラスのPathアノテーションで指定しているパスを指定.
			servletContextHandler.addServlet(servletHolder, "/*");
			// servletContextHandler.setErrorHandler(new MsfErrorHandler());

			// 作成したハンドラを返す
			return servletContextHandler;

		} finally {
			logger.methodEnd();
		}
	}

	public void setRestResourcePackage(String... restResourcePackages) {
		// RESTハンドラパッケージパスの初期化
		this.restResourcePackages.clear();
		// 指定されたRESTハンドラパッケージパスを設定する.
		for (String restResourcePackage : restResourcePackages) {
			if (restResourcePackage.length() != 0) {
				// 指定されたパッケージがサイズ0以外の時のみ設定する.
				this.restResourcePackages.add(restResourcePackage);
			}
		}
	}

	/**
	 * 指定されたIPアドレス/ポート番号にて通信可能なJetty用HTTPサーバコネクタを作成する.
	 *
	 * @param jettyServer
	 *            Jettyサーバインスタンス
	 * @param port
	 *            サーバポート番号
	 * @return HTTP通信用コネクタ
	 */
	private Connector createConnector(Server jettyServer, int port) {
		try {
			logger.methodStart();
			// HTTP設定
			HttpConfiguration httpConfiguration = new HttpConfiguration();
			// スキーマ(HTTP)の設定
			httpConfiguration.setSecureScheme("http");

			// HTTPサーバコネクタ
			ServerConnector serverConnector = new ServerConnector(jettyServer,
					new HttpConnectionFactory(httpConfiguration));

			// ポート番号の設定
			serverConnector.setPort(port);

			return serverConnector;
		} finally {
			logger.methodEnd();
		}
	}

	public static void main(String[] args) {
		new EventServer();
	}

}
