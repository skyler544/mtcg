package org.mtcg;

public class MonsterCard extends Card {
    private Element element;
    private MonsterType type;

    public MonsterCard(int damage, Element element, MonsterType type) {
        super(damage);
        this.setElement(element);
        this.setMonsterType(type);
    }

    public MonsterType getMonsterType() {
        return type;
    }

    public void setMonsterType(MonsterType type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
