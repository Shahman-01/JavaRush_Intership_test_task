package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlayerService {

    private PlayerRepository repository;

    @Autowired
    public PlayerService(PlayerRepository repository) {
        super();
        this.repository = repository;
    }

    public List<Player> getAllPlayers(String name, String title, Race race, Profession profession,
                                      Long after, Long before, Boolean banned, Integer minExperience,
                                      Integer maxExperience, Integer minLevel, Integer maxLevel) {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);

        final List<Player> players = new ArrayList<>();
        repository.findAll().forEach(player -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (after != null && player.getBirthday().before(afterDate)) return;
            if (before != null && player.getBirthday().after(beforeDate)) return;
            if (banned != null && player.getBanned().booleanValue() != banned.booleanValue()) return;
            if (minExperience != null && player.getExperience().compareTo(minExperience) < 0) return;
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0) return;
            if (minLevel != null && player.getLevel().compareTo(minLevel) < 0) return;
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0) return;
            players.add(player);
        });

        return players;
    }

    public List<Player> sortPlayers(List<Player> players, PlayerOrder order) {
        if (order != null) {
            players.sort((firstPlayer, secondPlayer) -> {
                switch (order) {
                    case ID:
                        return Long.compare(firstPlayer.getId(), secondPlayer.getId());
                    case NAME:
                        return firstPlayer.getName().compareTo(secondPlayer.getName());
                    case LEVEL:
                        return Integer.compare(firstPlayer.getLevel(), secondPlayer.getLevel());
                    case BIRTHDAY:
                        return firstPlayer.getBirthday().compareTo(secondPlayer.getBirthday());
                    case EXPERIENCE:
                        return Integer.compare(firstPlayer.getExperience(), secondPlayer.getExperience());
                    default:
                        return 0;

                }
            });
            return players;
        }

        return null;
    }

    public Player createPlayer(Player player) {
        return repository.save(player);
    }

    public Player updatePlayer(Player oldPlayer, Player newPlayer) throws IllegalArgumentException {
        boolean isPossiblePlayerUpdate = false;

        final String name = newPlayer.getName();
        if (name != null) {
            if (isNameValid(name)) {
                oldPlayer.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }

        final String title = newPlayer.getTitle();
        if (title != null) {
            if (isTitleValid(title)) {
                oldPlayer.setTitle(title);
            } else {
                throw new IllegalArgumentException();
            }
        }

        if (newPlayer.getRace() != null) {
            oldPlayer.setRace(newPlayer.getRace());
        }

        if (newPlayer.getProfession() != null) {
            oldPlayer.setProfession(newPlayer.getProfession());
        }

        final Integer experience = newPlayer.getExperience();
        if (experience != null) {
            if (isExperienceValid(experience)) {
                oldPlayer.setExperience(experience);
                isPossiblePlayerUpdate = true;
                oldPlayer.setLevel(calculateLevel(experience));
            } else {
                throw new IllegalArgumentException();
            }
        }

        Date birthday = newPlayer.getBirthday();
        if (birthday != null) {
            if (isBirthdayValid(birthday)) {
                oldPlayer.setBirthday(birthday);
                isPossiblePlayerUpdate = true;
            } else {
                throw new IllegalArgumentException();
            }
        }

        if (newPlayer.getBanned() != null) {
            oldPlayer.setBanned(newPlayer.getBanned());
            isPossiblePlayerUpdate = true;
        }

        if (isPossiblePlayerUpdate) {
            final int experienceUntilNextLevel = calculateExperienceUntilNextLevel(oldPlayer.getLevel(),
                                                 oldPlayer.getExperience());
            oldPlayer.setUntilNextLevel(experienceUntilNextLevel);
        }

        repository.save(oldPlayer);
        return oldPlayer;
    }

    public void deletePlayer(Player player) {
        repository.delete(player);
    }

    public Player getPlayer(Long id) {
        return repository.findById(id).orElse(null);
    }

    public boolean isValid(Player player) {
        return player != null && isNameValid(player.getName()) && isTitleValid(player.getTitle())
                && isBirthdayValid(player.getBirthday()) && isExperienceValid(player.getExperience());
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty() && name.length() <= 12;
    }

    private boolean isTitleValid(String title) {
        return title != null && !title.isEmpty() && title.length() <= 30;
    }

    private boolean isExperienceValid(Integer exp) {
        final int minExp = 0;
        final int maxExp = 10_000_000;
        return exp != null && exp.compareTo(minExp) >= 0 && exp.compareTo(maxExp) <= 0;
    }

    public int calculateLevel(Integer experience) {
        return (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    private boolean isBirthdayValid(Date birthday) {
        return birthday != null && birthday.after(getDateFromYear(2000)) && birthday.before(getDateFromYear(3000));
    }

    private Date getDateFromYear(int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public int calculateExperienceUntilNextLevel(Integer level, Integer experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }

    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {
        final int page = pageNumber == null ? 0 : pageNumber;
        final int size = pageSize == null ? 3 : pageSize;

        final int start = page * size;

        int finish = start * size;

        if (finish > players.size()) {
            finish = players.size();
        }

        return players.subList(start, finish);
    }
}
