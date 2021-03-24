package sandtechnology.config.section;

import sandtechnology.config.ConfigLoader;

import java.util.Collections;
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
        return moduleEnablerMap.getOrDefault(module, Collections.emptySet()).contains(groupID);
    }

    public void add(ModuleType moduleType, long groupID) {
        moduleEnablerMap.merge(moduleType, Collections.singleton(groupID), (oldSet, newSet) -> {
            oldSet.addAll(newSet);
            return oldSet;
        });
        ConfigLoader.save();
    }

    public enum ModuleType {
        REPEATER,
        NUDGE_RESPONSE
    }
}
