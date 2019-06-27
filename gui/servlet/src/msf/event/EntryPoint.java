package msf.event;


import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Restで「/v1」パスを受信するクラス.
 *
 * @author NTT
 *
 */
@Path("/v1")
public class EntryPoint {

	private static final MsfLogger logger = MsfLogger.getInstance(EntryPoint.class);

	private JsonParser parser= new JsonParser();

	/**
	 * 000101 オペレーション結果通知を受信する.
	 * @param operationId
	 * @param body
	 * @return
	 */
	@PUT
	@Path("operations/{operation_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response operations(@PathParam("operation_id") String operationId, String body) {
		return save("operations", body);
	}

	/**
	 * 000201 コントローラ運用状態通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response status(String body) {
		return save("status", body);
	}

	/**
	 * 010101 トラヒック情報通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/traffic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response traffic(String body) {
		return save("traffic", body);
	}

	/**
	 * 020101 障害情報通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/failure_status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response failureStatus(String body) {
		return save("failure_status", body);
	}

	/**
	 * 040101 サイレント故障通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/silent_failure")
	@Produces(MediaType.APPLICATION_JSON)
	public Response silentFailure(String body) {
		return save("silent_failure", body);
	}

	/**
	 * 050101 装置OSアップグレード通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/upgrade_operations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response upgradeOperations(String body) {
		return save("upgrade_operations", body);
	}

	/**
	 * 060101 コントローラ異常通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/controller_status_notification/failure")
	@Produces(MediaType.APPLICATION_JSON)
	public Response controllerStatusNotificationFailure(String body) {
		return save("controller_status_notification/failure", body);
	}

	/**
	 * 060201 コントローラログ通知を受信する.
	 * @param body
	 * @return
	 */
	@PUT
	@Path("MSFcontroller/controller_status_notification/log")
	@Produces(MediaType.APPLICATION_JSON)
	public Response controllerStatusNotificationLog(String body) {
		return save("controller_status_notification/log", body);
	}

	private Response save(String eventType, String body) {
		try {
			logger.methodStart();
			logger.info("eventType = " + eventType + " body = " + body);
			JsonObject jsonObject = parser.parse(body).getAsJsonObject();

			EventKeeper.put(eventType, jsonObject);
			return Response.ok().build();
		} catch (Exception e) {
			logger.warn("rest parse failed.", e);
			int status = 400;
			return Response.status(status).build();
		} finally {
			logger.methodEnd();
		}
	}

	/**
	 * 000101 通知取得を受信する.
	 * このメソッドのみブラウザからのRESTアクセスを想定.
	 * 指定イベントIDより大きいイベントが存在する場合、そのイベントリストを返す.
	 * @param eventId 指定イベントID
	 * @return
	 */
	@GET
	@Path("event/{event_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response event(@PathParam("event_id") String eventId) {
		try {
			logger.methodStart();
			Integer eid = Integer.parseInt(eventId);

			Event event = new Event(EventKeeper.getEventId());
			event.setNotifications(EventKeeper.get(eid));

			// Gsonを利用せずに、Eventを返しても、EventのJsonを返却することはできるが、順番が守られない。
			Gson gson = new Gson();
			String json = gson.toJson(event);
			logger.info("json = " + json);

			return Response.ok(json).build();
		} catch (Exception e) {
			logger.warn("exp", e);
			int status = 400;
			return Response.status(status).build();
		} finally {
			logger.methodEnd();
		}
	}

}
