package ephemera.the_stars_above.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface StuckEvent {
    Event<StuckEvent> EVENT = EventFactory.createArrayBacked(StuckEvent.class,
            (listeners) -> () -> {
                for (StuckEvent listener : listeners) {
                    listener.onStuck();
                }
            });

    void onStuck();
}
