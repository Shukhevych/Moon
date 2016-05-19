package org.sokolas.wsserver;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.sokolas.wsserver.cluster.ClusterEventListener;
import org.sokolas.wsserver.cluster.ZookeeperEventListener;
import org.sokolas.wsserver.model.PlayersRegistry;
import org.sokolas.wsserver.network.GWebSocket;
import org.sokolas.wsserver.network.WebSocketProvider;

public class AppModule extends AbstractModule {

    PlayersRegistry playersRegistry;

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
