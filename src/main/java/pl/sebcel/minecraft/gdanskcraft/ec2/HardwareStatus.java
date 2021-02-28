package pl.sebcel.minecraft.gdanskcraft.ec2;

import org.json.JSONObject;

public enum HardwareStatus {

    UNKNOWN(""), STOPPED("stopped"), STARTING("starting"), RUNNING("running"), STOPPING("stopping");

    private HardwareStatus(String statusName) {
        this.statusName = statusName;
    }

    private String statusName;

    public static HardwareStatus parse(String statusString) {
        if (statusString == null || statusString.trim().length() == 0) {
            return HardwareStatus.UNKNOWN;
        }

        try {
            JSONObject status = new JSONObject(statusString);
            String statusName = status.getJSONObject("message").getString("Name");
            for (HardwareStatus value : HardwareStatus.values()) {
                if (value.statusName.equals(statusName)) {
                    return value;
                }
            }
        } catch (Exception ex) {
            return HardwareStatus.UNKNOWN;
        }
        return HardwareStatus.UNKNOWN;
    }
}