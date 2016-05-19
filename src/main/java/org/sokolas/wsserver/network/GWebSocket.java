package org.sokolas.wsserver.network;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.sokolas.wsserver.model.PlayersRegistry;
import org.sokolas.wsserver.connection.WebSocketConnectionAdapter;

import java.io.IOException;

/**
 * Вебсокет для каждого активного подключения. Также управляет добавлением/удалением соединений в реестре
 */
@WebSocket
public class GWebSocket {

    private PlayersRegistry registry;

    private Long connectionId;

    /**
     * Адаптер для этого подключения
     */
    private WebSocketConnectionAdapter adapter;

    public GWebSocket(PlayersRegistry registry) {
        this.registry = registry;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected: " + session.getRemote());
        adapter = new WebSocketConnectionAdapter(session, registry);
        long id = registry.addConnection(adapter);
        adapter.setId(id);
        try {
            adapter.send("You're player number " + Long.toString(id));
        } catch (IOException e) {
            registry.remove(id);
            e.printStackTrace();
        }
        this.connectionId = id;
    }

    @OnWebSocketClose
    public void close(int code, String reason) {
        System.out.println("Connection closed: " + reason + " for " + connectionId);
        if (connectionId != null) {
            registry.remove(connectionId);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        if (adapter != null) {
            adapter.onMessage(message);
        }
    }
}
