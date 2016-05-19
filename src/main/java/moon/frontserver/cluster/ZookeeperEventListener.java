package moon.frontserver.cluster;

/**
 * Для работы с кластером через ZooKeeper
 */
public class ZookeeperEventListener implements ClusterEventListener {
    @Override
    public void onPlayerConnect() {
        System.out.println("Player connected somewhere else");
    }
}
