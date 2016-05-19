package moon.frontserver.model;

import moon.frontserver.connection.ConnectionAdapter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Хранилище всех активных подключений
 */
public class PlayersRegistry {
    private Map<Long, ConnectionAdapter> players;
    AtomicLong idCounter = new AtomicLong(0);

    public PlayersRegistry() {
        this.players = new ConcurrentHashMap<>();
    }

    public ConnectionAdapter getConnection(Long id) {
        return players.get(id);
    }

    public long addConnection(ConnectionAdapter connection) {
        long id = idCounter.incrementAndGet();
        players.put(id, connection);
        return id;
    }

    public void remove(Long id) {
        players.remove(id);
    }

    public Set<Long> keySet() {
        return players.keySet();
    }
}
