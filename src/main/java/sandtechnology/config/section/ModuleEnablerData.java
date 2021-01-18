package sandtechnology.config.section;

import sandtechnology.config.ConfigLoader;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleEnablerData {
    private final Map<ModuleType, Set<Long>> moduleEnablerMap = new ConcurrentHashMap<>();

    public ModuleEnablerData() {
        for (ModuleType type : ModuleType.values()) {
            moduleEnablerMap.put(type, new HashSet<>(5));
        }
    }

    public boolean isEnable(ModuleType module, long groupID) {
        return moduleEnablerMap.get(module).contains(groupID);
    }

    public void add(ModuleType moduleType, long groupID) {
        moduleEnablerMap.get(moduleType).add(groupID);
        ConfigLoader.save();
    }

    public enum ModuleType {
        REPEATER,
        NUDGE_RESPONSE
    }
}
