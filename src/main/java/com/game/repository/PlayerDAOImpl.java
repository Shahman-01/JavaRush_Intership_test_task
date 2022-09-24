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

    }

    @Override
    public void getPlayer(Player player) {

    }

    @Override
    public Player savePlayer(int id) {
        return null;
    }

    @Override
    public void deletePlayer(int id) {

    }
}
