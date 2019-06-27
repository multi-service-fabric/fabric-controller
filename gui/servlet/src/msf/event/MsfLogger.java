
package msf.event;

import java.text.MessageFormat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * MSF FCログ管理クラス.
 *
 * @author NTT
 *
 */
public class MsfLogger {

  /** ログ出力元パス(クラス)情報. */
  private final String fqcn;

  /** ロガーオブジェクト. */
  private final AbstractLogger logger;

  /**
   * PERFORMANCEログレベル.
   */
  private static final Level PERFORMANCE = Level.forName("PERFORMANCE", 450);

  /**
   * コンストラクタ.
   *
   * @param logger
   *          ロガーオブジェクト
   * @param fqcn
   *          ログ出力元パス(クラス)情報
   */
  private MsfLogger(AbstractLogger logger, String fqcn) {
    this.logger = logger;
    this.fqcn = fqcn;
  }

  /**
   * インスタンスを返す.
   * <p>
   * 引数で指定されたクラスのログ管理クラスインスタンスを返す.
   * </p>
   *
   * @param <T>
   *          クラスモデル
   * @param clazz
   *          ログ出力元クラス
   * @return ログ管理クラスインスタンス
   */
  public static <T> MsfLogger getInstance(Class<T> clazz) {
    final AbstractLogger log4j = (AbstractLogger) LogManager.getLogger(clazz);
    MsfLogger log = new MsfLogger(log4j, MsfLogger.class.getName());
    return log;
  }

  /**
   * FATALログ出力を行う.
   *
   * <pre>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   *
   * 例1) logger.fatal("your message."); -> your message.
   * 例2) logger.fatal(listObject); -> [entity1, entity2]
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   */
  public void fatal(Object object) {
    outputLog(Level.FATAL, object);
  }

  /**
   * FATALログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.fatal("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void fatal(Object object, Throwable exception) {
    outputLog(Level.FATAL, object, exception);
  }

  /**
   * FATALログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.fatal("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String fatal(String patternMessage, Object... args) {
    return outputLog(Level.FATAL, patternMessage, args);
  }

  /**
   * FATALログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.fatal("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String fatal(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.FATAL, patternMessage, exception, args);
  }

  /**
   * ERRORログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.error("your message."); -> "your message."<br>
   * 例2) logger.error(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void error(Object object) {
    outputLog(Level.ERROR, object);
  }

  /**
   * ERRORログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.error("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void error(Object object, Throwable exception) {
    outputLog(Level.ERROR, object, exception);
  }

  /**
   * ERRORログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.error("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String error(String patternMessage, Object... args) {
    return outputLog(Level.ERROR, patternMessage, args);
  }

  /**
   * ERRORログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.error("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String error(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.ERROR, patternMessage, exception, args);
  }

  /**
   * WARNログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.warn("your message."); -> "your message."<br>
   * 例2) logger.warn(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void warn(Object object) {
    outputLog(Level.WARN, object);
  }

  /**
   * WARNログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.warn("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void warn(Object object, Throwable exception) {
    outputLog(Level.WARN, object, exception);
  }

  /**
   * WARNログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.warn("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String warn(String patternMessage, Object... args) {
    return outputLog(Level.WARN, patternMessage, args);
  }

  /**
   * WARNログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.warn("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String warn(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.WARN, patternMessage, exception, args);
  }

  /**
   * INFOログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.info("your message."); -> "your message."<br>
   * 例2) logger.info(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void info(Object object) {
    outputLog(Level.INFO, object);
  }

  /**
   * INFOログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.info("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void info(Object object, Throwable exception) {
    outputLog(Level.INFO, object, exception);
  }

  /**
   * INFOログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.info("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String info(String patternMessage, Object... args) {
    return outputLog(Level.INFO, patternMessage, args);
  }

  /**
   * INFOログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.info("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String info(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.INFO, patternMessage, exception, args);
  }

  /**
   * PERFORMANCEログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.performance("your message."); -> "your message."<br>
   * 例2) logger.performance(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void performance(Object object) {
    outputLog(PERFORMANCE, object);
  }

  /**
   * PERFORMANCEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.performance("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void performance(Object object, Throwable exception) {
    outputLog(PERFORMANCE, object, exception);
  }

  /**
   * PERFORMANCEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.performance("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String performance(String patternMessage, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, args);
  }

  /**
   * PERFORMANCEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.performace("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String performance(String patternMessage, Throwable exception, Object... args) {
    return outputLog(PERFORMANCE, patternMessage, exception, args);
  }

  /**
   * DEBUGログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.debug("your message."); -> "your message."<br>
   * 例2) logger.debug(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void debug(Object object) {
    outputLog(Level.DEBUG, object);
  }

  /**
   * DEBUGログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.debug("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void debug(Object object, Throwable exception) {
    outputLog(Level.DEBUG, object, exception);
  }

  /**
   * DEBUGログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.debug("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String debug(String patternMessage, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, args);
  }

  /**
   * DEBUGログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.debug("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String debug(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.DEBUG, patternMessage, exception, args);
  }

  /**
   * TRACEログ出力を行う.
   * <p>
   * 引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.<br>
   * 例1) logger.trace("your message."); -> "your message."<br>
   * 例2) logger.trace(listObject); -> "[entity1, entity2]"<br>
   * </p>
   *
   * @param object
   *          出力されるログ内容
   */
  public void trace(Object object) {
    outputLog(Level.TRACE, object);
  }

