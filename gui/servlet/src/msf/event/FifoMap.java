package msf.event;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class FifoMap<K,V> extends LinkedHashMap<K,V> {

	int max;

	private static final long serialVersionUID = 1L;

	public FifoMap(int max) {
		super(max + 1);
		this.max = max;
	}

	@Override
	public V put(K key, V value) {
		V forReturn = super.put(key, value);
		if (super.size() > max) {
			removeEldest();
		}
		return forReturn;
	}

	private void removeEldest() {
		Iterator<K> iterator = this.keySet().iterator();
		if (iterator.hasNext()) {
			this.remove(iterator.next());
		}
	}

}