package msf.event;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 通知取得のレスポンス.
 *
 * @author NTT
 *
 */
public class Event {
	@SerializedName("last_event_id")
	String lastEventId;

	@SerializedName("events")
	List<Notification> notifications;

	public Event(String eventId) {
		this.lastEventId = eventId;
	}

	public String getLastEventId() {
		return lastEventId;
	}

	public void setLastEventId(String lastEventId) {
		this.lastEventId = lastEventId;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

}
