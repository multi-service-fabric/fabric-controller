
package msf.mfcfc.rest.common;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Implementation class which provides a function of deciding parameters that
 * are not serialized to JSON.
 *
 * @author NTT
 *
 */
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
