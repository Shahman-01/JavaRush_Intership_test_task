package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayers();

    Player getPlayer(int id);

    void savePlayer(Player player);

    void deletePlayer(int id);
}
