package org.mtcg.application.repository;

import java.util.List;

import org.mtcg.application.model.Battle;
import org.mtcg.application.model.BattleRound;

public interface BattleRepository {
    int saveBattle(Battle battle);
    Battle readBattleById(int id);
    void updateBattleResult(String result, int id);

    void saveBattleRound(BattleRound round);
    List<BattleRound> readBattleLog(int id);

    List<Battle> getBattlesByParticipant(String name);
}
