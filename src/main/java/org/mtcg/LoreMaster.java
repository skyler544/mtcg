package org.mtcg;

import java.util.ArrayList;

public class LoreMaster {
    public static void intro() {
        System.out.println("\nYou hesitantly open the flap to the LoreMaster's tent. The smoky air\n"
                + "makes you cough, and your heart races as you step into the shadows. You\n"
                + "can make out a hunched figure sitting at an intricately carved desk.\n"
                + "Curls of smoke drift across the ancient wooden surface as you hold out a\n"
                + "card. A gnarled hand moves with surprising swiftness, taking the card\n"
                + "from your hands. A strange voice, not emanating from where you would\n"
                + "expect, begins to chant: \n");
    }

    public static void describeMysticalProperties(Card card) {
        intro();
        describeCard(card);
    }

    public static void describeMysticalProperties(ArrayList<Card> stack) {
        intro();
        for (Card card : stack) {
            describeCard(card);
        }
    }

    private static void describeCard(Card card) {
        if (card instanceof MonsterCard) {
            describeMonsterCard((MonsterCard) card);
        } else {
            describeSpellCard((SpellCard) card);
        }
    }

    private static void describeMonsterCard(MonsterCard card) {
        System.out.println("Monstrous in nature is this card.");
        String type = describeMonsterType(card.getMonsterType());
        String element = describeElement(card.getElement());
        System.out.println("Your card depicts a Monster of the " + element + " " + type + " type and does "
                + card.getDamage() + " points of damage.\n");
    }

    private static void describeSpellCard(SpellCard card) {
        System.out.println("Of pure energy in the form of an elemental magical spell is this card.");
        String element = describeElement(card.getElement());
        System.out.println("Your card depicts " + element + " magic with " + card.getDamage() + " points of damage.\n");
    }

    private static String describeElement(Element e) {
        switch (e) {
        case FIRE:
            System.out.println("Flames dance and flicker, destroying all.");
            return "Fire";
        case WATER:
            System.out.println("Quick to change, the floodwaters carve new paths.");
            return "Water";
        case EARTH:
            System.out.println("Inexorable weight of earth crushes and smothers.");
            return "Earth";
        default:
            return "";
        }
    }

    private static String describeMonsterType(MonsterType t) {
        switch (t) {
        case DRAGON:
            System.out.println("Scaly and filled with hate; a hungry dragon.");
            return "Dragon";
        case KNIGHT:
            System.out.println("Glittering metal and foolish pride; a vengeful knight.");
            return "Knight";
        case ELF:
            System.out.println("Long of years, yet stubborn and slow to change; a graceful elf.");
            return "Elf";
        case GOBLIN:
            System.out.println("Cunning and manipulative; a cowardly goblin.");
            return "Goblin";
        default:
            return "";
        }
    }
}
