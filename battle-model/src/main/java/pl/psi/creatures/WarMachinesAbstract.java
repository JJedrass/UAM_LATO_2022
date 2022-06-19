package pl.psi.creatures;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class WarMachinesAbstract extends AbstractCreature {
    protected Creature decorated;
    protected int skillLevel;
    protected WarMachineActionType actionType;
    protected DamageCalculatorIf damageCalculator;

    public WarMachinesAbstract(Creature aDecorated,WarMachineActionType actionType,int skillLevel) {
        super(aDecorated);
        this.actionType = actionType;
        this.skillLevel = skillLevel;
    }

    @Override
    protected boolean canCounterAttack(final Creature aDefender) {
        return false;
    }

    public abstract void performAction(List<Creature> creatureList);

    @Override
    public boolean isMachine() {
        return true;
    }

    public void upgradeSkillLevel( int aNewLevel) {
        if (aNewLevel < 4 && aNewLevel > 0) {
            this.skillLevel = aNewLevel;
        }
    }
}