package stationary.core.app;

import stationary.core.db.MemoryDB;
import stationary.core.spi.StationaryPlugin;

import java.util.ServiceLoader;
import static spark.Spark.*;

public class MainApp {

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");
        
        System.out.println("Starting Stationary Core...");
        
        initPlugins();
        
        System.out.println("System Initialized! Go to http://localhost:4567");
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
