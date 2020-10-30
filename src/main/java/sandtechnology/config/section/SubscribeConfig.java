package sandtechnology.config.section;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SubscribeConfig {

    Map<Long, Set<Long>> subscribeDynamic = new ConcurrentHashMap<>();
    Map<Long, Set<Long>> subscribeWeiboContainer = new ConcurrentHashMap<>();
    Map<Long, Set<Long>> subscribeLiveRoom = new ConcurrentHashMap<>();

    public Map<Long, Set<Long>> getSubscribeWeiboContainer() {
        return subscribeWeiboContainer;
    }

    public Map<Long, Set<Long>> getSubscribeDynamic() {
        return subscribeDynamic;
    }

    public Map<Long, Set<Long>> getSubscribeLiveRoom() {
        return subscribeLiveRoom;
    }

    public boolean isEmpty() {
        return subscribeDynamic.isEmpty() && subscribeLiveRoom.isEmpty() && subscribeWeiboContainer.isEmpty();
    }
}
