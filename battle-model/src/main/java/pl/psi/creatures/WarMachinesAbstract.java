package pl.psi.creatures;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class WarMachinesAbstract extends AbstractCreature {
    protected Creature decorated;
    protected int skillLevel;
    protected WarMachineActionType actionType;

    public WarMachinesAbstract(Creature aDecorated,WarMachineActionType actionType) {
        super(aDecorated);
        this.actionType = actionType;
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
}
