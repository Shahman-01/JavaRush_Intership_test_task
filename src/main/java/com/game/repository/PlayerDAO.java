package com.game.repository;

import com.game.entity.Player;

import java.util.List;

public interface PlayerDAO {
    List<Player> getAllPlayers();

    Player getPlayer(int id);

    void savePlayer(Player player);

    void deletePlayer(int id);
}
