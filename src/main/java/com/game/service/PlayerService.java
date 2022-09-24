package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlayerService {

    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession,
                                      Long after, Long before, Boolean banned, Integer minExperience,
                                      Integer maxExperience, Integer minLevel, Integer maxLevel);

    public List<Player> sortPlayers(List<Player> players, PlayerOrder order);

    public Player createPlayer(Player player);

    public Player updatePlayer(Player oldPlayer, Player newPlayer) throws IllegalArgumentException;

    public void deletePlayer(Player player);

    public Player getPlayer(Long id);
}
