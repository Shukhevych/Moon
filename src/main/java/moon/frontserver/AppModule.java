package moon.frontserver;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import moon.frontserver.cluster.ClusterEventListener;
import moon.frontserver.network.WebSocketProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import moon.frontserver.cluster.ZookeeperEventListener;
import moon.frontserver.model.PlayersRegistry;
import moon.frontserver.network.GWebSocket;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppModule extends AbstractModule {

    private PlayersRegistry playersRegistry;

    private EntityManagerFactory emFactory;

    @Override
    protected void configure() {
        bind(Integer.class).annotatedWith(Names.named("app port")).toInstance(8081);
        bind(String.class).annotatedWith(Names.named("path")).toInstance("/game");
        bind(GWebSocket.class).toProvider(WebSocketProvider.class);
        bind(ClusterEventListener.class).to(ZookeeperEventListener.class);
    }

    @Provides
    PlayersRegistry getPlayerRegistry() {
        if (playersRegistry == null) {
            playersRegistry = new PlayersRegistry();
        }
        return playersRegistry;
    }

    @Provides
    EntityManager getEntityManager() {
        if (emFactory == null) {
            emFactory = Persistence.createEntityManagerFactory("moon.front-server");
        }
        return emFactory.createEntityManager();
    }

    @Provides
    @Inject
    Handler networkHandler(Injector injector) {
        return new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.setCreator((request, response) ->
                        injector.getInstance(GWebSocket.class));
            }
        };
    }
}
