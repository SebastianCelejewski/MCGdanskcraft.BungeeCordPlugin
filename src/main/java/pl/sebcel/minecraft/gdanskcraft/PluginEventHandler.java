package pl.sebcel.minecraft.gdanskcraft;

import java.util.logging.Logger;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginEventHandler implements Listener {
	
	private final static Logger logger = Logger.getLogger(PluginEventHandler.class.getName());
	
	@EventHandler
	public void onPluginMessageReceived(PluginMessageEvent event) {
		logger.info("onPluginMessageReceived, tag: " + event.getTag());
		String message = new String(event.getData());
		logger.info("Data: " + message);
	}
}