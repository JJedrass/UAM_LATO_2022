package pl.psi.creatures;

import pl.psi.shop.Money;

public class EconomyCastleFactory {

    private static final String EXCEPTION_MESSAGE = "We support tiers from 1 to 7";

    public EconomyCreature create(final boolean aIsUpgraded, final int aTier, final int aAmount) {
        if (!aIsUpgraded) {
            switch (aTier) {
                case 1:
                    return new EconomyCreature(CreatureStatistic.PIKEMAN, aAmount, new Money(60));
                case 2:
                    // TODO
                    // add archer
                    return new EconomyCreature(CreatureStatistic.GRIFFIN, aAmount, new Money(200));
                case 3:
                    return new EconomyCreature(CreatureStatistic.SWORDSMAN, aAmount, new Money(300));
                default:
                    throw new IllegalArgumentException(EXCEPTION_MESSAGE);
            }
        } else {
            switch (aTier) {
                case 1:
                    return new EconomyCreature(CreatureStatistic.HALBERDIER, aAmount, new Money(75));
                case 2:
                    return new EconomyCreature(CreatureStatistic.ROYAL_GRIFFIN, aAmount, new Money(240));
                case 3:
                    return new EconomyCreature(CreatureStatistic.CRUSADER, aAmount, new Money(400));
                default:
                    throw new IllegalArgumentException(EXCEPTION_MESSAGE);
            }
        }
    }
}