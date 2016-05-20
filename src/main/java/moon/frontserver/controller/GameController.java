package moon.frontserver.controller;

import com.google.inject.Inject;
import moon.frontserver.connection.ConnectionAdapter;
import moon.frontserver.database.dao.PlayerDao;
import moon.frontserver.database.domain.Player;

import java.io.IOException;

public class GameController {
    @Inject
    PlayerDao playerDao;

    public void onCommand(ConnectionAdapter source, String command) {
        String[] arguments = command.split(" ");
        if (arguments != null && arguments.length >= 2 && "load".equalsIgnoreCase(arguments[0])) {
            try {
                Player p = playerDao.loadPlayer(Long.parseLong(arguments[1]));
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

}
