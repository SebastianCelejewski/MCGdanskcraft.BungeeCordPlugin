package pl.sebcel.minecraft.gdanskcraft;

import java.io.File;
import java.util.logging.Logger;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfig {
	
	private final static Logger logger = Logger.getLogger(PluginConfig.class.getName());

	public static class ParameterNames {
		public static final String API_KEY = "apiKey";
		public static final String SERVICE_URL = "serviceUrl";
		public static final String INSTANCE_NAME = "instanceName";
		public static final String LOBBY_SERVER_SYMBOL = "lobbyServerSymbol";
		public static final String DEFAULT_SERVER_SYMBOL = "defaultServerSymbol";
		public static final String SERVER_COOL_DOWN_PERIOD_IN_SECONDS = "serverCoolDownPeriodInSeconds";
		public static final String INTER_WORLD_PORTALS = "interWorldPortals";
	}
	
	private String serviceUrl;
	private String apiKey;
	private String instanceName;
	private String lobbyServerSymbol;
	private String defaultServerSymbol;
	private int serverCoolDownPeriodInSeconds;
	private Configuration interWorldPortals;
	
	public void initialize(File dataFolder, String configurationFileName) {
    	File file = new File(dataFolder, configurationFileName);

    	try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            this.serviceUrl = configuration.getString(ParameterNames.SERVICE_URL);
            this.apiKey = configuration.getString(ParameterNames.API_KEY);
            this.instanceName = configuration.getString(ParameterNames.INSTANCE_NAME);
            this.lobbyServerSymbol = configuration.getString(ParameterNames.LOBBY_SERVER_SYMBOL);
            this.defaultServerSymbol = configuration.getString(ParameterNames.DEFAULT_SERVER_SYMBOL);
            this.serverCoolDownPeriodInSeconds = configuration.getInt(ParameterNames.SERVER_COOL_DOWN_PERIOD_IN_SECONDS);
			this.interWorldPortals = (Configuration) configuration.get(ParameterNames.INTER_WORLD_PORTALS);
        } catch (Exception ex) {
        	throw new RuntimeException("Failed to load configuration from " + file.getAbsolutePath() + ": " + ex.getMessage(), ex);
        }
        
        if (serviceUrl == null || serviceUrl.length() == 0) {
        	logger.warning("Parameter " + ParameterNames.SERVICE_URL + " not found in configuration file " + file.getAbsolutePath());
        }
        	
        if (apiKey == null || apiKey.length() == 0) {
        	logger.warning("Parameter " + ParameterNames.API_KEY + " not found in configuration file " + file.getAbsolutePath());
        }
        
        if (instanceName == null || instanceName.length() == 0) {
        	logger.warning("Parameter " + ParameterNames.INSTANCE_NAME + " not found in configuration file " + file.getAbsolutePath());
        }

        if (lobbyServerSymbol == null || lobbyServerSymbol.length() == 0) {
        	logger.warning("Parameter " + ParameterNames.LOBBY_SERVER_SYMBOL + " not found in configuration file " + file.getAbsolutePath());
        }

        if (defaultServerSymbol == null || defaultServerSymbol.length() == 0) {
        	logger.warning("Parameter " + ParameterNames.DEFAULT_SERVER_SYMBOL + " not found in configuration file " + file.getAbsolutePath());
        }

        if (serverCoolDownPeriodInSeconds == 0) {
        	logger.warning("Parameter " + ParameterNames.SERVER_COOL_DOWN_PERIOD_IN_SECONDS + " not found in configuration file " + file.getAbsolutePath());
        }
        
        if (interWorldPortals == null) {
        	logger.warning("Parameter " + ParameterNames.INTER_WORLD_PORTALS + " not found in configuration file " + file.getAbsolutePath());
        }

        logger.info("Plugin configuration loading completed");
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getApiKey() {
		return apiKey;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public String getLobbyServerSymbol() {
		return lobbyServerSymbol;
	}
	
	public String getDefaultServerSymbol() {
		return defaultServerSymbol;
	}
	
	public int getServerCoolDownInSeconds() {
		return serverCoolDownPeriodInSeconds;
	}
	
	public Configuration getInterWorldPortals() {
		return interWorldPortals;
	}
}