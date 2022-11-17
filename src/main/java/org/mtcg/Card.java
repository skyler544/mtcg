package org.mtcg;

public abstract class Card {
    private int damage;

    Card(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
