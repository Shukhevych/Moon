package org.sokolas.wsserver;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.sokolas.wsserver.model.PlayersRegistry;

public class App {
    @Inject
    PlayersRegistry players;

    private Server server;

    @Inject
    public App(@Named("app port") int port, @Named("path") String path, Handler networkHandler) {
        server = new Server(port);
        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath(path);
        contextHandler.setHandler(networkHandler);
        server.setHandler(contextHandler);
    }

    public void run() throws Exception {
        server.start();
        server.join();
    }
}
