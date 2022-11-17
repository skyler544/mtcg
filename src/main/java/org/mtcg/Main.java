package org.mtcg;

import java.util.ArrayList;

public class Main
{
    public static void main( String[] args )
    {
        ArrayList<Card> stack = new ArrayList<Card>();

        Card dragon = new MonsterCard(20, Element.FIRE, MonsterType.DRAGON);
        Card elf = new MonsterCard(25, Element.WATER, MonsterType.ELF);

        Card boulder = new SpellCard(25, Element.EARTH);

        stack.add(elf);
        stack.add(dragon);
        stack.add(boulder);

        LoreMaster.describeMysticalProperties(stack);
    }
}
