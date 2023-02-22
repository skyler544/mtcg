package org.mtcg.application.service;

import java.util.ArrayList;
import java.util.List;

import org.mtcg.application.model.Battle;
import org.mtcg.application.model.Stats;
import org.mtcg.application.model.User;
import org.mtcg.application.model.UserProfile;
import org.mtcg.application.repository.BattleRepository;
import org.mtcg.application.repository.CardRepository;
import org.mtcg.application.repository.UserRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BattleService {
    private final BattleRepository battleRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private static final ObjectMapper om = new ObjectMapper();

    public BattleService(BattleRepository battleRepository, UserRepository userRepository,
            CardRepository cardRepository) {
        this.battleRepository = battleRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public Stats getStatsObject(String token) {
        String name = userRepository.findUserProfile(token).getName();
        double wins = 0;
        double losses = 0;
        double elo;

        List<Battle> battles = battleRepository.getBattlesByParticipant(name);

        for (var battle : battles) {
            if (battle.getResult().equals(name)) {
                wins++;
            }
        }

        losses = battles.size() - wins;
        elo = battles.size() / wins;

        return new Stats(name, elo, wins, losses);
    }

    public String getStats(String token) {
        try {
            return om.writeValueAsString(getStatsObject(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String getScoreboard() {
        System.out.println("Made it into scoreboard");
        List<String> users = userRepository.findActiveUsers();
        System.out.println("queried users");
        List<Stats> stats = new ArrayList<>();

        for (var token : users) {
            System.out.println(token);
            stats.add(getStatsObject(token));
        }
        try {
            return om.writeValueAsString(stats);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }
}
