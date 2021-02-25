package pl.sebcel.minecraft.gdanskcraft.pluginconfig;

import org.junit.Test;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.sebcel.minecraft.gdanskcraft.PluginConfig;

public class When_loading_inter_world_configuration {
	
	@Test
	public void should_return_map_of_source_servers() {
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getClass().getClassLoader().getResourceAsStream("config.yaml"));
		Configuration portalsConfiguration = (Configuration) configuration.get(PluginConfig.ParameterNames.INTER_WORLD_PORTALS);
		for (String sourceServer : portalsConfiguration.getKeys()) {
			System.out.println("Source server: " + sourceServer);
			Configuration targetServers = (Configuration) portalsConfiguration.get(sourceServer);
			for (String targetServer : targetServers.getKeys()) {
				System.out.println("Target server: " + targetServer);
				String coordinates = targetServers.getString(targetServer);
				System.out.println("Coordinates: " + coordinates);
			}
		}
	}
}