  /**
   * TRACEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたObjectに対し、Object.toString()の内容を出力する.nullは不可.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.trace("Error occurred.", ex);
   *     -> Error occurred.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param object
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   */
  public void trace(Object object, Throwable exception) {
    outputLog(Level.TRACE, object, exception);
  }

  /**
   * TRACEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   *
   * 例) logger.trace("input1={0}, input2={1}.", input1, input2); -> input1=abc, input2=null.
   * </pre>
   *
   * @param patternMessage
   *          出力されるフォーマット指定されたログ内容
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列
   */
  public String trace(String patternMessage, Object... args) {
    return outputLog(Level.TRACE, patternMessage, args);
  }

  /**
   * TRACEログ出力を行う.
   *
   * <pre>
   * 第1引数で渡されたフォーマッタ文字列に対し、第3引数以降の可変引数のObjectの内容を出力する.Objectにはnullを設定可能.
   * 第2引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.trace("input1={0}, input2={1}.", ex, input1, input2);
   *     -> input1=abc, input2=null.
   *        java.io.NullPointerException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param patternMessage
   *          出力されるログ内容
   * @param exception
   *          出力される例外情報
   * @param args
   *          可変引数
   * @return フォーマッタにて整形後の文字列(Throwableは設定されない)
   */
  public String trace(String patternMessage, Throwable exception, Object... args) {
    return outputLog(Level.TRACE, patternMessage, exception, args);
  }

  /**
   * ログレベルがデバッグ以上であるか判定する.
   *
   * @return ログレベルのデバッグレベル以上かどうかの真偽値
   */
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * ログレベルがトレース以上であるか判定する.
   *
   * @return ログレベルのトレースレベル以上かどうかの真偽値
   */
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  /**
   * メソッド開始ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 引数なしの場合は、呼び出し元のメソッド名のみを出力する.
   *
   * 例) logger.methodStart(); -> Start yourCallerMethodName.
   * </pre>
   *
   */
  public void methodStart() {
    // ログレベルDEBUGの時のみ
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  /**
   * メソッド開始ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 呼び出し箇所のメソッド名と、引数で指定した文字列を出力する.
   *
   * 例) logger.methodStart("your message."); -> Start yourCallerMethodName. your message.
   * </pre>
   *
   * @param message
   *          メッセージ
   *
   */
  public void methodStart(String message) {
    // ログレベルDEBUGの時のみ
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "Start " + Thread.currentThread().getStackTrace()[2].getMethodName() + ". " + message);
    }
  }

  /**
   * メソッド開始ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 呼び出し箇所のメソッド名と、呼び出し元メソッドの引数情報を加工して出力する.
   * 第2引数のObject配列内にnullが含まれる場合も正常に動作する.
   *
   * 例) logger.methodStart(new String[] {"p1", "p2", "p3"}, new Object[] {p1, p2, p3} );
   *     -> Start yourCallerMethodName. p1=abc, p2=1, p3=null.
   * </pre>
   *
   * @param params
   *          呼び出し元のメソッドの引数名
   * @param paramValues
   *          呼び出し元のメソッドの引数
   */
  public void methodStart(String[] params, Object[] paramValues) {
    // ログレベルDEBUGの時のみ
    if (logger.isDebugEnabled()) {
      outputLog("Start ", params, paramValues);
    }
  }

  /**
   * メソッド終了ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 引数なしの場合は、呼び出し元のメソッド名のみを出力する.
   *
   * 例) logger.methodEnd(); -> End yourCallerMethodName.
   * </pre>
   */
  public void methodEnd() {
    // ログレベルDEBUGの時のみ
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + ".");
    }
  }

  /**
   * メソッド終了ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 呼び出し箇所のメソッド名と、引数で指定した文字列を出力する.
   *
   * 例) logger.methodEnd("your message."); -> End yourCallerMethodName. your message.
   * </pre>
   *
   * @param message
   *          メッセージ
   */
  public void methodEnd(String message) {
    // ログレベルDEBUGの時のみ
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message);
    }
  }

  /**
   * メソッド終了ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 呼び出し箇所のメソッド名と、第1引数で指定した文字列を出力する.
   * また第2に引数で渡されたThrowableに対して、printStackTraceの内容を出力する.
   *
   * 例) logger.methodEnd("your message.", ex);
   *     -> End yourCallerMethodName. your message.
   *        java.io.IOException: message...
   *           at org.apache.logging.log4j...
   * </pre>
   *
   * @param message
   *          メッセージ
   * @param exception
   *          出力される例外情報
   */
  public void methodEnd(String message, Throwable exception) {
    // ログレベルDEBUGの時のみ処理を行います
    if (logger.isDebugEnabled()) {
      outputLog(Level.DEBUG, "End " + Thread.currentThread().getStackTrace()[2].getMethodName() + "." + message,
          exception);
    }
  }

  /**
   * メソッド終了ログ出力(DEBUGレベル)を行う.
   *
   * <pre>
   * 呼び出し箇所のメソッド名と、呼び出し元メソッドへの戻り値情報を加工して出力する.
   * 第2引数のObject配列内にnullが含まれる場合も正常に動作する.
   *
   * 例) logger.methodEnd(new String[] {"result"}, new Object[] {result} );
   *     -> End yourCallerMethodName. result=false.
   * </pre>
   *
   * <p>
   * メソッド終了時に、メソッド名とそのメソッドの戻り値をログに出力する.
   * </p>
   *
   * @param params
   *          呼び出し元メソッドへの戻り値名
   * @param paramValues
   *          呼び出し元メソッドへの戻り値
   */
  public void methodEnd(String[] params, Object[] paramValues) {
    // ログレベルDEBUGの時のみ処理を行います
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
