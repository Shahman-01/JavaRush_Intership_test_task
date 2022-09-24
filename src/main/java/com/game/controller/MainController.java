package com.game.controller;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class MainController {

    @GetMapping("/rest/players")
    public String home(String name, String title, Race race,
                       Profession profession, Integer experience,
                       Integer level, Integer untilNextLevel,
                       Date birthday, Boolean banned) {
        Player player = new Player(name, title, race, profession, experience, level, untilNextLevel, birthday, banned);
        return "hello";
    }

    @GetMapping("/get/check")
    public String home1() {

        return "This is the get check request";
    }
}
