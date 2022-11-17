package org.mtcg;

public class SpellCard extends Card {
    private Element element;

    public SpellCard(int damage, Element element) {
        super(damage);
        this.setElement(element);
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
