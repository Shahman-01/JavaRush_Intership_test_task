package com.game.repository;

import com.game.entity.Player;

import java.util.List;

public interface PlayerDAO {
    List<Player> getAllPlayers();

    void getPlayer(Player player);

    Player savePlayer(int id);

    void deletePlayer(int id);
}
