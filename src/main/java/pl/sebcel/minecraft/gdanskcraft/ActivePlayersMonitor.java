package pl.sebcel.minecraft.gdanskcraft;

import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ActivePlayersMonitor {

	private final static Logger logger = Logger.getLogger(ActivePlayersMonitor.class.getName());
	
	private ServerWakeUpServiceProxy serviceProxy;
	private Thread monitoringThread;
	
	private long lastTimePlayersWereConnected;
	
	private enum Status { NEW, STARTED, COOL_DOWN, STOPPED };
	private Status previousStatus = Status.NEW;
	
	
	public ActivePlayersMonitor(ServerWakeUpServiceProxy serviceProxy) {
		this.serviceProxy = serviceProxy;
	}

	public void initialize(ProxyServer minecraftServer, String lobbyServerSymbol, String defaultServerSymbol, int serverCoolDownPeriodInSeconds) {
		lastTimePlayersWereConnected = new Date().getTime();
		
		this.monitoringThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {

					try {
						int numberOfPlayers = minecraftServer.getOnlineCount();
						ServerInfo defaultServer = minecraftServer.getServers().get(defaultServerSymbol);

						minecraftServer.getPlayers().forEach(new Consumer<ProxiedPlayer>() {

							@Override
							public void accept(ProxiedPlayer p) {
								try {
									if (lobbyServerSymbol.equals(p.getServer().getInfo().getName())) {
										p.sendMessage(TextComponent.fromLegacyText("Za chwile bedziesz przeniesiony do docelowego serwera. Trzeba zaczekac az serwer sie uruchomi."));
										p.sendMessage(TextComponent.fromLegacyText("Status maszyny, na ktorej uruchomiony bedzie serwer: " + serviceProxy.getStatus()));
										p.sendMessage(TextComponent.fromLegacyText("Jesli czekasz dluzej niz 1 minute, wyslij zgloszenie o problemie na Discordzie lub na adres email Sebastian.Celejewski@wp.pl"));
										logger.info("Trying to send player " + p.getName() + " to server " + defaultServerSymbol);
										
										p.connect(defaultServer);
									}
								} catch (Exception ex) {
									logger.info("Failed to send player " + p.getName() + " to server " + defaultServerSymbol + ": " + ex.getMessage());
								}
							}
						});
						
						if (numberOfPlayers > 0) {
							if (previousStatus != Status.STARTED) {
								logger.info("Starting server.");
								serviceProxy.startServer();
								previousStatus = Status.STARTED;
							}
							lastTimePlayersWereConnected = new Date().getTime();
						} else {
							if ((new Date().getTime() - lastTimePlayersWereConnected) < serverCoolDownPeriodInSeconds * 1000) {
								logger.info("No players detected. Server will shut down soon.");
							} else {
								if (previousStatus != Status.STOPPED) {
									logger.info("No players detected. Shutting down the server.");
									serviceProxy.stopServer();
									previousStatus = Status.STOPPED;
								}
							}
						}
					} catch (Exception ex) {
						logger.severe("Failed to send players to the target server: " + ex.getMessage());
						ex.printStackTrace();
					}

					try {
						Thread.sleep(10000);
					} catch (Exception ex) {
						// intentional
					}
				}
			}
		});

		logger.info("Starting a thread monitoring number of active players");
		
		this.monitoringThread.start();
	}
}