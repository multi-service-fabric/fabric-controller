
package msf.mfcfc.rest.common;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Utility class to execute the mutual conversion process between JSON character
 * string and Java object.
 *
 * @author NTT
 *
 */
public class JsonUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(JsonUtil.class);

  private static Gson gson = null;

  /**
   * Convert the specified Java object to Json string.
   *
   * @param <T>
   *          Class type of Java object conversion target
   * @param src
   *          Java object instance
   * @return Json string
   * @throws JsonIOException
   *           Input-output exception that occurs when converting to Json
   *           (RuntimeException)
   */
  public static <T> String toJson(T src) throws JsonIOException {
    try {
      logger.methodStart(new String[] { "src" }, new Object[] { src });
      Gson gson = createGson();
      String json = gson.toJson(src);
      logger.debug("json=" + json);
      return json;

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Convert the specified Json string to Java object of the specified type.
   *
   * @param <T>
   *          Class type of Java object conversion target
   * @param json
   *          Json string for Java object conversion target
   * @param clazz
   *          Class type of conversion target Java
   * @return Json string converted Java object instance
   * @throws MsfException
   *           If syntax error occurred (the error code is for parameter setting
   *           error)
   */
  public static <T> T fromJson(String json, Class<T> clazz) throws MsfException {
    return fromJson(json, clazz, ErrorCode.PARAMETER_FORMAT_ERROR);
  }

  /**
   * Convert the specified Json string to Java object of the specified type.
   *
   * @param <T>
   *          Class type of Java object conversion target
   * @param json
   *          Json string for Java object conversion target
   * @param clazz
   *          Class type of conversion target Java
   * @param errorCode
   *          Error code to specify for MsfException when an exception occurs
   * @return Json string converted Java object instance
   * @throws MsfException
   *           If syntax error occurred
   */
  public static <T> T fromJson(String json, Class<T> clazz, ErrorCode errorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "json", "clazz", "errorCode" }, new Object[] { json, clazz, errorCode });

      Objects.requireNonNull(errorCode, "error code is null.");
      Gson gson = createGson();
      T javaObject = gson.fromJson(json, clazz);

      if (null == javaObject) {
        String logMsg = logger.error("parameter syntax error. parameter = {0}", json);
        throw new MsfException(errorCode, logMsg);
      }
      return javaObject;
    } catch (JsonSyntaxException exp) {
      String logMsg = logger.error("parameter syntax error. parameter = {0}", exp, json);
      throw new MsfException(errorCode, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Convert the specified Json string to Java object according to the type
   * specified by TypeToken. To be used when Class is not available to express,
   * e.g. in case of converting Json string to List type.
   *
   * @param <T>
   *          Class type of Java object conversion target
   * @param json
   *          Json string for Java object conversion target
   * @param typeToken
   *          Class to get the type of conversion target Java class
   * @return Json string converted Java object instance
   * @throws MsfException
   *           If syntax error occurred (the error code is for parameter setting
   *           error)
   */
  public static <T> T fromJson(String json, TypeToken<T> typeToken) throws MsfException {
    return fromJson(json, typeToken, ErrorCode.PARAMETER_FORMAT_ERROR);
  }

  /**
   * Convert the specified Json string to Java object according to the type
   * specified by TypeToken. To be used when Class is not available to express,
   * e.g., in case of converting Json string to List type.
   *
   * @param <T>
   *          Class type of Java object conversion target
   * @param json
   *          Json string for Java object conversion target
   * @param typeToken
   *          Class to get the type of conversion target Java class
   * @param errorCode
   *          Error code to specify for MsfException when an exception occurs
   * @return Json string converted Java object instance
   * @throws MsfException
   *           If syntax error occurred
   */
  public static <T> T fromJson(String json, TypeToken<T> typeToken, ErrorCode errorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "json", "typeToken", "errorCode" },
          new Object[] { json, typeToken, errorCode });

      Objects.requireNonNull(errorCode, "error code is null.");
      Gson gson = createGson();
      T javaObject = gson.fromJson(json, typeToken.getType());

      if (null == javaObject) {
        String logMsg = logger.error("parameter syntax error. parameter = {0}", json);
        throw new MsfException(errorCode, logMsg);
      }
      return javaObject;
    } catch (JsonSyntaxException exp) {
      String logMsg = logger.error("parameter syntax error. parameter = {0}", exp, json);
      throw new MsfException(errorCode, logMsg);
    } finally {
      logger.methodEnd();
    }
  }

  private static Gson createGson() {
    try {
      logger.methodStart();

      if (gson == null) {
        GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new MsfExclusionStrategy());

        boolean isPrettyPrinting = ConfigManager.getInstance().isPrettyPrinting();
        if (isPrettyPrinting) {
          gsonBuilder.setPrettyPrinting();
        }

        boolean isSerializeNulls = ConfigManager.getInstance().isSerializeNulls();
        if (isSerializeNulls) {
          gsonBuilder.serializeNulls();
        }
        gson = gsonBuilder.create();
      }
      return gson;
    } finally {
      logger.methodEnd();
    }
  }

}
