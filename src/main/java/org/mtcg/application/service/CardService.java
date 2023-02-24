package org.mtcg.application.service;

import java.util.List;
import java.util.UUID;

import org.mtcg.application.model.Card;
import org.mtcg.application.model.CardType;
import org.mtcg.application.model.Type;
import org.mtcg.application.model.Element;
import org.mtcg.application.repository.CardRepository;
import org.mtcg.http.exception.ConflictException;
import org.mtcg.http.exception.ForbiddenException;
import org.mtcg.http.exception.NotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CardService {
    private final CardRepository cardRepository;
    private static final ObjectMapper om = new ObjectMapper();

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card findCardById(String id) {
        return cardRepository.findCardById(id);
    }

    public void addPackage(String pkg) {
        UUID packageId = UUID.randomUUID();
        try {
            List<Card> cards = om.readValue(pkg, new TypeReference<List<Card>>() {
            });
            for (var card : cards) {
                if (findCardById(card.getId()) != null) {
                    throw new ConflictException("Card already exists.");
                } else {
                    card.setPackageId(packageId.toString());
                    cardRepository.saveCard(card);
                }
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public void buyPackage(String token) throws NotFoundException {
        List<String> packages = cardRepository.getAvailablePackages();
        if (!packages.isEmpty()) {
            cardRepository.setPackageOwner(packages.get(0), token);
        } else {
            throw new NotFoundException("No packages available.");
        }
    }

    public String returnUserCards(String token) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(cardRepository.findCardsByOwner(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String[] cardIdArray(String jsonCardArray) {
        try {
            return om.readValue(jsonCardArray, new TypeReference<String[]>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public void doesUserOwnCard(String id, String token) {
        Card card = findCardById(id);

        if (card == null || !card.getOwner().equals(token)) {
            throw new ForbiddenException("Card does not exist or does not belong to this user.");
        }
    }

    public void clearUserDeck(String token) {
        cardRepository.clearUserDeck(token);
    }

    public void addCardToDeck(String id) {
        cardRepository.addCardToDeck(id);
    }

    public String getUserDeck(String token) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(cardRepository.getUserDeck(token));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Internal server error.", e);
        }
    }

    public String getUserDeckPlainText(String token) {
        List<Card> deck = cardRepository.getUserDeck(token);
        StringBuilder result = new StringBuilder();
        for (var card : deck) {
            result.append("Name: " + card.getName() + "\n");
            result.append("Damage: " + card.getDamage() + "\n\n");
        }
        return result.toString();
    }

    public String consultLoremaster(String token) {
        List<Card> deck = cardRepository.getUserDeck(token);
        StringBuilder mysticLore = new StringBuilder();

        mysticLore.append("+--  Mystic Lore  --+\n");
        mysticLore.append(loreMasterIntro() + "\n");

        for (var card : deck) {
            mysticLore.append(loreMaster(card));
        }

        mysticLore.append("\n");

        return mysticLore.toString();
    }

    public String loreMasterIntro() {
        return """
                You hesitantly open the flap to the LoreMaster's tent. The smoky
                air makes you cough, and your heart races as you step into the
                shadows. You can make out a hunched figure sitting at an
                intricately carved desk. Curls of smoke drift across the
                ancient wooden surface as you hold out your deck. A gnarled hand
                moves with surprising swiftness, taking the cards from your
                hands. A strange voice, not emanating from where you would
                expect, begins to chant:
                """;
    }

    public String loreMaster(Card card) {
        StringBuilder mysticLore = new StringBuilder();

        CardType type = CardType.valueOf(card.getName().toUpperCase());

        mysticLore.append(loreMasterDescribeCategory(type.getType()));
        mysticLore.append(loreMasterDescribeElement(type.getElement()));
        mysticLore.append(loreMasterDescribeMonsterType(type.getType()));

        mysticLore.append("   ...   Your card depicts a " + card.getName() + " and does "
                + card.getDamage() + " points of damage.\n\n");

        return mysticLore.toString();
    }

    public String loreMasterDescribeCategory(Type type) {
        String message = "";
        switch (type) {
            case SPELL:
                message = "  [@->]  Of pure energy in the form of an elemental magical spell is this card.\n";
                break;
            default:
                message = "  [-.-]  Monstrous in nature is this card.\n";
                break;
        }
        return message;
    }

    public String loreMasterDescribeElement(Element element) {
        String message = "";

        switch (element) {
            case WATER:
                message = "         Quick to change, the floodwaters carve new paths.\n";
                break;
            case FIRE:
                message = "         Flames dance and flicker, destroying all.\n";
                break;
            case REGULAR:
                message = "         The inexorable weight of earth crushes and smothers.\n";
                break;
        }
        return message;
    }

    public String loreMasterDescribeMonsterType(Type type) {
        String message = "";

        switch (type) {
            case GOBLIN:
                message = "         Cunning and manipulative; a cowardly goblin.\n";
                break;
            case TROLL:
                message = "         Cruel and filthy; an enormous troll.\n";
                break;
            case ELF:
                message = "         Long of years, yet stubborn and slow to change; a graceful elf.\n";
                break;
            case KNIGHT:
                message = "         Glittering metal and foolish pride; a vengeful knight.\n";
                break;
            case DRAGON:
                message = "         Scaly and filled with hate; a hungry dragon.\n";
                break;
            case ORK:
                message = "         Proud and barbaric; a conquering ork.\n";
                break;
            case KRAKEN:
                message = "         Terrible and stealthy; a mighty kraken.\n";
                break;
            case WIZZARD:
                message = "         Arrogant and hasty; a pompous wizzard.\n";
                break;
            default:
                break;
        }
        return message;
    }
}
