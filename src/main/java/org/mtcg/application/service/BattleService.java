package org.mtcg.application.service;

import org.mtcg.application.repository.BattleRepository;
import org.mtcg.application.repository.CardRepository;
import org.mtcg.application.repository.UserRepository;

public class BattleService {
    private final BattleRepository battleRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public BattleService(BattleRepository battleRepository, UserRepository userRepository, CardRepository cardRepository) {
        this.battleRepository = battleRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    // public String getPlayerStats(String token) {

    // }
}
