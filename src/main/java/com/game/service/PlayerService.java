package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    List<Player> getAllPlayers();

    void getPlayer(Player player);

    Player savePlayer(int id);

    void deletePlayer(int id);
}
