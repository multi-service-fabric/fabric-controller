
package msf.mfcfc.core.log.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LogDataEntity {

  @SerializedName("occurred_time")
  private String occurredTime;

  @SerializedName("log_level")
  private String logLevel;

  @SerializedName("thread_id")
  private String threadId;

  @SerializedName("class_name")
  private String className;

  @SerializedName("method_name")
  private String methodName;

  @SerializedName("line_number")
  private Integer lineNumber;

  @SerializedName("message")
  private String message;

  private transient String controller;

  private transient String serverName;

  public String getOccurredTime() {
    return occurredTime;
  }

  public void setOccurredTime(String occurredTime) {
    this.occurredTime = occurredTime;
  }

  public String getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Integer getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(Integer lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void appendMessage(String message) {
    this.message = new StringBuffer(this.message).append(message).toString();
  }

  public String getController() {
    return controller;
  }

  public void setController(String controller) {
    this.controller = controller;
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
