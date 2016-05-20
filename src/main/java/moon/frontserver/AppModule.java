package moon.frontserver;

import com.google.inject.*;
import com.google.inject.name.Names;
import moon.frontserver.cluster.ClusterEventListener;
import moon.frontserver.cluster.ZookeeperEventListener;
import moon.frontserver.controller.GameController;
import moon.frontserver.model.PlayersRegistry;
import moon.frontserver.network.GWebSocket;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Integer.class).annotatedWith(Names.named("app port")).toInstance(8081);
        bind(String.class).annotatedWith(Names.named("path")).toInstance("/game");
        bind(PlayersRegistry.class).in(Singleton.class);
        bind(GameController.class).in(Singleton.class);

        // конкретные реализации интерфейсов
        bind(GWebSocket.class);
        bind(ClusterEventListener.class).to(ZookeeperEventListener.class);
    }

    @Provides
    @Singleton
    EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory("moon.front-server");
    }

    @Provides
    @Singleton
    EntityManager entityManager() {
        return entityManagerFactory().createEntityManager();
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
