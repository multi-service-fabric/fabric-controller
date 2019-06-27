
package msf.mfcfc.core.log.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.LogLevel;
import msf.mfcfc.common.constant.LogType;
import msf.mfcfc.core.scenario.RestRequestBase;

public class LogReadRequest extends RestRequestBase {

  private String logType;

  private String logLevel;

  private String controller;

  private String cluster;

  private String startDate;

  private String endDate;

  private Integer limitNumber;

  private String searchString;

  private String mergeType;

  public LogReadRequest(String requestBody, String notificationAddress, String notificationPort, String logType,
      String logLevel, String controller, String cluster, String startDate, String endDate, Integer limitNumber,
      String searchString, String mergeType) {
    super(requestBody, notificationAddress, notificationPort);
    this.logType = logType;
    this.logLevel = logLevel;
    this.controller = controller;
    this.cluster = cluster;
    this.startDate = startDate;
    this.endDate = endDate;
    this.limitNumber = limitNumber;
    this.searchString = searchString;
    this.mergeType = mergeType;
  }

  public String getLogType() {
    return logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }

  public String getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }

  public String getController() {
    return controller;
  }

  public void setController(String controller) {
    this.controller = controller;
  }

  public String getCluster() {
    return cluster;
  }

  public void setCluster(String cluster) {
    this.cluster = cluster;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Integer getLimitNumber() {
    return limitNumber;
  }

  public void setLimitNumber(Integer limitNumber) {
    this.limitNumber = limitNumber;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public String getMergeType() {
    return mergeType;
  }

  public void setMergeType(String mergeType) {
    this.mergeType = mergeType;
  }

  public LogType getLogTypeEnum() {
    return LogType.getEnumFromMessage(logType);
  }

  public void setLogTypeEnum(LogType logType) {
    this.logType = logType.getMessage();
  }

  public LogLevel getLogLevelEnum() {
    return LogLevel.getEnumFromMessage(logLevel);
  }

  public void setLogLevelEnum(LogLevel logLevel) {
    this.logLevel = logLevel.getMessage();
  }

  public ControllerType getControllerEnum() {
    return ControllerType.getEnumFromMessage(controller);
  }

  public void setControllerEnum(ControllerType controller) {
    this.controller = controller.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
