
package msf.mfcfc.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * TreeMap with timeout.
 *
 * @param <K>
 *          Timeout map key
 * @param <V>
 *          Timeout map value
 */
public class TimeoutMap<K, V> extends TreeMap<K, V> {

  long timeout;
  HashMap<K, Long> timeMap;

  /**
   * Store within the specified time only.
   *
   * @param timeout
   *          Time to store (ms)
   */
  public TimeoutMap(long timeout) {
    super();
    this.timeout = timeout;
    this.timeMap = new HashMap<K, Long>();
  }

  /**
   * Returns the first key. Timed out keys do not apply.
   *
   * @return the first key
   */
  public K firstKey() {
    timeCheck();
    return super.firstKey();
  }

  /**
   * Returns the last key. Timed out keys do not apply.
   *
   * @return the last key
   */
  public K lastKey() {
    timeCheck();
    return super.lastKey();
  }

  /**
   * Returns the value of the specified key. Returns null if the specified key
   * is timed out.
   *
   * @param key
   *          Key
   * @return value for the specified key
   */
  public V get(Object key) {
    timeCheck();
    return super.get(key);
  }

  /**
   * Register the specified key and specified value pairs.
   *
   * @param key
   *          Key
   * @param value
   *          Value
   * @return the registered value
   *
   */
  public V put(K key, V value) {
    timeCheck();
    timeMap.put(key, System.currentTimeMillis());
    return super.put(key, value);
  }

  /**
   * Delete the specified key.
   *
   * @param key
   *          Key
   * @return value
   */
  public V remove(Object key) {
    timeCheck();
    timeMap.remove(key);
    return super.remove(key);
  }

  /**
   * Get the size of the specified key.
   *
   * @return size
   */
  public int size() {
    timeCheck();
    return super.size();
  }

  protected void timeCheck() {

    Iterator<K> ite = timeMap.keySet().iterator();
    while (ite.hasNext()) {
      K putKey = ite.next();
      Long putTime = timeMap.get(putKey);
      long diff = System.currentTimeMillis() - putTime;
      if (diff > timeout) {
        ite.remove();
        super.remove(putKey);
      }
    }
  }

  /**
   * Returns the registered time of the specified key.
   *
   * @param key
   *          Key
   * @return the registered time
   */
  public Date getTime(K key) {
    Long time = timeMap.get(key);
    if (time == null) {
      return null;
    }
    return new Date(time);
  }

  /**
   * Extend the time of specified key.
   *
   * @param key
   *          Key
   */
  public void updateTime(K key) {
    timeMap.put(key, System.currentTimeMillis());
  }
}
