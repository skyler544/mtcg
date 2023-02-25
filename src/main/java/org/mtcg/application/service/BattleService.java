package org.mtcg.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mtcg.application.model.Battle;
import org.mtcg.application.model.BattleRound;
import org.mtcg.application.model.Card;
import org.mtcg.application.model.CardType;
import org.mtcg.application.model.Element;
import org.mtcg.application.model.Stats;
import org.mtcg.application.model.Type;
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

    private static List<String> lobby = new ArrayList<>();
    final static Object monitor = new Object();
    static int battleId = -1;

    private static final Random rg = new Random();

    final int ROUND_LIMIT = 100;

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

        List<Battle> battles = battleRepository.getBattlesByParticipant(token);

        for (var battle : battles) {
            if (battle.getResult().equals(token)) {
                wins++;
            }
        }

        losses = battles.size() - wins;
        elo = wins > 0 ? battles.size() / wins : 0;

        return new Stats(name, elo, wins, losses);
    }

    public String getStats(String token) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(getStatsObject(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String getScoreboard() {
        List<String> users = userRepository.findActiveUsers();
        List<Stats> stats = new ArrayList<>();

        for (var token : users) {
            stats.add(getStatsObject(token));
        }
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(stats);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String battle(String token) {
        // NOTE: We will run into problems if any more than two users try to
        // join at a time. Consideration of more than a single pair of battle
        // participants will have to wait for a future rewrite.
        try {
            // Whenever threads enter this method, we want the statements to
            // execute in a certain order.
            synchronized (monitor) {
                // we add the user's token to the lobby
                lobby.add(token);
                if (lobby.size() != 2) {
                    // there is only one user in the lobby; they have to wait
                    monitor.wait();
                } else {
                    // the second user has entered; do the fight and wake up the
                    // other thread
                    battleId = fight();
                    monitor.notify();
                }
            }
            // construct battle object
            Battle battle = battleRepository.readBattleById(battleId);
            // retrieve log
            battle.setRounds(battleRepository.readBattleLog(battleId));
            // return it as JSON
            try {
                return om.writerWithDefaultPrettyPrinter().writeValueAsString(battle);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Internal server error.", e);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public int fight() {
        String playerOne = lobby.get(0);
        String playerTwo = lobby.get(1);
        String result = "draw";

        int battleId = battleRepository.saveBattle(new Battle(playerOne, playerTwo, result));

        // initialize round counter
        int roundCount = 0;
        boolean gameOver = false;

        // get user decks
        List<Card> deckOne = cardRepository.getUserDeck(playerOne);
        List<Card> deckTwo = cardRepository.getUserDeck(playerTwo);

        do {
            String resultLog = "+--  Round " + ++roundCount + "  --+\n";

            // pick a card randomly for each player
            int cardOne = rg.nextInt(deckOne.size());
            int cardTwo = rg.nextInt(deckTwo.size());

            // get type and damage from each card
            CardType typeOne = CardType.valueOf(deckOne.get(cardOne).getName().toUpperCase());
            CardType typeTwo = CardType.valueOf(deckTwo.get(cardTwo).getName().toUpperCase());

            int dmgOne = deckOne.get(cardOne).getDamage();
            int dmgTwo = deckTwo.get(cardTwo).getDamage();

            String cardOneInfo = "[" + deckOne.get(cardOne).getName() + ":" + dmgOne + "]";
            String cardTwoInfo = "[" + deckTwo.get(cardTwo).getName() + ":" + dmgTwo + "]";

            resultLog += cardOneInfo + "  VS  " + cardTwoInfo + "\n";

            String winner = "draw";

            // head-to-head the two cards
            if (typeOne.getType() == Type.SPELL || typeTwo.getType() == Type.SPELL) {
                // if A or B are spells, consider elements
                resultLog += "     ELEMENTAL DAMAGE\n";

                // Knight vs WaterSpell
                if (typeOne == CardType.KNIGHT && typeTwo == CardType.WATERSPELL
                        ||
                        typeOne == CardType.WATERSPELL && typeTwo == CardType.KNIGHT) {

                    resultLog += "The knight drowns in a torrent of water!\n";

                    if (typeOne == CardType.KNIGHT) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

                // Kraken vs Spell
                else if (typeOne == CardType.KNIGHT && typeTwo.getType() == Type.SPELL
                        ||
                        typeOne.getType() == Type.SPELL && typeTwo == CardType.KRAKEN) {

                    resultLog += "The kraken shrugs off the spell with a roar!\n";

                    if (typeOne == CardType.KRAKEN) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

                // Water vs Fire
                else if (typeOne.getElement() == Element.FIRE && typeTwo.getElement() == Element.WATER
                        ||
                        typeOne.getElement() == Element.WATER && typeTwo.getElement() == Element.FIRE) {

                    resultLog += "The water dims the fire!\n";

                    if (typeOne.getElement() == Element.WATER) {
                        // player one advantage
                        winner = dmgOne * 2 > dmgTwo / 2 ? "one" : "two";
                    } else {
                        // player two advantage
                        winner = dmgTwo * 2 > dmgOne / 2 ? "two" : "one";
                    }
                }

                // Fire vs Regular
                else if (typeOne.getElement() == Element.FIRE && typeTwo.getElement() == Element.REGULAR
                        ||
                        typeOne.getElement() == Element.REGULAR && typeTwo.getElement() == Element.FIRE) {

                    resultLog += "The flames burn unmercifully!\n";

                    if (typeOne.getElement() == Element.FIRE) {
                        // player one advantage
                        winner = dmgOne * 2 > dmgTwo / 2 ? "one" : "two";
                    } else {
                        // player two advantage
                        winner = dmgTwo * 2 > dmgOne / 2 ? "two" : "one";
                    }
                }

                // Regular vs Water
                else if (typeOne.getElement() == Element.REGULAR && typeTwo.getElement() == Element.WATER
                        ||
                        typeOne.getElement() == Element.WATER && typeTwo.getElement() == Element.REGULAR) {

                    resultLog += "The water must yield!\n";

                    if (typeOne.getElement() == Element.REGULAR) {
                        // player one advantage
                        winner = dmgOne * 2 > dmgTwo / 2 ? "one" : "two";
                    } else {
                        // player two advantage
                        winner = dmgTwo * 2 > dmgOne / 2 ? "two" : "one";
                    }
                }

                // No specials, regular spell fight.
                else {

                    resultLog += "Blasts of pure magic fill the air!\n";

                    if (dmgOne > dmgTwo) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }


            } else {
                // otherwise pure damage except specials

                // Goblin vs Dragon
                if (typeOne.getType() == Type.GOBLIN && typeTwo == CardType.DRAGON
                        ||
                        typeOne == CardType.DRAGON && typeTwo.getType() == Type.GOBLIN) {

                    resultLog += "The goblin is terrified of the dragon and can't attack!\n";

                    if (typeOne == CardType.DRAGON) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

                // Wizzard vs Ork
                else if (typeOne == CardType.ORK && typeTwo == CardType.WIZZARD
                        ||
                        typeOne == CardType.WIZZARD && typeTwo == CardType.ORK) {

                    resultLog += "The wizzard easily takes control of the ork!\n";

                    if (typeOne == CardType.WIZZARD) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

                // FireElf vs Dragon
                else if (typeOne == CardType.FIREELF && typeTwo == CardType.DRAGON
                        ||
                        typeOne == CardType.DRAGON && typeTwo == CardType.FIREELF) {

                    resultLog += "The FireElf chuckles wisely and easily evades the dragon!\n";

                    if (typeOne == CardType.FIREELF) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

                // No specials, pure monster fight.
                else {

                    resultLog += "The combatants charge furiously!\n";

                    if (dmgOne > dmgTwo) {
                        winner = "one";
                    } else {
                        winner = "two";
                    }
                }

            }

            // the loser's card goes to the winner
            switch (winner) {
                case "one":
                    deckOne.add(deckTwo.get(cardTwo));
                    deckTwo.remove(deckTwo.get(cardTwo));
                    resultLog += cardOneInfo + " wins!\n";
                    break;
                case "two":
                    deckTwo.add(deckOne.get(cardOne));
                    deckOne.remove(deckOne.get(cardOne));
                    resultLog += cardTwoInfo + " wins!\n";
                    break;
                default:
                    break;
            }

            // log what happened
            battleRepository.saveBattleRound(new BattleRound(battleId, cardOneInfo, cardTwoInfo, resultLog));

            // next round
            if (deckOne.size() == 0 || deckTwo.size() == 0) {
                gameOver = true;
            }
        } while (!gameOver && roundCount <= ROUND_LIMIT);

        result = deckOne.size() == 0 ? playerTwo : result;
        result = deckTwo.size() == 0 ? playerOne : result;

        battleRepository.updateBattleResult(result, battleId);

        lobby.remove(1);
        lobby.remove(0);

        return battleId;
    }
}
