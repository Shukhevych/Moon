package moon.frontserver.connection;

import org.eclipse.jetty.websocket.api.Session;
import moon.frontserver.model.PlayersRegistry;

import java.io.IOException;

/**
 * Работа с вебсокетами
 */
public class WebSocketConnectionAdapter implements ConnectionAdapter {

    Session session;
    Long id;

    /**
     * Реестр активных соединений. Не надо изменять его отсюда, лучше, чтобы добавлял/удалял тот класс, который создал этот адаптер
     */
    PlayersRegistry registry;

    public WebSocketConnectionAdapter(Session session, PlayersRegistry registry)
    {
        this.session = session;
        this.registry = registry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void onConnect() {
        System.out.println("Connected");
    }

    @Override
    public void onClose() {
        System.out.println("Closed");
    }

    @Override
    public void onMessage(String message)
    {
        System.out.println("Message: " + message);
        // перешлем сообщение всем, кто подключен
        for (Long key: registry.keySet()) {
            try {
                registry.getConnection(key).send(Long.toString(id) + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
        System.out.println("Sent: " + message);
    }
}
