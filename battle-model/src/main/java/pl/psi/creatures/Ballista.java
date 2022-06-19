package pl.psi.creatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ballista extends WarMachinesAbstract {


    public Ballista(Creature aDecorated, WarMachineActionType actionType, int aSkillLevel) {
        super(aDecorated, actionType, aSkillLevel);
    }


    @Override
    public void performAction(List<Creature> creatureList) {
        if (isAlive()) {

            List<Creature> creatures = new ArrayList<>(creatureList);
            Collections.shuffle(creatures);

            creatures.stream()
                    .filter(creature -> this.getHeroNumber() != creature.getHeroNumber())
                    .findAny()
                    .ifPresent(this::calculateAndApplyDamge);
        }
    }

    private void calculateAndApplyDamge(Creature aDefender) {
        final int damage = getCalculator().calculateDamage(this, aDefender);
        applyDamage(aDefender, damage);
    }

    protected void applyDamage(final Creature aDefender, final double aDamage) {
        aDefender.setCurrentHp(aDefender.getCurrentHp() - aDamage);
    }
}
