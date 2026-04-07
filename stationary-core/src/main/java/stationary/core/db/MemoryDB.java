package stationary.core.db;

import java.util.HashMap;
import java.util.Map;

public class MemoryDB {
    private static MemoryDB instance;
    private final Map<String, Object> datastores;

    private MemoryDB() {
        datastores = new HashMap<>();
    }

    public static synchronized MemoryDB getInstance() {
        if (instance == null) {
            instance = new MemoryDB();
        }
        return instance;
    }

    public void registerStore(String key, Object storeObj) {
        datastores.put(key, storeObj);
    }

    public Object getStore(String key) {
        return datastores.get(key);
    }
    
    public void clear() {
        datastores.clear();
    }
}
