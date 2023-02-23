package org.mtcg.application.model;

public enum CardType {
    WATERGOBLIN  (Element.WATER   , Type.GOBLIN ),
    FIREGOBLIN   (Element.FIRE    , Type.GOBLIN ),
    REGULARGOBLIN(Element.REGULAR , Type.GOBLIN ),
    WATERTROLL   (Element.WATER   , Type.TROLL  ),
    FIRETROLL    (Element.FIRE    , Type.TROLL  ),
    REGULARTROLL (Element.REGULAR , Type.TROLL  ),
    WATERELF     (Element.WATER   , Type.ELF    ),
    FIREELF      (Element.FIRE    , Type.ELF    ),
    REGULARELF   (Element.REGULAR , Type.ELF    ),
    WATERSPELL   (Element.WATER   , Type.SPELL  ),
    FIRESPELL    (Element.FIRE    , Type.SPELL  ),
    REGULARSPELL (Element.REGULAR , Type.SPELL  ),
    KNIGHT       (Element.REGULAR , Type.KNIGHT ),
    DRAGON       (Element.FIRE    , Type.DRAGON ),
    ORK          (Element.REGULAR , Type.ORK    ),
    KRAKEN       (Element.WATER   , Type.KRAKEN ),
    WIZZARD      (Element.FIRE    , Type.WIZZARD);

    private final Element element;
    private final Type type;

    CardType(Element element, Type type) {
        this.element = element;
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public Type getType() {
        return type;
    }
}
