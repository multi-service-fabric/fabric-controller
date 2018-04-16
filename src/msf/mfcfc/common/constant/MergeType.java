
package msf.mfcfc.common.constant;

public enum MergeType {

  SEPARATE("separate"),

  MERGE("merge");

  private String message;

  private MergeType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public static MergeType getEnumFromMessage(String message) {
    for (MergeType enumValue : values()) {
      if (enumValue.getMessage().equals(message)) {
        return enumValue;
      }
    }

    return null;
  }

}
