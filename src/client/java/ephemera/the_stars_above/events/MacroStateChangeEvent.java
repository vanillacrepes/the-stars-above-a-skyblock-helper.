package ephemera.the_stars_above.events;

import ephemera.the_stars_above.core.MacroState;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface MacroStateChangeEvent {
    Event<MacroStateChangeEvent> EVENT = EventFactory.createArrayBacked(MacroStateChangeEvent.class,
            (listeners) -> (oldState, newState) -> {
                for (MacroStateChangeEvent listener : listeners) {
                    listener.onStateChange(oldState, newState);
                }
            });

    void onStateChange(MacroState oldState, MacroState newState);
}
