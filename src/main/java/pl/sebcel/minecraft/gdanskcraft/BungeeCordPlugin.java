package pl.sebcel.minecraft.gdanskcraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCordPlugin extends Plugin {

	private final static String CONFIG_FILE_NAME = "config.yaml";

	private ServerWakeUpServiceProxy serviceProxy;
	private PluginEventHandler eventHandler;
	private PluginConfig pluginConfig;
	private ActivePlayersMonitor activePlayersMonitor;
	
	public BungeeCordPlugin() {
		this.pluginConfig = new PluginConfig();
		this.serviceProxy = new ServerWakeUpServiceProxy();
		this.activePlayersMonitor = new ActivePlayersMonitor(serviceProxy);
		this.eventHandler = new PluginEventHandler();
	}
	
	@Override
	public void onEnable() {
		initialize();
		this.eventHandler.setPlugin(this);
		this.eventHandler.setProxyServer(getProxy());
		pluginConfig.initialize(getDataFolder(), CONFIG_FILE_NAME);
		activePlayersMonitor.initialize(getProxy(), pluginConfig.getLobbyServerSymbol(), pluginConfig.getDefaultServerSymbol(), pluginConfig.getServerCoolDownInSeconds());
		serviceProxy.initialize(pluginConfig.getServiceUrl(), pluginConfig.getApiKey(), pluginConfig.getInstanceName());

		this.eventHandler.setPluginConfig(pluginConfig);

		getLogger().info("Minecraft Server Wake Up Plugin enabled. Default server symbol: " + pluginConfig.getDefaultServerSymbol() + ", server cool down period in seconds: " + pluginConfig.getServerCoolDownInSeconds());

		getProxy().getPluginManager().registerListener(this, eventHandler);
		
		getProxy().registerChannel("gdanskcraft:main");
	}
	
	public void initialize() {
		if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
		}

        File file = new File(getDataFolder(), CONFIG_FILE_NAME);
   
        if (!file.exists()) {
        	getLogger().info("Creating default configuration file in " + file.getAbsolutePath());
            try (InputStream in = getResourceAsStream(CONFIG_FILE_NAME)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
            	throw new RuntimeException("Failed to create default configuration file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
            }
        }
	}
}