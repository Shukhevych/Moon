package moon.frontserver.cluster;

/**
 * Для обработки событий в кластере, например, игрок подключился к другой ноде, нашелся противник, и т.д.
 */
public interface ClusterEventListener {
    void onPlayerConnect();
}
