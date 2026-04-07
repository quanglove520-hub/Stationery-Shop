package stationary.core.spi;

import stationary.core.db.MemoryDB;

public interface StationaryPlugin {
    String getName();
    void onInitData(MemoryDB db);
    void registerRoutes();
}
