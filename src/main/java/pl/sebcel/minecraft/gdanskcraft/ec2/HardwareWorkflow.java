package pl.sebcel.minecraft.gdanskcraft.ec2;

public class HardwareWorkflow {

    public enum Transition {
        START, STOP, DO_NOTHING
    }

    public Transition getTransition(HardwareStatus hardwareStatus, boolean activePlayers, boolean coolDownPeriod) {
        if (hardwareStatus == HardwareStatus.STOPPED && activePlayers) {
            return Transition.START;
        }
        if (hardwareStatus == HardwareStatus.RUNNING && !activePlayers && !coolDownPeriod) {
            return Transition.STOP;
        }
        return Transition.DO_NOTHING;
    }
}