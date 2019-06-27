
package msf.mfcfc.common.util;

/**
 * A simple tuple class for storing a pair of elements of specified types.
 *
 * @author NTT
 *
 * @param <T>
 *          Type of the first element
 * @param <U>
 *          Type of the second element
 */
public class Tuple<T, U> {

  private T element1;

  private U element2;

  public Tuple(T element1, U element2) {
    this.element1 = element1;
    this.element2 = element2;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((element1 == null) ? 0 : element1.hashCode());
    result = prime * result + ((element2 == null) ? 0 : element2.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null) {
      Tuple<?, ?> other = (Tuple<?, ?>) obj;
      return (element1.equals(other.element1) && element2.equals(other.element2));
    } else {
      return false;
    }

  }

  /**
   * Get the first element.
   *
   * @return the first element
   */
  public T getElement1() {
    return element1;
  }

  /**
   * Get the second element
   *
   * @return the second element
   */
  public U getElement2() {
    return element2;
  }

}
