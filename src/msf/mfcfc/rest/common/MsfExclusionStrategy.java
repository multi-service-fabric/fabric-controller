
package msf.mfcfc.rest.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class MsfExclusionStrategy implements ExclusionStrategy {

  @Override
  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
    return fieldAttributes.getAnnotation(MsfExclude.class) != null;
  }

  @Override
  public boolean shouldSkipClass(Class<?> clazz) {
    return false;
  }

}
