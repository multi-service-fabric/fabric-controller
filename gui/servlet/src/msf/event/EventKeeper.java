package msf.event;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;

public class EventKeeper {
	private static EventKeeper eventKeeper;

	int eventId;
	FifoMap<Integer, Notification> fifoMap;

	public EventKeeper(int max) {
		eventKeeper = this;
		eventId = 0;
		fifoMap = new FifoMap<Integer, Notification>(max);
	}

	public static synchronized String getEventId() {
		return "" + eventKeeper.eventId;
	}

	public static synchronized void put(String type, JsonObject json) {
		eventKeeper.eventId++;
		eventKeeper.fifoMap.put(eventKeeper.eventId, new Notification("" + eventKeeper.eventId, type, json));
	}

	public static synchronized List<Notification> get(int id) {
		List<Notification> list = new LinkedList<Notification>();
		for (Integer eid : eventKeeper.fifoMap.keySet()) {
			if (eid > id) {
				list.add(eventKeeper.fifoMap.get(eid));
			}
		}
		return list;
	}
}
