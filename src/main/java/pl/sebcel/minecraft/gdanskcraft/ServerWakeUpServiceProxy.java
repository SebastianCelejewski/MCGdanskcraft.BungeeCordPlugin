package pl.sebcel.minecraft.gdanskcraft;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ServerWakeUpServiceProxy {

	private final static Logger logger = Logger.getLogger(ActivePlayersMonitor.class.getName());

	private String url;
	private String apiKey;
	private String instanceName;

	public void initialize(String url, String apiKey, String instanceName) {
		this.url = url;
		this.apiKey = apiKey;
		this.instanceName = instanceName;
	}

	public void startServer() {
		logger.info("Starting Minecraft server (instanceName: " + instanceName + ")");
		String request = url + "?action=start&instanceName=" + instanceName;
		sendRequest(request);
	}

	public void stopServer() {
		logger.info("Stopping Minecraft server (instanceName: " + instanceName + ")");
		String request = url + "?action=stop&instanceName=" + instanceName;
		sendRequest(request);
	}

	public ServerWakeUpServiceStatus getStatus() {
		logger.info("Getting status of Minecraft server (instanceName: " + instanceName + ")");
		String request = url + "?action=getStatus&instanceName=" + instanceName;
		String status = sendRequest(request);
		return ServerWakeUpServiceStatus.parse(status);
	}

	private String sendRequest(String request) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(request);
			httpPost.addHeader("x-api-key", apiKey);

			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			logger.info("Failed to send request to the server: " + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
}