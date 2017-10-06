package msf.fc.rest.common;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;

public class JsonUtil {

  private static final MsfLogger logger = MsfLogger.getInstance(JsonUtil.class);

  private static Gson gson = null;

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

  public static <T> T fromJson(String json, Class<T> clazz) throws MsfException {
    return fromJson(json, clazz, ErrorCode.PARAMETER_FORMAT_ERROR);
  }

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

  public static <T> T fromJson(String json, TypeToken<T> typeToken) throws MsfException {
    return fromJson(json, typeToken, ErrorCode.PARAMETER_FORMAT_ERROR);
  }

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
        GsonBuilder gsonBuilder = new GsonBuilder();
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
