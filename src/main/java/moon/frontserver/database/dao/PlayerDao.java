package moon.frontserver.database.dao;

import com.google.inject.Inject;
import moon.frontserver.database.domain.Player;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PlayerDao {

    @Inject
    EntityManagerFactory entityManagerFactory;

    public Player loadPlayer(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Player player = em.find(Player.class, id);
        em.close();
        return player;
    }

    public Player loadPlayerByEmail(String email) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Player player = em.createQuery(
                "SELECT u from Player u WHERE u.email= :email", Player.class).
                setParameter("email", email).getSingleResult();
        return player;
    }
}
