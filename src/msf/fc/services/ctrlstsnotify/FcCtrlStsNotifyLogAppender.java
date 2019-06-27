
package msf.fc.services.ctrlstsnotify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.ctrlstsnotify.common.config.type.system.LogNotification;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoLog;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.ctrlstsnotify.CtrlStsNotifySender;
import msf.mfcfc.services.ctrlstsnotify.common.constant.ControllerLogType;
import msf.mfcfc.services.ctrlstsnotify.common.constant.LogLevelType;
import msf.mfcfc.services.ctrlstsnotify.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerLogNotificationRequest;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerLogNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerLogNotificationEntity;

/**
 * Class to provide the controller log notification function.
 *
 * @author NTT
 *
 * @param <T>
 *          Information that is overridden by log4j.
 */
@Plugin(name = "FcNotify", category = "Core", elementType = "appender", printObject = true)
public final class FcCtrlStsNotifyLogAppender extends AbstractAppender {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCtrlStsNotifyLogAppender.class);

  protected FcCtrlStsNotifyLogAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout);
  }

  /**
   * Appender class instantiation.
   *
   * @param name
   *          Appender class
   * @param filter
   *          filter
   * @param layout
   *          layout
   * @return Appender class
   */
  @PluginFactory
  public static FcCtrlStsNotifyLogAppender createAppender(@PluginAttribute("name") String name,
      @PluginElement("Filters") Filter filter, @PluginElement("Layout") Layout<? extends Serializable> layout) {
    if (name == null) {
      return null;
    }

    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new FcCtrlStsNotifyLogAppender(name, filter, layout);
  }

  @Override
  public void append(LogEvent arg0) {

    if (!arg0.getLoggerName().equals(RestClient.class.getName())) {
      LogNotification logNotification = FcCtrlStsNotifyManager.getInstance().getSystemConfData().getLogNotification();
      List<NoticeDestInfoLog> noticeDestList = logNotification.getNoticeDestInfo();
      LogLevelType logLevelType = getLogLevelType(arg0.getLevel());

      for (NoticeDestInfoLog noticeInfo : noticeDestList) {
        if (checkLogLevel(noticeInfo, logLevelType)) {

          int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
          ControllerLogNotificationEntity controller = new ControllerLogNotificationEntity();
          controller.setClusterId(String.valueOf(clusterId));
          controller.setControllerTypeEnum(ControllerLogType.FC_ACT);
          controller.setLogLevelEnum(logLevelType);
          controller.setLogList(getLogList(arg0));

          ControllerLogNotificationRequestBody requestBody = new ControllerLogNotificationRequestBody();
          requestBody.setController(controller);
          String bodyStr = JsonUtil.toJson(requestBody);

          ControllerLogNotificationRequest request = new ControllerLogNotificationRequest(bodyStr, null, null);
          request.setRequestBody(bodyStr);

          CtrlStsNotifySender.sendNotify(noticeInfo.getNoticeAddress(), noticeInfo.getNoticePort(),
              logNotification.getNoticeRetryNum(), logNotification.getNoticeTimeout(), request,
              MfcFcRequestUri.NOTIFY_CONTROLLER_LOG);
        }
      }
    }
  }

  protected LogLevelType getLogLevelType(Level level) {
    LogLevelType type = null;
    if (level.intLevel() == Level.ERROR.intLevel()) {
      type = LogLevelType.ERROR;
    } else if (level.intLevel() == Level.WARN.intLevel()) {
      type = LogLevelType.WARNING;
    } else if (level.intLevel() == Level.INFO.intLevel()) {
      type = LogLevelType.INFO;
    }
    return type;
  }

  protected List<String> getLogList(LogEvent arg0) {
    List<String> strList = new ArrayList<>();
    byte[] bytes = getLayout().toByteArray(arg0);
    String messageLog = new String(bytes).trim();
    strList.add(messageLog);
    return strList;
  }

  protected boolean checkLogLevel(NoticeDestInfoLog noticeInfo, LogLevelType logLevelType) {
    if (noticeInfo.getLogLevel().isError() && logLevelType == LogLevelType.ERROR) {
      return true;
    }
    if (noticeInfo.getLogLevel().isWarning() && logLevelType == LogLevelType.WARNING) {
      return true;
    }
    if (noticeInfo.getLogLevel().isInfo() && logLevelType == LogLevelType.INFO) {
      return true;
    }
    return false;
  }

}
