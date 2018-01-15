
package msf.mfcfc.common.log;

import java.text.MessageFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * MSF FC log management class.
 *
 * @author NTT
 *
 */
public class MsfLogger {

  private final String fqcn;

  private final AbstractLogger logger;

  private static final Level PERFORMANCE = Level.forName("PERFORMANCE", 450);

  private MsfLogger(AbstractLogger logger, String fqcn) {
    this.logger = logger;
    this.fqcn = fqcn;
  }

  /**
   * Return the instance.
   * <p>
   * Return the log management class instance for the specified class in
   * argument.
   * </p>
   *
   * @param <T>
   *          Class model
   * @param clazz
   *          Log output source class
   * @return the instance of log management class
   */
  public static <T> MsfLogger getInstance(Class<T> clazz) {
    final AbstractLogger log4j = (AbstractLogger) LogManager.getLogger(clazz);
    MsfLogger log = new MsfLogger(log4j, MsfLogger.class.getName());
    return log;
  }

  /**
   * Execute FATAL log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in argument. Setting null is not available.
   *
   * Example 1) logger.fatal("your message."); -> $ your message.
   * Example 2) logger.fatal(listObject); -> $ [entity1, entity2]
   * </pre>
   *
   * @param object
   *          Output log contents
   */
  public void fatal(Object object) {
    outputLog(Level.FATAL, object);
  }

  /**
   * Execute FATAL log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.fatal("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void fatal(Object object, Throwable exception) {
    outputLog(Level.FATAL, object, exception);
  }

  /**
   * Execute FATAL log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.fatal("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String fatal(String patternMessage, Object... args) {
    return outputLog(Level.FATAL, patternMessage, args);
  }

  /**
   * Execute FATAL log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.fatal("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Output exception information
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String fatal(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.FATAL, patternMessage, exception, args);
  }

  /**
   * Execute ERROR log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.error("your message."); ->
   * "your message." Example 2) logger.error(listObject); -> "[entity1,
   * entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void error(Object object) {
    outputLog(Level.ERROR, object);
  }

  /**
   * Execute ERROR log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.error("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void error(Object object, Throwable exception) {
    outputLog(Level.ERROR, object, exception);
  }

  /**
   * Execute ERROR log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.error("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String error(String patternMessage, Object... args) {
    return outputLog(Level.ERROR, patternMessage, args);
  }

  /**
   * Execute ERROR log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.error("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable argument
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String error(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.ERROR, patternMessage, exception, args);
  }

  /**
   * Execute WARN log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.warn("your message."); ->
   * "your message." Example 2) logger.warn(listObject); -> "[entity1, entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void warn(Object object) {
    outputLog(Level.WARN, object);
  }

  /**
   * Execute WARN log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.warn("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void warn(Object object, Throwable exception) {
    outputLog(Level.WARN, object, exception);
  }

  /**
   * Execute WARN log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.warn("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable argument
   * @return post-format string with formatter
   */
  public String warn(String patternMessage, Object... args) {
    return outputLog(Level.WARN, patternMessage, args);
  }

  /**
   * Execute WARN log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.warn("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable parameter
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String warn(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.WARN, patternMessage, exception, args);
  }

  /**
   * Execute INFO log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.info("your message."); ->
   * "your message." Example 2) logger.info(listObject); -> "[entity1, entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void info(Object object) {
    outputLog(Level.INFO, object);
  }

  /**
   * Execute INFO log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.info("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void info(Object object, Throwable exception) {
    outputLog(Level.INFO, object, exception);
  }

  /**
   * Execute INFO log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.info("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String info(String patternMessage, Object... args) {
    return outputLog(Level.INFO, patternMessage, args);
  }

  /**
   * Execute INFO log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.info("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable parameter
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String info(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.INFO, patternMessage, exception, args);
  }

  /**
   * Execute PERFORMANCE log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.performance("your
   * message."); -> "your message." Example 2) logger.performance(listObject);
   * -> "[entity1, entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void performance(Object object) {
    outputLog(PERFORMANCE, object);
  }

  /**
   * Execute PERFORMANCE log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.performance("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void performance(Object object, Throwable exception) {
    outputLog(PERFORMANCE, object, exception);
  }

  /**
   * Execute PERFORMANCE log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.performance("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String performance(String patternMessage, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, args);
  }

  /**
   * Execute PERFORMANCE log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.performace("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable parameter
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String performance(String patternMessage, Throwable exception, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, exception, args);
  }

  /**
   * Execute DEBUG log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.debug("your message."); ->
   * "your message." Example 2) logger.debug(listObject); -> "[entity1,
   * entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void debug(Object object) {
    outputLog(Level.DEBUG, object);
  }

  /**
   * Execute DEBUG log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.debug("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void debug(Object object, Throwable exception) {
    outputLog(Level.DEBUG, object, exception);
  }

  /**
   * Execute DEBUG log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.debug("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String debug(String patternMessage, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, args);
  }

  /**
   * Execute DEBUG log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.debug("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable parameter
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String debug(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, exception, args);
  }

  /**
   * Execute TRACE log output.
   * <p>
   * Output the contents of Object.toString() against Object passed in argument.
   * Setting null is not available. Example 1) logger.trace("your message."); ->
   * "your message." Example 2) logger.trace(listObject); -> "[entity1,
   * entity2]"
   * </p>
   *
   * @param object
   *          Output log contents
   */
  public void trace(Object object) {
    outputLog(Level.TRACE, object);
  }

