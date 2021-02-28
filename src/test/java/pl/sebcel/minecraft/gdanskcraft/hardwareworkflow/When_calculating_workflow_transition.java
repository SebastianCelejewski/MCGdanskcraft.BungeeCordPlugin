package pl.sebcel.minecraft.gdanskcraft.hardwareworkflow;

import org.junit.Assert;
import org.junit.Test;

import pl.sebcel.minecraft.gdanskcraft.ec2.HardwareStatus;
import pl.sebcel.minecraft.gdanskcraft.ec2.HardwareWorkflow;

public class When_calculating_workflow_transition {

    private HardwareWorkflow workflow = new HardwareWorkflow();

    @Test
    public void should_return_DO_NOTHING_if_hardware_status_is_STARTING_regardless_of_number_of_players_and_cooldown_period() {
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STARTING, false, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STARTING, false, true));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STARTING, true, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STARTING, true, true));
    }

    @Test
    public void should_return_DO_NOTHING_if_hardware_status_is_STOPPING_regardless_of_number_of_players_and_cooldown_period() {
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPING, false, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPING, false, true));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPING, true, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPING, true, true));
    }

    @Test
    public void should_return_START_if_hardware_status_is_STOPPED_and_there_are_players_on_the_server() {
        Assert.assertEquals(HardwareWorkflow.Transition.START, workflow.getTransition(HardwareStatus.STOPPED, true, false));
        Assert.assertEquals(HardwareWorkflow.Transition.START, workflow.getTransition(HardwareStatus.STOPPED, true, true));
    }

    @Test
    public void should_return_DO_NOTHING_if_hardware_status_is_STOPPED_and_there_are_no_players_on_the_server() {
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPED, false, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.STOPPED, false, true));
    }

    @Test
    public void should_return_STOP_if_hardware_status_is_RUNNING_and_there_are_no_players_on_the_server_for_longer_time() {
        Assert.assertEquals(HardwareWorkflow.Transition.STOP, workflow.getTransition(HardwareStatus.RUNNING, false, false));
    }

    @Test
    public void should_return_DO_NOTHING_if_hardware_status_is_RUNNING_and_there_are_no_players_on_the_server_but_were_recently() {
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.RUNNING, false, true));
    }

    @Test
    public void should_return_DO_NOTHING_if_hardware_status_is_RUNNING_and_there_are_players() {
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.RUNNING, true, false));
        Assert.assertEquals(HardwareWorkflow.Transition.DO_NOTHING, workflow.getTransition(HardwareStatus.RUNNING, true, true));
    }
}
