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
	
	public void setPlugin(BungeeCordPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void setPluginConfig(PluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}
	
	public void setProxyServer(ProxyServer server) {
		this.proxyServer = server;
	}
	
	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		try {
			String message = new String(event.getData());
			
			if (!event.getTag().equals("gdanskcraft:main")) {
				return;
			}
			
			String[] tokens = message.split(",");
			String playerName = tokens[0].substring(2);
			int dimension = Integer.parseInt(tokens[1]);
			int x = Integer.parseInt(tokens[2]);
			int y = Integer.parseInt(tokens[3]);
			int z = Integer.parseInt(tokens[4]);

			String coordinates = dimension + "," + x + "," + y + "," + z;

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
				if (targetServerCoordinates.equals(coordinates)) {
					ServerInfo server = this.plugin.getProxy().getServers().get(targetServerSymbol);
					logger.info("Sending player " + playerName + " to server " + targetServerSymbol);
					p.connect(server);
				}
			}
			
		} catch (Exception ex) {
			logger.warning("Failed to handle message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}