package pl.sebcel.minecraft.gdanskcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

public class PluginEventHandler implements Listener {
	
	private final static Logger logger = Logger.getLogger(PluginEventHandler.class.getName());
	
	private BungeeCordPlugin plugin;

	private PluginConfig pluginConfig;
	
	public void setPlugin(BungeeCordPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setPluginConfig(PluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}
	
	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		try {
			logger.info("onPluginMessageReceived, tag: " + event.getTag());
			String message = new String(event.getData());
			logger.info("Data: " + message);
			
			String[] tokens = message.split(",");
			String playerName = tokens[0];
			int dimension = Integer.parseInt(tokens[1]);
			int x = Integer.parseInt(tokens[2]);
			int y = Integer.parseInt(tokens[3]);
			int z = Integer.parseInt(tokens[4]);
			String coordinates = dimension + "," + x + "," + y + "," + z;

			ProxiedPlayer p = this.plugin.getProxy().getPlayer(playerName);
			logger.info("Player " + playerName + " found");
			
			String playerCurrentServer = p.getServer().getInfo().getName();
			logger.info("Player is currently on server " + playerCurrentServer);
			
			Configuration interWorldPortals = pluginConfig.getInterWorldPortals();
			if (interWorldPortals != null) {
				logger.info("Found inter-world portals");
			}
			
			if (interWorldPortals.getKeys().size() > 0) {
				logger.info("Inter-world portals contain something");
			}
			
			logger.info("Looking for portals matching coordinates " + coordinates);

			Configuration currentServerPortals = (Configuration) interWorldPortals.get(playerCurrentServer);
			
			logger.info("Found portals for current server");
			
			for(String targetServerSymbol : currentServerPortals.getKeys()) {
				String targetServerCoordinates = currentServerPortals.getString(targetServerSymbol);
				if (targetServerCoordinates.equals(coordinates)) {
					logger.info("Found mapping for server " + targetServerSymbol);
					
					ServerInfo server = this.plugin.getProxy().getServers().get(targetServerSymbol);
					logger.info("Target server found. Sending player to that server");
					p.connect(server);
				}
			}
			
		} catch (Exception ex) {
			logger.warning("Failed to handle message: " + ex.getMessage());
		}
	}
}