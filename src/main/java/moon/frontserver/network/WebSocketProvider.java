package moon.frontserver.network;

import com.google.inject.Inject;
import com.google.inject.Provider;
import moon.frontserver.model.PlayersRegistry;

public class WebSocketProvider implements Provider<GWebSocket>{
    @Inject
    private PlayersRegistry registry;

    @Override
    public GWebSocket get() {
        return new GWebSocket(registry);
    }
}
