package pl.psi.creatures;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class AmmoCart extends WarMachinesAbstract {


    public AmmoCart(Creature aDecorated, WarMachineActionType actionType,int aSkillLevel) {
        super(aDecorated, actionType, aSkillLevel);
    }

    @Override
    public void performAction(List<Creature> creatureList) {
        if (isAlive()) {
            creatureList.stream()
                    .filter(creature -> this.getHeroNumber() == creature.getHeroNumber())
                    .filter(creature -> creature instanceof ShooterCreature)
                    .map(ShooterCreature.class::cast)
                    .forEach(ShooterCreature::resetShots);
        }
    }
}