  /**
   * Execute TRACE log output.
   *
   * <pre>
   * Output the contents of Object.toString() against Object passed in the first argument. Setting null is not available.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.trace("Error occurred.", ex);
   *     -> $ Error occurred.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          Output log contents
   * @param exception
   *          Output exception information
   */
  public void trace(Object object, Throwable exception) {
    outputLog(Level.TRACE, object, exception);
  }

  /**
   * Execute TRACE log output.
   *
   * <pre>
   * Output the Object contents of variable argument against the formatter string passed in the first argument. Setting null is available to Object.
   *
   * Example) logger.trace("input1={0}, input2={1}.", input1, input2); -> $ input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          Format specified log contents to be output
   * @param args
   *          Variable parameter
   * @return post-format string with formatter
   */
  public String trace(String patternMessage, Object... args) {
    return outputLog(Level.TRACE, patternMessage, args);
  }

  /**
   * Execute TRACE log output.
   *
   * <pre>
   * Output the Object contents of variable argument following the third argument against the formatter string passed in the first argument. Setting null is available to Object.
   * Output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.trace("input1={0}, input2={1}.", ex, input1, input2);
   *     -> $ input1=abc, input2=null.
   *        $ java.io.NullPointerException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          Output log contents
   * @param exception
   *          Output exception information
   * @param args
   *          Variable parameter
   * @return post-format string with formatter (Throwable won't be set)
   */
  public String trace(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.TRACE, patternMessage, exception, args);
  }

  /**
   * Judge whether the log level is for debug or more.
   *
   * @return Boolean value of log level whether over debug level or not
   */
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * Judge whether the log level is for trace or more.
   *
   * @return Boolean value of log level whether over trace level or not
   */
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  /**
   * Execute the method start log output (DEBUG level).
   *
   * <pre>
   * In case of no parameter, output the method name of caller only.
   *
   * Example) logger.methodStart(); -> $ Start yourCallerMethodName.
   * </pre>
   *
   */
  public void methodStart() {

    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  /**
   * Execute the method start log output (DEBUG level).
   *
   * <pre>
   * Output the caller method name and string specified in argument.
   *
   * Example) logger.methodStart("your message."); -> $ Start yourCallerMethodName. your message.
   * </pre>
   *
   * @param message
   *          Message
   *
   */
  public void methodStart(String message) {

    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ". " + message);
    }
  }

  /**
   * Execute the method start log output (DEBUG level).
   *
   * <pre>
   * Process and output the caller method name and parameter information of the caller method.
   * Operate normally even when null is included within Object Array of the second argument.
   *
   * Example) logger.methodStart(new String[] {"p1", "p2", "p3"}, new Object[] {p1, p2, p3} );
   *     -> $ Start yourCallerMethodName. p1=abc, p2=1, p3=null.
   * </pre>
   *
   * @param params
   *          Parameter names of the caller method
   * @param paramValues
   *          Parameters of the caller method
   */
  public void methodStart(String[] params, Object[] paramValues) {

    if (logger.isDebugEnabled()) {
      outputLog("Start ", params, paramValues);
    }
  }

  /**
   * Execute the method end log output (DEBUG level).
   *
   * <pre>
   * In case of no parameter, output the method name of caller only.
   *
   * Example) logger.methodEnd(); -> $ End yourCallerMethodName.
   * </pre>
   */
  public void methodEnd() {

    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  /**
   * Execute the method end log output (DEBUG level).
   *
   * <pre>
   * Output the called method name and string specified in argument.
   *
   * Example) logger.methodEnd("your message."); -> $ End yourCallerMethodName. your message.
   * </pre>
   *
   * @param message
   *          Message
   */
  public void methodEnd(String message) {

    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message);
    }
  }

  /**
   * Execute the method end log output (DEBUG level).
   *
   * <pre>
   *  Output the called method name and string specified in the first argument.
   * Also output the contents of printStackTrace against Throwable passed in the second argument.
   *
   * Example) logger.methodEnd("your message.", ex);
   *     -> $ End yourCallerMethodName. your message.
   *        $ java.io.IOException message...
   *        $    at org.apache.logging.log4j...
   * </pre>
   *
   * @param message
   *          Message
   * @param exception
   *          Output exception information
   */
  public void methodEnd(String message, Throwable exception) {

    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message,
          exception);
    }
  }

  /**
   * Execute the method end log output (DEBUG level).
   *
   * <pre>
   * Process and output the called method name, and the return value information to the caller method.
   * Operate normally even when null is included within Object Array of the second argument.
   *
   * Example) logger.methodEnd(new String[] {"result"}, new Object[] {result} );
   *     -> $ End yourCallerMethodName. result=false.
   * </pre>
   *
   * <p>
   * At the end of method, output the method name and its return value to the
   * log.
   * </p>
   *
   * @param params
   *          Return value name to the calling method
   * @param paramValues
   *          Return value to the calling method
   */
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
