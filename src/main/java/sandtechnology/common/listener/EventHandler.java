package sandtechnology.common.listener;

import sandtechnology.common.event.Event;

import java.util.List;
import java.util.Map;

public class EventHandler {

    private Map<Class<? extends Event>, List<Listener<? extends Event>>> eventList;

    public void register(Listener<?> listener) {
        eventList.get(listener.getClass().getGenericSuperclass().getTypeName()).add(listener);

    }
}
