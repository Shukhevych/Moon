package moon.frontserver.controller;

import com.google.inject.Inject;
import moon.frontserver.connection.ConnectionAdapter;
import moon.frontserver.model.Player;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class GameController {
    @Inject
    EntityManagerFactory emFactory;

    public void onCommand(ConnectionAdapter source, String command) {
        String[] arguments = command.split(" ");
        if (arguments != null && arguments.length >= 2 && "load".equalsIgnoreCase(arguments[0])) {
            try {
                Player p = loadPlayer(Long.parseLong(arguments[1]));
                source.send("Player found: " + p.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                source.send("Error");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Player loadPlayer(long id) {
        EntityManager em = emFactory.createEntityManager();
        Player player = em.find(Player.class, id);
        em.close();
        return player;
    }
}
