package pl.psi.creatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FirstAidTent extends WarMachinesAbstract {

    final private Random random = new Random();

    public FirstAidTent(Creature aDecorated, WarMachineActionType actionType) {
        super(aDecorated, actionType);
    }


    @Override
    public void performAction(List<Creature> creatureList) {
        if (isAlive()) {

            int maxHp = calculateHealHp(getSkillLevel());
            double hp = random.nextInt(maxHp - 1) + 1;

            List<Creature> creatures = new ArrayList<>(creatureList);
            Collections.shuffle(creatures);

            creatures.stream()
                    .filter(creature -> this.getHeroNumber() == creature.getHeroNumber())
                    .filter(Creature::isAlive)
                    .findAny()
                    .ifPresent(creature -> creature.heal(hp));
        }

    }

    public void healCreature(Creature creature) {
        int maxHp = calculateHealHp(getSkillLevel());
        double hp = random.nextInt(maxHp - 1) + 1;

        creature.heal(hp);
    }

    private int calculateHealHp(int skillLevel) {
        switch (skillLevel) {
            case 0:
                return 25;
            case 1:
                return 50;
            case 2:
                return 75;
            case 3:
                return 100;
            default:
                return 0;
        }
    }

}
