package pl.sebcel.minecraft.gdanskcraft;

import org.json.JSONObject;

public enum ServerWakeUpServiceStatus {

	UNKNOWN(""), STOPPED("stopped"), STARTING("starting"), RUNNING("running"), STOPPING("stopping");

	private ServerWakeUpServiceStatus(String statusName) {
		this.statusName = statusName;
	}

	private String statusName;

	public static ServerWakeUpServiceStatus parse(String statusString) {
		if (statusString == null || statusString.trim().length() == 0) {
			return ServerWakeUpServiceStatus.UNKNOWN;
		}

		try {
			JSONObject status = new JSONObject(statusString);
			String statusName = status.getJSONObject("message").getString("Name");
			for (ServerWakeUpServiceStatus value : ServerWakeUpServiceStatus.values()) {
				if (value.statusName.equals(statusName)) {
					return value;
				}
			}
		} catch (Exception ex) {
			return ServerWakeUpServiceStatus.UNKNOWN;
		}
		return ServerWakeUpServiceStatus.UNKNOWN;
	}
}