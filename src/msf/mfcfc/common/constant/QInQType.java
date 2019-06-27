
package msf.mfcfc.common.constant;

public enum QInQType {

  Q_IN_Q_ONLY(1, "q_in_q_only"),

  Q_IN_Q_SUPPORT(2, "q_in_q_support"),

  Q_IN_Q_UNSUPPORT(-1, "q_in_q_unsupport");

  private int code;

  private String message;

  private QInQType(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public static QInQType getEnumFromCode(Integer code) {
    if (code == null) {
      return Q_IN_Q_UNSUPPORT;
    }
    for (QInQType enumValue : values()) {
      if (enumValue.getCode() == code) {
        return enumValue;
      }
    }

    return null;
  }

  public static QInQType getEnumFromMessage(String message) {
    if (message == null) {
      return QInQType.Q_IN_Q_UNSUPPORT;
    }
    for (QInQType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

  public static QInQType getEnumFromEcMessage(String ecMessage) {
    return getEnumFromMessage(ecMessage);
  }
}
