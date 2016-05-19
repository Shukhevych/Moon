package org.sokolas.wsserver.connection;

import java.io.IOException;

/**
 * Интерфейс для работы с соединением игрока (вебсокеты, комет, etc)
 */
public interface ConnectionAdapter {
    void onConnect();
    void onClose();
    void onMessage(String message);
    void send(String message) throws IOException;
}
