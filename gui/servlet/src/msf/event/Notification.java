package msf.event;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Notification {
	transient SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

	@SerializedName("event_id")
	String eventId;

	@SerializedName("event_type")
	String notificationType;

	@SerializedName("occurred_time")
	String occurredTime;

	@SerializedName("body")
	JsonObject body;

	public Notification(String eventId, String notificationType, JsonObject body) {
		this.eventId = eventId;
		this.notificationType = notificationType;
		this.occurredTime = writeDateFormat.format(new Date());
		this.body = body;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public JsonObject getBody() {
		return body;
	}

	public void setBody(JsonObject body) {
		this.body = body;
	}

}
