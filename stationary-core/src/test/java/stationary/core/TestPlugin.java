package stationary.core;

import stationary.core.spi.StationaryPlugin;
import stationary.core.db.MemoryDB;

public class TestPlugin implements StationaryPlugin {
    @Override
    public String getName() {
        return "Test Plugin";
    }

    @Override
    public void onInitData(MemoryDB db) {
        db.registerStore("test_store", "test_value");
    }

    @Override
    public void registerRoutes() {
    }
}
