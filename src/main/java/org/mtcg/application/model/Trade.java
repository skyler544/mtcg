package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("CardToTrade")
    private String cardId;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("MinimumDamage")
    private int damage;

    public Trade() {}

    public Trade (String id, String cardId, String type, int damage) {
        this.id = id;
        this.cardId = cardId;
        this.type = type;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }
    public String getCardId() {
        return cardId;
    }
    public String getType() {
        return type;
    }
    public int getDamage() {
        return damage;
    }
}
