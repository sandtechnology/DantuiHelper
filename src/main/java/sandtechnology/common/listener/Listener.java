package sandtechnology.common.listener;

import sandtechnology.common.event.Event;

public interface Listener<T extends Event> {

    void handle(Class<T> event);
}
