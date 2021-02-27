package pl.sebcel.minecraft.gdanskcraft;

import java.util.Optional;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class PluginEventHandler implements Listener {
	
	private final static Logger logger = Logger.getLogger(PluginEventHandler.class.getName());
	
	private BungeeCordPlugin plugin;
	
	private ProxyServer proxyServer;

	private PluginConfig pluginConfig;
	
	public void initialize(BungeeCordPlugin plugin, PluginConfig pluginConfig, ProxyServer proxyServer) {
		if (plugin == null) {
			throw new IllegalArgumentException("Argument plugin can not be null");
		}
		if (pluginConfig == null) {
			throw new IllegalArgumentException("Argument pluginConfig can not be null");
		}
		if (proxyServer == null) {
			throw new IllegalArgumentException("Argument proxyServer can not be null");
		}
		this.plugin = plugin;
		this.pluginConfig = pluginConfig;
		this.proxyServer = proxyServer;
	}
	
	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		try {
			String message = new String(event.getData());
			
			if (!event.getTag().equals("gdanskcraft:main")) {
				return;
			}
			
			logger.info(message);
			
			String[] currentPlayerTokens = message.split(",");
			String playerName = currentPlayerTokens[0].substring(2);
			int dimension = Integer.parseInt(currentPlayerTokens[1]);
			int x = Integer.parseInt(currentPlayerTokens[2]);
			int y = Integer.parseInt(currentPlayerTokens[3]);
			int z = Integer.parseInt(currentPlayerTokens[4]);

			Optional<ProxiedPlayer> maybePlayer = proxyServer.getPlayers().stream().filter(player -> player.getName().equals(playerName)).findFirst();
			
			if (!maybePlayer.isPresent()) {
				logger.info("Player " + playerName + " not found on any server. Strange. Aborting");
				return;
			}
			
			ProxiedPlayer p = maybePlayer.get();

			String playerCurrentServer = p.getServer().getInfo().getName();
			
			Configuration interWorldPortals = pluginConfig.getInterWorldPortals();
			Configuration currentServerPortals = (Configuration) interWorldPortals.get(playerCurrentServer);
			
			for(String targetServerSymbol : currentServerPortals.getKeys()) {
				String targetServerCoordinates = currentServerPortals.getString(targetServerSymbol);
				String[] targetServerTokens = targetServerCoordinates.split(",");
				int targetDimension = Integer.parseInt(targetServerTokens[0]);
				int targetX = Integer.parseInt(targetServerTokens[1]);
				int targetY = Integer.parseInt(targetServerTokens[2]);
				int targetZ = Integer.parseInt(targetServerTokens[3]);
				
				double distance = Math.sqrt((x - targetX)*(x-targetX) + (y - targetY)*(y-targetY) + (z - targetZ)*(z - targetZ));
				
				logger.info("Distance: " + distance);

				if (dimension == targetDimension && distance < 4.0) {
					ServerInfo server = this.plugin.getProxy().getServers().get(targetServerSymbol);
					if (pluginConfig.isInterWorldPortalsEnabled()) {
						logger.info("Sending player " + playerName + " to server " + targetServerSymbol);
						p.connect(server);
					} else {
						logger.info("Would send player " + playerName + " to server " + targetServerSymbol + " but not sending (functionality is disabled)");
					}
				}
			}
		} catch (Exception ex) {
			logger.warning("Failed to handle message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}