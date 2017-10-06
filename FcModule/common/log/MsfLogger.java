package msf.fc.common.log;

import java.text.MessageFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.AbstractLogger;

public class MsfLogger {

  private final AbstractLogger logger;

  private static final Level PERFORMANCE = Level.forName("PERFORMANCE", 450);

  private MsfLogger(AbstractLogger logger, String fqcn) {
    this.logger = logger;
    this.fqcn = fqcn;
  }

  public static <T> MsfLogger getInstance(Class<T> clazz) {
    final AbstractLogger log4j = (AbstractLogger) LogManager.getLogger(clazz);
    MsfLogger log = new MsfLogger(log4j, MsfLogger.class.getName());
    return log;
  }

  public void fatal(Object object) {
    outputLog(Level.FATAL, object);
  }

  public void fatal(Object object, Throwable exception) {
    outputLog(Level.FATAL, object, exception);
  }

  public String fatal(String patternMessage, Object... args) {
    return outputLog(Level.FATAL, patternMessage, args);
  }

  public String fatal(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.FATAL, patternMessage, exception, args);
  }

  public void error(Object object) {
    outputLog(Level.ERROR, object);
  }

  public void error(Object object, Throwable exception) {
    outputLog(Level.ERROR, object, exception);
  }

  public String error(String patternMessage, Object... args) {
    return outputLog(Level.ERROR, patternMessage, args);
  }

  public String error(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.ERROR, patternMessage, exception, args);
  }

  public void warn(Object object) {
    outputLog(Level.WARN, object);
  }

  public void warn(Object object, Throwable exception) {
    outputLog(Level.WARN, object, exception);
  }

  public String warn(String patternMessage, Object... args) {
    return outputLog(Level.WARN, patternMessage, args);
  }

  public String warn(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.WARN, patternMessage, exception, args);
  }

  public void info(Object object) {
    outputLog(Level.INFO, object);
  }

  public void info(Object object, Throwable exception) {
    outputLog(Level.INFO, object, exception);
  }

  public String info(String patternMessage, Object... args) {
    return outputLog(Level.INFO, patternMessage, args);
  }

  public String info(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.INFO, patternMessage, exception, args);
  }

  public void performance(Object object) {
    outputLog(PERFORMANCE, object);
  }

  public void performance(Object object, Throwable exception) {
    outputLog(PERFORMANCE, object, exception);
  }

  public String performance(String patternMessage, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, args);
  }

  public String performance(String patternMessage, Throwable exception, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, exception, args);
  }

  public void debug(Object object) {
    outputLog(Level.DEBUG, object);
  }

  public void debug(Object object, Throwable exception) {
    outputLog(Level.DEBUG, object, exception);
  }

  public String debug(String patternMessage, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, args);
  }

  public String debug(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, exception, args);
  }

  public void trace(Object object) {
    outputLog(Level.TRACE, object);
  }

  public void trace(Object object, Throwable exception) {
    outputLog(Level.TRACE, object, exception);
  }

  public String trace(String patternMessage, Object... args) {
    return outputLog(Level.TRACE, patternMessage, args);
  }

  public String trace(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.TRACE, patternMessage, exception, args);
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  public void methodStart() {
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  public void methodStart(String message) {
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ". " + message);
    }
  }

  public void methodStart(String[] params, Object[] paramValues) {
    if (logger.isDebugEnabled()) {
      outputLog("Start ", params, paramValues);
    }
  }

  public void methodEnd() {
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  public void methodEnd(String message) {
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message);
    }
  }

  public void methodEnd(String message, Throwable exception) {
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message,
          exception);
    }
  }

  public void methodEnd(String[] params, Object[] paramValues) {
    if (logger.isDebugEnabled()) {
      outputLog("End ", params, paramValues);
    }
  }

  private void outputLog(String type, String[] params, Object[] paramValues) {

    StringBuilder builder = new StringBuilder();
    builder.append(type);
    builder.append(Thread.currentThread().getStackTrace()[3].getMethodName());
    builder.append(" (");

    if ((params != null) && (paramValues != null) && (params.length == paramValues.length)) {

      for (int i = 0; i < params.length; i++) {

        builder.append(params[i]);
        builder.append("=");
        builder.append(paramValues[i]);

        if (i + 1 < params.length) {
          builder.append(", ");
        }
      }
    }
    builder.append(").");
    outputLog(Level.DEBUG, builder.toString());
  }

  private void outputLog(Level logLevel, Object object) {
    outputLog(logLevel, object, null);
  }

  private String outputLog(Level logLevel, String patternMessage, Object... args) {
    String message = null;
    if (args.length != 0) {
      message = MessageFormat.format(patternMessage, args);
    } else {
      message = patternMessage;
    }
    outputLog(logLevel, (Object) message, null);

    return message;
  }

  private String outputLog(Level logLevel, String patternMessage, Throwable exception, Object... args) {
    String message = null;
    if (args.length != 0) {
      message = MessageFormat.format(patternMessage, args);
    } else {
      message = patternMessage;
    }
    outputLog(logLevel, message, exception);

    return message;
  }

  private void outputLog(Level logLevel, Object object, Throwable exception) {
    this.logger.logIfEnabled(this.fqcn, logLevel, null, object.toString(), exception);
  }
}
