package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/rest/players")
    public List<Player> getPlayers(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "race", required = false) Race race,
                                   @RequestParam(value = "profession", required = false) Profession profession,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "banned", required = false) Boolean banned,
                                   @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize,
                                   @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order) {

        return playerService
                .getPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel, pageNumber, pageSize, order)
                .getContent();
    }
}
