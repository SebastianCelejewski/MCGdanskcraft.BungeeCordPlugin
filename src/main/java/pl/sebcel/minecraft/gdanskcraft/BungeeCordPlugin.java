package pl.sebcel.minecraft.gdanskcraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCordPlugin extends Plugin {

	private final static String CONFIG_FILE_NAME = "config.yaml";
	
	private final static Logger logger = Logger.getLogger(BungeeCordPlugin.class.getName());

	private ServerWakeUpServiceProxy serviceProxy = new ServerWakeUpServiceProxy();
	private PluginEventHandler eventHandler = new PluginEventHandler();
	private PluginConfig pluginConfig = new PluginConfig();
	private ActivePlayersMonitor activePlayersMonitor = new ActivePlayersMonitor();
	private HardwareStatusManager hardwareStatusManager = new HardwareStatusManager();
	private PlayerTransferManager playerTransferManager = new PlayerTransferManager();
	
	@Override
	public void onEnable() {
		logger.info("Initializing MCGdanskcraft Bungee Cord plugin");
		initialize();
		pluginConfig.initialize(getDataFolder(), CONFIG_FILE_NAME);
		serviceProxy.initialize(pluginConfig.getServiceUrl(), pluginConfig.getApiKey(), pluginConfig.getInstanceName());
		hardwareStatusManager.initialize(getProxy(), this.serviceProxy, pluginConfig.getServerCoolDownInSeconds());
		playerTransferManager.initialize(getProxy(), pluginConfig.getLobbyServerSymbol(), pluginConfig.getDefaultServerSymbol());
		activePlayersMonitor.initialize(hardwareStatusManager, playerTransferManager);
		eventHandler.initialize(this, pluginConfig, getProxy());

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