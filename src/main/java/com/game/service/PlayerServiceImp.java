package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PlayerServiceImp implements PlayerService {
    @Autowired
    private PlayerDAO playerDAO;

    @Override
    @Transactional
    public List<Player> getAllPlayers() {
        return playerDAO.getAllPlayers();
    }

    @Override
    @Transactional
    public Player getPlayer(int id) {
        return playerDAO.getPlayer(id);
    }

    @Override
    @Transactional
    public void savePlayer(Player player) {
        playerDAO.savePlayer(player);
    }

    @Override
    @Transactional
    public void deletePlayer(int id) {
        playerDAO.deletePlayer(id);
    }
}
