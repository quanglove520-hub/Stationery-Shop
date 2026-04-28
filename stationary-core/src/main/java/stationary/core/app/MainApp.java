package stationary.core.app;

import stationary.core.db.MemoryDB;
import stationary.core.spi.StationaryPlugin;

import java.util.ServiceLoader;
import static spark.Spark.*;

public class MainApp {

    public static void main(String[] args) throws InterruptedException {
        port(4567);
        staticFiles.location("/public");
        
        System.out.println("Starting Stationary Core Modular System...");
        
        // Initialize JDBC Database Manager
        try {
            Class.forName("stationary.core.db.DbConnectionManager");
        } catch (ClassNotFoundException e) {
            System.err.println("DbConnectionManager not found!");
        }
        
        initPlugins();
        
        awaitInitialization();
        System.out.println("System Initialized! Go to http://localhost:4567");
        Thread.sleep(Long.MAX_VALUE); // Block the main thread to keep server running
    }

    public static int initPlugins() {
        MemoryDB coreDb = MemoryDB.getInstance();
        ServiceLoader<StationaryPlugin> loader = ServiceLoader.load(StationaryPlugin.class);
        
        int count = 0;
        for (StationaryPlugin plugin : loader) {
            System.out.println("Loading plugin: " + plugin.getName());
            try {
                plugin.onInitData(coreDb);
                plugin.registerRoutes();
                count++;
            } catch (Exception e) {
                System.err.println("Failed to load plugin: " + plugin.getName());
                e.printStackTrace();
            }
        }
        return count;
    }
}
