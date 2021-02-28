package pl.sebcel.minecraft.gdanskcraft.serwerwakeupservicestatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import pl.sebcel.minecraft.gdanskcraft.ec2.HardwareStatus;

public class When_parsing_status_string {

    @Test
    public void should_recognize_RUNNING_status() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse(loadFile("status-running.json"));
        Assert.assertEquals(HardwareStatus.RUNNING, result);
    }

    @Test
    public void should_recognize_STOPPED_status() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse(loadFile("status-stopped.json"));
        Assert.assertEquals(HardwareStatus.STOPPED, result);
    }

    @Test
    public void should_recognize_STARTING_status() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse(loadFile("status-starting.json"));
        Assert.assertEquals(HardwareStatus.STARTING, result);
    }

    @Test
    public void should_recognize_STOPPING_status() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse(loadFile("status-stopping.json"));
        Assert.assertEquals(HardwareStatus.STOPPING, result);
    }

    @Test
    public void should_return_UNKNOWN_status_for_null_input() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse(null);
        Assert.assertEquals(HardwareStatus.UNKNOWN, result);
    }

    @Test
    public void should_return_UNKNOWN_status_for_empty_input() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse("");
        Assert.assertEquals(HardwareStatus.UNKNOWN, result);
    }

    @Test
    public void should_return_UNKNOWN_status_for_invalid_JSON() throws FileNotFoundException, URISyntaxException {
        HardwareStatus result = HardwareStatus.parse("asdb");
        Assert.assertEquals(HardwareStatus.UNKNOWN, result);
    }

    private String loadFile(String fileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(fileName);
            try (Scanner s = new Scanner(new File(url.toURI()), "UTF8").useDelimiter("\\Z")) {
                return s.next();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load file " + fileName + ": " + ex.getMessage());
        }
    }
}