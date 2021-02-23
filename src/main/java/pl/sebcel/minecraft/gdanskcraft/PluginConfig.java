package pl.sebcel.minecraft.gdanskcraft;

import java.io.File;
import java.util.logging.Logger;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfig {
	
	private final static Logger logger = Logger.getLogger(PluginConfig.class.getName());

	private static final String API_KEY_PARAMETER_NAME = "apiKey";
	private static final String SERVICE_URL_PARAMETER_NAME = "serviceUrl";
	private static final String INSTANCE_NAME_PARAMETER_NAME = "instanceName";
	private static final String LOBBY_SERVER_SYMBOL_PARAMETER_NAME = "lobbyServerSymbol";
	private static final String DEFAULT_SERVER_SYMBOL_PARAMETER_NAME = "defaultServerSymbol";
	private static final String SERVER_COOL_DOWN_PERIOD_IN_SECONDS = "serverCoolDownPeriodInSeconds";
	
	private String serviceUrl;
	private String apiKey;
	private String instanceName;
	private String lobbyServerSymbol;
	private String defaultServerSymbol;
	private int serverCoolDownPeriodInSeconds;
	
	public void initialize(File dataFolder, String configurationFileName) {
    	File file = new File(dataFolder, configurationFileName);

    	try {
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            this.serviceUrl = configuration.getString(SERVICE_URL_PARAMETER_NAME);
            this.apiKey = configuration.getString(API_KEY_PARAMETER_NAME);
            this.instanceName = configuration.getString(INSTANCE_NAME_PARAMETER_NAME);
            this.lobbyServerSymbol = configuration.getString(LOBBY_SERVER_SYMBOL_PARAMETER_NAME);
            this.defaultServerSymbol = configuration.getString(DEFAULT_SERVER_SYMBOL_PARAMETER_NAME);
            this.serverCoolDownPeriodInSeconds = configuration.getInt(SERVER_COOL_DOWN_PERIOD_IN_SECONDS);
        } catch (Exception ex) {
        	throw new RuntimeException("Failed to load configuration from " + file.getAbsolutePath() + ": " + ex.getMessage(), ex);
        }
        
        if (serviceUrl == null || serviceUrl.length() == 0) {
        	logger.warning("Parameter " + SERVICE_URL_PARAMETER_NAME + " not found in configuration file " + file.getAbsolutePath());
        }
        	
        if (apiKey == null || apiKey.length() == 0) {
        	logger.warning("Parameter " + API_KEY_PARAMETER_NAME + " not found in configuration file " + file.getAbsolutePath());
        }
        
        if (instanceName == null || instanceName.length() == 0) {
        	logger.warning("Parameter " + INSTANCE_NAME_PARAMETER_NAME + " not found in configuration file " + file.getAbsolutePath());
        }

        if (lobbyServerSymbol == null || lobbyServerSymbol.length() == 0) {
        	logger.warning("Parameter " + LOBBY_SERVER_SYMBOL_PARAMETER_NAME + " not found in configuration file " + file.getAbsolutePath());
        }

        if (defaultServerSymbol == null || defaultServerSymbol.length() == 0) {
        	logger.warning("Parameter " + DEFAULT_SERVER_SYMBOL_PARAMETER_NAME + " not found in configuration file " + file.getAbsolutePath());
        }

        if (serverCoolDownPeriodInSeconds == 0) {
        	logger.warning("Parameter " + SERVER_COOL_DOWN_PERIOD_IN_SECONDS + " not found in configuration file " + file.getAbsolutePath());
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
}