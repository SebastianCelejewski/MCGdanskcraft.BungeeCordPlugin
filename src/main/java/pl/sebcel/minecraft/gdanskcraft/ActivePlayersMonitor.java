package pl.sebcel.minecraft.gdanskcraft;

import java.util.logging.Logger;

public class ActivePlayersMonitor {

	private final static Logger logger = Logger.getLogger(ActivePlayersMonitor.class.getName());
	
	private Thread playerTransferManagerThread;
	private Thread hardwareStatusManagerThread;
	
	public void initialize(HardwareStatusManager hardwareStatusManager, PlayerTransferManager playerTransferManager) {
		if (hardwareStatusManager == null) {
			throw new IllegalArgumentException("Argument hardwareStatusManager can not be null");
		}
		
		if (playerTransferManager == null) {
			throw new IllegalArgumentException("Argument playerTransferManager can not be null");
		}
		
		logger.info("Starting monitoring threads");

		this.hardwareStatusManagerThread = new Thread(hardwareStatusManager);
		this.playerTransferManagerThread = new Thread(playerTransferManager);
		
		this.hardwareStatusManagerThread.start();
		this.playerTransferManagerThread.start();
	}
}