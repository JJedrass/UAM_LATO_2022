package pl.psi;

import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.FirstAidTent;
import pl.psi.creatures.WarMachineActionType;
import pl.psi.creatures.WarMachinesAbstract;
import pl.psi.spells.AreaDamageSpell;
import pl.psi.spells.Spell;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.ceil;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class GameEngine {

    public static final String CREATURE_MOVED = "CREATURE_MOVED";
    private final TurnQueue turnQueue;
    private final Board board;
    private final PropertyChangeSupport observerSupport = new PropertyChangeSupport(this);
    @Getter
    private final Hero hero1;
    @Getter
    private final Hero hero2;

    public GameEngine(final Hero aHero1, final Hero aHero2) {
        hero1 = aHero1;
        hero2 = aHero2;
        turnQueue = new TurnQueue(aHero1.getCreatures(), aHero2.getCreatures());
        turnQueue.addObserver(TurnQueue.NEW_TURN, this::onNextTurn);
        board = new Board(aHero1.getCreatures(), aHero2.getCreatures());
    }

    public void attack(final Point point) {
        board.getCreature(point).ifPresent(defender -> turnQueue.getCurrentCreature().attack(defender));
    }

    public void heal(final Point point) {
        FirstAidTent firstAidTent = (FirstAidTent) turnQueue.getCurrentCreature();
        board.getCreature(point).ifPresent(firstAidTent::healCreature);
    }

    public boolean canMove(final Point aPoint) {
        return board.canMove(turnQueue.getCurrentCreature(), aPoint);
    }

    public void move(final Point aPoint) {
        board.move(turnQueue.getCurrentCreature(), aPoint);
        observerSupport.firePropertyChange(CREATURE_MOVED, null, aPoint);
    }

    public Optional<Creature> getCreature(final Point aPoint) {
        return board.getCreature(aPoint);
    }

    public void pass() {
        turnQueue.next();
    }

    public void addObserver(final String aEventType, final PropertyChangeListener aObserver) {
        observerSupport.addPropertyChangeListener(aEventType, aObserver);
    }

    public boolean canAttack(final Point point) {
        return false;
    }

    public boolean canHeal(final Point point) {
        Creature currentCreature = turnQueue.getCurrentCreature();

        Optional<Creature> otherCreatureOptional = board.getCreature(point);

        if (currentCreature.isMachine() && otherCreatureOptional.isPresent()) {
            Creature other = otherCreatureOptional.get();
            WarMachinesAbstract warMachinesAbstract = (WarMachinesAbstract) currentCreature;

            return warMachinesAbstract.getActionType() == WarMachineActionType.HEAL_CREATURE
                    &&
                    other.getHeroNumber() == currentCreature.getHeroNumber();
        }
        return false;
    }

    public void castSpell(final Point point, Spell spell) {
        switch (spell.getCategory()) {
            case FIELD:
                board.getCreature(point).ifPresent(defender -> {
                    turnQueue.getCurrentCreature().castSpell(defender, spell);
                });
                break;
            case AREA:
                List<Creature> creatureList = getCreaturesFromArea(point, (AreaDamageSpell) spell);
                turnQueue.getCurrentCreature().castSpell(creatureList, spell);
                break;
            default:
                throw new IllegalArgumentException("Not supported category.");
        }

    }

    //ToDO: In the future think about better solution and refactor this
    private List<Creature> getCreaturesFromArea(Point point, AreaDamageSpell areaDamageSpell) {
        List<Creature> creatures = new ArrayList<>();

        int centerOfArea = (int) ceil((float) areaDamageSpell.getArea().length / 2);

        int startX = point.getX() - centerOfArea + 1;
        int endX = startX + (centerOfArea * 2) - 1;
        int startY = point.getY() - centerOfArea + 1;
        int endY = startY + (centerOfArea * 2) - 1;

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                if (board.getCreature(new Point(j, i)).isPresent()) {
                    board.getCreature(new Point(j, i)).ifPresent(creatures::add);
                }
            }
        }

        return creatures;
    }

    private void onNextTurn(PropertyChangeEvent propertyChangeEvent) {
        try {
            if (propertyChangeEvent.getPropertyName().equals(TurnQueue.NEW_TURN)) {
                Creature creature = (Creature) propertyChangeEvent.getNewValue();

                if (creature.isMachine()) {
                    handleWarAutoMachineAction((WarMachinesAbstract) creature,
                            Stream.concat(
                                    hero1.getCreatures().stream(),
                                    hero2.getCreatures().stream()).collect(Collectors.toList())
                    );
                }
            }
        } catch (Exception e) {
            //ignore
        }
    }

    private void handleWarAutoMachineAction(WarMachinesAbstract warMachine, List<Creature> creatures) {
        if (warMachine.getSkillLevel() == 0) {
            List<Creature> creatureList = new ArrayList<>(creatures);
            warMachine.performAction(creatureList);
            turnQueue.next();
        }
    }
}
