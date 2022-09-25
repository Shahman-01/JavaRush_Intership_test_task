package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class PlayerController {

    final private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.GET)
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
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
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        final List<Player> playersList = playerService.getAllPlayers(
                name, title, race, profession,
                after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel);

        final List<Player> sorted = playerService.sortPlayers(playersList, order);

        return playerService.getPage(sorted, pageNumber, pageSize);
    }

    @RequestMapping(value = "/rest/players/count", method = RequestMethod.GET)
    public Integer getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        return playerService.getAllPlayers(
                name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel
                ).size();
    }

    @RequestMapping(value = "/rest/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (playerService.isValid(player) == false) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getBanned() == null) {
            player.setBanned(false);
        }

        final int level = playerService.calculateLevel(player.getExperience());
        player.setLevel(level);

        final int untilNextLevel = playerService.
                calculateExpUntilNextLevel(player.getLevel(), player.getExperience());

        player.setUntilNextLevel(untilNextLevel);

        final Player newPlayer = playerService.createNewPlayer(player);

        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @RequestMapping(value = "rest/players/{id}", method = RequestMethod.GET)
    public ResponseEntity<Player> getPlayer(@PathVariable(value = "id") String getId) {
        final Long id = toLong(getId);
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Player player = playerService.getPlayer(id);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    private Long toLong(String getId) {
        if (getId != null) {
            try {
                return Long.parseLong(getId);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @RequestMapping(value = "rest/players/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") String getId,
                                               @RequestBody Player player) {
        final ResponseEntity<Player> findPlayer = getPlayer(getId);
        final Player oldPlayer = findPlayer.getBody();
        if (oldPlayer == null)
            return findPlayer;

        final Player newPlayer;
        try {
            newPlayer = playerService.updatePlayer(oldPlayer, player);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @RequestMapping(value = "rest/players/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Player> deletePlayer(@PathVariable(value = "id") String getId) {
        final ResponseEntity<Player> getPlayer = getPlayer(getId);
        final Player oldPlayer = getPlayer.getBody();

        if (oldPlayer == null)
            return getPlayer;
        playerService.deletePlayer(oldPlayer);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}