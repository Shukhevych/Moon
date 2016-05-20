package moon.frontserver.database.dao;

import com.google.inject.Inject;
import moon.frontserver.database.domain.Player;

import javax.persistence.EntityManager;

public class PlayerDao {

    @Inject
    EntityManager em;

    public Player loadPlayer(long id) {
        Player player = em.find(Player.class, id);
        em.close();
        return player;
    }
}
