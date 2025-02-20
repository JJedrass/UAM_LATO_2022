package pl.psi.creatures;

import java.beans.PropertyChangeEvent;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class SelfHealAfterTurnCreature extends AbstractCreature {
    private final Creature decorated;

    public SelfHealAfterTurnCreature(final Creature aDecorated) {
        super(aDecorated);
        decorated = aDecorated;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        decorated.propertyChange(evt);
        decorated.restoreCurrentHpToMax();
    }
}
