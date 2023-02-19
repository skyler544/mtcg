package org.mtcg.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Damage")
    private int damage;
    @JsonProperty("Owner")
    private String owner;
    @JsonProperty("PackageId")
    private String packageId;

	public Card() {}

    public Card(String id, String name, int damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public Card(String id, String name, int damage, String owner, String packageId) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.owner = owner;
        this.packageId = packageId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
}
