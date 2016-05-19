package org.sokolas.wsserver.network;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sokolas.wsserver.model.PlayersRegistry;

public class WebSocketProvider implements Provider<GWebSocket>{
    @Inject
    private PlayersRegistry registry;

    @Override
    public GWebSocket get() {
        return new GWebSocket(registry);
    }
}
