package com.game.repository;

import com.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PlayerDAOImpl implements PlayerDAO {

    @Autowired
    private EntityManagerFactory managerFactory;

    @Override
    public List<Player> getAllPlayers() {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        List players = manager.createQuery("SELECT id, name, title, " +
                "race, profession, birthday, banned, " +
                "experience, level, untilNextLevel from Player").getResultList();
        return players;
    }

    @Override
    public Player getPlayer(int id) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        return manager.find(Player.class, id);
    }

    @Override
    public void savePlayer(Player player) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        manager.persist(player);
        manager.getTransaction().commit();
    }

    @Override
    public void deletePlayer(int id) {
        EntityManager manager = managerFactory.createEntityManager();
        manager.getTransaction().begin();
        Player player = manager.find(Player.class, id);
        manager.remove(player);
        manager.getTransaction().commit();
    }
}
