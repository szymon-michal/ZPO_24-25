package pl.police.fines.service;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionMonitor {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionMonitor.class);
    private static final int CHECK_INTERVAL_SECONDS = 30;
    private static final int INITIAL_DELAY_SECONDS = 5;
    
    private static ConnectionMonitor instance;
    private ScheduledExecutorService scheduler;
    private BooleanProperty connected = new SimpleBooleanProperty(true);
    
    private ConnectionMonitor() {}
    
    public static synchronized ConnectionMonitor getInstance() {
        if (instance == null) {
            instance = new ConnectionMonitor();
        }
        return instance;
    }
    
    public void startMonitoring() {
        if (scheduler != null && !scheduler.isShutdown()) {
            return;
        }
        
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ConnectionMonitor");
            t.setDaemon(true);
            return t;
        });
        
        scheduler.scheduleWithFixedDelay(this::checkConnection, 
                INITIAL_DELAY_SECONDS, CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS);
        
        logger.info("Connection monitoring started");
    }
    
    public void stopMonitoring() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        logger.info("Connection monitoring stopped");
    }
    
    private void checkConnection() {
        ApiService.getInstance().checkConnection()
                .thenAccept(isConnected -> {
                    boolean wasConnected = connected.get();
                    
                    Platform.runLater(() -> connected.set(isConnected));
                    
                    if (wasConnected != isConnected) {
                        if (isConnected) {
                            logger.info("Connection restored");
                        } else {
                            logger.warn("Connection lost");
                        }
                    }
                })
                .exceptionally(throwable -> {
                    Platform.runLater(() -> connected.set(false));
                    logger.error("Error checking connection", throwable);
                    return null;
                });
    }
    
    public BooleanProperty connectedProperty() {
        return connected;
    }
    
    public boolean isConnected() {
        return connected.get();
    }
}