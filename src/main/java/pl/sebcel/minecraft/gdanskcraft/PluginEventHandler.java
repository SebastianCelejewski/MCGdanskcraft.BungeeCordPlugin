package pl.sebcel.minecraft.gdanskcraft;

import java.util.logging.Logger;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginEventHandler implements Listener {
	
	private final static Logger logger = Logger.getLogger(PluginEventHandler.class.getName());
	
	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		logger.info("Player " + event.getPlayer().getDisplayName() + " logged in.");
	}
	
	@EventHandler
	public void onPlayerDisconnected(PlayerDisconnectEvent event) {
		logger.info("Player " + event.getPlayer().getDisplayName() + " disconnected");
	}
	
	@EventHandler
	public void onServerConnect(ServerConnectEvent event) {
		logger.info("onServerConnect, player: " + event.getPlayer().getDisplayName()+", server: " + event.getTarget().getName());
	}
	
	@EventHandler
	public void onServerConnected(ServerConnectedEvent event) {
		logger.info("onServerConnected, player: " + event.getPlayer().getDisplayName()+", server: " + event.getServer().getInfo().getName());
	}
	
	@EventHandler
	public void onServerDisconnected(ServerDisconnectEvent event) {
		logger.info("onServerDisconnect, player: " + event.getPlayer().getDisplayName()+", server: " + event.getTarget().getName());
	}
	
	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		logger.info("onPluginMessageReceived, tag: " + event.getTag());
	}
}