package pl.sebcel.minecraft.gdanskcraft.ec2;

import java.util.Date;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import pl.sebcel.minecraft.gdanskcraft.ServerWakeUpServiceProxy;

public class HardwareStatusManager implements Runnable {

    private final static Logger logger = Logger.getLogger(HardwareStatusManager.class.getName());

    private final static int PLAYERS_COUNT_CHECK_PERIOD_IN_SECONDS = 10;

    private ProxyServer proxyServer;
    private ServerWakeUpServiceProxy serviceProxy;
    private HardwareWorkflow hardwareWorkflow;

    private long lastTimePlayersWereConnected;
    private int serverCoolDownPeriodInSeconds;

    public void initialize(ProxyServer proxyServer, ServerWakeUpServiceProxy serviceProxy, HardwareWorkflow hardwareWorkflow, int serverCoolDownPeriodInSeconds) {
        if (proxyServer == null) {
            throw new IllegalArgumentException("Argument proxyServer can not be null");
        }
        if (serviceProxy == null) {
            throw new IllegalArgumentException("Argument serviceProxy can not be null");
        }
        if (hardwareWorkflow == null) {
            throw new IllegalArgumentException("Argument hardwareWorkflow can not be null");
        }
        this.proxyServer = proxyServer;
        this.serviceProxy = serviceProxy;
        this.hardwareWorkflow = hardwareWorkflow;
        this.serverCoolDownPeriodInSeconds = serverCoolDownPeriodInSeconds;
        this.lastTimePlayersWereConnected = new Date().getTime();
    }

    @Override
    public void run() {
        logger.info("Starting Hardware Status Manager");

        while (true) {
            int numberOfPlayers = proxyServer.getOnlineCount();
            logger.info("Number of active players: " + numberOfPlayers);
            if (numberOfPlayers > 0) {
                lastTimePlayersWereConnected = new Date().getTime();
            }

            HardwareStatus hardwareStatus = serviceProxy.getStatus();
            boolean activePlayers = numberOfPlayers > 0;
            boolean cooldownPeriod = (new Date().getTime() - lastTimePlayersWereConnected) < serverCoolDownPeriodInSeconds * 1000;

            HardwareWorkflow.Transition transition = hardwareWorkflow.getTransition(hardwareStatus, activePlayers, cooldownPeriod);
            logger.info("Calculated server transition: " + transition);

            if (transition == HardwareWorkflow.Transition.START) {
                logger.info("Sending request to start the server.");
                serviceProxy.startServer();
            }

            if (transition == HardwareWorkflow.Transition.STOP) {
                logger.info("No players detected. Sending request to shut down the server.");
                serviceProxy.stopServer();
            }

            try {
                Thread.sleep(PLAYERS_COUNT_CHECK_PERIOD_IN_SECONDS * 1000);
            } catch (Exception ex) {
                // intentional
            }
        }
    }
}