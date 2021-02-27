package pl.sebcel.minecraft.gdanskcraft;

import java.util.Date;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;

public class HardwareStatusManager implements Runnable {
	
	private final static Logger logger = Logger.getLogger(HardwareStatusManager.class.getName());
	
	private final static int PLAYERS_COUNT_CHECK_PERIOD_IN_SECONDS = 10;

	private enum Status { NEW, STARTED, COOL_DOWN, STOPPED };
	
	private ProxyServer proxyServer;
	private ServerWakeUpServiceProxy serviceProxy;
	
	private Status previousStatus = Status.NEW;
	private long lastTimePlayersWereConnected;
	private int serverCoolDownPeriodInSeconds;
	
	public void initialize(ProxyServer proxyServer, ServerWakeUpServiceProxy serviceProxy, int serverCoolDownPeriodInSeconds) {
		if (proxyServer == null) {
			throw new IllegalArgumentException("Argument proxyServer can not be null");
		}
		if (serviceProxy == null) {
			throw new IllegalArgumentException("Argument serviceProxy can not be null");
		}
		this.proxyServer = proxyServer;
		this.serviceProxy = serviceProxy;
		this.serverCoolDownPeriodInSeconds = serverCoolDownPeriodInSeconds;
		this.lastTimePlayersWereConnected = new Date().getTime();		
	}
	
	@Override
	public void run() {
		logger.info("Starting Hardware Status Manager");
		
		while(true) {
			int numberOfPlayers = proxyServer.getOnlineCount();
			logger.info("Number of active players: " + numberOfPlayers);
			
			if (numberOfPlayers > 0) {
				logger.info("Sending request to start the server.");
				serviceProxy.startServer();
				previousStatus = Status.STARTED;
				lastTimePlayersWereConnected = new Date().getTime();
			} else {
				if ((new Date().getTime() - lastTimePlayersWereConnected) < serverCoolDownPeriodInSeconds * 1000) {
					logger.info("No players detected. Will send request to shut down the server soon.");
				} else {
					if (previousStatus != Status.STOPPED) {
						logger.info("No players detected. Sending request to shut down the server.");
						serviceProxy.stopServer();
						previousStatus = Status.STOPPED;
					}
				}
			}
			
			try {
				Thread.sleep(PLAYERS_COUNT_CHECK_PERIOD_IN_SECONDS * 1000);
			} catch (Exception ex) {
				// intentional
			}
		}
	}
}