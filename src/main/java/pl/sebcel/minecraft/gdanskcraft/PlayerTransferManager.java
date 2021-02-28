package pl.sebcel.minecraft.gdanskcraft;

import java.util.logging.Logger;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.sebcel.minecraft.gdanskcraft.ec2.HardwareStatus;

public class PlayerTransferManager implements Runnable {

    private final static Logger logger = Logger.getLogger(PlayerTransferManager.class.getName());
    private final static int PLAYERS_TRANSFER_PERIOD_IN_SECONDS = 10;

    private ProxyServer proxyServer;
    private String lobbyServerSymbol;
    private String defaultServerSymbol;
    private ServerWakeUpServiceProxy serviceProxy;

    public void initialize(ProxyServer proxyServer, ServerWakeUpServiceProxy serviceProxy, String lobbyServerSymbol, String defaultServerSymbol) {
        if (proxyServer == null) {
            throw new IllegalArgumentException("Argument proxyServer can not be null");
        }
        if (serviceProxy == null) {
            throw new IllegalArgumentException("Argument serviceProxy can not be null");
        }
        if (lobbyServerSymbol == null) {
            throw new IllegalArgumentException("Argument lobbyServerSymbol can not be null");
        }
        if (defaultServerSymbol == null) {
            throw new IllegalArgumentException("Argument defaultServerSymbol can not be null");
        }
        if (lobbyServerSymbol.trim().length() == 0) {
            throw new IllegalArgumentException("Argument lobbyServerSymbol can not be empty");
        }
        if (defaultServerSymbol.trim().length() == 0) {
            throw new IllegalArgumentException("Argument defaultServerSymbol can not be empty");
        }
        this.proxyServer = proxyServer;
        this.serviceProxy = serviceProxy;
        this.lobbyServerSymbol = lobbyServerSymbol;
        this.defaultServerSymbol = defaultServerSymbol;
    }

    @Override
    public void run() {
        while (true) {

            try {
                ServerInfo defaultServer = proxyServer.getServers().get(defaultServerSymbol);

                proxyServer.getPlayers().forEach(p -> {
                    try {
                        if (lobbyServerSymbol.equals(p.getServer().getInfo().getName())) {
                            p.sendMessage(TextComponent.fromLegacyText("Za chwile bedziesz przeniesiony do docelowego serwera. Trzeba zaczekac az serwer sie uruchomi."));
                            logger.info("Trying to send player " + p.getName() + " to server " + defaultServerSymbol);

                            tryToSendPlayer(p, defaultServer);
                        }
                    } catch (Exception ex) {
                        logger.info("Failed to send player " + p.getName() + " to server " + defaultServerSymbol + ": " + ex.getMessage());
                    }
                });

            } catch (Exception ex) {
                logger.severe("Failed to send players to the target server: " + ex.getMessage());
                ex.printStackTrace();
            }

            try {
                Thread.sleep(PLAYERS_TRANSFER_PERIOD_IN_SECONDS * 1000);
            } catch (Exception ex) {
                // intentional
            }
        }
    }

    private void tryToSendPlayer(ProxiedPlayer p, ServerInfo defaultServer) {
        HardwareStatus hardwareStatus = serviceProxy.getStatus();
        logger.info("Hardware status: " + hardwareStatus.toString());

        String helpInfo = "Jesli czekasz dluzej niz 2 minuty, wyslij zgloszenie o problemie na Discordzie lub na adres email Sebastian.Celejewski@wp.pl";

        if (hardwareStatus == HardwareStatus.RUNNING) {
            Callback<ServerPing> callback = (result, error) -> {
                if (result != null) {
                    String message = "Status maszyny: " + hardwareStatus + ", status serwera: RUNNING. " + helpInfo;
                    p.sendMessage(TextComponent.fromLegacyText(message));
                    p.connect(defaultServer);
                } else {
                    String message = "Status maszyny: " + hardwareStatus + ", status serwera: STARTING. " + helpInfo;
                    p.sendMessage(TextComponent.fromLegacyText(message));
                }
            };
            defaultServer.ping(callback);
        } else {
            String message = "Status maszyny: " + hardwareStatus + ", status serwera: STOPPED. " + helpInfo;
            p.sendMessage(TextComponent.fromLegacyText(message));
        }
    }
}