package stationary.notify;

import stationary.core.db.MemoryDB;
import stationary.core.spi.StationaryPlugin;
import static spark.Spark.*;

public class NotifyPlugin implements StationaryPlugin {
    
    @Override
    public String getName() {
        return "Notification & Mailing Plugin";
    }

    @Override
    public void onInitData(MemoryDB coreDb) {
        // Khởi tạo data
    }

    @Override
    public void registerRoutes() {
        // Có thể không cần route trực tiếp hoặc expose health check
    }
}
