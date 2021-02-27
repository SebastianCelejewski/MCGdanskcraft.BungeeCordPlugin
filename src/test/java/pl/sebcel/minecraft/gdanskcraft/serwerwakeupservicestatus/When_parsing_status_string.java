package pl.sebcel.minecraft.gdanskcraft.serwerwakeupservicestatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import pl.sebcel.minecraft.gdanskcraft.ServerWakeUpServiceStatus;

public class When_parsing_status_string {

	@Test
	public void should_recognize_RUNNING_status() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse(loadFile("status-running.json"));
		Assert.assertEquals(ServerWakeUpServiceStatus.RUNNING, result);
	}
	
	@Test
	public void should_recognize_STOPPED_status() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse(loadFile("status-stopped.json"));
		Assert.assertEquals(ServerWakeUpServiceStatus.STOPPED, result);
	}

	@Test
	public void should_recognize_STARTING_status() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse(loadFile("status-starting.json"));
		Assert.assertEquals(ServerWakeUpServiceStatus.STARTING, result);
	}

	@Test
	public void should_recognize_STOPPING_status() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse(loadFile("status-stopping.json"));
		Assert.assertEquals(ServerWakeUpServiceStatus.STOPPING, result);
	}

	@Test
	public void should_return_UNKNOWN_status_for_null_input() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse(null);
		Assert.assertEquals(ServerWakeUpServiceStatus.UNKNOWN, result);
	}

	@Test
	public void should_return_UNKNOWN_status_for_empty_input() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse("");
		Assert.assertEquals(ServerWakeUpServiceStatus.UNKNOWN, result);
	}

	@Test
	public void should_return_UNKNOWN_status_for_invalid_JSON() throws FileNotFoundException, URISyntaxException {
		ServerWakeUpServiceStatus result = ServerWakeUpServiceStatus.parse("asdb");
		Assert.assertEquals(ServerWakeUpServiceStatus.UNKNOWN, result);
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